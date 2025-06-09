package pl.maniecek.fileData

import pl.maniecek.average.ScalaApp

import java.io.File

object Main extends ScalaApp {

  try {
    run()
  } catch {
    case th: Throwable =>
      logger.error("", th)
  }

  private def run(): Unit = {
    val directory = new File("/tmp/weather")
    HourlyDataHelper.allFromDir(directory, 2000, 2024)

  }

}
