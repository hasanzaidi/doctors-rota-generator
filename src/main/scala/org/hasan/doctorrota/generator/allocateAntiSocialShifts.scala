package org.hasan.doctorrota.generator

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.WeeklyRota
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._
import java.time.DayOfWeek._

import scala.collection.mutable.ListBuffer

/**
 * Allocates the anti-social shifts.
 */
object allocateAntiSocialShifts extends ((Seq[WeeklyRota], ListBuffer[Doctor]) => Unit) {
  def apply(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Unit = {
    doctors.zipWithIndex.foreach { case (d, i) => allocateWeekendShiftsToDoctor(d, i, weeklyRotas) }
  }

  private def allocateWeekendShiftsToDoctor(doctor: Doctor, i: Int, weeklyRotas: Seq[WeeklyRota]): Unit = {
    // Assign all Weekday night shifts in week i to doctor i
    doctor.shifts ++= weeklyRotas(i).shifts.filter(s => s.shiftType == NIGHT && s.dayType == WEEKDAY).toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (4 * NIGHT.hours)

    // Assign all weekend night shifts in week n - i - 1 to doctor i. This ensures they can't do a whole week of
    // night shifts in a row
    doctor.shifts ++= weeklyRotas(weeklyRotas.size - i - 1).shifts
      .filter(s => s.shiftType == NIGHT && s.dayType == WEEKEND)
      .toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (3 * NIGHT.hours)

    // Assign all weekend long day shifts in week i + 2 % n to doctor i. This ensures they can't do a whole week of
    // night shifts/long days in a row
    doctor.shifts ++= weeklyRotas((i + 2) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKEND)
      .toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (3 * LONG_DAY.hours)

    allocateLongDayWeekdayShiftsToDoctor(doctor, i, weeklyRotas)
  }

  private def allocateLongDayWeekdayShiftsToDoctor(doctor: Doctor, i: Int, weeklyRotas: Seq[WeeklyRota]) = {
    doctor.shifts ++= weeklyRotas((i + 1) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY && s.startDateTime.getDayOfWeek == MONDAY)
      .toBuffer
    doctor.shifts ++= weeklyRotas((i + 2) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY && s.startDateTime.getDayOfWeek == TUESDAY)
      .toBuffer
    doctor.shifts ++= weeklyRotas((i + 3) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY && s.startDateTime.getDayOfWeek == WEDNESDAY)
      .toBuffer
    doctor.shifts ++= weeklyRotas((i + 4) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY && s.startDateTime.getDayOfWeek == THURSDAY)
      .toBuffer
    doctor.hoursAllocated = doctor.hoursAllocated + (4 * LONG_DAY.hours)
  }
}
