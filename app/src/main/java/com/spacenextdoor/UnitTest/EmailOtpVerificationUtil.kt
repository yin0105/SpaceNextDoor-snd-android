package com.spacenextdoor.UnitTest

object EmailOtpVerificationUtil {
    private val email = "arslanmir24@gmail.com"
    private val otp = "123456"
    /**
     * the input is not valid if...
     * ...the otp is not correct
     * ...the email field is empty
     * ... the otp field is empty
     */

    fun validateEmailOtpVerificationInput (
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