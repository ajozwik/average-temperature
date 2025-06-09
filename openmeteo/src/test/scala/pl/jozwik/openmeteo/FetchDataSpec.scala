package pl.jozwik.openmeteo

import pl.maniecek.mean.ScalaApp

import java.io.File
import java.time.{ LocalDate, ZoneId }
import scala.util.{ Failure, Success }

object FetchDataSpec extends ScalaApp {
  private val latitude   = BigDecimal("50.8165")
  private val longitude  = BigDecimal("20.9383")
  private val startDate  = LocalDate.ofYearDay(2000, 1)
  private val endDate    = startDate.plusYears(25)
  private val tz         = ZoneId.of("Europe/Warsaw")
  private val outputFile = new File(scala.util.Properties.tmpDir, "Belno.csv")
  FetchData.downloadToCsv(latitude, longitude)(startDate, endDate, tz)(outputFile) match {
    case Success(f) =>
      logger.debug(s"Stored to $f")
    case Failure(th) =>
      logger.error("", th)
  }
}
