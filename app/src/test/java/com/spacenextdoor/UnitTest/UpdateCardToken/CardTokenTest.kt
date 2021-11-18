package com.spacenextdoor.UnitTest.UpdateCardToken

import com.google.common.truth.Truth
import org.junit.Test

class CardTokenTest {

    @Test
    fun `empty card Number returns false`() {
        val result = CardToken.cardUpdate(
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `wrong card Number returns false`() {
        val result = CardToken.cardUpdate(
            "213456789"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `Updated card Number returns true`() {
        val result = CardToken.cardUpdate(
            "4242424242424242"
        )
        Truth.assertThat(result).isTrue()
    }
}