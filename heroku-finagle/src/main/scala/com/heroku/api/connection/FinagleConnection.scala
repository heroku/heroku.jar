package com.heroku.api.connection

import com.twitter.util.Future
import java.lang.String
import com.heroku.api.HerokuAPI
import com.twitter.finagle.builder.ClientBuilder
import com.heroku.api.http._
import collection.mutable.HashMap
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import sun.misc.BASE64Encoder
import com.heroku.api.command.{LoginCommand, Command, CommandResponse}
import collection.JavaConversions._
import java.net.{InetSocketAddress, URL}
import com.twitter.finagle.http.{ProxyCredentials, Http}
import org.jboss.netty.buffer.{ChannelBuffers, ChannelBufferInputStream}


class FinagleConnection(val loginCommand: LoginCommand) extends Connection[Future[_]] {

  type HttpService = Service[HttpRequest, HttpResponse]

  val clients = new HashMap[(String, Int), HttpService]

  val encoder = new BASE64Encoder

  val getEndpoint: URL = HttpUtil.toURL(loginCommand.getApiEndpoint)

  val loginResponse = executeCommand(loginCommand)

  def executeCommand[T <: CommandResponse](command: Command[T]): T = executeCommandAsync(command).get()

  def executeCommandAsync[T <: CommandResponse](command: Command[T]): Future[T] = {
    getClient(command).apply(toReq(command)).map {
      resp =>
        command.getResponse(new ChannelBufferInputStream(resp.getContent), resp.getStatus.getCode)
    }
  }

  def toReq(cmd: Command[_]): HttpRequest = {
    val method = cmd.getHttpMethod match {
      case Method.GET => HttpMethod.GET
      case Method.PUT => HttpMethod.PUT
      case Method.POST => HttpMethod.POST
      case Method.DELETE => HttpMethod.DELETE
    }
    val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, cmd.getEndpoint)
    req.addHeader(cmd.getResponseType.getHeaderName, cmd.getResponseType.getHeaderValue)
    req.addHeader(HttpHeaders.Names.HOST, getEndpoint.getHost)
    req.addHeader(HerokuApiVersion.HEADER, HerokuApiVersion.v2.getHeaderValue)
    req.addHeader(HttpHeaders.Names.HOST, getHostHeader(cmd.getEndpoint))

    if (loginResponse != null) {
      req.addHeader(HttpHeaders.Names.AUTHORIZATION, ProxyCredentials("", loginResponse.api_key()).basicAuthorization)
    }

    cmd.getHeaders.foreach {
      _ match {
        case (k, v) => req.addHeader(k, v)
      }
    }
    if (cmd.hasBody) req.setContent(ChannelBuffers.wrappedBuffer(cmd.getBody.getBytes("UTF-8")))

    req
  }

  def getEmail: String = loginResponse.email()

  def getApiKey: String = loginResponse.api_key()

  def getApi: HerokuAPI = new HerokuAPI(this)

  def getUrl(cmdEndpoint: String): URL = {
    if (cmdEndpoint.startsWith("https//") || cmdEndpoint.startsWith("http://")) {
      HttpUtil.toURL(cmdEndpoint)
    } else {
      getEndpoint
    }
  }

  def getPath(cmdEndpoint: String): String = {
    if (cmdEndpoint.startsWith("https//") || cmdEndpoint.startsWith("http://")) {
      HttpUtil.toURL(cmdEndpoint).getPath
    } else {
      cmdEndpoint
    }
  }

  def getPort(url: URL): Int = {
    if (url.getPort == -1) url.getDefaultPort
    else url.getPort
  }

  def getHostHeader(cmdEndpoint: String): String = {
    val url = getUrl(cmdEndpoint)
    val host = url.getHost
    var port = ""
    if (url.getPort != url.getDefaultPort) port = ":" + url.getPort.toString
    host + port
  }

  def getClient(cmd: Command[_]): HttpService = {
    val url = getUrl(cmd.getEndpoint)
    clients.getOrElseUpdate((url.getHost, getPort(url)), {
      ClientBuilder()
        .codec(Http())
        .hosts(new InetSocketAddress(url.getHost, getPort(url)))
        .hostConnectionLimit(10)
        .tlsWithoutValidation()
        .build()
    })
  }

  def releaseClients() {
    clients.keys.foreach {
      hp => clients.remove(hp).foreach(_.release())
    }
  }
}