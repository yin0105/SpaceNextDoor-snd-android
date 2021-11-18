package com.spacenextdoor.UnitTest.UpdateCardToken

object CardToken {
    private val cardInput = "4242424242424242"

    /**
     * the input is not valid if...
     * ... If the card input is not valid
     */

    fun cardUpdate(
        customerCardNumber: String
    ): Boolean {
        if (customerCardNumber.isNullOrEmpty()) {
            return false
        }

        if (customerCardNumber != cardInput) {
            return false
        }

        if (customerCardNumber == cardInput) {
            return true
        }
        return true
    }
}