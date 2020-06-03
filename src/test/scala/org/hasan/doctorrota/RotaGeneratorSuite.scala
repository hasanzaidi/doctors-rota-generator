package org.hasan.doctorrota

import java.time.LocalDate

import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.generator.RotaGenerator
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RotaGeneratorSuite extends AnyFunSuite with Matchers {
  test("should generate a rota") {
    // Given:
    val generator = new RotaGenerator(LocalDate.of(2020, 6, 15), 5)

    // When:
    val rota = generator.generate()

    // Then:
    val doctors = rota.doctors
    doctors should have size (5)

    val doctor1: Doctor = doctors(0)
    doctor1.name should equal("Name 1")
    doctor1.hoursAllocated should equal(0)
    doctor1.shifts should have size (10)

    val doctor2: Doctor = doctors(1)
    doctor2.name should equal("Name 2")
    doctor2.hoursAllocated should equal(0)
    doctor2.shifts should have size (10)

    val doctor3: Doctor = doctors(2)
    doctor3.name should equal("Name 3")
    doctor3.hoursAllocated should equal(0)
    doctor3.shifts should have size (10)

    val doctor4: Doctor = doctors(3)
    doctor4.name should equal("Name 4")
    doctor4.hoursAllocated should equal(0)
    doctor4.shifts should have size (10)

    val doctor5: Doctor = doctors(4)
    doctor5.name should equal("Name 5")
    doctor5.hoursAllocated should equal(0)
    doctor5.shifts should have size (10)
  }
}
