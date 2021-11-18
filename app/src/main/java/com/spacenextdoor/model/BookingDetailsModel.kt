package com.spacenextdoor.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class BookingDetailsModel(
    var siteName: String? = "",
    var bookingId: Int? = null,
    var shortId: String? = null,
    var unitMovieInDate: String? = "",
    var unitMoveOutDate: String? = "",
    var isTerminationRequested: Boolean = false,
    var subTotalAmount: Double? = 0.0,
    var totalAmount: Double? = 0.0,
    var insuranceAmount: Double? = 0.0,
    var Status: String? = "",
    var currencySign : String? = "",
    var unitCurrency: String? = "",
    var bookingCreatedAt: String? = "",
    var unitLoadImageUrl : ArrayList<ImagesModel>? = null,
    var unitDetailModel: UnitDetailModel? = null,
    var insuranceDetailsModel: InsuranceModel? = null,
    var renewalDetailModel: RenewalsModel? = null,
    var transactionDetailModel: TransactionsModel? = null,
    var addressDetailsModel: AddressModel? = null,
    var countryDetailModel: CountryModel? = null,
    var districtDetailModel: DistrictModel? = null,
    var terminationDetailModel: TerminationModel? = null,
    var statusModel: StatusModel? = null,
    var siteFeatureModel: ArrayList<SiteFeatureModel>? = null,
    var bookingSpaceSize: Double? = 0.0,
    var bookingSpaceSizeUnit: String = "",
    var bookingSpaceWidth: Double? = 0.0,
    var bookingSpaceHeight: Double? = 0.0,
    var bookingSpaceLength: Double? = 0.0,


    ) : Parcelable {

}