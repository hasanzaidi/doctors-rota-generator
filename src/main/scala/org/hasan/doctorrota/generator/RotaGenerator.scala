package org.hasan.doctorrota.generator

import java.time.DayOfWeek
import java.time.LocalDate

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.WeeklyRota

class RotaGenerator(var startDate: LocalDate, numOfWeeks: Int) {
  def generate(): Rota = {
    // If start date is not a Monday then get the Monday before to use as start date
    if (startDate.getDayOfWeek != DayOfWeek.MONDAY) {
      startDate = startDate.minusDays(startDate.getDayOfWeek.getValue - 1)
    }

    val doctors = Nil
    for (i <- 1 to numOfWeeks) {
      val doctor = Doctor(i, 0)
      doctors :+ doctor
    }

    val rota = generateInitialRota(startDate, numOfWeeks, doctors)
    rota
  }

  private def generateInitialRota(startDate: LocalDate, numOfWeeks: Int, doctors: Seq[Doctor]): Rota = {
    var weeklyRotas = Nil
    for (i <- 0 to numOfWeeks - 1) {
      val weeklyRota = generateWeeklyRota(startDate, i, doctors)
      weeklyRotas :+ weeklyRota
    }
    Rota(weeklyRotas)
  }

  private def generateWeeklyRota(startDate: LocalDate, weekNumber: Int, doctors: Seq[Doctor]): WeeklyRota = {
    WeeklyRota(Seq())
  }
}
