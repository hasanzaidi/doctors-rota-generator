package org.hasan.doctorrota

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDate

import com.typesafe.scalalogging.StrictLogging
import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.generator.RotaGenerator
import org.hasan.doctorrota.generator.Swapper

import scala.collection.mutable.ListBuffer

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
      println("rota.jar swap <doctor wanting swap> <date of shift 1 to swap> [<date of shift n to swap>]")
    } else {
      val ois = new ObjectInputStream(new FileInputStream("rota.txt"))
      val rota = ois.readObject.asInstanceOf[Rota]
      ois.close

      val doctorName = args(1)
      val swapDates = new ListBuffer[LocalDate]()
      for (i <- 2 to args.length - 1) {
        swapDates += LocalDate.parse(args(i))
      }

      val swapper = new Swapper(rota, doctorName, swapDates)
      val output = swapper.generateSwaps()
      println(output)
    }
  }
}
