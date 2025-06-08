package pl.maniecek.download

import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.mean.DownloadUtils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object DownloadTest extends App with StrictLogging {
  private val json =
    """{"units":{"temperature":"CELSIUS","velocity":"KILOMETER_PER_HOUR","length":"metric","energy":"watts"},"geometry":{"type":"MultiPoint","coordinates":[[7.57327,47.5584,279]],"locationNames":["Bazylea"]},"format":"csvTimeOriented","timeIntervals":["2024-01-01T+00:00\/2025-06-08T+00:00"],"timeIntervalsAlignment":"none","queries":[{"domain":"ERA5T","timeResolution":"hourly","codes":[{"code":11,"level":"2 m elevation corrected"}]}]}"""

  private val decoded = URLEncoder.encode(json, StandardCharsets.UTF_8)

  private val out = DownloadUtils.downloadToString(
    """https://my.meteoblue.com/dataset/query?json=_JSON_&apikey=5838a18e295d&ts=1749387530&sig=636bdeacec874b58603a80de07b77cc3""",
    Map(
      "_JSON_" -> decoded
    )
  )

  logger.debug(s"$out")
}
