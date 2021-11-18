package com.spacenextdoor.service

import android.app.Activity
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo.coroutines.await
import com.spacenextdoor.ApolloGraphqlClient.Apollo
import com.spacenextdoor.FetchProfileQuery
import com.spacenextdoor.UpdateCustomerCardMutation
import com.spacenextdoor.listeners.ProfileListener
import com.spacenextdoor.utils.StringDeclerations

class FetchProfile {

    var profileListener: ProfileListener? = null

    fun getProfile(lifecycleScope: LifecycleCoroutineScope, context: FragmentActivity?) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context)
                    .query(FetchProfileQuery()).await()
            } catch (e: Exception) {
                null
            }

            val profileData = response?.data?.profile
            if (response != null) {
                if (profileData == null || response.hasErrors()) {
                    profileListener!!.onFailure(response!!.errors!!.get(0).message)
                    return@launchWhenResumed
                } else {
                    profileListener!!.onSuccess(profileData)
                }
            } else {
                profileListener!!.onFailure(StringDeclerations.FAILURE)
            }

        }
    }

    fun setFetchProfileListener(profileListeners: ProfileListener) {
        profileListener = profileListeners
    }


    fun updateProfileCardToken(
        cardToken: String,
        lifecycleScope: LifecycleCoroutineScope,
        context: Activity?
    ) {
        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).mutate(
                    UpdateCustomerCardMutation(customerCardToken = cardToken)
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val updateCardTokenData = response?.data?.updateProfile!!.modified

            if (updateCardTokenData == null || response.hasErrors()) {
                Log.e("TAG", "errorData" + response.toString())
                profileListener!!.onUpdateProfileFailure(response.toString())
                return@launchWhenResumed
            } else {
                profileListener!!.onUpdateProfileSuccess(StringDeclerations.CARDUPDATE)
            }

        }
    }
}