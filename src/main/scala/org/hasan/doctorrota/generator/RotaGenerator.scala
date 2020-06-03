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

import scala.collection.mutable.ListBuffer

/**
 * Class for generating the rota.
 *
 * @param startDate  the start date of the rota
 * @param numOfWeeks the number of weeks
 */
class RotaGenerator(var startDate: LocalDate, numOfWeeks: Int) {

  /**
   * Generates the rota.
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

  private def allocateShiftsToDoctors(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Rota = {
    doctors.zipWithIndex.foreach { case (d, i) => allocateNightShiftsToDoctor(d, i, weeklyRotas) }
    Rota(weeklyRotas, doctors.toSeq)
  }

  private def allocateNightShiftsToDoctor(doctor: Doctor, i: Int, weeklyRotas: Seq[WeeklyRota]): Unit = {
    // Assign all Weekday night shifts in week i to doctor i
    doctor.shifts ++= weeklyRotas(i).shifts.filter(s => s.shiftType == NIGHT && s.dayType == WEEKDAY).toBuffer

    // Assign all weekend night shifts in week n - i - 1 to doctor i. This ensures they can't do a whole week of
    // night shifts in a row
    doctor.shifts ++= weeklyRotas(weeklyRotas.size - i - 1).shifts
      .filter(s => s.shiftType == NIGHT && s.dayType == WEEKEND)
      .toBuffer

    // Assign all weekend long day shifts in week i + 2 % n to doctor i. This ensures they can't do a whole week of
    // night shifts/long days in a row
    doctor.shifts ++= weeklyRotas((i + 2) % weeklyRotas.size).shifts
      .filter(s => s.shiftType == LONG_DAY && s.dayType == WEEKEND)
      .toBuffer
  }
}
