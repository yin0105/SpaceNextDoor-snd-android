package com.spacenextdoor.listeners

import com.spacenextdoor.model.TransactionsModel

interface PaymentDetailListener {

    fun onPaymentDetailListener(transactionsModel: TransactionsModel)
    fun onPaymentDetailInsuranceListener(transactionsModel: TransactionsModel)
}