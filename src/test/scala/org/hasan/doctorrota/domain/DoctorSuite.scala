package org.hasan.doctorrota.domain

import java.time.LocalDateTime

import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.DayType._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer

class DoctorSuite extends AnyFunSuite with Matchers {
  val DOCTOR_NAME = "My name"

  test("can't have duplicate shifts") {
    // Given:
    val shifts = new ListBuffer[Shift]()
    val shift1 = Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    shifts += shift1 += shift1

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("can't work more than 470 hours") {
    val doctor = Doctor(DOCTOR_NAME, 471, new ListBuffer[Shift]())
    doctor.validShifts() shouldBe false
  }

  test("valid shifts") {
    // Given:
    val shifts = buildValidShifts()

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe true
  }

  test("must have 13 hours gap after night shift") {
    // Given:
    val shifts = new ListBuffer[Shift]()
    val shift1 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    val shift2 = Shift(LONG_DAY, WEEKDAY, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 16, 21, 0))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1 += shift2)
    doctor.validShifts() shouldBe false
  }

//  test("can't do more than 7 days in a row") {
//    // Given:
//    val shifts = buildValidShifts()
//    shifts(14) = shifts(14).copy(startDateTime = LocalDateTime.of(2020, 6, 30, 8, 30))
//
//    // When/Then:
//    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
//    doctor.validShifts() shouldBe false
//  }

  test("must do more than 1 normal weekday shift") {
    // Given:
    val shifts = buildValidShifts()
    shifts(15) = shifts(15).copy(dayType = WEEKEND)

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("must do 3 long day weekend shifts") {
    // Given:
    val shifts = buildValidShifts()
    shifts(1) = shifts(1).copy(dayType = WEEKDAY)

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("must do 3 night weekend shifts") {
    // Given:
    val shifts = buildValidShifts()
    shifts(4) = shifts(4).copy(shiftType = NORMAL, dayType = WEEKDAY)

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("must do 4 long day weekday shifts") {
    // Given:
    val shifts = buildValidShifts()
    shifts(7) = shifts(7).copy(shiftType = NORMAL)

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("must do 4 night weekday shifts") {
    // Given:
    val shifts = buildValidShifts()
    shifts(14) = shifts(14).copy(shiftType = NORMAL)

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

  test("is valid proposed shift check for same day") {
    // Given:
    val shifts = new ListBuffer[Shift]()
    val shift1 = Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 15, 21, 0), LocalDateTime.of(2020, 6, 16, 17, 0))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1)
    val shiftOnSameDay =
      Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    doctor.isValidShift(shiftOnSameDay) shouldBe false

    val shiftOnDifferentDay =
      Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 16, 21, 0))
    doctor.isValidShift(shiftOnDifferentDay) shouldBe true
  }

  test("is valid proposed shift check for night day before") {
    // Given:
    val shifts = new ListBuffer[Shift]()
    val shift1 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 15, 21, 0), LocalDateTime.of(2020, 6, 16, 17, 0))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1)
    val shiftNextDay =
      Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    doctor.isValidShift(shiftNextDay) shouldBe false

    val nightNextDay =
      Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 16, 21, 0))
    doctor.isValidShift(nightNextDay) shouldBe true
  }

  private def buildValidShifts(): ListBuffer[Shift] = {
    val shifts = new ListBuffer[Shift]()
    val shift1 = Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 6, 15, 8, 30), LocalDateTime.of(2020, 6, 15, 17, 0))
    val shift2 = Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 6, 16, 8, 30), LocalDateTime.of(2020, 6, 19, 21, 0))
    val shift3 = Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 6, 17, 8, 30), LocalDateTime.of(2020, 6, 20, 21, 0))
    val shift4 = Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 6, 18, 8, 30), LocalDateTime.of(2020, 6, 21, 21, 0))
    val shift5 = Shift(NIGHT, WEEKEND, LocalDateTime.of(2020, 6, 19, 8, 30), LocalDateTime.of(2020, 6, 15, 21, 0))
    val shift6 = Shift(NIGHT, WEEKEND, LocalDateTime.of(2020, 6, 20, 8, 30), LocalDateTime.of(2020, 6, 16, 21, 0))
    val shift7 = Shift(NIGHT, WEEKEND, LocalDateTime.of(2020, 6, 21, 8, 30), LocalDateTime.of(2020, 6, 17, 21, 0))
    val shift8 = Shift(LONG_DAY, WEEKDAY, LocalDateTime.of(2020, 6, 23, 8, 30), LocalDateTime.of(2020, 6, 18, 21, 0))
    val shift9 = Shift(LONG_DAY, WEEKDAY, LocalDateTime.of(2020, 6, 24, 8, 30), LocalDateTime.of(2020, 6, 19, 21, 0))
    val shift10 = Shift(LONG_DAY, WEEKDAY, LocalDateTime.of(2020, 6, 25, 8, 30), LocalDateTime.of(2020, 6, 20, 21, 0))
    val shift11 = Shift(LONG_DAY, WEEKDAY, LocalDateTime.of(2020, 6, 26, 8, 30), LocalDateTime.of(2020, 6, 21, 21, 0))
    val shift12 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 27, 8, 30), LocalDateTime.of(2020, 6, 27, 21, 0))
    val shift13 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 28, 8, 30), LocalDateTime.of(2020, 6, 28, 21, 0))
    val shift14 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 6, 29, 8, 30), LocalDateTime.of(2020, 6, 29, 21, 0))
    val shift15 = Shift(NIGHT, WEEKDAY, LocalDateTime.of(2020, 7, 1, 8, 30), LocalDateTime.of(2020, 7, 1, 21, 0))
    val shift16 = Shift(NORMAL, WEEKDAY, LocalDateTime.of(2020, 7, 3, 8, 30), LocalDateTime.of(2020, 7, 3, 17, 0))
    shifts += shift1 += shift2 += shift3 += shift4 += shift5 += shift6 += shift7 += shift8 += shift9 += shift10 += shift11 +=
      shift12 += shift13 += shift14 += shift15 += shift16
    shifts
  }
}
