package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class CancelBookingModel(
    var cancelbookingId : Int? = null,
    var cancelRefundedAmount : Double? = 0.0,
    var cancelPenaltyApplied : Boolean? = false,
    var cancelPenaltyPercent : Double? = 0.0
) : Parcelable {

}