package org.hasan.doctorrota.generator

import java.time.LocalDate
import java.time.LocalDateTime

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Shift
import org.hasan.doctorrota.domain.Swap

/**
 * Class for generating swaps.
 * @param rota       the rota
 * @param doctorName the doctor requesting the swaps
 * @param swap       the dates which the doctor is requesting swaps for
 */
class Swapper(rota: Rota, doctorName: String, swap: LocalDate, currentDate: LocalDateTime) {

  /**
   * Generates swaps.
   *
   * @return the list of swaps
   */
  def generateSwaps(): Seq[Swap] = {
    val doctor = rota.doctors.filter(d => d.name == doctorName).head
    val shift = doctor.shifts.filter(s => s.startDateTime.toLocalDate == swap).head

    val possibleDoctors = rota.doctors.filter(d => d != doctor && d.isValidShift(shift))
    possibleDoctors.flatMap(d => findValidSwap(d, shift, doctor))
  }

  private def findValidSwap(possibleDoctor: Doctor, shift: Shift, doctor: Doctor): Seq[Swap] = {
    val shifts = possibleDoctor.shifts.filter(s =>
      s.dayType == shift.dayType && s.shiftType == shift.shiftType && s.startDateTime.isAfter(
        currentDate.plusDays(1)
      )
    )

    val validShifts = shifts.filter(s => doctor.isValidShift(s))
    validShifts.map(s => Swap(doctor, shift, possibleDoctor, s)).toSeq
  }
}
