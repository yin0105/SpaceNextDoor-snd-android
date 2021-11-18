package com.spacenextdoor.service

import com.spacenextdoor.CancelBookingMutation
import com.spacenextdoor.model.CancelBookingModel

object SetCancelBookingData {

    fun setDataInObject(cancelBookingDataDetails: CancelBookingMutation.CancelBooking): CancelBookingModel {
        var cancelBookingData = CancelBookingModel()


        if (cancelBookingDataDetails.id != null) {
            cancelBookingData.cancelbookingId =
                cancelBookingDataDetails.id
        }

        if (cancelBookingDataDetails.penalty_applied != null) {
            cancelBookingData.cancelPenaltyApplied =
                cancelBookingDataDetails.penalty_applied
        }

        if (cancelBookingDataDetails.penalty_percent != null) {
            cancelBookingData.cancelPenaltyPercent =
                cancelBookingDataDetails.penalty_percent
        }

        if (cancelBookingDataDetails.refunded_amount != null) {
            cancelBookingData.cancelRefundedAmount =
                cancelBookingDataDetails.refunded_amount
        }

        return cancelBookingData

    }
}