package org.hasan.doctorrota.domain

import java.time.LocalDate
import java.time.LocalDateTime

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.hasan.doctorrota.domain.ShiftType._

class WeeklyRotaSuite extends AnyFunSuite with Matchers {
  test("should generate different shifts") {
    // Given/When:
    val weeklyRota = WeeklyRotaFactory(LocalDate.of(2020, 6, 15))

    // Then:
    val shifts = weeklyRota.shifts
    shifts should have size (24)
    shifts(0) should equal(Shift(LONG_DAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0)))
    shifts(1) should equal(Shift(NIGHT, LocalDateTime.of(2020, 6, 15, 20, 30), LocalDateTime.of(2020, 6, 16, 9, 0)))
  }
}
