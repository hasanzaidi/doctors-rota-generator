package org.hasan.doctorrota.config

import org.hasan.doctorrota.domain.Doctor

import scala.collection.mutable.ListBuffer

/**
 * Trait for reading in a list of doctors.
 */
trait DoctorReader {

  /**
   * Reads the list of doctors.
   *
   * @return
   */
  def read(): ListBuffer[Doctor]
}
