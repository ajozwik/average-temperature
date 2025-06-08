package pl.maniecek.mean

import pl.jozwik.mean.model.Temperature

import java.time.{ LocalDate, LocalDateTime }

final case class MapHourlyData(private val map: Map[LocalDateTime, Temperature]) extends HourlyData {

  private val byDay: Map[LocalDate, Map[Int, Temperature]] =
    map.foldLeft(Map.empty[LocalDate, Map[Int, Temperature]]) { case (acc, (ld, t)) =>
      val day    = ld.toLocalDate
      val forDay = acc.getOrElse(day, Map.empty) + (ld.toLocalTime.getHour -> t)
      acc + (day -> forDay)
    }

  override lazy val fromTo: Option[(LocalDate, LocalDate)] = {
    val keys = map.keySet
    (keys.minOption.map(_.toLocalDate), keys.maxOption.map(_.toLocalDate)) match {
      case (Some(min), Some(max)) =>
        Option((min, max))
      case _ =>
        None
    }
  }

  override def hourlyData(day: LocalDate): Map[Int, Temperature] =
    byDay.getOrElse(day, Map.empty)
}
