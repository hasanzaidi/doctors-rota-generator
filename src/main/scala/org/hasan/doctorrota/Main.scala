package org.hasan.doctorrota

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.time.LocalDate
import java.time.LocalDateTime

import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.generator.RotaGenerator
import org.hasan.doctorrota.generator.Swapper
import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Swap

// scalastyle:off
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
      val oos = new ObjectOutputStream(new FileOutputStream("rota.txt"))
      oos.writeObject(rota)
      oos.close
    }
  }

  private def handleSwapRun(): Unit = {
    if (args.length < 3) {
      println("rota.jar swap <doctor wanting swap> <date of shift 1 to swap> [<date of shift n to swap>]")
    } else {
      // De-serialise rota
      val ois = new ObjectInputStream(new FileInputStream("rota.txt"))
      val rota = ois.readObject.asInstanceOf[Rota]
      ois.close

      val doctorName = args(1)
      val swapDate = LocalDate.parse(args(2))

      val swapper = new Swapper(rota, doctorName, swapDate, LocalDateTime.of(2020, 8, 3, 23, 0))
      val swaps = swapper.generateSwaps()
      displayOutput(swaps)
    }
  }

  def displayOutput(swaps: Seq[Swap]): Unit = {
    println("Possible swaps for")
    println(s"Original doctor: ${swaps.head.sourceDoctor.name}")
    println(s"Their shifts: ${swaps.head.sourceShift}\n")
    swaps.foreach(s => {
      println(s"Doctor: ${s.targetDoctor.name}")
      println(s"Shift: ${s.targetShift}\n")
    })
  }
}
// scalastyle:on
