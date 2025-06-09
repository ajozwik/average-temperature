package pl.maniecek.average

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.Try

object DateTimeUtils {
  def parseDateTime(txt: String, formatter: DateTimeFormatter): Try[LocalDateTime] = Try {
    LocalDateTime.parse(txt, formatter)
  }
}
