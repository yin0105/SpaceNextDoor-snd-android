package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InsuranceModel(
    var insuranceId : Int? = null,
    var insuranceNameEn: String? = "",
    var insuranceNameTh: String? = "",
    var insuranceNameJp: String? = "",
    var insuranceNameKr: String? = "",
    var insuranceCoveredAmount: Int? = 0,
    var insurancePricePerDay: Double? = 0.0,
    var insuranceThirdPartyProvider : String? = "",
    var insuranceTotalAmount : Double? = 0.0,
    var insuranceCreatedAt : String? = "",
    var updatedAt : String? = "",
) : Parcelable {


}