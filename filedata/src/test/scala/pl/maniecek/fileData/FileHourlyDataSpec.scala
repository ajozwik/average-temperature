package pl.maniecek.fileData

import pl.maniecek.mean.AbstractSpec

import scala.io.{ Codec, Source }

class FileHourlyDataSpec extends AbstractSpec {

  "FileHourlyDataSpec" should {
    "Read csv " in {
      val input  = getClass.getResourceAsStream("/bazylea.csv")
      val source = Source.fromInputStream(input)(Codec.UTF8)
      val txt    = HourlyDataHelper.fromCsv(source).success.value
      txt.fromTo should not be empty
    }
  }
}
