package pl.maniecek.fileData

import pl.jozwik.mean.YearAggregator
import pl.maniecek.mean.AbstractSpec

import java.time.format.DateTimeFormatter
import scala.io.{ Codec, Source }

class FileHourlyDataSpec extends AbstractSpec {
  private val days = 365

  "FileHourlyDataSpec" should {
    "Read csv " in {
      val input      = getClass.getResourceAsStream("/bazylea.csv")
      val source     = Source.fromInputStream(input)(Codec.UTF8)
      val formatter  = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm")
      val hourlyData = HourlyDataHelper.fromCsv(source)(formatter).success.value
      val fromTo     = hourlyData.fromTo
      fromTo should not be empty
      fromTo match {
        case Some((from, _)) =>
          val (mean4, mean8) = YearAggregator.yearAggregatorTemperature(from.getYear, hourlyData)
          logger.debug(s"${mean4.celsius / days} ${mean8.celsius / days}")
        case _ =>
      }

    }
  }

}
