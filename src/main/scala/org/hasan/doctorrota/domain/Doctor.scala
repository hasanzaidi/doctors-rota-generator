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

    val groupedShifts: Map[(ShiftType, DayType), ListBuffer[Shift]] =
      shifts.groupBy(s => (s.shiftType, s.dayType))

    if (!groupedShifts.contains(NIGHT, WEEKEND) || groupedShifts(NIGHT, WEEKEND).size != 3) {
      println("INVALID: Incorrect number of night weekend shifts")
      return false
    }

    if (!groupedShifts.contains(LONG_DAY, WEEKEND) || groupedShifts(LONG_DAY, WEEKEND).size != 3) {
      println("INVALID: Incorrect number of long day weekend shifts")
      return false
    }

    if (!groupedShifts.contains(NIGHT, WEEKDAY) || groupedShifts(NIGHT, WEEKDAY).size != 4) {
      println("INVALID: Incorrect number of night weekday shifts")
      return false
    }

    if (!groupedShifts.contains(LONG_DAY, WEEKDAY) || groupedShifts(LONG_DAY, WEEKDAY).size != 4) {
      println("INVALID: Incorrect number of long day weekday shifts")
      return false
    }

    if (groupedShifts(NORMAL, WEEKDAY).size <= 1) {
      println("INVALID: Not enough normal weekday shifts")
      return false
    }

    // Correct number of anti social shifts
    var currentConsecutiveStreak = 1
    var maxConsecutiveStreak = 1
    val sortedShifts = shifts.sortBy(s => s.startDateTime)
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
    }

// TODO: Fix this
//    if (maxConsecutiveStreak > 7) {
//      println("INVALID: Too many days in a row")
//      return false
//    }

    true
  }
  // scalastyle:on
}
