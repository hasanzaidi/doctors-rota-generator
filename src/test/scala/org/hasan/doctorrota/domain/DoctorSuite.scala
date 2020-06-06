package org.hasan.doctorrota.domain

import java.time.LocalDate
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
    val shift1 = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15))
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
    val shift1 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 15))
    val shift2 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 16))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1 += shift2)
    doctor.validShifts() shouldBe false
  }

  test("can't do more than 7 days in a row") {
    // Given:
    val shifts = buildValidShifts()
    shifts(14) = shifts(14).copy(startDateTime = LocalDateTime.of(2020, 6, 30, 8, 30))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts)
    doctor.validShifts() shouldBe false
  }

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
    val shift1 = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1)
    val shiftOnSameDay = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15))
    doctor.isValidShift(shiftOnSameDay) shouldBe false

    val shiftOnDifferentDay = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 16))
    doctor.isValidShift(shiftOnDifferentDay) shouldBe true
  }

  test("is valid proposed shift check for night day before") {
    // Given:
    val shifts = new ListBuffer[Shift]()
    val shift1 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 15))

    // When/Then:
    val doctor = Doctor(DOCTOR_NAME, 410, shifts += shift1)
    val shiftNextDay = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 16))
    doctor.isValidShift(shiftNextDay) shouldBe false

    val nightNextDay = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 16))
    doctor.isValidShift(nightNextDay) shouldBe true
  }

  private def buildValidShifts(): ListBuffer[Shift] = {
    val shifts = new ListBuffer[Shift]()
    val shift1 = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 6, 15))
    val shift2 = ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 16))
    val shift3 = ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 17))
    val shift4 = ShiftFactory(LONG_DAY, WEEKEND, LocalDate.of(2020, 6, 18))
    val shift5 = ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 19))
    val shift6 = ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 20))
    val shift7 = ShiftFactory(NIGHT, WEEKEND, LocalDate.of(2020, 6, 21))
    val shift8 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 23))
    val shift9 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 24))
    val shift10 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 25))
    val shift11 = ShiftFactory(LONG_DAY, WEEKDAY, LocalDate.of(2020, 6, 26))
    val shift12 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 27))
    val shift13 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 28))
    val shift14 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 6, 29))
    val shift15 = ShiftFactory(NIGHT, WEEKDAY, LocalDate.of(2020, 7, 1))
    val shift16 = ShiftFactory(NORMAL, WEEKDAY, LocalDate.of(2020, 7, 3))

    shifts += shift1 += shift2 += shift3 += shift4 += shift5 += shift6 += shift7 += shift8 += shift9 += shift10 += shift11 +=
      shift12 += shift13 += shift14 += shift15 += shift16
    shifts
  }
}
