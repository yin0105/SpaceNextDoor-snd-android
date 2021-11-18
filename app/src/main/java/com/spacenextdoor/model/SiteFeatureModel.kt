package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SiteFeatureModel(
    var nameEn: String? = "",
    var iconUrl: String? = ""
) : Parcelable {

}