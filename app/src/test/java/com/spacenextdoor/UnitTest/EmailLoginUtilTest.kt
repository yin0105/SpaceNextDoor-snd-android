package com.spacenextdoor.UnitTest

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class EmailLoginUtilTest {

    @Test
    fun `empty email returns false`(){
        val result = EmailLoginUtil.validateEmailLoginInput(
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `same email returns false`(){
        val result = EmailLoginUtil.validateEmailLoginInput(
            "arslanmir24@gmail.com"
        )
        assertThat(result).isFalse()
    }
}