package org.hasan.doctorrota

import java.time.LocalDate

import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.generator.RotaGenerator
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RotaGeneratorSuite extends AnyFunSuite with Matchers {
    test("should generate a rota") {
        val generator = new RotaGenerator(LocalDate.of(2020, 5, 31), 10)
        generator.generate() should equal(Rota(Seq()))
    }
}
