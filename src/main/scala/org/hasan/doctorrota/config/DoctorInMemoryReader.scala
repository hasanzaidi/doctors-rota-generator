package org.hasan.doctorrota.config
import org.hasan.doctorrota.domain.Doctor
import org.hasan.doctorrota.domain.Shift

import scala.collection.mutable.ListBuffer

/**
 * Class for reading an in-memory list of doctors.
 *
 * @param numOfWeeks the number of weeks (which corresponds to number of doctors)
 */
class DoctorInMemoryReader(numOfWeeks: Int) extends DoctorReader {
  override def read(): ListBuffer[Doctor] = {
    val doctors: ListBuffer[Doctor] = new ListBuffer[Doctor]()
    for (i <- 1 to numOfWeeks) {
      val doctor = Doctor("Name " + i, 0, new ListBuffer[Shift]())
      doctors += doctor
    }
    doctors
  }
}
