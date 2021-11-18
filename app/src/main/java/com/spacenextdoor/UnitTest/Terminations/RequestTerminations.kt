package com.spacenextdoor.UnitTest.Terminations

import com.spacenextdoor.UnitTest.Bookings.FetchBookings

object RequestTerminations {

    private val BookingId = 1
    private val moveOutDate = "19-05-2021"

    /**
     * the input is not valid if...
     * ... Request Termination Request
     * .. return false if it is empty
     * .. reutrn false if its null
     */

    fun requestTermination(
        booking_id: String,
        moveOutDate: String
    ): Boolean {
        if (booking_id.isNullOrEmpty()) {
            return false
        }
        if (moveOutDate.isNullOrEmpty()) {
            return false
        }
        if (booking_id.isNullOrEmpty() && moveOutDate.isNullOrEmpty()) {
            return false
        }

        if (booking_id == BookingId.toString() && moveOutDate.isNullOrEmpty()) {
            return false
        }

        if (booking_id.isNullOrEmpty() && moveOutDate == moveOutDate) {
            return false
        }

        if (booking_id == BookingId.toString() && moveOutDate == moveOutDate) {
            return true
        }
        return true
    }
}