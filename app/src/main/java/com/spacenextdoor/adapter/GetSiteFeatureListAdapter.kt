package com.spacenextdoor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.spacenextdoor.R
import com.spacenextdoor.model.SiteFeatureModel
import com.spacenextdoor.ui.activities.UnitDetailsActivity
//import com.squareup.picasso.Picasso

class GetSiteFeatureListAdapter(
    private val context: Context,
    private val bookingList: MutableList<SiteFeatureModel>,
    private val unitDetailsActivity: UnitDetailsActivity
) :
    RecyclerView.Adapter<GetSiteFeatureListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(context).inflate(R.layout.adapter_site_feature, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bookingDetailsItem = bookingList[position]

        holder.unitTitle.text = bookingDetailsItem.nameEn

       /* Picasso.get()
            .load(bookingDetailsItem.iconUrl)
            .placeholder(R.drawable.ic_clock)
            .error(R.drawable.ic_cctv)
            .into(holder.unitIcon)*/

        GlideToVectorYou.justLoadImage(
            unitDetailsActivity,
            bookingDetailsItem.iconUrl!!.toUri(),
            holder.unitIcon
        )

    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val unitIcon: ImageView = itemView.findViewById(R.id.icon)
        val unitTitle: TextView = itemView.findViewById(R.id.title)

    }
}