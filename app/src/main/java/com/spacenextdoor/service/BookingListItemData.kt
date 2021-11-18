package com.spacenextdoor.service

import com.spacenextdoor.FetchBookingsQuery
import com.spacenextdoor.model.UnitDetailModel

object BookingListItemData {

    fun setDataInObject(bookingsData: FetchBookingsQuery.Bookings): MutableList<UnitDetailModel> {

        var bookingList = mutableListOf<UnitDetailModel>()
        for (item in bookingsData.edges) {

            var unitDetail = UnitDetailModel()

            if (item.id != null) {
                unitDetail.unitId = item.id.toString()
            }

            if (item.short_id != null) {
                unitDetail.shortId = item.short_id
            }

            if (item.site_name != null) {
                unitDetail.unitName = item.site_name
            }

            if (item.total_amount != null) {
                unitDetail.unitAmount = item.total_amount
            }

            if (item.currency_sign != null) {
                unitDetail.unitCurrencySign = item.currency_sign
            }


            if (item.currency != null) {
                unitDetail.unitCurrency = item.currency
            }

            if (item.move_in_date != null) {
                unitDetail.unitMovieInDate = item.move_in_date.toString()
            }

            if (item.move_out_date != null) {
                unitDetail.unitMoveOutDate = item.move_out_date.toString()
            }

            if (item.original_space != null) {
                if (item.original_space!!.space_type != null) {
                    unitDetail.unitSize = item.original_space!!.space_type!!.name_en
                }
                if (item.original_space.size != null) {
                    unitDetail.unitSpaceSize = item.original_space.size
                }
                if (item.original_space.size_unit != null) {
                    unitDetail.unitSpaceUnit = item.original_space.size_unit.toString()
                }
            }

            bookingList!!.add(unitDetail)
        }
        return bookingList
    }
}