package com.spacenextdoor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R
import com.spacenextdoor.model.PaymentDetail
import com.spacenextdoor.ui.activities.BottomNavigationActivity
import kotlinx.android.synthetic.main.card_view_payment.view.*


class PaymentAdapter(
    private val context: Context,
    private val paymentDetails: List<PaymentDetail>
) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.card_view_payment, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val paymentDetailsData = paymentDetails[position]
        holder.bind(paymentDetailsData)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BottomNavigationActivity::class.java)
            intent.putExtra("viewid", position)
            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return paymentDetails.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(paymentDetailsData: PaymentDetail) {
            itemView.paymentName.text = "Unit"
            itemView.paymentDescription.text = "Unit 0123, M size with AC"
            itemView.paymentDate.text = "1 Dec,2020"
            itemView.paymentTime.text = "11.30 AM"
            itemView.paymentTotalAmount.text = "$114.75"

        }
    }
}