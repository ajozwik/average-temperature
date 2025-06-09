package pl.maniecek.fileData

import pl.jozwik.mean.{ HourlyData, Mean, YearAggregator }
import pl.maniecek.mean.ScalaApp

import java.io.File
import scala.util.{ Failure, Success }

object Main extends ScalaApp {

  try {
    run()
  } catch {
    case th: Throwable =>
      logger.error("", th)
  }

  private def run(): Unit = {
    val directory = new File("/tmp/weather")
    directory.listFiles.foreach { file =>
      val hourlyData = fromCsvFile(file)
      for { i <- (2000 to 2024) } {
        aggregateForYear(file.getName)(i, hourlyData)
      }

    }

  }

  private def aggregateForYear(name: String)(year: Int, hourlyData: HourlyData): Unit = {
    val mean2  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean2)
    val mean4  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean4)
    val mean8  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean8)
    val mean24 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Mean.mean24)
    logger.debug(s"$name $year mean2=${mean2.print}, mean4=${mean4.print}, mean8=${mean8.print} mean24=${mean24.print}")
  }

  private def fromCsvFile(file: File): HourlyData =
    HourlyDataHelper.fromCsvFile(file) match {
      case Success(hd) =>
        hd
      case Failure(th) =>
        throw th
    }
}
