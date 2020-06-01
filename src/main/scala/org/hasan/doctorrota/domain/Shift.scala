package org.hasan.doctorrota.domain

import java.time.LocalDateTime

/**
 * Class representing a shift.
 *
 * @param startDateTime    the start date/time of the shift.
 * @param endDateTime      the end date/time of the shift.
 * @param doctorsAllocated the doctors allocated to this shift.
 */
case class Shift(startDateTime: LocalDateTime, endDateTime: LocalDateTime, doctorsAllocated: Seq[Doctor])
