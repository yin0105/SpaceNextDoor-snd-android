package com.spacenextdoor.UnitTest.Bookings

object CancelBookings {

    private val booknigId = 1
    private val cancellationReasonId = 1
    private val cancellationNoted = "I am not comfortable"

    /**
     * the input is not valid if...
     * ... Cancellation Request
     * .. return false if it is empty
     * .. return false if its null
     */

    fun cancelBooking(
        booking_Id: String,
        cancellation_Reason_Id: String,
        cancellation_Note: String
    ): Boolean {
        if (booking_Id.isNullOrEmpty()) {
            return false
        }
        if (cancellation_Reason_Id.isNullOrEmpty()) {
            return false
        }
        if (booking_Id.isNullOrEmpty() && cancellation_Reason_Id == cancellationReasonId.toString()) {
            return false
        }
        if (booking_Id == booknigId.toString() && cancellation_Reason_Id.isNullOrEmpty()) {
            return false
        }
        if (booking_Id == booknigId.toString() && cancellation_Reason_Id == cancellationReasonId.toString()) {
            return true
        }

        if (booking_Id == booknigId.toString() && cancellation_Reason_Id == cancellationReasonId.toString() && cancellation_Note == cancellationNoted) {
            return true
        }
        return true
    }
}