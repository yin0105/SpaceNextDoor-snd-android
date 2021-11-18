package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UnitDetailModel(
    var unitId: CharSequence? = "",
    var unitName: String? = "",
    var shortId: String? = "",
    var unitSize: String? = "",
    var unitCurrencySign: String? = "",
    var unitSpaceSize: Double? = 0.00,
    var unitSpaceUnit: String? = "",
    var unitMovieInDate: String? = "",
    var unitMoveOutDate: String? = "",
    var unitAmount: Double? = 0.00,
    var unitCurrency: String = "",
    var newDate: String? = "",
    var unitLoadImageUrl : ImagesModel? = null
) : Parcelable {

}