package org.hasan.doctorrota

import java.time.Month

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.WeeklyRota

import scala.collection.mutable.ListBuffer
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._
import org.hasan.doctorrota.domain.Shift

/**
 * Allocates the weekday normal shifts. Will allocate any valid weekday normal shift.
 */
object allocateWeekdayNormalShifts extends ((Seq[WeeklyRota], ListBuffer[Doctor]) => Unit) {
  def apply(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Unit = {
    val shifts =
      weeklyRotas.map(w => w.shifts.filter(s => s.shiftType == NORMAL && s.dayType == WEEKDAY)).flatten.toBuffer
    shifts.map(s => allocateSingleShiftToDoctors(doctors, s))
  }

  private def allocateSingleShiftToDoctors(doctors: ListBuffer[Doctor], proposed: Shift): Unit = {
    for (i <- 0 to doctors.size - 1) {
      val doctor = doctors(i)
      if (doctor.isValidShift(proposed)) {
        doctor.shifts += proposed
        doctor.hoursAllocated = doctor.hoursAllocated + NORMAL.hours
      }
    }
  }
}
