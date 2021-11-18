package com.spacenextdoor.UnitTest.Bookings

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class FetchBookingsTest {
    @Test
    fun `empty terminationId returns false`() {
        val result = FetchBookings.cancelBooking(
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `null terminationId returns false`() {
        val result = FetchBookings.cancelBooking(
            "null"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `terminationId is int Then returns true`() {
        val result = FetchBookings.cancelBooking(
            "1"
        )
        assertThat(result).isTrue()
    }
}