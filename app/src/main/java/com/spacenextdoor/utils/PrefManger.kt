package com.spacenextdoor.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.spacenextdoor.FetchBookingsQuery
import com.spacenextdoor.FetchProfileQuery
import com.spacenextdoor.FetchTransactionsQuery
import com.spacenextdoor.model.UserInfoModel


object PrefManger {

    private val PREF_NAME = "PrefManager"
    private val PREF_DATA = "PrefManagerData"
    private val TAG = "PrefManger"
    private val USER = "user"
    private val PROFILE_DATA = "profileData"
    private val PROFILE_DATA_PAYMENT = "profileDataPayment"
    private val TRANSCATION_DATA = "TransactionData"
    private val BOOKING_DATA = "bookingData"
    private val SINGLE_TRANSCATION_DATA = "singleTransactionData"
    private val PROFILE_DATA_DETAILS = "profileDataDetails"
    private lateinit var sharedPreferences: SharedPreferences


    fun clearPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences!!.edit().clear().commit()

    }

    fun clearPreferencesData(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        sharedPreferences!!.edit().clear().commit()

    }


    // For saving a UserModel in a Shared Pref
    fun saveUser(user: UserInfoModel?, context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(user)

        val editor = sharedPreferences!!.edit()
        editor.putString(USER, json)
        editor.commit()

    }

    fun getUser(context: Context): UserInfoModel? {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(USER, "")

        return if (str != "") {
            gson.fromJson<UserInfoModel>(str, UserInfoModel::class.java)
        } else {
            null
        }
    }

    // For saving a UserProfile in a Shared Pref

    fun saveProfileData(user: FetchProfileQuery.Profile?, context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(user)

        val editor = sharedPreferences!!.edit()
        editor.putString(PROFILE_DATA, json)
        editor.commit()

    }

    fun getProfileData(context: Context): FetchProfileQuery.Profile? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(PROFILE_DATA, "")

        return if (str != "") {
            gson.fromJson(str, FetchProfileQuery.Profile::class.java)
        } else {
            null
        }
    }

    // For saving a BookingsData in a Shared Pref

    fun saveBookingData(bookingsData: FetchBookingsQuery.Bookings, context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(bookingsData)

        val editor = sharedPreferences!!.edit()
        editor.putString(BOOKING_DATA, json)
        editor.commit()

    }

    fun getBookingData(context: Context): FetchBookingsQuery.Bookings? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(BOOKING_DATA, "")

        return if (str != "") {
            gson.fromJson(str, FetchBookingsQuery.Bookings::class.java)
        } else {
            null
        }
    }

    // For saving a SingleTransaction in a Shared Pref

    fun saveSingleTransactionData(
        transactionsData: FetchTransactionsQuery.Transactions,
        context: Context
    ) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(transactionsData)

        val editor = sharedPreferences!!.edit()
        editor.putString(SINGLE_TRANSCATION_DATA, json)
        editor.commit()

    }

    fun getSingleTransactionData(context: Context): FetchTransactionsQuery.Transactions? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(SINGLE_TRANSCATION_DATA, "")

        return if (str != "") {
            gson.fromJson(str, FetchTransactionsQuery.Transactions::class.java)
        } else {
            null
        }
    }

    // For saving a TransactionPaymentList in a Shared Pref

    fun saveProfileDataPayment(user: FetchProfileQuery.Profile?, context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(user)

        val editor = sharedPreferences!!.edit()
        editor.putString(PROFILE_DATA_PAYMENT, json)
        editor.commit()

    }

    fun getProfileDataPayment(context: Context): FetchProfileQuery.Profile? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(PROFILE_DATA_PAYMENT, "")

        return if (str != "") {
            gson.fromJson(str, FetchProfileQuery.Profile::class.java)
        } else {
            null
        }
    }

    // For saving a ProfileData on Payment Screen in a Shared Pref

    fun saveTransactionData(
        transactionsData: FetchTransactionsQuery.Transactions,
        context: Context
    ) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(transactionsData)

        val editor = sharedPreferences!!.edit()
        editor.putString(TRANSCATION_DATA, json)
        editor.commit()

    }

    fun getTransactionData(context: Context): FetchTransactionsQuery.Transactions? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(TRANSCATION_DATA, "")

        return if (str != "") {
            gson.fromJson(str, FetchTransactionsQuery.Transactions::class.java)
        } else {
            null
        }
    }

    // For saving a ProfileData on Account Page in a Shared Pref

    fun saveProfile(user: FetchProfileQuery.Profile?, context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = gson.toJson(user)

        val editor = sharedPreferences!!.edit()
        editor.putString(PROFILE_DATA_DETAILS, json)
        editor.commit()

    }

    fun getProfile(context: Context): FetchProfileQuery.Profile? {
        sharedPreferences = context.getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE)
        val gson = Gson()
        val str = sharedPreferences!!.getString(PROFILE_DATA_DETAILS, "")

        return if (str != "") {
            gson.fromJson(str, FetchProfileQuery.Profile::class.java)
        } else {
            null
        }
    }
}