package com.spacenextdoor.listeners

import com.spacenextdoor.FetchTransactionsQuery


interface TransactionListener {
    fun onSuccess(transactionsData: FetchTransactionsQuery.Transactions)
    fun onFailure(toString: String)
}