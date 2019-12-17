package com.heroku.api.connection

import com.heroku.api.http._
import com.twitter.finagle.{Service, http}
import com.heroku.api.request.Request

import collection.JavaConversions._
import java.net.{InetSocketAddress, URL}

import com.twitter.finagle.http.{HeaderMap, Response}
import com.twitter.finagle.builder.ClientBuilder
import com.heroku.api.http.Http.Method
import com.heroku.api.http.Http.UserAgent
import com.heroku.api.Heroku.ApiVersion
import com.heroku.api.Heroku
import com.twitter.util.{Await, Base64StringEncoder, Future}
import com.twitter.conversions.DurationOps._
import java.util

import com.twitter.finagle
import com.twitter.io.Buf

import scala.collection.JavaConversions

trait TwitterFutureConnection extends AsyncConnection[Future[_]] {
  def executeAsync[T](request: Request[T], apiKey: String): Future[T]

  def executeAsync[T](request: Request[T], extraHeaders: util.Map[String, String], apiKey: String): Future[T]

  def execute[T](request: Request[T], extraHeaders: util.Map[String, String], apiKey: String): T

  def execute[T](request: Request[T], apiKey: String): T
}


class FinagleConnection(val host: String) extends TwitterFutureConnection {

  def this() {
    this(Heroku.Config.ENDPOINT.value)
  }

  type HttpService = Service[http.Request, Response]
  val timeout = 60.seconds

  @volatile var client = newClient()

  val hostHeader = getHostHeader


  def execute[T](request: Request[T], key: String): T = Await.result(executeAsync(request, key), timeout)

  def executeAsync[T](request: Request[T], apiKey: String): Future[T] = executeAsync(request, mapAsJavaMap(Map.empty), apiKey)

  def execute[T](request: Request[T], extraHeaders: util.Map[String, String], apiKey: String): T = Await.result(executeAsync(request, extraHeaders, apiKey), timeout)

  def executeAsync[T](command: Request[T], extraHeaders:util.Map[String,String], key: String): Future[T] = {
    if (!client.isAvailable) {
      client.close()
      client = newClient()
    }
    client(toReq(command, extraHeaders, key)).map {
      resp =>
        command.getResponse(resp.contentString.getBytes("UTF-8"), resp.status.code, toJavaMap(resp.headerMap))
    }
  }

  def toReq(cmd: Request[_], extraHeaders: util.Map[String, String], key: String): com.twitter.finagle.http.Request = {
    val method = cmd.getHttpMethod match {
      case Method.GET => http.Method.Get
      case Method.PUT => http.Method.Put
      case Method.POST => http.Method.Post
      case Method.DELETE => http.Method.Delete
      case Method.PATCH => http.Method.Patch
    }
    val req = http.Request(method, cmd.getEndpoint)
    req.accept = cmd.getResponseType.getHeaderValue
    req.headerMap.add(ApiVersion.HEADER, ApiVersion.v3.getHeaderValue)
    req.host = hostHeader
    req.headerMap.add(UserAgent.LATEST.getHeaderName, UserAgent.LATEST.getHeaderValue("finagle"))

    if (key != null) {
      req.authorization = "Basic " + Base64StringEncoder.encode((":" + key).getBytes("UTF-8"))
    }

    (cmd.getHeaders ++ extraHeaders) foreach {
      _ match {
        case (k, v) => req.headerMap.add(k, v)
      }
    }
    if (cmd.hasBody) {
      val body = Buf.Utf8(cmd.getBody)
      req.write(body)
      req.contentLength = body.length
    }
    req
  }

  def getPort(url: URL): Int = {
    if (url.getPort == -1) url.getDefaultPort
    else url.getPort
  }

  def getHostHeader: String = {
    val url = HttpUtil.toURL(host)
    val hhost = url.getHost
    var port = ""
    if (url.getPort != url.getDefaultPort) port = ":" + url.getPort.toString
    hhost + port
  }

  def newClient(): Service[http.Request, Response] = {
    val endpoint = HttpUtil.toURL(host)
    var builder = ClientBuilder()
      .stack(finagle.Http.client)
      .hosts(new InetSocketAddress(endpoint.getHost, getPort(endpoint)))
      .hostConnectionLimit(10)
    if (endpoint.getProtocol.equals("https")) {
      if (Heroku.Config.ENDPOINT.isDefault) {
        builder = builder.tls(endpoint.getHost)
      } else {
        builder = builder.tlsWithoutValidation()
      }
    }
    builder.build()
  }

  def close() {
    client.close()
  }

  def toJavaMap(headers:HeaderMap) = {
    JavaConversions.mapAsJavaMap(headers.toMap)
  }
}


object FinagleConnection {


  def apply(): FinagleConnection = {
    new FinagleConnection
  }

  class Provider extends ConnectionProvider {
    def getConnection() = {
      FinagleConnection()
    }
  }

}
