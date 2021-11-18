package com.spacenextdoor.service

import com.spacenextdoor.FetchTransactionsQuery
import com.spacenextdoor.model.*

object TransactionListItemData {

    fun setDataInObject(transactionsData: FetchTransactionsQuery.Transactions): MutableList<TransactionsModel> {

        var transactionsList = mutableListOf<TransactionsModel>()

        for (item in transactionsData.edges.indices) {
            var transactionsDetails = TransactionsModel()
            var renewalModelDetails = RenewalsModel()
            var insuranceDetails = InsuranceModel()
            var bookingDetails = BookingDetailsModel()
            var districtDetails = DistrictModel()
            var addressDetails = AddressModel()
            var countryDetails = CountryModel()
            var spaceDetails = UnitDetailModel()
            var terminationDetails = TerminationModel()

            if (transactionsData.edges[item].id != null) {
                transactionsDetails.transactionId = transactionsData.edges[item].id
            }

            if (transactionsData.edges[item].created_at != null) {
                transactionsDetails.createdAt = transactionsData.edges[item].created_at.toString()
            }

            if (transactionsData.edges[item].currency != null) {
                transactionsDetails.transactionCurrency = transactionsData.edges[item].currency
            }

            if (transactionsData.edges[item].type != null) {
                transactionsDetails.type = transactionsData.edges[item].type.toString()
            }

            if (transactionsData.edges[item].renewal != null) {
                if (transactionsData.edges[item].renewal!!.sub_total_amount != null) {
                    renewalModelDetails.renewalSubTotalAmount =
                        transactionsData.edges[item].renewal!!.sub_total_amount
                }

                if (transactionsData.edges[item].renewal!!.deposit_amount != null) {
                    renewalModelDetails.renewalDepositAmount =
                        transactionsData.edges[item].renewal!!.deposit_amount
                }

                if (transactionsData.edges[item].renewal!!.discount_amount != null) {
                    renewalModelDetails.renewalDiscountAmount =
                        transactionsData.edges[item].renewal!!.discount_amount
                }

                if (transactionsData.edges[item].renewal!!.insurance_amount != null) {
                    renewalModelDetails.renewalInsuranceAmount =
                        transactionsData.edges[item].renewal!!.insurance_amount
                }

                transactionsDetails.renewalDetails = renewalModelDetails
            }


            if (transactionsData.edges[item].booking != null) {

                if (transactionsData.edges[item].booking!!.id != null) {
                    bookingDetails.bookingId =
                        transactionsData.edges[item].booking.id
                }

                if (transactionsData.edges[item].booking!!.currency != null) {
                    bookingDetails.unitCurrency =
                        transactionsData.edges[item].booking.currency
                }
                if (transactionsData.edges[item].booking!!.currency_sign != null) {
                    bookingDetails.currencySign =
                        transactionsData.edges[item].booking.currency_sign
                }
                if (transactionsData.edges[item].booking!!.short_id != null) {
                    bookingDetails.shortId =
                        transactionsData.edges[item].booking.short_id
                }

                if (transactionsData.edges[item].booking!!.space_size_unit != null) {
                    bookingDetails.bookingSpaceSizeUnit =
                        transactionsData.edges[item].booking.space_size_unit.toString()
                }

                if (transactionsData.edges[item].booking!!.space_size != null) {
                    bookingDetails.bookingSpaceSize =
                        transactionsData.edges[item].booking.space_size
                }

                if (transactionsData.edges[item].booking!!.space_height != null) {
                    bookingDetails.bookingSpaceHeight =
                        transactionsData.edges[item].booking.space_height
                }

                if (transactionsData.edges[item].booking!!.space_length != null) {
                    bookingDetails.bookingSpaceLength =
                        transactionsData.edges[item].booking.space_length
                }

                if (transactionsData.edges[item].booking!!.status != null) {
                    bookingDetails.Status =
                        transactionsData.edges[item].booking.status.toString()
                }

                if (transactionsData.edges[item].booking!!.total_amount != null) {
                    bookingDetails.totalAmount =
                        transactionsData.edges[item].booking.total_amount
                }

                if (transactionsData.edges[item].booking!!.created_at != null) {
                    bookingDetails.bookingCreatedAt =
                        transactionsData.edges[item].booking.created_at.toString()
                }


                transactionsDetails.bookingDetails = bookingDetails
            }

            if (transactionsData.edges[item].booking.original_space!!.space_type != null) {

                if (transactionsData.edges[item].booking.original_space!!.space_type!!.name_en != null) {
                    spaceDetails.unitSize =
                        transactionsData.edges[item].booking.original_space!!.space_type!!.name_en
                }

                if (transactionsData.edges[item].booking.original_space!!.space_type != null) {
                    spaceDetails.unitSpaceUnit =
                        transactionsData.edges[item].booking.original_space!!.size_unit.toString()
                }
                transactionsDetails.spaceDetails = spaceDetails
            }

            if (transactionsData.edges[item].booking!!.termination != null) {
                if (transactionsData.edges[item].booking!!.termination!!.status != null) {
                    terminationDetails.paymentStatus =
                        transactionsData.edges[item].booking!!.termination!!.payment_status.toString()
                }
                transactionsDetails.terminationDetails = terminationDetails
            }


            if (transactionsData.edges[item].booking.site_address.district != null) {

                if (transactionsData.edges[item].booking!!.site_address!!.district!!.name_en != null) {
                    districtDetails.nameEn =
                        transactionsData.edges[item].booking.site_address!!.district!!.name_en
                }

                transactionsDetails.districtDetails = districtDetails
            }

            if (transactionsData.edges[item].booking.site_address != null) {

                if (transactionsData.edges[item].booking!!.site_address!!.postal_code != null) {
                    addressDetails.postalCode =
                        transactionsData.edges[item].booking.site_address.postal_code
                }

                if (transactionsData.edges[item].booking!!.site_address!!.lat != null) {
                    addressDetails.latitude =
                        transactionsData.edges[item].booking.site_address.lat
                }

                if (transactionsData.edges[item].booking!!.site_address!!.lng != null) {
                    addressDetails.longitude =
                        transactionsData.edges[item].booking.site_address.lng
                }
                if (transactionsData.edges[item].booking!!.site_address!!.street != null) {
                    addressDetails.street =
                        transactionsData.edges[item].booking.site_address.street
                }

                transactionsDetails.addressDetails = addressDetails
            }

            if (transactionsData.edges[item].booking.site_address.country != null) {

                if (transactionsData.edges[item].booking!!.site_address!!.country!!.name_en != null) {
                    countryDetails.nameEn =
                        transactionsData.edges[item].booking.site_address.country!!.name_en
                }

                transactionsDetails.countryDetails = countryDetails

            }


            if (transactionsData.edges[item].insurance != null) {
                if (transactionsData.edges[item].insurance!!.id != null) {
                    insuranceDetails.insuranceId =
                        transactionsData.edges[item].insurance!!.id
                }

                if (transactionsData.edges[item].insurance!!.name_en != null) {
                    insuranceDetails.insuranceNameEn =
                        transactionsData.edges[item].insurance!!.name_en
                }

                if (transactionsData.edges[item].insurance!!.total_amount != null) {
                    insuranceDetails.insuranceTotalAmount =
                        transactionsData.edges[item].insurance!!.total_amount
                }

                if (transactionsData.edges[item].insurance!!.third_party_provider != null) {
                    insuranceDetails.insuranceThirdPartyProvider =
                        transactionsData.edges[item].insurance!!.third_party_provider
                }
                if (transactionsData.edges[item].insurance!!.price_per_day != null) {
                    insuranceDetails.insurancePricePerDay =
                        transactionsData.edges[item].insurance!!.price_per_day
                }

                if (transactionsData.edges[item].insurance!!.created_at != null) {
                    insuranceDetails.insuranceCreatedAt =
                        transactionsData.edges[item].insurance!!.created_at.toString()
                }

                transactionsDetails.insuranceDetails = insuranceDetails
            }
            transactionsList!!.add(transactionsDetails)
        }
        return transactionsList
    }
}