package pl.maniecek.fileData

import pl.jozwik.mean.{ HourlyData, YearAggregator }
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
    val (mean4, mean8) = YearAggregator.yearAggregatorTemperature(year, hourlyData)
    logger.debug(s"$name $year mean4=${mean4.celsius}, mean8=${mean8.celsius}  diff=${mean4.celsius - mean8.celsius}")
  }

  private def fromCsvFile(file: File): HourlyData =
    HourlyDataHelper.fromCsvFile(file) match {
      case Success(hd) =>
        hd
      case Failure(th) =>
        throw th
    }
}
