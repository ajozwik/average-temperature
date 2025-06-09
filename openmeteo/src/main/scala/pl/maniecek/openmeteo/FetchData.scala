package pl.maniecek.openmeteo

import pl.maniecek.data.DownloadUtils
import java.io.File
import java.time.{ LocalDate, ZoneId }
import scala.util.Try

object FetchData {

  private val Latitude    = "_Latitude_"
  private val Longitude   = "_Longitude_"
  private val StartDate   = "_StartDate_"
  private val EndDate     = "_EndDate_"
  private val TimeZone    = "_TimeZone_"
  private val templateUrl =
    s"https://archive-api.open-meteo.com/v1/archive?latitude=$Latitude&longitude=$Longitude&start_date=$StartDate&end_date=$EndDate&hourly=temperature_2m&timezone=$TimeZone&format=csv"

  def downloadToCsv(latitude: BigDecimal, longitude: BigDecimal)(startDate: LocalDate, endDate: LocalDate, tz: ZoneId)(outputFile: File): Try[File] = {
    val params =
      Map(Latitude -> latitude.toString, Longitude -> longitude.toString, StartDate -> startDate.toString, EndDate -> endDate.toString, TimeZone -> tz.getId)
    DownloadUtils.downloadToFile(templateUrl, outputFile, params)
  }
}
