package pl.jozwik.openmeteo

import pl.maniecek.mean.ScalaApp

import java.io.File
import java.time.{ LocalDate, ZoneId }
import scala.util.{ Failure, Success }

object FetchDataSpec extends ScalaApp {

  private val latitude   = BigDecimal("47.5585")
  private val longitude  = BigDecimal("7.58085")
  private val tz         = ZoneId.of("Europe/Warsaw")
  private val outputFile = new File(scala.util.Properties.tmpDir, "Basel.csv")

  fetch(latitude, longitude)(tz)

  private def fetch(latitude: BigDecimal, longitude: BigDecimal)(zoneId: ZoneId): Unit = {
    val startDate = LocalDate.ofYearDay(2000, 1)
    val endDate   = startDate.plusYears(25)

    FetchData.downloadToCsv(latitude, longitude)(startDate, endDate, zoneId)(outputFile) match {
      case Success(f) =>
        logger.debug(s"Stored to $f")
      case Failure(th) =>
        logger.error("", th)
    }
  }
}
