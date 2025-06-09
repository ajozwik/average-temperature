package pl.maniecek.average

import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.average.model.Temperature

import java.time.temporal.ChronoUnit
import java.time.LocalDate
import scala.annotation.tailrec

object YearAggregator extends StrictLogging {

  def yearAggregatorTemperature(year: Int, hourlyData: HourlyData)(implicit average: (LocalDate, HourlyData) => Either[String, Temperature]): Temperature = {
    val start = LocalDate.ofYearDay(year, 1)
    val end   = start.plusYears(1).minusDays(1)
    val days  = ChronoUnit.DAYS.between(start, end) + 1
    val sum   = loop(hourlyData, start, end, 0)
    Temperature(sum / days)
  }

  @tailrec
  private def loop(hourlyData: HourlyData, day: LocalDate, to: LocalDate, sum: BigDecimal)(implicit
      average: (LocalDate, HourlyData) => Either[String, Temperature]
  ): BigDecimal =
    if (day.isBefore(to)) {
      val m = averageUnsafe(hourlyData, day)(average)
      loop(hourlyData, day.plusDays(1), to, sum + m)
    } else {
      sum
    }

  private def averageUnsafe(hourlyData: HourlyData, day: LocalDate)(implicit average: (LocalDate, HourlyData) => Either[String, Temperature]): BigDecimal =
    average(day, hourlyData) match {
      case Right(m) =>
        m.celsius
      case Left(error) =>
        sys.error(s"Wrong data $day, error $error")
    }
}
