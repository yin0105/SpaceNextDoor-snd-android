package com.spacenextdoor.listeners

import com.spacenextdoor.FetchProfileQuery

interface ProfileListener {
    fun onSuccess(profileDetails: FetchProfileQuery.Profile?)
    fun onFailure(msg: String)
    fun onUpdateProfileSuccess(msg: String)
    fun onUpdateProfileFailure(message: String)
}