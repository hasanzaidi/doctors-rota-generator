package org.hasan.doctorrota

import java.time.LocalDate

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.generator.RotaGenerator
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RotaGeneratorSuite extends AnyFunSuite with Matchers {
  test("should generate a rota") {
    // Given:
    val numWeeks = 10
    val generator = new RotaGenerator(LocalDate.of(2020, 6, 15), numWeeks)

    // When:
    val rota = generator.generate()

    // Then:
    // TODO: Replace with validity check of rota
    // All antisocial shifts have one doctor
    // All normal shifts have more than one doctor
    // All doctors hours average < 47
    // All doctors don't do two shifts on same day
    // All doctors have 13 hour gap between night
    // All doctors don't do 7 days in a row
    // All doctors do 4 consecutive weekday nights
    // All doctors do 1 weekend nights
    // All doctors do 1 weekend long days
    // All doctors do 4 long days
    val doctors = rota.doctors
    doctors should have size (numWeeks)

    val doctor1: Doctor = doctors(0)
    doctor1.name should equal("Name 1")
    doctor1.hoursAllocated should equal(431)
    doctor1.shifts should have size (46)

    val doctor2: Doctor = doctors(1)
    doctor2.name should equal("Name 2")
    doctor2.hoursAllocated should equal(431)
    doctor2.shifts should have size (46)

    val doctor3: Doctor = doctors(2)
    doctor3.name should equal("Name 3")
    doctor3.hoursAllocated should equal(431)
    doctor3.shifts should have size (46)

    val doctor4: Doctor = doctors(3)
    doctor4.name should equal("Name 4")
    doctor4.hoursAllocated should equal(431)
    doctor4.shifts should have size (46)

    val doctor5: Doctor = doctors(4)
    doctor5.name should equal("Name 5")
    doctor5.hoursAllocated should equal(431)
    doctor5.shifts should have size (46)

    val doctor6: Doctor = doctors(5)
    doctor6.name should equal("Name 6")
    doctor6.hoursAllocated should equal(431)
    doctor6.shifts should have size (46)

    val doctor7: Doctor = doctors(6)
    doctor7.name should equal("Name 7")
    doctor7.hoursAllocated should equal(431)
    doctor7.shifts should have size (46)

    val doctor8: Doctor = doctors(7)
    doctor8.name should equal("Name 8")
    doctor8.hoursAllocated should equal(431)
    doctor8.shifts should have size (46)

    val doctor9: Doctor = doctors(8)
    doctor9.name should equal("Name 9")
    doctor9.hoursAllocated should equal(431)
    doctor9.shifts should have size (46)

    val doctor10: Doctor = doctors(9)
    doctor10.name should equal("Name 10")
    doctor10.hoursAllocated should equal(431)
    doctor10.shifts should have size (46)
  }
}
