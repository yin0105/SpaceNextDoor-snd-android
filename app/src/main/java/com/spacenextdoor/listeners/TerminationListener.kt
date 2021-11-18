package com.spacenextdoor.listeners

import com.spacenextdoor.CalculateTerminationDuesMutation
import com.spacenextdoor.PayTerminationMutation
import com.spacenextdoor.RequestTerminationMutation

interface TerminationListener {
    fun onSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination)
    fun onFailure(message: String)
    fun onSuccessCalculateTerminationDues(calculateTerminationDuesData: CalculateTerminationDuesMutation.CalculateTerminationDues)
    fun onFailureCalculateTerminationDues(message: String)
    fun onFailurePayForTheBooking(message: String)
    fun onSuccessPayForTheBookingData(payForTheBookingData: PayTerminationMutation.PayTerminationAmount)

}