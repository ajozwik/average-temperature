package pl.maniecek.data

import com.typesafe.scalalogging.StrictLogging
import sttp.client4.*

import java.io.File
import scala.util.Try

object DownloadUtils extends StrictLogging {

  private val backend = DefaultSyncBackend()

  def downloadToString(url: String, params: Map[String, String] = Map.empty[String, String]): Try[String] =
    for {
      response <- sendRequest(url, params)
    } yield {
      response.send(backend).body match {
        case Right(r) =>
          r
        case Left(error) =>
          throw new IllegalStateException(error)
      }
    }

  def downloadToFile(url: String, outputFile: File, params: Map[String, String] = Map.empty[String, String]): Try[File] =
    for {
      response <- sendRequest(url, params)
    } yield {
      response.response(asFile(outputFile)).send(backend).body match {
        case Right(r) =>
          r
        case Left(error) =>
          throw new IllegalStateException(error)
      }
    }

  private def sendRequest(url: String, params: Map[String, String]): Try[Request[Either[String, String]]] = Try {
    val urlWithParams = params.foldLeft(url) { case (acc, (k, v)) => acc.replace(k, v) }
    logger.debug(s"Send $urlWithParams")
    basicRequest.get(uri"$urlWithParams")
  }

}
