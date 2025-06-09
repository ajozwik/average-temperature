package pl.maniecek.average

import pl.maniecek.average.model.Temperature

import java.time.LocalDate

trait HourlyData {
  def fromTo: Option[(LocalDate, LocalDate)]

  def hourlyData(day: LocalDate): Map[Int, Temperature]
}
