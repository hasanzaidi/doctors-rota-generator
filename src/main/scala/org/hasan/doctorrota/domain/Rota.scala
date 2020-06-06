package org.hasan.doctorrota.domain

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

import scala.collection.mutable.ListBuffer

/**
 * Class representing a rota.
 *
 * @param weeklyShifts     the list of weekly shifts
 * @param doctors          the list of doctors
 * @param shiftToDoctorMap map of shifts to doctors
 */
case class Rota(weeklyShifts: Seq[WeeklyRota], doctors: Seq[Doctor], shiftToDoctorMap: Map[Shift, ListBuffer[Doctor]]) {
  override def toString: String = {
    val sb = new StringBuilder()
    for (i <- 0 to weeklyShifts.size - 1) {
      sb.append("Week " + (i + 1) + "\n")
      sb.append(formatWeeklyRota(weeklyShifts(i).shifts, shiftToDoctorMap))
      sb.append("\n\n")
    }
    sb.toString()
  }

  def formatWeeklyRota(shifts: Seq[Shift], shiftToDoctorMap: Map[Shift, ListBuffer[Doctor]]): String = {
    val sb = new StringBuilder()
    val sortedShifts = shifts.sortBy(s => s.startDateTime)
    for (i <- 0 to sortedShifts.size - 1) {
      val shift = sortedShifts(i)
      sb.append(
        s"${shift.startDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))} (${shift.shiftType})\n"
      )
      sb.append(shiftToDoctorMap(shift).map(f => f.name).mkString(", "))
      sb.append("\n\n")
    }
    sb.toString()
  }
}
