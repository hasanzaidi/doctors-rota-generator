package org.hasan.doctorrota.domain

import java.time.LocalDate
import java.time.LocalDateTime
import org.hasan.doctorrota.domain.ShiftType._

/**
 * Class representing a shift.
 *
 * @param shiftType     the type of shift
 * @param dayType       the type of day, i.e. weekend or weekday
 * @param startDateTime the start date/time of the shift.
 * @param endDateTime   the end date/time of the shift.
 */
case class Shift(shiftType: ShiftType, dayType: DayType, startDateTime: LocalDateTime, endDateTime: LocalDateTime)

object ShiftFactory {
  // scalastyle:off
  def apply(shiftType: ShiftType, dayType: DayType, date: LocalDate): Shift = {
    shiftType match {
      case NORMAL   => Shift(NORMAL, dayType, date.atTime(9, 0), date.atTime(17, 0))
      case LONG_DAY => Shift(LONG_DAY, dayType, date.atTime(8, 30), date.atTime(21, 0))
      case NIGHT    => Shift(NIGHT, dayType, date.atTime(20, 30), date.plusDays(1).atTime(9, 0))
    }
  }
  // scalastyle:on
}
