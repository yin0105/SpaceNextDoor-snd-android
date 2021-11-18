package com.spacenextdoor.UnitTest.Terminations

object PayTermination {

    private val terminationid = 1

    /**
     * the input is not valid if...
     * ... PayTermination Request
     * .. return false if it is empty
     * .. return false if its null
     * .. return true if it is int
     */

    fun payTermination(
        terminationId: String
    ): Boolean {
        if (terminationId.isNullOrEmpty()) {
            return false
        }
        if (terminationId == "null") {
            return false
        }
        if (terminationId == terminationid.toString()) {
            return true
        }
        return true
    }
}