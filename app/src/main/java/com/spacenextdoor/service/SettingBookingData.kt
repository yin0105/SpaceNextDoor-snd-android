package com.spacenextdoor.service

import com.spacenextdoor.FetchBookingDetailsQuery
import com.spacenextdoor.model.*
import com.spacenextdoor.type.BookingStatus

object SettingBookingData {

    fun setDataInObject(bookingsDataByIdData: FetchBookingDetailsQuery.Booking): BookingDetailsModel {

        var bookingDetails = BookingDetailsModel()

        var unitDetails = UnitDetailModel()
        var insuranceDetail = InsuranceModel()
        var renewalDetail = RenewalsModel()
        var transactionDetail = TransactionsModel()
        var addressDetail = AddressModel()
        var countryDetail = CountryModel()
        var districtDetail = DistrictModel()


        if (bookingsDataByIdData.sub_total_amount != null) {
            bookingDetails.subTotalAmount = bookingsDataByIdData.sub_total_amount
        }

        if (bookingsDataByIdData.insurance_amount != null) {
            bookingDetails.insuranceAmount = bookingsDataByIdData.insurance_amount
        }

        if (bookingsDataByIdData.is_termination_requested != null) {
            bookingDetails.isTerminationRequested = bookingsDataByIdData.is_termination_requested
        }

        if (bookingsDataByIdData.site_name != null) {
            bookingDetails.siteName = bookingsDataByIdData.site_name
        }

        if (bookingsDataByIdData.id != null) {
            bookingDetails.bookingId = bookingsDataByIdData.id
        }

        if (bookingsDataByIdData.short_id != null) {
            bookingDetails.shortId = bookingsDataByIdData.short_id
        }


        if (bookingsDataByIdData.status != null) {


            bookingDetails.Status = bookingsDataByIdData.status.rawValue
        }

        if (bookingsDataByIdData.move_in_date != null) {
            bookingDetails.unitMovieInDate = bookingsDataByIdData.move_in_date.toString()
        }

        if (bookingsDataByIdData.move_out_date != null) {
            bookingDetails.unitMoveOutDate = bookingsDataByIdData.move_out_date.toString()
        }

        if (bookingsDataByIdData.currency != null) {
            bookingDetails.unitCurrency = bookingsDataByIdData.currency
        }

        if (bookingsDataByIdData.site_name != null) {
            unitDetails.unitName = bookingsDataByIdData.site_name
        }

        if (bookingsDataByIdData.move_out_date != null) {
            unitDetails.unitMoveOutDate = bookingsDataByIdData.move_out_date.toString()
        }

        if (bookingsDataByIdData.move_in_date != null) {
            unitDetails.unitMovieInDate = bookingsDataByIdData.move_in_date.toString()
        }

        if (bookingsDataByIdData.original_space != null) {
            if (bookingsDataByIdData.original_space!!.space_type != null) {
                if (bookingsDataByIdData.original_space!!.space_type!!.name_en != null) {
                    unitDetails.unitSize =
                        bookingsDataByIdData.original_space!!.space_type!!.name_en
                }
            }

            if (bookingsDataByIdData.original_space!!.size_unit != null) {
                unitDetails.unitSpaceUnit =
                    bookingsDataByIdData.original_space!!.size_unit.toString()
            }

            var imageFeatureList: ArrayList<ImagesModel> = ArrayList<ImagesModel>()


            if (bookingsDataByIdData.original_space!!.images != null) {
                if (bookingsDataByIdData.original_space.images!!.size > 0) {

                    for (item in bookingsDataByIdData.original_space.images) {
                        var imageUrleModel = ImagesModel()
                        imageUrleModel.urlImage = item.toString()
                        imageFeatureList.add(imageUrleModel)
                    }
                    bookingDetails.unitLoadImageUrl = imageFeatureList

                }
            }


            if (bookingsDataByIdData.original_space!!.size != null) {
                unitDetails.unitSpaceSize = bookingsDataByIdData.original_space!!.size
            }
        }

        bookingDetails.unitDetailModel = unitDetails

        if (bookingsDataByIdData.site_address != null) {

            if (bookingsDataByIdData.site_address!!.district!!.name_en != null) {
                districtDetail.nameEn = bookingsDataByIdData.site_address!!.district!!.name_en
            }

            if (bookingsDataByIdData.site_address!!.country!!.name_en != null) {
                countryDetail.nameEn = bookingsDataByIdData.site_address!!.country!!.name_en
            }

            if (bookingsDataByIdData.site_address!!.street != null) {
                addressDetail.street = bookingsDataByIdData.site_address!!.street
            }

            if (bookingsDataByIdData.site_address!!.postal_code != null) {
                addressDetail.postalCode = bookingsDataByIdData.site_address!!.postal_code
            }

            if (bookingsDataByIdData.site_address!!.lat != null) {
                addressDetail.latitude = bookingsDataByIdData.site_address!!.lat
            }

            if (bookingsDataByIdData.site_address!!.lng != null) {
                addressDetail.longitude = bookingsDataByIdData.site_address!!.lng
            }
        }


        bookingDetails.countryDetailModel = countryDetail
        bookingDetails.districtDetailModel = districtDetail
        bookingDetails.addressDetailsModel = addressDetail



        if (bookingsDataByIdData.insurance != null) {

            if (bookingsDataByIdData.insurance.name_en != null) {
                insuranceDetail.insuranceNameEn = bookingsDataByIdData.insurance!!.name_en
            }

            if (bookingsDataByIdData.insurance.price_per_day != null) {
                insuranceDetail.insurancePricePerDay =
                    bookingsDataByIdData.insurance!!.price_per_day
            }

            if (bookingsDataByIdData.insurance.covered_amount != null) {
                insuranceDetail.insuranceCoveredAmount =
                    bookingsDataByIdData.insurance!!.covered_amount
            }
        }

        bookingDetails.insuranceDetailsModel = insuranceDetail


        if (bookingsDataByIdData.renewals != null) {

            if (bookingsDataByIdData.renewals!![0].sub_total_amount != null) {
                renewalDetail.renewalSubTotalAmount =
                    bookingsDataByIdData.renewals!![0].sub_total_amount
            }

            if (bookingsDataByIdData.renewals!![0].insurance_amount != null) {
                renewalDetail.renewalInsuranceAmount =
                    bookingsDataByIdData.renewals!![0].insurance_amount
            }

            if (bookingsDataByIdData.renewals!![0].total_amount != null) {
                renewalDetail.renewalTotalAmount = bookingsDataByIdData.renewals!![0].total_amount
            }
        }

        bookingDetails.renewalDetailModel = renewalDetail


        if (bookingsDataByIdData.transactions != null) {
            if (bookingsDataByIdData.transactions!!.count() > 0) {
                transactionDetail.transactionCardBrandName =
                    bookingsDataByIdData.transactions!![0].card_brand_name
                transactionDetail.transactionCardLastDigits =
                    bookingsDataByIdData.transactions!![0].card_last_digits
            }

        }

        var siFeatureList: ArrayList<SiteFeatureModel> = ArrayList<SiteFeatureModel>()

        if (bookingsDataByIdData.site_features != null) {

            for (item in bookingsDataByIdData.site_features) {
                var siteFeatureModel = SiteFeatureModel()
                siteFeatureModel.nameEn = item.name_en
                siteFeatureModel.iconUrl = item.icon
                siFeatureList.add(siteFeatureModel)
            }
            bookingDetails.siteFeatureModel = siFeatureList
        } else {
            bookingDetails.siteFeatureModel = siFeatureList
        }

        bookingDetails.transactionDetailModel = transactionDetail

        return bookingDetails
    }
}