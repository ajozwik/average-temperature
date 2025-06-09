package pl.maniecek.openmeteo

import com.github.tototoshi.csv.CSVReader
import pl.maniecek.average.ScalaApp
import pl.maniecek.fileData.HourlyDataHelper

import java.io.File
import java.time.{ LocalDate, ZoneId }
import scala.annotation.tailrec
import scala.util.{ Failure, Success, Using }

object Main extends ScalaApp {
  private val tmpDir    = new File(scala.util.Properties.tmpDir, args.lift(3).getOrElse("weather"))
  private val outputDir = new File(scala.util.Properties.tmpDir, args.lift(3).getOrElse("output"))
  private val inputCsv  = new File(args.headOption.getOrElse(sys.error("Provide csv with places: <file.csv> [fromYear] [toYear] [tmpDir] [outputDir]")))

  private val startYear = args.lift(1).map(_.toInt).getOrElse(2000)
  private val endYear   = args.lift(2).map(_.toInt).getOrElse(LocalDate.now.getYear - 1)

  logger.debug(s"${inputCsv.getAbsolutePath}  ${tmpDir.getAbsolutePath}")
  for {
    _ <-
      Using(CSVReader.open(inputCsv)) { reader =>
        reader.readNext()
        downloadWeather(reader, startYear, endYear)
      }
  } {
    HourlyDataHelper.allFromDir(tmpDir, startYear, endYear)(outputDir)
  }

  @tailrec
  private def downloadWeather(reader: CSVReader, start: Int, end: Int): Unit = reader.readNext() match {
    case Some(latitude +: longitude +: city +: timezone +: _) =>
      val outputFile = new File(tmpDir, s"$city.csv")
      val zoneId     = ZoneId.of(timezone)
      if (outputFile.exists()) {
        logger.warn(s"Probably you already downloaded ${outputFile.getAbsolutePath}, skipping...")
      } else {
        FetchData
          .downloadToCsv(latitude.toDouble, longitude.toDouble)(LocalDate.ofYearDay(start, 1), LocalDate.ofYearDay(end, 1).plusYears(1).minusDays(1), zoneId)(
            outputFile
          ) match {
          case Success(f) =>
            logger.debug(s"Stored to $f")
          case Failure(th) =>
            logger.error("", th)
        }
      }
      downloadWeather(reader, start, end)
    case _ =>
  }

}
