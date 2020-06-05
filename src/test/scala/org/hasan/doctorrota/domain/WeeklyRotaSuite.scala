package org.hasan.doctorrota.domain

import java.time.LocalDate

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._

class WeeklyRotaSuite extends AnyFunSuite with Matchers {
  test("should generate different shifts") {
    // Given/When:
    val weeklyRota = WeeklyRotaFactory(LocalDate.of(2020, 6, 15))

    // Then:
    val shifts = weeklyRota.shifts
    shifts should have size (18)

    // Weekday shifts
    shifts(0) should equal(ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 15)))
    shifts(1) should equal(ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 15)))
    shifts(2) should equal(ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15)))
    shifts(3) should equal(ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 16)))
    shifts(4) should equal(ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 16)))
    shifts(5) should equal(ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 16)))
    shifts(6) should equal(ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 17)))
    shifts(7) should equal(ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 17)))
    shifts(8) should equal(ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 17)))
    shifts(9) should equal(ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 18)))
    shifts(10) should equal(ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 18)))
    shifts(11) should equal(ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 18)))

    // Weekend shifts
    shifts(12) should equal(ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 19)))
    shifts(13) should equal(ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 19)))
    shifts(14) should equal(ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 20)))
    shifts(15) should equal(ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 20)))
    shifts(16) should equal(ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 21)))
    shifts(17) should equal(ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 21)))
  }
}
