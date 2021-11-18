package com.spacenextdoor.UnitTest

import com.google.common.truth.Truth
import org.junit.Test

class EmailOtpVerificationUtilTest {
    @Test
    fun `empty email and otp returns false`(){
        val result = EmailOtpVerificationUtil.validateEmailOtpVerificationInput(
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `email entered but otp pin empty returns false`(){
        val result = EmailOtpVerificationUtil.validateEmailOtpVerificationInput(
            "arslanmir24@gmail.com",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `otp entered but email empty returns false`(){
        val result = EmailOtpVerificationUtil.validateEmailOtpVerificationInput(
            "",
            "123456"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `valid email & otp returns true`(){
        val result = EmailOtpVerificationUtil.validateEmailOtpVerificationInput(
            "arslanmir24@gmail.com",
            "123456"
        )
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `wrong otp pin returns false`(){
        val result = EmailOtpVerificationUtil.validateEmailOtpVerificationInput(
            "arslanmir24@gmail.com",
            "12345678"
        )
        Truth.assertThat(result).isTrue()
    }
}