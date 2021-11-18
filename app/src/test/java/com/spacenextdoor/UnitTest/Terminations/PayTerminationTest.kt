package com.spacenextdoor.UnitTest.Terminations

import com.google.common.truth.Truth
import com.spacenextdoor.UnitTest.Bookings.FetchBookings
import org.junit.Test

class PayTerminationTest {
    @Test
    fun `empty terminationId returns false`() {
        val result = PayTermination.payTermination(
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `null terminationId returns false`() {
        val result = PayTermination.payTermination(
            "null"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `terminationId is int Then returns true`() {
        val result = PayTermination.payTermination(
            "1"
        )
        Truth.assertThat(result).isTrue()
    }

}