package pl.maniecek.mean

import com.typesafe.scalalogging.StrictLogging
import org.scalatest.{ BeforeAndAfterAll, TryValues }
import org.scalatest.concurrent.{ AsyncTimeLimitedTests, TimeLimitedTests }
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{ Seconds, Span }
import org.scalatest.wordspec.{ AnyWordSpecLike, AsyncWordSpecLike }
import org.scalatestplus.scalacheck.Checkers

import java.time.temporal.ChronoUnit
import java.time.{ Instant, LocalDate, LocalDateTime, ZoneOffset }

trait AbstractSpecScalaCheck extends AbstractSpec with Checkers

trait Spec extends StrictLogging {
  val TIMEOUT_SECONDS = 600
  val timeLimit       = Span(TIMEOUT_SECONDS, Seconds)
}

trait AbstractSpec extends AnyWordSpecLike with TimeLimitedTests with Spec with Matchers with BeforeAndAfterAll with TryValues {

  protected val now: Instant            = Instant.now().truncatedTo(ChronoUnit.SECONDS)
  protected val today: LocalDate        = LocalDate.now
  protected val dateTime: LocalDateTime = LocalDateTime.ofInstant(now, ZoneOffset.UTC)

}

trait AbstractAsyncSpec extends AsyncWordSpecLike with AsyncTimeLimitedTests with Spec with Matchers
