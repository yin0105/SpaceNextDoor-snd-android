package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RenewalsModel(
    var renewalId: Int? = null,
    var nextRenewalDate: String? = "",
    var renewalStartDate: String? = "",
    var renewalEndDate: String? = "",
    var renewalPaidDate: String? = "",
    var renewalBaseAmount: Double? = 0.0,
    var renewalDepositAmount: Double? = 0.0,
    var renewalInsuranceAmount: Double? = 0.0,
    var renewalSubTotalAmount: Double? = 0.0,
    var renewalDiscountAmount: Double? = 0.0,
    var renewalTotalAmount: Double? = 0.0,
    var createdAt: String? = "",
    var updatedAt: String? = "",
    var bookingDetails: BookingDetailsModel? = null,
    var type: RenewalType? = null,
    var status: RenewalStatus? = null
) : Parcelable {


}