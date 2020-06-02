package org.hasan.doctorrota.domain

import java.time.LocalDate
import org.hasan.doctorrota.domain.ShiftType._
import scala.collection.mutable.ListBuffer

/**
 * Class representing all the shifts in a week.
 *
 * @param shifts the shifts in a week
 */
case class WeeklyRota(shifts: Seq[Shift])

object WeeklyRotaFactory {
  // scalastyle:off
  def apply(startDate: LocalDate): WeeklyRota = {
    val shifts = new ListBuffer[Shift]()

    // Week day shifts
    for (i <- 0 to 5) {
      val longDayShift = ShiftFactory(LONG_DAY, startDate.plusDays(i))
      val nightShift = ShiftFactory(NIGHT, startDate.plusDays(i))
      val normalShift = ShiftFactory(NORMAL, startDate.plusDays(i))
      shifts += longDayShift += nightShift += normalShift
    }

    // Weekend shifts
    for (i <- 5 to 7) {
      val longDayShift = ShiftFactory(LONG_DAY, startDate.plusDays(i))
      val nightShift = ShiftFactory(NIGHT, startDate.plusDays(i))
      shifts += longDayShift += nightShift
    }

    WeeklyRota(shifts.toSeq)
  }
  // scalastyle:on
}
