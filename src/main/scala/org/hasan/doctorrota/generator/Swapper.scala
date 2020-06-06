package org.hasan.doctorrota.generator

import java.time.LocalDate

import org.hasan.doctorrota.domain.Rota
import org.hasan.doctorrota.domain.Swaps

import scala.collection.mutable.ListBuffer

/**
 * Class for generating swaps.
 * @param rota       the rota
 * @param doctorName the doctor requesting the swaps
 * @param swaps      the dates which the doctor is requesting swaps for
 */
class Swapper(rota: Rota, doctorName: String, swaps: ListBuffer[LocalDate]) {

  /**
   * Generates swaps
   * @return the list of swaps
   */
  def generateSwaps(): Seq[Swaps] = {
    null
  }
}
