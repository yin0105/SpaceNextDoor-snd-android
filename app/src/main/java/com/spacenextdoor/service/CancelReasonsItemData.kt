package com.spacenextdoor.service

import com.spacenextdoor.CancellationReasonsQuery
import com.spacenextdoor.model.CancelReasonsModel

object CancelReasonsItemData {
    fun setDataInObject(cancellationReasonsData: CancellationReasonsQuery.Cancellation_reasons): MutableList<CancelReasonsModel> {

        var cancelReasonsList = mutableListOf<CancelReasonsModel>()


        for (item in cancellationReasonsData.edges) {

            var cancelDetail = CancelReasonsModel()

            if (item.id != null) {
                cancelDetail.id = item.id
            }
            if (item.description_en != null) {
                cancelDetail.description_en = item.description_en
            }

            cancelReasonsList.add(cancelDetail)

        }
        return cancelReasonsList
    }
}