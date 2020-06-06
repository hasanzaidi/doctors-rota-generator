package org.hasan.doctorrota.domain

import java.time.LocalDate

import org.hasan.doctorrota.domain.DayType.WEEKDAY
import org.hasan.doctorrota.domain.ShiftType.NORMAL
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer
import scala.io.Source

class RotaSuite extends AnyFunSuite with Matchers {
  val DOCTOR_NAME = "My name"

  test("toString") {
    // Given:
    val shift1 = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15))
    val shifts = Seq(shift1)
    val weeklyRota = WeeklyRota(shifts)

    val doctor = Doctor("John Smith", 100, new ListBuffer[Shift]())
    val doctors = Seq(doctor)
    val doctorsInShift1 = new ListBuffer[Doctor]()
    doctorsInShift1 += doctor

    val map = Map[Shift, ListBuffer[Doctor]](shift1 -> doctorsInShift1)
    val rota = Rota(Seq(weeklyRota), doctors, map)

    // When:
    val output = rota.toString()

    // Then:
    val expected = Source.fromResource("rotaSuite.expected.txt").getLines.mkString("\n")
    output should equal(expected)
  }
}
