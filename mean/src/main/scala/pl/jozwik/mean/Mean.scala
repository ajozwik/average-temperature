package pl.jozwik.mean

import pl.jozwik.mean.model.Temperature

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object Mean {
  private val allDayHour  = ChronoUnit.DAYS.getDuration.toHours.toInt
  private val filter8     = (0 to 7).map(_ * 3).toSet
  private val filter8size = filter8.size

  def mean24(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val data = hourlyData.hourlyData(day)
    if (data.sizeIs == allDayHour) {
      val a = average(data.values)
      Option(Temperature(a))
    } else {
      None
    }
  }

  def mean8(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val data = hourlyData.hourlyData(day)
    data.filter { case (k, _) => filter8.contains(k) }.values.map(_.celsius).toSeq match {
      case seq if seq.sizeIs == filter8size =>
        val a = average(seq*)
        Option(Temperature(a))
      case _ =>
        None
    }
  }

  def mean4(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val data   = hourlyData.hourlyData(day)
    val values = data.values.map(_.celsius)
    if (data.sizeIs == allDayHour) {
      (data.get(6), data.get(18), values.minOption, values.maxOption) match {
        case (Some(t6), Some(t18), Some(min), Some(max)) =>
          val a = average(min, max, t6.celsius, t18.celsius)
          Option(Temperature(a))
        case _ =>
          None
      }
    } else {
      None
    }
  }

  def mean2(day: LocalDate, hourlyData: HourlyData): Option[Temperature] = {
    val data   = hourlyData.hourlyData(day)
    val values = data.values.map(_.celsius)
    if (data.sizeIs == allDayHour) {
      (values.minOption, values.maxOption) match {
        case (Some(min), Some(max)) =>
          val a = average(min, max)
          Option(Temperature(a))
        case _ =>
          None
      }
    } else {
      None
    }
  }

  private def average(t: Iterable[Temperature]): BigDecimal =
    average(t.map(_.celsius).toSeq*)

  private def average(d: BigDecimal*): BigDecimal = {
    val size = d.size
    d.sum / size
  }

}
