package com.heroku.api.connection

import java.lang.String
import com.heroku.api.http._
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import com.heroku.api.request.Request
import collection.JavaConversions._
import java.net.{InetSocketAddress, URL}
import com.twitter.finagle.http.Http
import org.jboss.netty.buffer.ChannelBuffers
import java.nio.charset.Charset
import com.twitter.finagle.builder.ClientBuilder
import com.heroku.api.http.Http.Method
import com.heroku.api.http.Http.UserAgent
import com.heroku.api.Heroku.ApiVersion
import com.heroku.api.Heroku
import com.twitter.util.{Base64StringEncoder, Future}
import com.twitter.conversions.time._

trait TwitterFutureConnection extends AsyncConnection[Future[_]] {
  def executeAsync[T](request: Request[T], apiKey: String): Future[T]

  def execute[T](request: Request[T], apiKey: String): T
}


class FinagleConnection extends TwitterFutureConnection {

  type HttpService = Service[HttpRequest, HttpResponse]
  val timeout = 60.seconds

  @volatile var client = newClient()

  val hostHeader = getHostHeader


  def execute[T](request: Request[T], key: String): T = executeAsync(request, key).get(timeout).get()

  def executeAsync[T](command: Request[T], key: String): Future[T] = {
    if (!client.isAvailable) {
      client.release()
      client = newClient()
    }
    client(toReq(command, key)).map {
      resp =>
        command.getResponse(resp.getContent.array(), resp.getStatus.getCode)
    }
  }

  def toReq(cmd: Request[_], key: String): HttpRequest = {
    val method = cmd.getHttpMethod match {
      case Method.GET => HttpMethod.GET
      case Method.PUT => HttpMethod.PUT
      case Method.POST => HttpMethod.POST
      case Method.DELETE => HttpMethod.DELETE
    }
    val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, cmd.getEndpoint)
    req.addHeader(cmd.getResponseType.getHeaderName, cmd.getResponseType.getHeaderValue)
    req.addHeader(ApiVersion.HEADER, ApiVersion.v2.getHeaderValue)
    req.addHeader(HttpHeaders.Names.HOST, hostHeader)
    req.addHeader(UserAgent.LATEST.getHeaderName, UserAgent.LATEST.getHeaderValue("finagle"))

    if (key != null) {
      req.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + Base64StringEncoder.encode((":" + key).getBytes("UTF-8")))
    }

    cmd.getHeaders.foreach {
      _ match {
        case (k, v) => req.addHeader(k, v)
      }
    }
    if (cmd.hasBody) {
      val body = ChannelBuffers.copiedBuffer(cmd.getBody, Charset.forName("UTF-8"))
      req.setContent(body)
      req.setHeader(HttpHeaders.Names.CONTENT_LENGTH, body.readableBytes().toString)
    }

    req
  }

  def getPort(url: URL): Int = {
    if (url.getPort == -1) url.getDefaultPort
    else url.getPort
  }

  def getHostHeader: String = {
    val url = HttpUtil.toURL(Heroku.Config.ENDPOINT.value)
    val host = url.getHost
    var port = ""
    if (url.getPort != url.getDefaultPort) port = ":" + url.getPort.toString
    host + port
  }

  def newClient(): HttpService = {
    val endpoint = HttpUtil.toURL(Heroku.Config.ENDPOINT.value)
    var builder = ClientBuilder()
      .codec(Http())
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
    client.release()
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
