package com.spacenextdoor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R
import com.spacenextdoor.model.UnitDetailModel
import com.spacenextdoor.ui.activities.UnitDetailsActivity
import kotlinx.android.synthetic.main.card_view_home.view.*

class UnitAdapter(private val context: Context, private val unitDetails: List<UnitDetailModel>) :
    RecyclerView.Adapter<UnitAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view_home, parent, false)
        view.minimumWidth = parent.measuredWidth
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unitDetailsData = unitDetails[position]
        holder.bind(unitDetailsData)
        holder.moreDetails.setOnClickListener() {
            val intent = Intent(context, UnitDetailsActivity::class.java)
            intent.putExtra("moveOutDate", "")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return unitDetails.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(unitDetailsData: UnitDetailModel) {
            itemView.tvUnitName.text = "Unit 0123"
            itemView.tvUnitSize.text = "M size with AC"
            itemView.tvUnitDescription.text = "4.9 sq Ft"
            // itemView.unitAdress.text = "Packages Mall"
            itemView.tvUnitMovieInDate.text = "10 jan 2002"
            itemView.tvUnitMoveOutDate.text = "Monthly Ongoing"
            itemView.tvUnitAmount.text = "$114.75"
        }

        val moreDetails: TextView = itemView.findViewById<TextView>(R.id.tvMoreDetails)
    }

}