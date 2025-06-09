package pl.maniecek.average

import com.typesafe.scalalogging.StrictLogging
import pl.maniecek.average.model.Temperature

object AverageHelper extends StrictLogging {
  def aggregateForYear(name: String)(year: Int, hourlyData: HourlyData): (Temperature, Temperature, Temperature, Temperature) = {
    val a2  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Average.average2)
    val a4  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Average.average4)
    val a8  = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Average.average8)
    val a24 = YearAggregator.yearAggregatorTemperature(year, hourlyData)(Average.average24)
    logger.debug(s"$name $year average2=${a2.print}, average4=${a4.print}, average8=${a8.print} average24=${a24.print}")
    (a2, a4, a8, a24)
  }

}
