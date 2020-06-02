package org.hasan.doctorrota.domain

/**
 * Class representing a doctor.
 *
 * @param name           the name of the doctor (currently just an integer)
 * @param hoursAllocated the number of hours they have been allocated
 * @param shifts         the shifts they have been allocated to
 */
case class Doctor(name: String, hoursAllocated: Int, shifts: Seq[Shift])
