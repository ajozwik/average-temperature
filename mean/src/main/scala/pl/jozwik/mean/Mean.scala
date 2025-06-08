package pl.jozwik.mean

import pl.jozwik.mean.model.Temperature

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object Mean {
  private val allDayHour = ChronoUnit.DAYS.getDuration.toHours
  private val filter8    = (0 to 7).map(_ * 3).toSet

  def mean8(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val size = filter8.size
    val data = hourlyData.hourlyData(day)
    data.filter { case (k, _) => filter8.contains(k) }.values.map(_.celsius).toList match {
      case seq if seq.size == size =>
        val sum = seq.sum
        val a   = sum / size
        Option(Temperature(a))
      case _ =>
        None
    }
  }

  def mean4(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val map    = hourlyData.hourlyData(day)
    val values = map.values.map(_.celsius)
    if (map.size == allDayHour) {
      (map.get(6), map.get(18), values.minOption, values.maxOption) match {
        case (Some(t6), Some(t18), Some(min), Some(max)) =>
          val sum = t6.celsius + t18.celsius + min + max
          val a   = sum / 4
          Option(Temperature(a))
        case _ =>
          None
      }

    } else {
      None
    }
  }
}
