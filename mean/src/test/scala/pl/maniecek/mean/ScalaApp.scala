package pl.maniecek.mean

import com.typesafe.scalalogging.StrictLogging

import scala.io.Codec

@SuppressWarnings(Array("org.wartremover.warts.ScalaApp"))
trait ScalaApp extends App with StrictLogging {
  protected implicit val codec: Codec = Codec.UTF8
}
