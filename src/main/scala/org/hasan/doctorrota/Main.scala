package org.hasan.doctorrota

import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.time.LocalDate

import com.typesafe.scalalogging.StrictLogging
import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.generator.RotaGenerator

object Main extends App with StrictLogging {
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
      val output = rotaGenerator.generate()
      println(output)

      // Serialise rota for when do swaps
      val oos = new ObjectOutputStream(new FileOutputStream("rota.txt"))
      oos.writeObject(output)
      oos.close
    }
  }

  private def handleSwapRun(): Unit = {
    if (args.length < 3) {
      println("<rota file> <doctor wanting swap> <date of shift 1 to swap> [<date of shift n to swap>]")
    }
  }
}
