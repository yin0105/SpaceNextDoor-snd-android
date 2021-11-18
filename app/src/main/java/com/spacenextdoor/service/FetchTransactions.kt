package com.spacenextdoor.service

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleCoroutineScope
import com.apollographql.apollo.coroutines.await
import com.spacenextdoor.ApolloGraphqlClient.Apollo
import com.spacenextdoor.FetchTransactionsQuery
import com.spacenextdoor.listeners.TransactionListener
import com.spacenextdoor.utils.StringDeclerations

class FetchTransactions {

    private var transactionsListener: TransactionListener? = null

    fun setTransactionListener(transactionsListeners: TransactionListener) {
        transactionsListener = transactionsListeners
    }

    fun getTransactions(lifecycleScope: LifecycleCoroutineScope, context: FragmentActivity?) {

        lifecycleScope.launchWhenResumed {
            val response = try {
                Apollo.AuthorizationInterceptor(context!!).apolloClient(context).query(
                     FetchTransactionsQuery(limit = 100)
                ).await()
            } catch (e: Exception) {
                null
            }

            val transactionsData = response?.data?.transactions
            if (response != null) {
                if (transactionsData == null || response.hasErrors()) {
                    transactionsListener!!.onFailure(response!!.errors!![0].message)
                    return@launchWhenResumed
                } else {
                    transactionsListener!!.onSuccess(transactionsData)
                }
            } else {
                transactionsListener!!.onFailure(StringDeclerations.FAILURE)
            }

        }
    }
}