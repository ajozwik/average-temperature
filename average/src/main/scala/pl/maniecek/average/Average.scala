package pl.maniecek.average

import pl.maniecek.average.model.Temperature

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object Average {
  private val allDayHour  = ChronoUnit.DAYS.getDuration.toHours.toInt
  private val filter8     = (0 to 7).map(_ * 3).toSet
  private val filter8size = filter8.size

  def average2(day: LocalDate, hourlyData: HourlyData): Either[String, Temperature] = {
    val data   = hourlyData.hourlyData(day)
    val values = data.values.map(_.celsius)
    if (data.sizeIs == allDayHour) {
      (values.minOption, values.maxOption) match {
        case (Some(min), Some(max)) =>
          val a = average(min, max)
          Right(Temperature(a))
        case _ =>
          Left("min/max missing")
      }
    } else {
      Left(s"Data missing ${data.size} != $allDayHour")
    }
  }

  def average4(day: LocalDate, hourlyData: HourlyData): Either[String, Temperature] = {
    val data   = hourlyData.hourlyData(day)
    val values = data.values.map(_.celsius)
    if (data.sizeIs == allDayHour) {
      (data.get(6), data.get(18), values.minOption, values.maxOption) match {
        case (Some(t6), Some(t18), Some(min), Some(max)) =>
          val a = average(min, max, t6.celsius, t18.celsius)
          Right(Temperature(a))
        case _ =>
          Left("t6/t18/min/max missing")
      }
    } else {
      Left(s"Data missing ${data.size} != $allDayHour")
    }
  }

  def average8(day: LocalDate, hourlyData: HourlyData): Either[String, Temperature] = {
    val data = hourlyData.hourlyData(day)
    val seq  = data.filter { case (k, _) => filter8.contains(k) }.values.map(_.celsius).toSeq
    seq match {
      case _ if seq.sizeIs == filter8size =>
        val a = average(seq*)
        Right(Temperature(a))
      case _ =>
        Left(s"Data missing ${seq.size} != $filter8size")
    }
  }

  def average24(day: LocalDate, hourlyData: HourlyData): Either[String, Temperature] = {
    val data = hourlyData.hourlyData(day)
    if (data.sizeIs == allDayHour) {
      val a = average(data.values)
      Right(Temperature(a))
    } else {
      Left(s"Data missing ${data.size} != $allDayHour")
    }
  }

  private def average(t: Iterable[Temperature]): BigDecimal =
    average(t.map(_.celsius).toSeq*)

  private def average(d: BigDecimal*): BigDecimal = {
    val size = d.size
    d.sum / size
  }

}
