package com.spacenextdoor.service

import com.spacenextdoor.RequestTerminationMutation
import com.spacenextdoor.model.TerminationModel

object SetTerminationData {

    fun setDataInObject (requestTerminationData: RequestTerminationMutation.RequestTermination) : TerminationModel {

        var terminationRequestDetails = TerminationModel ()

        if (requestTerminationData.id != null) {
            terminationRequestDetails.terminationId = requestTerminationData.id
        }

        if (requestTerminationData.termination_date != null) {
            terminationRequestDetails.terminationDate =
                requestTerminationData.termination_date.toString()
        }

        if (requestTerminationData.move_out_date != null) {
            terminationRequestDetails.moveOutDate = requestTerminationData.move_out_date.toString()
        }

        if (requestTerminationData.failed_renewals_amount != null) {
            terminationRequestDetails.failedRenewalsAmount =
                requestTerminationData.failed_renewals_amount
        }

        if (requestTerminationData.remaining_days_amount != null) {
            terminationRequestDetails.remainingDaysAmount =
                requestTerminationData.remaining_days_amount
        }

        if (requestTerminationData.notice_period_amount != null) {
            terminationRequestDetails.noticePeriodAmount =
                requestTerminationData.notice_period_amount
        }

        if (requestTerminationData.promotion_amount != null) {
            terminationRequestDetails.promotionAmount = requestTerminationData.promotion_amount
        }


        if (requestTerminationData.discount != null) {
            terminationRequestDetails.discount = requestTerminationData.discount
        }

        if (requestTerminationData.total_amount != null) {
            terminationRequestDetails.totalAmount = requestTerminationData.total_amount
        }


        if (requestTerminationData.currency != null) {
            terminationRequestDetails.currency = requestTerminationData.currency
        }

        if (requestTerminationData.is_overdue != null) {
            terminationRequestDetails.isOverDue = requestTerminationData.is_overdue
        }

//        if ( terminationRequestDetails.status != null) {
//            terminationRequestDetails.statusModel.name = requestTerminationData.status
//        }
//
//        if ( terminationRequestDetails.paymentStatusModel!!.name != null) {
//            terminationRequestDetails.paymentStatus = requestTerminationData.payment_status
//        }
        return terminationRequestDetails
    }
}