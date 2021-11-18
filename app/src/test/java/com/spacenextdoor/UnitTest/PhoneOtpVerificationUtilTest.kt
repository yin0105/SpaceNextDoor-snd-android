package com.spacenextdoor.UnitTest

import com.google.common.truth.Truth
import org.junit.Test

class PhoneOtpVerificationUtilTest {
    @Test
    fun `empty phone and otp returns false`(){
        val result = PhoneOtpVerificationUtil.validatePhoneOtpVerificationInput (
            "",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `email entered but otp pin empty returns false`(){
        val result = PhoneOtpVerificationUtil.validatePhoneOtpVerificationInput(
            "+923454042087",
            ""
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `otp entered but phone empty returns false`(){
        val result = PhoneOtpVerificationUtil.validatePhoneOtpVerificationInput(
            "",
            "123456"
        )
        Truth.assertThat(result).isFalse()
    }

    @Test
    fun `valid phonenumber & otp returns true`(){
        val result = PhoneOtpVerificationUtil.validatePhoneOtpVerificationInput(
            "+923454042087",
            "123456"
        )
        Truth.assertThat(result).isTrue()
    }

    @Test
    fun `wrong otp pin returns false`(){
        val result = PhoneOtpVerificationUtil.validatePhoneOtpVerificationInput(
            "+923454042087",
            "12345678"
        )
        Truth.assertThat(result).isTrue()
    }
}