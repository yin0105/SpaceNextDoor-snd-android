package com.spacenextdoor.service

import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.spacenextdoor.LoginMutation
import com.spacenextdoor.SendOtpMutation
import com.spacenextdoor.listeners.AuthListener
import com.spacenextdoor.type.LoginPayload
import com.spacenextdoor.type.SendOTPPayload
import com.spacenextdoor.utils.Constant
import com.spacenextdoor.utils.StringDeclerations

class LoginServiceCalling {

    var authListenear: AuthListener? = null

    val apolloClient = ApolloClient.builder().serverUrl(Constant.BASE_URL).build()


    fun setAuthListener(authListeners: AuthListener) {

        authListenear = authListeners
    }

    fun verifyCodeAndEmail(
        email: String,
        otp: String,
        lifecycleScope: LifecycleCoroutineScope
    ) {
        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.mutate(LoginMutation(payload = LoginPayload(email, otp))).await()

            } catch (e: Exception) {
                null
            }

            val verifyLogin = response?.data?.login

            if (response != null) {
                if (verifyLogin == null || response.hasErrors()) {
                    authListenear!!.onFailure(response!!.errors!![0].message)

                    return@launchWhenResumed
                } else {
                    val accessToken: String = response.data!!.login.access_token
                    val refresh_token : String = response.data!!.login.refresh_token
                    authListenear!!.onSuccess(accessToken , refresh_token)
                }
            } else {
                authListenear!!.onFailure(StringDeclerations.FAILURE)
            }
        }
    }

    fun sendOtp(email: String, lifecycleScope: LifecycleCoroutineScope) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.mutate(SendOtpMutation(payload = SendOTPPayload(email))).await()
            } catch (e: Exception) {
                null
            }

            val login = response?.data?.sendOTP

            if (response != null) {
                if (login == null || response.hasErrors()) {
                    authListenear!!.onFailure(response!!.errors!![0].message)
                    return@launchWhenResumed
                } else {
                    authListenear!!.onSendOTPSuccess(email)
                }
            } else {
                authListenear!!.onFailure(StringDeclerations.FAILURE)
            }
        }
    }

    fun sendOtpPhone(phone: String, lifecycleScope: LifecycleCoroutineScope) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                apolloClient.mutate(SendOtpMutation(payload = SendOTPPayload(phone))).await()
            } catch (e: Exception) {
                null
            }

            val login = response?.data?.sendOTP

            if (response != null) {
                if (login == null || response.hasErrors()) {
                    authListenear!!.onFailurePhone(response!!.errors!![0].message)
                    return@launchWhenResumed
                } else {
                    authListenear!!.onSendOTPSuccess(phone)
                }
            } else {
                authListenear!!.onFailure(StringDeclerations.FAILURE)
            }
        }
    }
}