package pl.maniecek.fileData

import pl.jozwik.mean.{ Mean, YearAggregator }
import pl.maniecek.mean.AbstractSpec

import java.time.format.DateTimeFormatter
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
        case Some((from, _)) =>
          val year  = from.getYear
          val mean2 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean2)
          val mean4 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean4)
          val mean8 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean8)
          logger.debug(s"${mean2.celsius} ${mean4.celsius} ${mean8.celsius}")
        case _ =>
      }

    }
  }

}
