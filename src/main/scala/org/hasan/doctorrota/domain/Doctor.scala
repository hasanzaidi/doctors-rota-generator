package org.hasan.doctorrota.domain

import scala.collection.mutable.ListBuffer

/**
 * Class representing a doctor.
 *
 * @param name           the name of the doctor (currently just an integer)
 * @param hoursAllocated the number of hours they have been allocated
 * @param shifts         the shifts they have been allocated to
 */
case class Doctor(name: String, var hoursAllocated: Double, shifts: ListBuffer[Shift])
