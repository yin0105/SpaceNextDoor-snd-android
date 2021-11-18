package com.spacenextdoor.UnitTest

object PhoneOtpVerificationUtil {
    private val phonenumber = "+923454042087"
    private val otp = "123456"
    /**
     * the input is not valid if...
     * ...the otp is not correct
     * ...the phone field is empty
     * ... the otp field is empty
     * ...successfully login
     */

    fun validatePhoneOtpVerificationInput (
        username: String,
        otp: String
    ) : Boolean {
        if ( username.isEmpty()) {
            return false
        }
        if ( otp.isEmpty()) {
            return false
        }
        if (username.isEmpty() && otp.isEmpty()) {
            return false
        }
        if (otp != otp){
            return false
        }
        return true
    }
}