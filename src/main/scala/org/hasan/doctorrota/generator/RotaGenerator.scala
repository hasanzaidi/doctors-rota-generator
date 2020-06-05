package org.hasan.doctorrota.generator

import java.time.DayOfWeek
import java.time.LocalDate

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Shift
import org.hasan.doctorrota.domain.WeeklyRota
import org.hasan.doctorrota.domain.WeeklyRotaFactory
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * Class for generating the rota.
 *
 * @param startDate  the start date of the rota
 * @param numOfWeeks the number of weeks
 */
class RotaGenerator(var startDate: LocalDate, numOfWeeks: Int) {

  /**
   * Generates the rota.
   *
   * @return the rota
   */
  def generate(): Rota = {
    // If start date is not a Monday then get the Monday before to use as start date
    if (startDate.getDayOfWeek != DayOfWeek.MONDAY) {
      startDate = startDate.minusDays(startDate.getDayOfWeek.getValue - 1)
    }

    val doctors = readDoctors()
    val weeklyRotas = generateEmptyRota(startDate, numOfWeeks)

    allocateShiftsToDoctors(weeklyRotas, doctors)
  }

  private def generateEmptyRota(startDate: LocalDate, numOfWeeks: Int): Seq[WeeklyRota] = {
    val weeklyRotas = new ListBuffer[WeeklyRota]()
    for (i <- 0 to numOfWeeks - 1) {
      val weeklyRota = WeeklyRotaFactory(startDate.plusDays(i * 7))
      weeklyRotas += weeklyRota
    }
    weeklyRotas.toSeq
  }

  private def readDoctors(): ListBuffer[Doctor] = {
    // TODO: Read this from file
    val doctors: ListBuffer[Doctor] = new ListBuffer[Doctor]()
    for (i <- 1 to numOfWeeks) {
      val doctor = Doctor("Name " + i, 0, new ListBuffer[Shift]())
      doctors += doctor
    }
    doctors
  }

  def isValidShift2(doctor: Doctor, proposed: Shift): Boolean = {
    !doctor.shifts.exists(s =>
      (s.startDateTime.getDayOfYear == proposed.startDateTime.getDayOfYear) ||
        (s.shiftType == NIGHT && s.startDateTime.getDayOfYear + 1 == proposed.startDateTime.getDayOfYear)
    )
  }

  def allocateSingleShiftToDoctors(doctors: ListBuffer[Doctor], proposed: Shift): Unit = {
    for (i <- 0 to doctors.size - 1) {
      val doctor = doctors(i)
      if (isValidShift2(doctors(i), proposed)) {
        doctor.shifts += proposed
        doctor.hoursAllocated = doctor.hoursAllocated + 8
      }
    }
  }

  def allocateWeekdayNormalToDoctors(doctors: ListBuffer[Doctor], weeklyRotas: Seq[WeeklyRota]): Unit = {
    val shifts =
      weeklyRotas.map(w => w.shifts.filter(s => s.shiftType == NORMAL && s.dayType == WEEKDAY)).flatten.toBuffer
    shifts.map(s => allocateSingleShiftToDoctors(doctors, s))
  }

  def buildMap(d: Doctor, s: Shift, map: mutable.Map[Shift, ListBuffer[Doctor]]): Unit = {
    if (map.contains(s)) {
      val seq = map(s)
      map.put(s, seq += d)
    } else {
      val e = new ListBuffer[Doctor]()
      e += d
      map.put(s, e)
    }
  }

  private def allocateShiftsToDoctors(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Rota = {
    val mutableMap = mutable.Map[Shift, ListBuffer[Doctor]]()
    doctors.zipWithIndex.foreach { case (d, i) => allocateWeekendShiftsToDoctor(d, i, weeklyRotas) }
    allocateWeekdayLongDayToDoctors(doctors, weeklyRotas)
    allocateWeekdayNormalToDoctors(doctors, weeklyRotas)

    doctors.foreach(d => d.shifts.foreach(s => buildMap(d, s, mutableMap)))

    Rota(weeklyRotas, doctors.toSeq, mutableMap.toMap)
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

  private def allocateWeekdayLongDayToDoctors(doctors: ListBuffer[Doctor], weeklyRotas: Seq[WeeklyRota]): Unit = {
    val shifts =
      weeklyRotas.map(w => w.shifts.filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKDAY)).flatten.toBuffer
    doctors.map(d => allocateWeekdayLongDayToDoctor(d, shifts))
  }

  private def allocateWeekdayLongDayToDoctor(
      doctor: Doctor,
      shifts: mutable.Buffer[Shift]
  ): Unit = {
    val r = new Random()
    for (_ <- 0 to 3) {
      var foundValidShift = false
      var index = r.nextInt(shifts.size)
      while ((!foundValidShift) && (shifts.size > 1)) {
        index = r.nextInt(shifts.size)
        foundValidShift = isValidShift(doctor, shifts(index))
      }
      doctor.shifts += shifts(index)
      doctor.hoursAllocated = doctor.hoursAllocated + 12.5
      shifts.remove(index)
    }
  }

  // Shift is valid as long as:
  // - don't have another shift on same day
  // - haven't done a night shift the day before
  private def isValidShift(doctor: Doctor, proposed: Shift): Boolean = {
    !doctor.shifts.exists(s =>
      (s.startDateTime.getDayOfYear == proposed.startDateTime.getDayOfYear)
        || (s.shiftType == NIGHT && s.startDateTime.getDayOfYear + 1 == proposed.startDateTime.getDayOfYear)
    )
  }
}
