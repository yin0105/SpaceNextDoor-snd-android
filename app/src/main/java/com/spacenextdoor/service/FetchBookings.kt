package com.spacenextdoor.service

import android.app.Activity
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.spacenextdoor.*
import com.spacenextdoor.ApolloGraphqlClient.Apollo
import com.spacenextdoor.listeners.BookingsListener
import com.spacenextdoor.type.CancelBookingPayload
import com.spacenextdoor.utils.StringDeclerations

class FetchBookings {

    private var bookingsListener: BookingsListener? = null

    fun setBookingsListener(bookingsListeners: BookingsListener) {
        bookingsListener = bookingsListeners
    }

    fun getBookings(lifecycleScope: LifecycleCoroutineScope, context: FragmentActivity?) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).query(
                    FetchBookingsQuery(limit = 100)
                ).await()
            } catch (e: Exception) {
                null
            }

            val bookingsData = response?.data?.bookings
            if (response != null) {
                if (bookingsData == null || response.hasErrors()) {
                    bookingsListener!!.onFailure(response!!.errors!![0].message)
                    return@launchWhenResumed
                } else {
                    bookingsListener!!.onSuccess(bookingsData)
                }
            } else {
                bookingsListener!!.onFailure(StringDeclerations.FAILURE)
            }

        }
    }

    fun getBookingById(lifecycleScope: LifecycleCoroutineScope, context: Activity?, Id: Int) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).query(
                    FetchBookingDetailsQuery(bookingId = Id)
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val bookingsData = response?.data?.booking

            if (bookingsData == null || response.hasErrors()) {
                bookingsListener!!.onGetByIdFailure("response not getting")
                return@launchWhenResumed
            } else {
                bookingsListener!!.onGetByIdSuccess(bookingsData)
            }

        }
    }

    fun getCancellationReasons(lifecycleScope: LifecycleCoroutineScope, context: Activity?) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).query(
                    CancellationReasonsQuery()
                ).await()
            } catch (e: Exception) {
                null
            }

            val cancellationReasonsData = response?.data?.cancellation_reasons
            if (response != null) {
                if (cancellationReasonsData == null || response.hasErrors()) {
                    bookingsListener!!.onFailure(response!!.errors!![0].message)
                    return@launchWhenResumed
                } else {
                    bookingsListener!!.onCancellationReasonsSuccess(cancellationReasonsData)
                }
            } else {
                bookingsListener!!.onCancellationReasonsFailure(StringDeclerations.FAILURE)
            }

        }
    }


    fun requestForCancelBooking(
        lifecycleScope: LifecycleCoroutineScope,
        context: Activity?,
        booking_id: Int,
        cancellation_reason_id: Int
    ) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).mutate(
                    CancelBookingMutation(
                        payload = CancelBookingPayload(
                            booking_id,
                            cancellation_reason_id
                        )
                    )
                ).await()
            } catch (e: Exception) {
                Log.e("TAG", e.toString())
                null
            }

            val cancelBookingData = response?.data?.cancelBooking

            if (cancelBookingData == null || response.hasErrors()) {
                bookingsListener!!.onCancelBookingFailure(response!!.errors!![0].message)
                return@launchWhenResumed
            } else {
                bookingsListener!!.onCancelBookingSuccess(cancelBookingData)
            }

        }
    }


}