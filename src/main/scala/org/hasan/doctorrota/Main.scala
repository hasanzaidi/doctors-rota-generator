package org.hasan.doctorrota

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Base64

import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.generator.RotaGenerator
import org.hasan.doctorrota.generator.Swapper
import java.nio.charset.StandardCharsets.UTF_8
import java.nio.file.Files
import java.nio.file.Path

import org.hasan.doctorrota.domain.Rota

object Main extends App {
  val NUM_WEEKS = 10

  if (args.length == 0) {
    println("The first argument must either be \"swap\" or \"gen\"")
  } else {
    val program = args(0)
    if (program.equals("gen")) {
      handleGenRun()
    } else {
      handleSwapRun()
    }
  }

  private def handleGenRun(): Unit = {
    if (args.length < 2) {
      println("rota.jar gen <start date>")
    } else {
      val startDate = LocalDate.parse(args(1))
      val rotaGenerator = new RotaGenerator(startDate, NUM_WEEKS, new DoctorInMemoryReader(NUM_WEEKS))
      val rota = rotaGenerator.generate()
      println(rota)

      // Serialise rota for when do swaps
      val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(stream)
      oos.writeObject(rota)
      oos.close
      val rotaAsString = new String(Base64.getEncoder().encode(stream.toByteArray), UTF_8)
      Files.writeString(Path.of("rota.txt"), rotaAsString)
    }
  }

  private def handleSwapRun(): Unit = {
    if (args.length < 3) {
      println("rota.jar swap <doctor wanting swap> <date of shift 1 to swap> [<date of shift n to swap>]")
    } else {
      // De-serialise rota
      val rotaAsString = Files.readString(Path.of("rota.txt"))
      val bytes = Base64.getDecoder().decode(rotaAsString.getBytes(UTF_8))
      val ois = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val rota = ois.readObject.asInstanceOf[Rota]
      ois.close

      val doctorName = args(1)
      val swapDate = LocalDate.parse(args(2))

      val swapper = new Swapper(rota, doctorName, swapDate, LocalDateTime.now())
      val output = swapper.generateSwaps()
      println(output)
    }
  }
}
