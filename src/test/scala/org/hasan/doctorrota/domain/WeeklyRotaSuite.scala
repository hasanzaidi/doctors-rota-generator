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
    shifts should have size (18)
    shifts(0) should equal(Shift(LONG_DAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0)))
    shifts(1) should equal(Shift(NIGHT, LocalDateTime.of(2020, 6, 15, 20, 30), LocalDateTime.of(2020, 6, 16, 9, 0)))
    shifts(2) should equal(Shift(NORMAL, LocalDateTime.of(2020, 6, 15, 9, 0), LocalDateTime.of(2020, 6, 15, 17, 0)))
    shifts(3) should equal(Shift(LONG_DAY, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 16, 21, 0)))
    shifts(4) should equal(Shift(NIGHT, LocalDateTime.of(2020, 6, 16, 20, 30), LocalDateTime.of(2020, 6, 17, 9, 0)))
    shifts(5) should equal(Shift(NORMAL, LocalDateTime.of(2020, 6, 16, 9, 0), LocalDateTime.of(2020, 6, 16, 17, 0)))
    shifts(6) should equal(Shift(LONG_DAY, LocalDateTime.of(2020, 6, 17, 8, 30), LocalDateTime.of(2020, 6, 17, 21, 0)))
    shifts(7) should equal(Shift(NIGHT, LocalDateTime.of(2020, 6, 17, 20, 30), LocalDateTime.of(2020, 6, 18, 9, 0)))
    shifts(8) should equal(Shift(NORMAL, LocalDateTime.of(2020, 6, 17, 9, 0), LocalDateTime.of(2020, 6, 17, 17, 0)))
    shifts(9) should equal(Shift(LONG_DAY, LocalDateTime.of(2020, 6, 18, 8, 30), LocalDateTime.of(2020, 6, 18, 21, 0)))
    shifts(10) should equal(Shift(NIGHT, LocalDateTime.of(2020, 6, 18, 20, 30), LocalDateTime.of(2020, 6, 19, 9, 0)))
    shifts(11) should equal(Shift(NORMAL, LocalDateTime.of(2020, 6, 18, 9, 0), LocalDateTime.of(2020, 6, 18, 17, 0)))
  }
}
