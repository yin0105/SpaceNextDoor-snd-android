package com.spacenextdoor.UnitTest.Terminations

import com.google.common.truth.Truth
import org.junit.Test

class CaluclateTerminationTest {
    @Test
    fun `empty bookingId and moveoutDate returns false`() {
        val result = CalculateTermination.calculateTermination(
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty bookingId returns false`() {
        val result = CalculateTermination.calculateTermination(
            "",
            "19-05-2021"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `empty moveout Date returns false`() {
        val result = CalculateTermination.calculateTermination(
            "1",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `bookingId returns true`() {
        val result = CalculateTermination.calculateTermination(
            "1",
            "19-05-2021"
        )
        Truth.assertThat(result).isTrue()
    }
}