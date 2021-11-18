package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransactionsModel(
    var transactionId : Int? = null,
    var transactionShortId : String? = "",
    var invoiceId : String? = "",
    var transactionCardBrandName: String? = "",
    var transactionCardLastDigits: String? = "",
    var transactionAmount : Float? = 0.0F,
    var transactionCurrency : String? = "",
    var createdAt : String? = "",
    var updatedAt : String? = "",
    var bookingDetails : BookingDetailsModel? = null,
    var renewalDetails : RenewalsModel? = null,
    var insuranceDetails : InsuranceModel? = null,
    var districtDetails : DistrictModel? = null,
    var addressDetails : AddressModel? = null,
    var countryDetails : CountryModel? = null,
    var spaceDetails : UnitDetailModel? = null,
    var terminationDetails : TerminationModel? = null,
    var type : String? = null
    ) : Parcelable {


}