package com.spacenextdoor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R
import com.spacenextdoor.model.TransactionListItem
import kotlinx.android.synthetic.main.transaction_list_item.view.*

class TransactionAdapter(
    private val context: Context,
    private val transactionListDetails: List<TransactionListItem>
) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false)
        view.minimumWidth = parent.measuredWidth
        val vh = ViewHolder(view)
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unitDetailsData = transactionListDetails[position]
        holder.bind(unitDetailsData)
        /*holder.moreDetails.setOnClickListener() {
            val intent = Intent(context, UnitDetails::class.java)
            context.startActivity(intent)
        }*/
    }

    override fun getItemCount(): Int {
        return transactionListDetails.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(unitDetailsData: TransactionListItem) {
            itemView.unitName.text = "Unit"
            itemView.unitDiscription.text = "M size with AC"
            itemView.unitDate.text = "Dec 2020"
            itemView.uniTotalamount.text = "$114.75"
        }

        val unitTotalAmount: TextView = itemView.findViewById<TextView>(R.id.uniTotalamount)
        val unitTv: TextView = itemView.findViewById<TextView>(R.id.unitName)
        val unitDiscription: TextView = itemView.findViewById<TextView>(R.id.unitDiscription)
        val unitDateTv: TextView = itemView.findViewById<TextView>(R.id.unitDate)
        val unitImg: ImageView = itemView.findViewById<ImageView>(R.id.unitListIimg)
    }

}