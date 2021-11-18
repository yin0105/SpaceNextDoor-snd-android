package com.spacenextdoor.UnitTest

object PhoneLoginUtil {
    private val phonenumber = "+923454042087"
    /**
     * the input is not valid if...
     * ...the email is empty
     * ...the email is already taken
     * ...the email is not valid
     */

    fun validatePhoneLoginInput(
        username: String
    ) : Boolean {
        if (username.isEmpty()){
            return false
        }
        if (username == phonenumber) {
            return false
        }
        return true
    }
}