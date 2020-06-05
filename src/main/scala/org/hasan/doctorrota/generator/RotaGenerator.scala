package org.hasan.doctorrota.generator

import java.time.DayOfWeek
import java.time.LocalDate

import org.hasan.doctorrota.allocateWeekdayNormalShifts
import org.hasan.doctorrota.config.DoctorReader
import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Shift
import org.hasan.doctorrota.domain.WeeklyRota
import org.hasan.doctorrota.domain.WeeklyRotaFactory

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Class for generating the rota.
 *
 * @param startDate  the start date of the rota
 * @param numOfWeeks the number of weeks
 * @param reader     reads in the list of doctors
 */
class RotaGenerator(var startDate: LocalDate, numOfWeeks: Int, reader: DoctorReader) {

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

    val doctors = reader.read()
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

  private def buildShiftToDoctorsMap(
      doctor: Doctor,
      shift: Shift,
      map: mutable.Map[Shift, ListBuffer[Doctor]]
  ): Unit = {
    if (map.contains(shift)) {
      val seq = map(shift)
      map.put(shift, seq += doctor)
    } else {
      val doctors = new ListBuffer[Doctor]()
      doctors += doctor
      map.put(shift, doctors)
    }
  }

  private def allocateShiftsToDoctors(weeklyRotas: Seq[WeeklyRota], doctors: ListBuffer[Doctor]): Rota = {
    allocateWeekendShifts(weeklyRotas, doctors)
    allocateWeekdayLongDayShifts(weeklyRotas, doctors)
    allocateWeekdayNormalShifts(weeklyRotas, doctors)

    // Create a map of Shift -> Doctor for when we need to print out the rota
    val mutableMap = mutable.Map[Shift, ListBuffer[Doctor]]()
    doctors.foreach(d => d.shifts.foreach(s => buildShiftToDoctorsMap(d, s, mutableMap)))

    Rota(weeklyRotas, doctors.toSeq, mutableMap.toMap)
  }
}
