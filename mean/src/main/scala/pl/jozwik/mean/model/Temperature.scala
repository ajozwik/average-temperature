package pl.jozwik.mean.model

final case class Temperature(celsius: BigDecimal) extends AnyVal {

  def print: String = String.format("%.3f", celsius.doubleValue)
}
