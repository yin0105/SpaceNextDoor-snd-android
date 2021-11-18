package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class CancelReasonModel (
    var bookingId : Int? = null,
    var cancellationReasonId : Int? = null,
    var cancellationNote : String? = ""
) : Parcelable {

}