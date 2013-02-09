package com.heroku.api.connection

import com.heroku.api.Heroku
import com.heroku.api.http.Http
import play.api.libs.ws.{Response, WS}
import collection.JavaConverters._
import com.ning.http.client.Realm
import com.heroku.api.request.Request
import concurrent.{Await, Future}
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.duration._

trait PlayConnection extends AsyncConnection[Future[_]] {
  def executeAsync[T](request: Request[T], apiKey: String): Future[T]

  def execute[T](request: Request[T], apiKey: String): T
}

class PlayWSConnection(val host: String) extends PlayConnection {

  def this() {
    this(Heroku.Config.ENDPOINT.value)
  }

  def executeAsync[T](request: Request[T], key: String): Future[T] = {

    val path = request.getEndpoint
    var url = WS.url(host + path)
      .withHeaders(Heroku.ApiVersion.HEADER -> Heroku.ApiVersion.v2.getHeaderValue)
      .withHeaders(Http.UserAgent.LATEST.getHeaderName -> Http.UserAgent.LATEST.getHeaderValue("playws"))
      .withHeaders(request.getResponseType.getHeaderName -> request.getResponseType.getHeaderValue)
      .withHeaders(request.getHeaders.asScala.toArray: _*)

    if (key != null) {
      url = url.withAuth("", key, Realm.AuthScheme.BASIC)
    }

    val body = if (request.hasBody) request.getBody else ""

    request.getHttpMethod match {
      case Http.Method.GET => toResponse(request, url.get())
      case Http.Method.PUT => toResponse(request, url.put(body))
      case Http.Method.POST => toResponse(request, url.post(body))
      case Http.Method.DELETE => toResponse(request, url.delete())
    }

  }

  def toResponse[T](req: Request[T], p: Future[Response]): Future[T] = p.map(res => req.getResponse(res.body.getBytes, res.status))

  def execute[T](request: Request[T], key: String): T = Await.result(executeAsync(request, key), Duration(60, SECONDS))

  def close() {}
}

object PlayWSConnection {

  def apply(): PlayWSConnection = new PlayWSConnection()

  class Provider extends ConnectionProvider {

    def getConnection(): Connection = PlayWSConnection()
  }

}
