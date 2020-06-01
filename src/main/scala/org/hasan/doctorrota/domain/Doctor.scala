package org.hasan.doctorrota.domain

/**
 * Class representing a doctor.
 *
 * @param name           the name of the doctor (currently just an integer)
 * @param hoursAllocated the number of hours they have been allocated
 */
case class Doctor(name: Int, hoursAllocated: Int)
