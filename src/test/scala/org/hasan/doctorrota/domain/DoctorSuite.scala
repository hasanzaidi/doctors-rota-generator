package org.hasan.doctorrota.domain

import java.time.LocalDateTime

import org.hasan.doctorrota.domain.ShiftType.NORMAL
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer

class DoctorSuite extends AnyFunSuite with Matchers {
  test("valid shifts") {
    val shifts = new ListBuffer[Shift]()
    val shift1 =
      Shift(NORMAL, DayType.WEEKDAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    shifts += shift1 += shift1

    val doctor = Doctor("My name", 410, shifts)

    doctor.validShifts() shouldBe false
  }
}
