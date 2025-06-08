package pl.maniecek.fileData

import com.github.tototoshi.csv.*
import pl.jozwik.mean.model.Temperature
import pl.maniecek.mean.{ HourlyData, MapHourlyData }

import java.io.File
import java.time.LocalDateTime
import scala.io.Source
import scala.util.{ Try, Using }

object HourlyDataHelper {

  def fromCsv(source: Source): Try[HourlyData] = Try {
    val reader = CSVReader.open(source)
    val map    = reader.iterator.foldLeft(Map.empty[LocalDateTime, Temperature]) { case (acc, line) =>
      acc
    }
    MapHourlyData(map)
  }

  def fromCsv(file: File): Try[HourlyData] = Using(Source.fromFile(file)) { reader =>
    fromCsv(reader)
  }.flatten

}
