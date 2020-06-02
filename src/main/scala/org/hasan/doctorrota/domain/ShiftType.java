package org.hasan.doctorrota.domain;

/**
 *  Type of shift.
 */
public enum ShiftType {
    /**
     * Long day shift, i.e. 08:30 - 21:00
     */
    LONG_DAY,

    /**
     * Night shift, i.e. 20:30 - 09:00 (+1)
     */
    NIGHT,

    /**
     * Normal shift, i.e. 09:00 - 17:00
     */
    NORMAL
}
