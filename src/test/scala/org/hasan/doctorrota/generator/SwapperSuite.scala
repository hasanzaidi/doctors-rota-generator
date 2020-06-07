package org.hasan.doctorrota.generator

import java.io.ByteArrayInputStream
import java.io.ObjectInputStream
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Base64

import org.hasan.doctorrota.domain.DayType._
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Shift
import org.hasan.doctorrota.domain.ShiftType._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.PropertiesHasAsScala

class SwapperSuite extends AnyFunSuite with Matchers {
  // For some reason the deserialization does not work when running in SBT, so need to disable
  val properties = System.getProperties().asScala
  val classPath = properties.get("java.class.path").get
  val onSbt = classPath.contains("sbt-launch.jar")

  test("can find swaps") {
    if (!onSbt) {
      println(classPath)
      // Given:
      val rota = deserialiseRota()
      val sourceDoctor = "Name 6"
      val swapper = new Swapper(rota, sourceDoctor, LocalDate.of(2020, 8, 8), LocalDateTime.of(2020, 8, 3, 23, 0))

      // When:
      val swaps = swapper.generateSwaps()

      // Then:
      swaps should have size (6)
      val sourceShift =
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 8, 8, 30), LocalDateTime.of(2020, 8, 8, 21, 0))

      val swap1 = swaps(0)
      swap1.sourceDoctor.name should equal(sourceDoctor)
      swap1.sourceShift should equal(sourceShift)
      swap1.targetDoctor.name should equal("Name 7")
      swap1.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 14, 8, 30), LocalDateTime.of(2020, 8, 14, 21, 0))
      )

      val swap2 = swaps(1)
      swap2.sourceDoctor.name should equal(sourceDoctor)
      swap2.sourceShift should equal(sourceShift)
      swap2.targetDoctor.name should equal("Name 7")
      swap2.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 15, 8, 30), LocalDateTime.of(2020, 8, 15, 21, 0))
      )

      val swap3 = swaps(2)
      swap3.sourceDoctor.name should equal(sourceDoctor)
      swap3.sourceShift should equal(sourceShift)
      swap3.targetDoctor.name should equal("Name 7")
      swap3.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 16, 8, 30), LocalDateTime.of(2020, 8, 16, 21, 0))
      )

      val swap4 = swaps(3)
      swap4.sourceDoctor.name should equal(sourceDoctor)
      swap4.sourceShift should equal(sourceShift)
      swap4.targetDoctor.name should equal("Name 8")
      swap4.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 21, 8, 30), LocalDateTime.of(2020, 8, 21, 21, 0))
      )

      val swap5 = swaps(4)
      swap5.sourceDoctor.name should equal(sourceDoctor)
      swap5.sourceShift should equal(sourceShift)
      swap5.targetDoctor.name should equal("Name 8")
      swap5.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 22, 8, 30), LocalDateTime.of(2020, 8, 22, 21, 0))
      )

      val swap6 = swaps(5)
      swap6.sourceDoctor.name should equal(sourceDoctor)
      swap6.sourceShift should equal(sourceShift)
      swap6.targetDoctor.name should equal("Name 8")
      swap6.targetShift should equal(
        Shift(LONG_DAY, WEEKEND, LocalDateTime.of(2020, 8, 23, 8, 30), LocalDateTime.of(2020, 8, 23, 21, 0))
      )
    } else {
      println("WARNING: Not running SwapperSuite test due to SBT issues")
    }
  }

  test("earlier date should find more swaps") {
    if (!onSbt) {
      // Given:
      val rota = deserialiseRota()
      val swapper = new Swapper(rota, "Name 6", LocalDate.of(2020, 8, 8), LocalDateTime.of(2020, 7, 1, 23, 0))

      // When:
      val swaps = swapper.generateSwaps()

      // Then:
      swaps should have size (16)
    } else {
      println("WARNING: Not running SwapperSuite test due to SBT issues")
    }
  }

  test("cannot find swaps") {
    if (!onSbt) {
      // Given:
      val rota = deserialiseRota()
      val swapper = new Swapper(rota, "Name 2", LocalDate.of(2020, 7, 23), LocalDateTime.of(2020, 6, 15, 23, 0))

      // When:
      val swaps = swapper.generateSwaps()

      // Then:
      swaps should have size (0)
    } else {
      println("WARNING: Not running SwapperSuite test due to SBT issues")
    }
  }

  private def deserialiseRota(): Rota = {
    val rotaAsString = Files.readString(Path.of("src/test/resources/swapperSuite.rota.txt"))
    val bytes = Base64.getDecoder().decode(rotaAsString.getBytes(UTF_8))
    val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
    val rota = ois.readObject.asInstanceOf[Rota]
    ois.close
    rota
  }
}
