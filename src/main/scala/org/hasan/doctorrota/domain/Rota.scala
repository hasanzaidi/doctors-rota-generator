package org.hasan.doctorrota.domain

/**
 * Class representing a rota.
 *
 * @param shifts the number of weekly shifts
 */
case class Rota(shifts: Seq[WeeklyRota])
