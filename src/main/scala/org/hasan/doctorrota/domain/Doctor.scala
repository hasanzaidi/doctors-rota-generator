package org.hasan.doctorrota.domain

import scala.collection.mutable.ListBuffer
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._

/**
 * Class representing a doctor.
 *
 * @param name           the name of the doctor (currently just an integer)
 * @param hoursAllocated the number of hours they have been allocated
 * @param shifts         the shifts they have been allocated to
 */
case class Doctor(name: String, var hoursAllocated: Double, shifts: ListBuffer[Shift]) {
  // scalastyle:off
  def validShifts(): Boolean = {
    // Can't average more than 47 hours
    if (hoursAllocated > 470) {
      println("INVALID: Too many hours")
      return false
    }

    // No duplicate shifts
    if (shifts.toSet.size != shifts.size) {
      println("INVALID: Duplicate shifts")
      return false
    }

    val sortedShifts = shifts.sortBy(s => s.startDateTime)

    // Correct number of anti social shifts
    var numWeekendNights = 0
    var numWeekendLongDays = 0
    var numWeekdayLongDays = 0
    var numWeekdayNormal = 0
    var numWeekdayNights = 0
    var currentConsecutiveStreak = 1
    var maxConsecutiveStreak = 1
    for (i <- 0 to sortedShifts.size - 1) {
      val shift = sortedShifts(i)

      if (i > 0) {
        val previous = sortedShifts(i - 1)
        if (shift.startDateTime.toLocalDate == previous.startDateTime.toLocalDate.plusDays(1)) {
          if (previous.shiftType == NIGHT && shift.shiftType != NIGHT) {
            println("INVALID: Shouldn't have non-night shift day after night")
            return false
          }

          currentConsecutiveStreak = currentConsecutiveStreak + 1
          maxConsecutiveStreak = Math.max(currentConsecutiveStreak, maxConsecutiveStreak)
        } else {
          currentConsecutiveStreak = 1
        }
      }

      if ((shift.shiftType == LONG_DAY) && (shift.dayType == WEEKEND)) {
        numWeekendLongDays = numWeekendLongDays + 1
      }

      if ((shift.shiftType == LONG_DAY) && (shift.dayType == WEEKDAY)) {
        numWeekdayLongDays = numWeekdayLongDays + 1
      }

      if ((shift.shiftType == NIGHT) && (shift.dayType == WEEKDAY)) {
        numWeekdayNights = numWeekdayNights + 1
      }

      if ((shift.shiftType == NIGHT) && (shift.dayType == WEEKEND)) {
        numWeekendNights = numWeekendNights + 1
      }

      if ((shift.shiftType == NORMAL) && (shift.dayType == WEEKDAY)) {
        numWeekdayNormal = numWeekdayNormal + 1
      }
    }

    if ((numWeekendNights != 3) || (numWeekendLongDays != 3) || (numWeekdayLongDays != 4) || (numWeekdayNights != 4)) {
      println("INVALID: Not enough anti-social shifts")
      return false
    }

//    if (maxConsecutiveStreak > 7) {
//      println("INVALID: Too many days in a row")
//      return false
//    }

    if (numWeekdayNormal <= 1) {
      println("INVALID: Not enough normal weekday shifts")
      return false
    }

    true
  }
  // scalastyle:on
}
