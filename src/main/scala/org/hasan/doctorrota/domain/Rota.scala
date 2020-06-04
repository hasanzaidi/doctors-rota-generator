package org.hasan.doctorrota.domain

import scala.collection.mutable.ListBuffer

/**
 * Class representing a rota.
 *
 * @param weeklyShifts     the list of weekly shifts
 * @param doctors          the list of doctors
 * @param shiftToDoctorMap map of shifts to doctors
 */
case class Rota(weeklyShifts: Seq[WeeklyRota], doctors: Seq[Doctor], shiftToDoctorMap: Map[Shift, ListBuffer[Doctor]])
