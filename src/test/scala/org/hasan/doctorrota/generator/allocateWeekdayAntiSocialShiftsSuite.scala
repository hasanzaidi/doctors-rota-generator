package org.hasan.doctorrota.generator

import java.time.LocalDate

import org.hasan.doctorrota.domain.DayType._
import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Shift
import org.hasan.doctorrota.domain.ShiftFactory
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.WeeklyRota
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer
import scala.io.Source

class allocateWeekdayAntiSocialShiftsSuite extends AnyFunSuite with Matchers {
  test("can allocate anti-social shifts") {
    // Given:
    val shift1 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 15))
    val shifts = Seq(shift1)
    val weeklyRota = WeeklyRota(shifts)

    val doctor = Doctor("John Smith", 100, new ListBuffer[Shift]())
    val doctors = new ListBuffer[Doctor]()
    doctors += doctor

    // When:
    allocateAntiSocialShifts(Seq(weeklyRota), doctors)

    // Then:
    doctors(0).shifts should have size (1)
    doctors(0).shifts(0) should equal(shift1)
  }
}
