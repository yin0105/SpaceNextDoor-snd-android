package com.spacenextdoor.listeners

interface AuthListener {
    fun onSuccess( accessToken: String , refresh_token : String)
    fun onFailure(msg: String)
    fun onFailurePhone(msg: String)
    fun onSendOTPSuccess(email: String)

}