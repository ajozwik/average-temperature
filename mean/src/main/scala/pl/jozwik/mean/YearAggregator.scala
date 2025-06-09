package pl.jozwik.mean

import com.typesafe.scalalogging.StrictLogging
import pl.jozwik.mean.model.Temperature

import java.time.temporal.ChronoUnit
import java.time.LocalDate
import scala.annotation.tailrec

object YearAggregator extends StrictLogging {

  def yearAggregatorTemperature(year: Int, hourlyData: HourlyData)(implicit meanModel: (LocalDate, HourlyData) => Option[Temperature]): Temperature = {
    val start = LocalDate.ofYearDay(year, 1)
    val end   = start.plusYears(1).minusDays(1)
    val days  = ChronoUnit.DAYS.between(start, end) + 1
    val sum   = loop(hourlyData, start, end, 0)
    Temperature(sum / days)
  }

  @tailrec
  private def loop(hourlyData: HourlyData, day: LocalDate, to: LocalDate, sum: BigDecimal)(implicit
      meanModel: (LocalDate, HourlyData) => Option[Temperature]
  ): BigDecimal =
    if (day.isBefore(to)) {
      val m = mean(hourlyData, day)(meanModel)
      loop(hourlyData, day.plusDays(1), to, sum + m)
    } else {
      sum
    }

  private def mean(hourlyData: HourlyData, day: LocalDate)(implicit meanModel: (LocalDate, HourlyData) => Option[Temperature]): BigDecimal =
    meanModel(day, hourlyData) match {
      case Some(m) =>
        m.celsius
      case _ =>
        sys.error("Wrong data")
    }
}
