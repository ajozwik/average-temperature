package pl.jozwik.mean

import com.typesafe.scalalogging.StrictLogging
import pl.jozwik.mean.model.Temperature

import java.time.temporal.ChronoUnit
import java.time.LocalDate
import scala.annotation.tailrec

object YearAggregator extends StrictLogging {

  def yearAggregatorTemperature(year: Int, hourlyData: HourlyData): (Temperature, Temperature) = {
    val start          = LocalDate.ofYearDay(year, 1)
    val end            = start.plusYears(1).minusDays(1)
    val days           = ChronoUnit.DAYS.between(start, end) + 1
    val (mean4, mean8) = loop(hourlyData, start, end, 0, 0)
    (Temperature(mean4 / days), Temperature(mean8 / days))
  }

  @tailrec
  private def loop(hourlyData: HourlyData, day: LocalDate, to: LocalDate, sum4: BigDecimal, sum8: BigDecimal): (BigDecimal, BigDecimal) =
    if (day.isBefore(to)) {
      val (mean4, mean8) = mean(hourlyData, day)
      loop(hourlyData, day.plusDays(1), to, sum4 + mean4, sum8 + mean8)
    } else {
      (sum4, sum8)
    }

  private def mean(hourlyData: HourlyData, day: LocalDate): (BigDecimal, BigDecimal) =
    (Mean.mean8(day, hourlyData), Mean.mean4(day, hourlyData)) match {
      case (Some(mean4), Some(mean8)) =>
        (mean4.celsius, mean8.celsius)
      case _ =>
        (0, 0)
    }
}
