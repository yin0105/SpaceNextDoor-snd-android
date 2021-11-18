package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class TerminationModel(
    var terminationId: Int? = null,
    var moveOutDate: String? = "",
    var terminationDate: String? = "",
    var failedRenewalsAmount: Double? = 0.0,
    var remainingDaysAmount:  Double? = 0.0,
    var noticePeriodAmount:  Double? = 0.0,
    var promotionAmount:  Double? = 0.0,
    var discount:  Double? = 0.0,
    var totalAmount:  Double? = 0.0,
    var currency: String? = "",
    var currencySign: String? = "",
    var isOverDue: Boolean? = true,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var status : String? = null,
    var paymentStatus : String? = null,
    var statusModel: StatusModel? = null,
    var paymentStatusModel: PaymentStatusModel? = null
) : Parcelable {


}