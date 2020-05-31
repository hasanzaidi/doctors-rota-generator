package org.hasan.doctorrota

import java.time.LocalDate

import com.typesafe.scalalogging.StrictLogging

object Main extends App with StrictLogging {
    if (args.length == 0) {
        println("rota.jar <StartDate>")
    } else {
        val startDate = LocalDate.parse(args(0))
        val rotaGenerator = new RotaGenerator(startDate)
        val output = rotaGenerator.generate()
        println(output)
    }
}
