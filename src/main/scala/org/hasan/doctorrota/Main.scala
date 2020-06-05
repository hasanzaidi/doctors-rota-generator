package org.hasan.doctorrota

import java.time.LocalDate

import com.typesafe.scalalogging.StrictLogging
import org.hasan.doctorrota.config.DoctorInMemoryReader
import org.hasan.doctorrota.generator.RotaGenerator

object Main extends App with StrictLogging {
  // scalastyle:off
  if (args.length == 0) {
    println("rota.jar <StartDate>")
  } else {
    val startDate = LocalDate.parse(args(0))
    val rotaGenerator = new RotaGenerator(startDate, 10, new DoctorInMemoryReader(10))
    val output = rotaGenerator.generate()
    println(output)
  }
  // scalastyle:on
}
