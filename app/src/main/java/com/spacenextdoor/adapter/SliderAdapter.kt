package com.spacenextdoor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacenextdoor.R


class SliderAdapter(private var context: Context) :
    RecyclerView.Adapter<SliderAdapter.Pager2ViewHolder>() {

    private var mCurrentPageSelected = 0

    lateinit var listImages: List<Int>
    lateinit var listTitles: List<String>
    lateinit var listDesc: List<String>

    fun setContentList(image: List<Int>, title: List<String>, description: List<String>) {
        this.listImages = image
        this.listTitles = title
        this.listDesc = description
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderAdapter.Pager2ViewHolder {
        return Pager2ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.onboarding_items, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listTitles.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.itemTitle.text = listTitles[position]
        holder.itemDetail.text = listDesc[position]
        holder.itemImage.setImageResource(listImages[position])
        mCurrentPageSelected = position;
        // return mCurrentPageSelected
    }

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tvTitles)
        val itemDetail: TextView = itemView.findViewById(R.id.tvDescription)
        val itemImage: ImageView = itemView.findViewById(R.id.sliderIv)

    }

}