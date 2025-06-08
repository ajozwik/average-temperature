package pl.maniecek.mean

import pl.jozwik.mean.model.Temperature

import java.time.LocalDate

trait HourlyData {
  def fromTo: Option[(LocalDate, LocalDate)]

  def hourlyData(day: LocalDate): Map[Int, Temperature]
}
