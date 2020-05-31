package org.hasan.doctorrota

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RotaGeneratorSuite extends AnyFunSuite with Matchers {
    test("An empty Set should have size 0") {
        assert(Set.empty.size == 0)
    }
}
