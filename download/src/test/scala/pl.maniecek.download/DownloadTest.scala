package pl.maniecek.download

import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.mean.DownloadUtils

import java.net.{ URLDecoder, URLEncoder }
import java.nio.charset.StandardCharsets

object DownloadTest extends App with StrictLogging {
  val json =
    """{"units":{"temperature":"CELSIUS","velocity":"KILOMETER_PER_HOUR","length":"metric","energy":"watts"},"geometry":{"type":"MultiPoint","coordinates":[[7.57327,47.5584,279]],"locationNames":["Bazylea"]},"format":"csvTimeOriented","timeIntervals":["2024-01-01T+00:00\/2025-06-08T+00:00"],"timeIntervalsAlignment":"none","queries":[{"domain":"ERA5T","timeResolution":"hourly","codes":[{"code":11,"level":"2 m elevation corrected"}]}]}"""
  val str =
    """%7B%22units%22%3A%7B%22temperature%22%3A%22CELSIUS%22%2C%22velocity%22%3A%22KILOMETER_PER_HOUR%22%2C%22length%22%3A%22metric%22%2C%22energy%22%3A%22watts%22%7D%2C%22geometry%22%3A%7B%22type%22%3A%22MultiPoint%22%2C%22coordinates%22%3A%5B%5B7.57327%2C47.5584%2C279%5D%5D%2C%22locationNames%22%3A%5B%22Bazylea%22%5D%7D%2C%22format%22%3A%22csvTimeOriented%22%2C%22timeIntervals%22%3A%5B%222024-01-01T%2B00%3A00%5C%2F2025-06-08T%2B00%3A00%22%5D%2C%22timeIntervalsAlignment%22%3A%22none%22%2C%22queries%22%3A%5B%7B%22domain%22%3A%22ERA5T%22%2C%22timeResolution%22%3A%22hourly%22%2C%22codes%22%3A%5B%7B%22code%22%3A11%2C%22level%22%3A%222+m+elevation+corrected%22%7D%5D%7D%5D%7D^"""
  val decoded = URLEncoder.encode(json, StandardCharsets.UTF_8)

  private val out = DownloadUtils.downloadToString(
    """https://my.meteoblue.com/dataset/query?json=_JSON_&apikey=5838a18e295d&ts=1749387530&sig=636bdeacec874b58603a80de07b77cc3""",
    Map(
      "_JSON_" -> decoded
    )
  )

  logger.debug(s"$out")
}
