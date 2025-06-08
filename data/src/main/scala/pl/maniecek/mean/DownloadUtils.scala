package pl.maniecek.mean

import com.typesafe.scalalogging.StrictLogging
import sttp.client4.*

object DownloadUtils extends StrictLogging {

  private val backend = DefaultSyncBackend()

  def downloadToString(url: String, params: Map[String, String] = Map.empty): String = {
    val urlWithParams = params.foldLeft(url) { case (acc, (k, v)) => acc.replace(k, v) }
    val request       = basicRequest.get(uri"$urlWithParams")
    val uri           = request.uri.toString()
    logger.debug(s"$uri")
    val response = request.send(backend)
    response.body match {
      case Right(r) =>
        r
      case Left(error) =>
        error
    }
  }
}
