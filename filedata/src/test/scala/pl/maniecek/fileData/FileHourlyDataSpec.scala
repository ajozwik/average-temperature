package pl.maniecek.fileData

import pl.jozwik.mean.{ HourlyData, Mean }
import pl.maniecek.mean.AbstractSpec

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.annotation.tailrec
import scala.io.{ Codec, Source }

class FileHourlyDataSpec extends AbstractSpec {

  "FileHourlyDataSpec" should {
    "Read csv " in {
      val input      = getClass.getResourceAsStream("/bazylea.csv")
      val source     = Source.fromInputStream(input)(Codec.UTF8)
      val formatter  = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm")
      val hourlyData = HourlyDataHelper.fromCsv(source)(formatter).success.value
      val fromTo     = hourlyData.fromTo
      fromTo should not be empty
      fromTo match {
        case Some((from, to)) =>
          val (mean4, mean8) = loop(hourlyData, from, to, 0, 0)
          logger.debug(s"$mean4 $mean8")
      }

    }
  }
  @tailrec
  private def loop(hourlyData: HourlyData, day: LocalDate, to: LocalDate, sum4: BigDecimal, sum8: BigDecimal): (BigDecimal, BigDecimal) =
    if (day.isBefore(to)) {
      val (mean4, mean8) = mean(hourlyData, day)
      loop(hourlyData, day.plusDays(1), to, sum4 + mean4, sum8 + mean8)
    } else {
      (sum4, sum8)
    }

  private def mean(hourlyData: HourlyData, day: LocalDate): (BigDecimal, BigDecimal) = {
    (Mean.mean8(day, hourlyData), Mean.mean4(day, hourlyData)) match {
      case (Some(mean4), Some(mean8)) =>
//        logger.debug(s"$day mean4=${mean4.celsius} mean8=${mean8.celsius} ${mean4.celsius > mean8.celsius}")
        (mean4.celsius, mean8.celsius)
      case _ =>
        (0, 0)
    }

  }
}
