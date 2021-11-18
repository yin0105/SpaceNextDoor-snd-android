package com.spacenextdoor.service

import com.spacenextdoor.CalculateTerminationDuesMutation
import com.spacenextdoor.model.TerminationModel

object SetCalculationTerminationDuesData {

    fun setDataInObject(calculateTerminationDuesData: CalculateTerminationDuesMutation.CalculateTerminationDues): TerminationModel {
        var calculateTerminationDuesDetails = TerminationModel()


        if (calculateTerminationDuesData.move_out_date != null) {
            calculateTerminationDuesDetails.moveOutDate =
                calculateTerminationDuesData.move_out_date.toString()
        }


        if (calculateTerminationDuesData.currency_sign != null) {
            calculateTerminationDuesDetails.currencySign =
                calculateTerminationDuesData.currency_sign
        }

        if (calculateTerminationDuesData.currency != null) {
            calculateTerminationDuesDetails.currency = calculateTerminationDuesData.currency
        }

        if (calculateTerminationDuesData.termination_date != null) {
            calculateTerminationDuesDetails.terminationDate =
                calculateTerminationDuesData.termination_date.toString()
        }

        if (calculateTerminationDuesData.failed_renewals_amount != null) {
            calculateTerminationDuesDetails.failedRenewalsAmount =
                calculateTerminationDuesData.failed_renewals_amount
        }


        if (calculateTerminationDuesData.remaining_days_amount != null) {
            calculateTerminationDuesDetails.remainingDaysAmount =
                calculateTerminationDuesData.remaining_days_amount
        }

        if (calculateTerminationDuesData.notice_period_amount != null) {
            calculateTerminationDuesDetails.noticePeriodAmount =
                calculateTerminationDuesData.notice_period_amount
        }

        if (calculateTerminationDuesData.promotion_amount != null) {
            calculateTerminationDuesDetails.promotionAmount =
                calculateTerminationDuesData.promotion_amount
        }


        if (calculateTerminationDuesData.total_amount != null) {
            calculateTerminationDuesDetails.totalAmount =
                calculateTerminationDuesData.total_amount
        }

        return calculateTerminationDuesDetails

    }
}