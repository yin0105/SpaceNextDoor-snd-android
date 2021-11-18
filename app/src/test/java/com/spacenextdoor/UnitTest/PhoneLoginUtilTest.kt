package com.spacenextdoor.UnitTest

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PhoneLoginUtilTest {
    @Test
    fun `empty phone number returns false`(){
        val result = PhoneLoginUtil.validatePhoneLoginInput(
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `same phone number returns false`(){
        val result = PhoneLoginUtil.validatePhoneLoginInput(
            "+923454042087"
        )
        assertThat(result).isFalse()
    }
}