package org.hasan.doctorrota.generator

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.WeeklyRota
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._
import org.hasan.doctorrota.domain.Shift

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Allocates the weekday long day shifts.
 */
object allocateWeekdayLongDayShifts extends ((Seq[WeeklyRota], ListBuffer[Doctor]) => Unit) {
  def apply(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Unit = {
    val shifts =
      weeklyRotas.map(w => w.shifts.filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY)).flatten.toBuffer
    doctors.map(d => allocateWeekdayLongDayToDoctor(d, shifts))
  }

  private def allocateWeekdayLongDayToDoctor(doctor: Doctor, shifts: mutable.Buffer[Shift]): Unit = {
    val r = new Random()
    for (_ <- 0 to 3) {
      var foundValidShift = false
      var index = r.nextInt(shifts.size)
      while ((!foundValidShift) && (shifts.size > 1)) {
        index = r.nextInt(shifts.size)
        foundValidShift = doctor.isValidShift(shifts(index))
      }
      doctor.shifts += shifts(index)
      doctor.hoursAllocated = doctor.hoursAllocated + 12.5
      shifts.remove(index)
    }
  }
}
