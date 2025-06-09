package pl.maniecek.fileData

import com.github.tototoshi.csv.*
import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.average.model.Temperature
import pl.maniecek.average.*

import java.io.{ File, PrintWriter }
import java.time.format.DateTimeFormatter
import java.time.{ LocalDate, LocalDateTime }
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

  def allFromDir(directory: File, startYear: Int, endYear: Int)(outputDir: File)(implicit codec: Codec): Unit = {
    directory.listFiles.foreach { file =>
      val hourlyData = fromCsvFileUnsafe(file)
      forOneModel(file.getName)(startYear, endYear, hourlyData)(outputDir)(Average.average2, "T2")
      forOneModel(file.getName)(startYear, endYear, hourlyData)(outputDir)(Average.average4, "T4")
      forOneModel(file.getName)(startYear, endYear, hourlyData)(outputDir)(Average.average8, "T8")
      forOneModel(file.getName)(startYear, endYear, hourlyData)(outputDir)(Average.average24, "T24")
    }
  }

  private def forOneModel(
      name: String
  )(startYear: Int, endYear: Int, hourlyData: HourlyData)(
      outputDir: File
  )(average: (LocalDate, HourlyData) => Either[String, Temperature], averageName: String): Unit = {
    val file = new File(outputDir, s"${name}_$averageName.csv")
    file.getParentFile.mkdirs()
    val output = new PrintWriter(file)
    output.println("year,temperature")
    for { year <- (startYear to endYear) } {
      val a2 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(average)
      logger.debug(s"$name $year ${a2.print}")
      output.println(s"$year,${a2.celsius}")
    }
    logger.debug(s"Store results to ${file.getAbsolutePath}")
    output.close()
  }
}
