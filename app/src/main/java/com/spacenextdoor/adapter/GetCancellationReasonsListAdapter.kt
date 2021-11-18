package com.spacenextdoor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R
import com.spacenextdoor.model.CancelReasonsModel

class GetCancellationReasonsListAdapter(
    private val context: Context,
    private val reasonsList: MutableList<CancelReasonsModel>
) :
    RecyclerView.Adapter<GetCancellationReasonsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cancel_booking_spinner, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val cancelReasonDetailsItem = reasonsList[position]
        holder.sp_months.text = cancelReasonDetailsItem.description_en
    }

    override fun getItemCount(): Int {
        return reasonsList.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sp_months: TextView = itemView.findViewById(R.id.sp_months)
    }

}