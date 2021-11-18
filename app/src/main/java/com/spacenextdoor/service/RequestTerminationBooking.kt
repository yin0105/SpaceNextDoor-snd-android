package com.spacenextdoor.service

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo.coroutines.await
import com.spacenextdoor.ApolloGraphqlClient.Apollo
import com.spacenextdoor.CalculateTerminationDuesMutation
import com.spacenextdoor.PayTerminationMutation
import com.spacenextdoor.RequestTerminationMutation
import com.spacenextdoor.listeners.TerminationListener
import com.spacenextdoor.type.PayTerminationPayload
import com.spacenextdoor.type.TerminationPayload

class RequestTerminationBooking {

    var terminationListener: TerminationListener? = null
    fun setRequestTerminationListener(requestTerminationListener: TerminationListener) {
        terminationListener = requestTerminationListener
    }

    fun requestTerminationForBooking(
        lifecycleScope: LifecycleCoroutineScope,
        context: Activity?,
        moveOutDate: String,
        Id: Int
    ) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).mutate(
                    RequestTerminationMutation(
                        payload = TerminationPayload(
                            moveOutDate,
                            Id
                        )
                    )
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val requestTerminationData = response?.data?.requestTermination

            if (requestTerminationData == null || response.hasErrors()) {
                terminationListener!!.onFailure(response!!.errors!![0].message)
                return@launchWhenResumed
            } else {
                terminationListener!!.onSuccess(requestTerminationData)
            }

        }
    }


    fun calculateTerminationDues(
        lifecycleScope: LifecycleCoroutineScope,
        context: Activity?,
        moveOutDate: String,
        Id: Int
    ) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).mutate(
                    CalculateTerminationDuesMutation(
                        payload = TerminationPayload(
                            moveOutDate,
                            Id
                        )
                    )
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val calculateTerminationDuesData = response?.data?.calculateTerminationDues

            if (calculateTerminationDuesData == null || response.hasErrors()) {
                terminationListener!!.onFailureCalculateTerminationDues(response!!.errors!![0].message)
                return@launchWhenResumed
            } else {
                terminationListener!!.onSuccessCalculateTerminationDues(calculateTerminationDuesData)
            }

        }
    }


    fun payForTheBooking(
        lifecycleScope: LifecycleCoroutineScope,
        context: Activity?,
        Id: Int
    ) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).mutate(
                    PayTerminationMutation(
                        payload = PayTerminationPayload(
                            Id
                        )
                    )
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val payForTheBookingData = response?.data?.payTerminationAmount

            if (payForTheBookingData == null || response.hasErrors()) {
                terminationListener!!.onFailurePayForTheBooking(response!!.errors!![0].message)
                return@launchWhenResumed
            } else {
                terminationListener!!.onSuccessPayForTheBookingData(payForTheBookingData)
            }

        }
    }


}