package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AddressModel(
    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var postalCode: String? = "",
    var flat: String? = "",
    var street: String? = "",
    var districtDetailModel: DistrictModel? = null

) : Parcelable {


}