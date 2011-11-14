package com.heroku.api.connection

import com.twitter.util.Future
import java.lang.String
import com.heroku.api.HerokuAPI
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import java.net.{InetSocketAddress, URL}
import com.heroku.api.http._
import org.jboss.netty.buffer.{ChannelBufferInputStream, ChannelBuffers}
import collection.mutable.HashMap
import com.twitter.finagle.Service
import org.jboss.netty.handler.codec.http._
import sun.misc.BASE64Encoder
import com.heroku.api.command.{CommandUtil, LoginCommand, Command, CommandResponse}


class FinagleConnection(val loginCommand: LoginCommand) extends Connection[Future[_]] {

  type HttpService = Service[HttpRequest, HttpResponse]

  val clients = new HashMap[(String, Int), HttpService]

  val encoder = new BASE64Encoder

  def buildClient()

  val client = ClientBuilder()
    .codec(Http())
    .hosts(new InetSocketAddress(getEndpoint.getHost, getEndpoint.getPort))
    .hostConnectionLimit(10)
    .build()

  val loginResponse = executeCommand(loginCommand)

  def executeCommand[T <: CommandResponse](command: Command[T]): T = executeCommandAsync(command).get()

  def executeCommandAsync[T <: CommandResponse](command: Command[T]): Future[T] = {
    client(toReq(command)).map(resp => command.getResponse(new ChannelBufferInputStream(resp.getContent), resp.getStatus.getCode))
  }

  def toReq(cmd: Command[_]): HttpRequest = {
    val method = cmd.getHttpMethod match {
      case Method.GET => HttpMethod.GET
      case Method.PUT => HttpMethod.PUT
      case Method.POST => HttpMethod.POST
      case Method.DELETE => HttpMethod.DELETE
    }
    val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, CommandUtil.getCommandEndpoint(getEndpoint, cmd.getEndpoint).toString)
    req.addHeader(HerokuApiVersion.HEADER, HerokuApiVersion.v2)
    req.addHeader(cmd.getResponseType.getHeaderName, cmd.getResponseType.getHeaderValue)
    if (loginResponse != null) {
      req.addHeader(HttpHeaders.Names.AUTHORIZATION, "Basic " + encoder.encode((loginResponse.email() + ":" + loginResponse.api_key()).getBytes))
    }
    req.
      cmd.getHeaders.foreach {
      _ match {
        case (k, v) => req.addHeader(k, v)
      }
    }
    if (cmd.hasBody) {
      req.setContent(ChannelBuffers.wrappedBuffer(cmd.getBody.getBytes))
    }
    req
  }

  val getEndpoint: URL = HttpUtil.toURL(loginCommand.getApiEndpoint)

  def getEmail: String = loginResponse.email()

  def getApiKey: String = loginResponse.api_key()

  def getApi: HerokuAPI = new HerokuAPI(this)
}