package pl.maniecek.fileData

import com.github.tototoshi.csv.*
import com.typesafe.scalalogging.StrictLogging
import pl.jozwik.mean.{DateTimeUtils, HourlyData}
import pl.jozwik.mean.model.Temperature
import pl.maniecek.mean.MapHourlyData

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.io.Source
import scala.util.{ Failure, Success, Try, Using }

object HourlyDataHelper extends StrictLogging {

  def fromCsv(source: Source)(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): Try[HourlyData] = Try {
    val reader = CSVReader.open(source)
    val map    = reader.iterator.foldLeft(Map.empty[LocalDateTime, Temperature]) { case (acc, a +: b +: _) =>
      (DateTimeUtils.parseDateTime(a, formatter), b.toDoubleOption) match {
        case (Success(dt), Some(t)) =>
          acc + (dt -> Temperature(t))
        case (Success(_), None) =>
          acc
        case (Failure(th), _) =>
          logger.error("", th)
          acc
      }
    }
    MapHourlyData(map)
  }

  def fromCsvFile(file: File)(formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME): Try[HourlyData] = Using(Source.fromFile(file)) { reader =>
    fromCsv(reader)(formatter)
  }.flatten

}
