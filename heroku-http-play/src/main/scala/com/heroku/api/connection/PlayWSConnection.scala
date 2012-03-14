package com.heroku.api.connection

import play.api.libs.concurrent.Promise
import com.heroku.api.Heroku
import com.heroku.api.http.Http
import play.api.libs.ws.{Response, WS}
import collection.JavaConverters._
import com.ning.http.client.Realm
import com.heroku.api.request.{LoginRequest, Request}
import com.heroku.api.request.login.BasicAuthLogin


class PlayWSConnection(val config: Either[LoginRequest, String]) extends AsyncConnection[Promise[_]] with MultiUserAsyncConnection[Promise[_]] {

  val apiKey: String = config match {
    case Left(login) => executeAsync(login, null).await.get.getApiKey
    case Right(key) => key
  }


  def executeAsync[T](request: Request[T]): Promise[T] = executeAsync(request, apiKey)


  def executeAsync[T](request: Request[T], key: String): Promise[T] = {
    val host = Heroku.Config.ENDPOINT.value;
    val path = request.getEndpoint;
    var url = WS.url(host + path)
      .withHeaders(Heroku.ApiVersion.HEADER -> Heroku.ApiVersion.v2.getHeaderValue)
      .withHeaders(request.getResponseType.getHeaderName -> request.getResponseType.getHeaderValue)
      .withHeaders(request.getHeaders.asScala.toArray: _*)

    if (key != null) {
      url = url.withAuth("", getApiKey, Realm.AuthScheme.BASIC)
    }

    request.getHttpMethod match {
      case Http.Method.GET => toResponse(request, url.get())
      case Http.Method.PUT => toResponse(request, url.put(request.getBody))
      case Http.Method.POST => toResponse(request, url.post(request.getBody))
      case Http.Method.DELETE => toResponse(request, url.delete())
    }

  }

  def toResponse[T](req: Request[T], p: Promise[Response]): Promise[T] = p.map(res => req.getResponse(res.body.getBytes, res.status))

  def execute[T](request: Request[T]): T = executeAsync(request).await.get

  def getApiKey: String = apiKey

  def close() {}
}

object PlayWSConnection {
  def apply(cmd: LoginRequest): PlayWSConnection = new PlayWSConnection(Left(cmd))

  def apply(apiKey: String): PlayWSConnection = new PlayWSConnection(Right(apiKey))

  class Provider extends ConnectionProvider {
    def get(username: String, password: String): Connection = PlayWSConnection(new BasicAuthLogin(username, password))

    def get(apiKey: String): Connection = PlayWSConnection(apiKey)
  }

}
