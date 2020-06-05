package org.hasan.doctorrota

import java.time.LocalDate

import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.generator.RotaGenerator
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.hasan.doctorrota.domain.ShiftType._
import org.hasan.doctorrota.domain.Rota

class RotaGeneratorSuite extends AnyFunSuite with Matchers {
  test("should generate a rota") {
    // Given:
    val numWeeks = 10
    val generator = new RotaGenerator(LocalDate.of(2020, 6, 15), numWeeks, new DoctorInMemoryReader(10))

    // When:
    val rota = generator.generate()

    // Then:
    val doctors = rota.doctors
    doctors should have size (numWeeks)
    doctors.foreach(d => d.validShifts() shouldBe true)

    assertOnRotaMap(rota)
  }

  private def assertOnRotaMap(rota: Rota) = {
    val shiftMap = rota.shiftToDoctorMap
    // ((4 * 3) + (3 * 2)) * 10
    shiftMap should have size (180)

    val antisocialShifts = shiftMap.keys.filter(k => k.shiftType != NORMAL)
    antisocialShifts.foreach(s => shiftMap(s) should have size (1))

    val normalShifts = shiftMap.keys.filter(k => k.shiftType == NORMAL)
    normalShifts.foreach(s => shiftMap(s).size should be > 1)
  }
}
