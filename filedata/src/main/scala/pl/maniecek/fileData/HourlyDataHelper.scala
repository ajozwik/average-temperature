package pl.maniecek.fileData

import com.github.tototoshi.csv.*
import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.average.{ AverageHelper, DateTimeUtils, HourlyData, MapHourlyData }
import pl.maniecek.average.model.Temperature

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.io.{ Codec, Source }
import scala.util.{ Failure, Success, Try, Using }

object HourlyDataHelper extends StrictLogging {

  def fromCsv(source: Source)(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): Try[HourlyData] = Try {
    val reader = CSVReader.open(source)
    val map    = reader.iterator.foldLeft(Map.empty[LocalDateTime, Temperature]) { (acc, seq) =>
      seq match {
        case a +: b +: _ =>
          (DateTimeUtils.parseDateTime(a, formatter), b.toDoubleOption) match {
            case (Success(dt), Some(t)) =>
              acc + (dt -> Temperature(t))
            case (Success(_), None) =>
              acc
            case (Failure(th), _) =>
              logger.trace("", th)
              acc
          }
        case _ =>
          acc
      }
    }
    MapHourlyData(map)
  }

  def fromCsvFileUnsafe(file: File)(implicit codec: Codec): HourlyData =
    fromCsvFile(file) match {
      case Success(hd) =>
        hd
      case Failure(th) =>
        throw th
    }

  def fromCsvFile(file: File, formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME)(implicit codec: Codec): Try[HourlyData] =
    Using(Source.fromFile(file)) { reader =>
      fromCsv(reader)(formatter)
    }.flatten

  def allFromDir(directory: File, startYear: Int, endYear: Int)(implicit codec: Codec): Unit = {
    directory.listFiles.foreach { file =>
      val hourlyData = fromCsvFileUnsafe(file)
      for { i <- (startYear to endYear) } {
        AverageHelper.aggregateForYear(file.getName)(i, hourlyData)
      }

    }
  }

}
