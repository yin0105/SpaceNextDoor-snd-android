package com.spacenextdoor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R
import com.spacenextdoor.listeners.PaymentDetailListener
import com.spacenextdoor.model.TransactionsModel
import com.spacenextdoor.utils.Util

class GetTransactionListAdapter(
    private val context: Context,
    private val transactionList: MutableList<TransactionsModel>,
    private val onPaymentListener: PaymentDetailListener?
) :
    RecyclerView.Adapter<GetTransactionListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false)
        view.minimumWidth = parent.measuredWidth
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transactionDetailsItem = transactionList[position]

        holder.transactionDate.text =
            Util.getFormattedDate(transactionDetailsItem.createdAt.toString())

        if (transactionDetailsItem.createdAt != null && transactionDetailsItem.createdAt != "") {
            holder.unitDate.text =
                Util.getFormattedDate(transactionDetailsItem.createdAt.toString())
        }

        if (transactionDetailsItem.bookingDetails!!.totalAmount != null) {
            holder.unitTotalAmount.text =
                transactionDetailsItem.bookingDetails!!.currencySign + String.format(
                    "%.2f", transactionDetailsItem.bookingDetails!!.totalAmount
                )
        }

        if (transactionDetailsItem.bookingDetails!!.bookingId != null && transactionDetailsItem.bookingDetails!!.bookingSpaceSize != null) {
            holder.unitDiscription.text =
                context.getString(R.string.label_unit) + " " + transactionDetailsItem.bookingDetails!!.shortId + ", " + transactionDetailsItem.bookingDetails!!.bookingSpaceSize.toString() + " " + transactionDetailsItem.bookingDetails!!.bookingSpaceSizeUnit.toLowerCase()
        }

        //  transactionDetailsItem.insuranceDetails!!.insuranceNameEn + " " +
        holder.unitDiscriptionInsurance.text =
            transactionDetailsItem.insuranceDetails!!.insuranceNameEn + ", " +
                    transactionDetailsItem.bookingDetails!!.currencySign + transactionDetailsItem.insuranceDetails!!.insurancePricePerDay.toString() + " " + "Per day"
        holder.unitDateInsurance.text =
            Util.getFormattedDate(transactionDetailsItem.insuranceDetails!!.insuranceCreatedAt.toString())
        holder.uniTotalAmountInsurance.text =
            transactionDetailsItem.bookingDetails!!.currencySign + String.format(
                "%.2f", transactionDetailsItem.insuranceDetails!!.insuranceTotalAmount
            )

//        holder.sendPaymentData.setOnClickListener {
//            onPaymentListener!!.onPaymentDetailListener(transactionList[position])
//        }

        holder.cardview1.setOnClickListener {
            onPaymentListener!!.onPaymentDetailListener(transactionList[position])
        }

        holder.cardview2.setOnClickListener {
            onPaymentListener!!.onPaymentDetailInsuranceListener(transactionList[position])
        }

        if (position == transactionList.size - 1) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 200)
            holder.sendPaymentData.setLayoutParams(params)
        }

    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val unitTotalAmount: TextView = itemView.findViewById(R.id.uniTotalamount)
        val unitDiscription: TextView = itemView.findViewById(R.id.unitDiscription)
        val unitDate: TextView = itemView.findViewById(R.id.unitDate)
        val unitDiscriptionInsurance: TextView =
            itemView.findViewById(R.id.unitDiscription_insurance)
        val unitDateInsurance: TextView = itemView.findViewById(R.id.unitDate_insurance)
        val uniTotalAmountInsurance: TextView = itemView.findViewById(R.id.uniTotalamount_insurance)
        val transactionDate: TextView = itemView.findViewById(R.id.transactionDate)
        val sendTransactionDetails: View = itemView.findViewById(R.id.sendTransactionDetails)
        var sendPaymentData: RelativeLayout = itemView.findViewById(R.id.sendPaymentData)
        var cardview1: LinearLayout = itemView.findViewById(R.id.cardview1)
        var cardview2: LinearLayout = itemView.findViewById(R.id.cardview2)

    }
}