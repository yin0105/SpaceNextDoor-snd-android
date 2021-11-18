package com.spacenextdoor.UnitTest.Bookings

import com.google.common.truth.Truth
import org.junit.Test

class CancelBookingsTest {

    @Test
    fun `empty bookingId returns false`() {
        val result = CancelBookings.cancelBooking(
            "",
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty cancellationReasonId returns false`() {
        val result = CancelBookings.cancelBooking(
            "",
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty bookingId with a cancellationReasonId returns false`() {
        val result = CancelBookings.cancelBooking(
            "",
            "1",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty cancellationReasonId with a BookingId returns false`() {
        val result = CancelBookings.cancelBooking(
            "1",
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `Binding all values returns true`() {
        val result = CancelBookings.cancelBooking(
            "1",
            "1",
            "Not comfortable"
        )
        Truth.assertThat(result).isTrue()
    }


}