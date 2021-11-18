package com.spacenextdoor.listeners

import com.spacenextdoor.*

interface BookingsListener {

    fun onSuccess(bookingsData: FetchBookingsQuery.Bookings)
    fun onFailure(toString: String)
    fun onGetByIdSuccess(bookingsDataById: FetchBookingDetailsQuery.Booking)
    fun onGetByIdFailure(toString: String)
    fun onRequestTerminationSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination)
    fun onRequestTerminationFailure(message: String)
    fun onCancellationReasonsSuccess(cancellationReasonsData: CancellationReasonsQuery.Cancellation_reasons)
    fun onCancellationReasonsFailure(message: String)
    fun onCancelBookingFailure(message: String)
    fun onCancelBookingSuccess(cancelBookingData: CancelBookingMutation.CancelBooking)

}