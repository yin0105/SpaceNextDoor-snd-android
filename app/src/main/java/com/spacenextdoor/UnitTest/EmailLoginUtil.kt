package com.spacenextdoor.UnitTest

object EmailLoginUtil {
    private val email = "arslanmir24@gmail.com"
    /**
     * the input is not valid if...
     * ...the email is empty
     * ...the email is already taken
     * ...the email is not valid
     */

    fun validateEmailLoginInput(
        username: String
    ) : Boolean {
        if (username.isEmpty()){
            return false
        }
        if (username == email) {
            return false
        }
        return true
    }
}