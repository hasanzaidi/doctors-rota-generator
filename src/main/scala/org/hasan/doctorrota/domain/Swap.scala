package org.hasan.doctorrota.domain

/**
 * Class representing a swap.
 *
 * @param sourceDoctor the doctor requesting the swap
 * @param sourceShift  the shift being swapped for
 * @param targetDoctor the doctor being swapped
 * @param targetShift  the shift for the doctor being swapped
 */
case class Swap(sourceDoctor: Doctor, sourceShift: Shift, targetDoctor: Doctor, targetShift: Shift)
