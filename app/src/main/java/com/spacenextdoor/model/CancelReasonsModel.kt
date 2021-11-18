package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class CancelReasonsModel(
    var id: Int? = null,
    var description_en: String? = "",
    var description_th: String? = "",
    var description_jp: String? = "",
    var description_kr: String? = "",
    var cancelDetail : CancelReasonsModel? = null
) : Parcelable {

}