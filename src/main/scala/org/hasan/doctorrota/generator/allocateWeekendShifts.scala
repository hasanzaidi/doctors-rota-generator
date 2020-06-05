package org.hasan.doctorrota.generator

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.WeeklyRota
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._

import scala.collection.mutable.ListBuffer

/**
 * Allocates the weekend shifts and the weekday nights shifts.
 */
object allocateWeekendShifts extends ((Seq[WeeklyRota], ListBuffer[Doctor]) => Unit) {
  def apply(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Unit = {
    doctors.zipWithIndex.foreach { case (d, i) => allocateWeekendShiftsToDoctor(d, i, weeklyRotas) }
  }

  private def allocateWeekendShiftsToDoctor(doctor: Doctor, i: Int, weeklyRotas: Seq[WeeklyRota]): Unit = {
    // Assign all Weekday night shifts in week i to doctor i
    doctor.shifts ++= weeklyRotas(i).shifts.filter(s => s.shiftType == NIGHT && s.dayType == WEEKDAY).toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (4 * 12.5)

    // Assign all weekend night shifts in week n - i - 1 to doctor i. This ensures they can't do a whole week of
    // night shifts in a row
    doctor.shifts ++= weeklyRotas(weeklyRotas.size - i - 1).shifts
      .filter(s => s.shiftType == NIGHT && s.dayType == WEEKEND)
      .toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (3 * 12.5)

    // Assign all weekend long day shifts in week i + 2 % n to doctor i. This ensures they can't do a whole week of
    // night shifts/long days in a row
    doctor.shifts ++= weeklyRotas((i + 2) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKEND)
      .toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (3 * 12.5)
  }
}
