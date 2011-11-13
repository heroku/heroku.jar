package com.heroku.api.connection

import com.twitter.util.Future
import java.lang.String
import com.heroku.api.command.{LoginCommand, Command, CommandResponse}
import com.heroku.api.HerokuAPI
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.http.Http
import java.net.{InetSocketAddress, URL}
import org.jboss.netty.handler.codec.http.{HttpMethod, HttpVersion, DefaultHttpRequest, HttpRequest}
import org.jboss.netty.buffer.ChannelBuffers
import com.heroku.api.http._
import collection.JavaConversions._


class TwitterFutureWrapper[T <: CommandResponse](f: Future[T]) extends FutureWrapper[Future[T]](f) {
  def getFuture = future;
}

class FinagleConnection(val loginCommand: LoginCommand) extends Connection[TwitterFutureWrapper[_ <: CommandResponse]] {

  val client = ClientBuilder()
    .codec(Http())
    .hosts(new InetSocketAddress(getEndpoint.getHost, getEndpoint.getPort))
    .hostConnectionLimit(10)
    .build()

  val loginResponse = executeCommand(loginCommand)

  def executeCommand[T <: CommandResponse](command: Command[T]): T = executeCommandAsync(command).getFuture.get()

  def executeCommandAsync[T <: CommandResponse](command: Command[T]): TwitterFutureWrapper[T] = {
    val req = toReq(command)
    val future = client(req).map(resp => command.getResponse(resp.getContent.array(), resp.getStatus.getCode))
    new TwitterFutureWrapper(future)
  }

  def toReq(cmd: Command[_]): HttpRequest = {
    val method = cmd.getHttpMethod match {
      case Method.GET => HttpMethod.GET
      case Method.PUT => HttpMethod.PUT
      case Method.POST => HttpMethod.POST
      case Method.DELETE => HttpMethod.DELETE
    }
    val req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, method, getEndpoint.toString + cmd.getEndpoint)
    req.addHeader(HerokuApiVersion.HEADER, HerokuApiVersion.v2)
    req.addHeader(cmd.getResponseType.getHeaderName, cmd.getResponseType.getHeaderValue)
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

  def getEndpoint: URL = HttpUtil.toURL(loginCommand.getApiEndpoint)

  def getEmail: String = loginResponse.email()

  def getApiKey: String = loginResponse.api_key()

  def getApi: HerokuAPI = new HerokuAPI(this)
}