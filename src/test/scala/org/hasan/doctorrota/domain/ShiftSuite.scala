package org.hasan.doctorrota.domain

import java.time.LocalDate
import java.time.LocalDateTime

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._

class ShiftSuite extends AnyFunSuite with Matchers {
  test("should generate different shifts") {
    val longDayShift = ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 1, 31))
    longDayShift.startDateTime should equal(LocalDateTime.of(2020, 1, 31, 8, 30))
    longDayShift.endDateTime should equal(LocalDateTime.of(2020, 1, 31, 21, 0))
    longDayShift.shiftType should equal(LONG_DAY)

    val nightShift = ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 1, 31))
    nightShift.startDateTime should equal(LocalDateTime.of(2020, 1, 31, 20, 30))
    nightShift.endDateTime should equal(LocalDateTime.of(2020, 2, 1, 9, 0))
    nightShift.shiftType should equal(NIGHT)

    val normalShift = ShiftFactory(NORMAL, WEEKEND, LocalDate.of(2020, 1, 31))
    normalShift.startDateTime should equal(LocalDateTime.of(2020, 1, 31, 9, 0))
    normalShift.endDateTime should equal(LocalDateTime.of(2020, 1, 31, 17, 0))
    normalShift.shiftType should equal(NORMAL)
  }
}
