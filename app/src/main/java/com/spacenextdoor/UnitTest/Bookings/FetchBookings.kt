package com.spacenextdoor.UnitTest.Bookings

object FetchBookings {

    private val cancelBookingId = 1

    /**
     * the input is not valid if...
     * ... Cancell Booking Request
     * .. return false if it is empty
     * .. reutrn false if its null
     * .. return true if it is int
     */

    fun cancelBooking(
        terminationId: String
    ): Boolean {
        if (terminationId.isNullOrEmpty()) {
            return false
        }
        if (terminationId == "null") {
            return false
        }
        if (terminationId == cancelBookingId.toString()) {
            return true
        }
        return true
    }

}