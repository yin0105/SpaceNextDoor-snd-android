package com.spacenextdoor.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.BuildConfig
import com.spacenextdoor.R
import com.spacenextdoor.model.UnitDetailModel
import com.spacenextdoor.ui.activities.UnitDetailsActivity
import com.spacenextdoor.utils.Util


class GetBookingsListAdapter(
    private val context: Context,
    private val bookingList: MutableList<UnitDetailModel>
) :
    RecyclerView.Adapter<GetBookingsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.card_view_home, parent, false)
        if (bookingList.size == 1) {
            BuildConfig.BASE_URL
            view.layoutParams = ViewGroup.LayoutParams(
                (parent.measuredWidth * 1.0).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )

        } else {
            view.layoutParams = ViewGroup.LayoutParams(
                (parent.measuredWidth * 0.90).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bookingDetailsItem = bookingList[position]

        holder.unitId.text =
            context.getString(R.string.label_unit) + " " + bookingDetailsItem.shortId

        holder.unitName.text = bookingDetailsItem.unitName
        holder.unitSize.text =
            context.getString(R.string.label_unit) + " " + bookingDetailsItem.shortId

        holder.moveInDate.text =
            Util.getFormattedDate(bookingDetailsItem.unitMovieInDate.toString())

        if (bookingDetailsItem.unitMoveOutDate != null && bookingDetailsItem.unitMoveOutDate != "") {
            holder.moveOutDate.text =
                Util.getFormattedDate(bookingDetailsItem.unitMoveOutDate.toString())
        } else {
            holder.moveOutDate.text = context.getString(R.string.label_monthly_ongoing)
        }

        holder.totalAmount.text =
            bookingDetailsItem.unitCurrencySign + String.format(
                "%.2f",
                bookingDetailsItem.unitAmount
            ) + " "

        holder.unitDescription.text =
            bookingDetailsItem.unitSpaceSize.toString() + " " + bookingDetailsItem.unitSpaceUnit!!.toLowerCase() + " " + bookingDetailsItem.unitSize

        holder.moreDetails.setOnClickListener() {

            if (Util.DETECT_INTERNET_CONNECTION(context)) {
                val intent = Intent(context, UnitDetailsActivity::class.java)
                intent.putExtra("bookingID", bookingDetailsItem.unitId)
                intent.putExtra("shortID", bookingDetailsItem.shortId)
                intent.putExtra("moveOutDate", "")
                context.startActivity(intent)
            } else {
                Util.showDialog(
                    context.getString(R.string.alert),
                    context.getString(R.string.no_Internet),
                    context
                )
            }
        }

        if (position != 0) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(20, 10, 20, 25)
            holder.cardViewSize.layoutParams = params
            holder.cardViewSize.requestLayout()
        } else {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            if (bookingList.size == 1) {
                params.setMargins(35, 10, 20, 25)
            } else {
                params.setMargins(35, 10, 10, 25)
            }

            holder.cardViewSize.layoutParams = params
            holder.cardViewSize.requestLayout()
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val unitName: TextView = itemView.findViewById(R.id.tvUnitName)
        val unitId: TextView = itemView.findViewById(R.id.tvUnitId)
        val moveInDate: TextView = itemView.findViewById(R.id.tvUnitMovieInDate)
        val moveOutDate: TextView = itemView.findViewById(R.id.tvUnitMoveOutDate)
        val unitSize: TextView = itemView.findViewById(R.id.tvUnitSize)
        val totalAmount: TextView = itemView.findViewById(R.id.tvUnitAmount)
        var unitDescription: TextView = itemView.findViewById(R.id.tvUnitDescription)
        val moreDetails: TextView = itemView.findViewById(R.id.tvMoreDetails)
        val cardViewSize: LinearLayout = itemView.findViewById(R.id.cardview)
        val rlTop: RelativeLayout = itemView.findViewById(R.id.rlTop)

    }
}