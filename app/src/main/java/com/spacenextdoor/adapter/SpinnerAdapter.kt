package com.spacenextdoor.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.spacenextdoor.R
import com.spacenextdoor.model.CancelReasonsModel
import kotlinx.android.synthetic.main.cancel_booking_spinner.view.*

class SpinnerAdapter1(context: Context, selectionList: MutableList<CancelReasonsModel>) :
    ArrayAdapter<CancelReasonsModel>(context, 0, selectionList) {

    var lisData = selectionList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val spinner = getItem(position)
        val view =
            LayoutInflater.from(context).inflate(R.layout.cancel_booking_spinner, parent, false)
        view.sp_months.text = spinner!!.description_en
        return view
    }

    override fun getItem(position: Int): CancelReasonsModel? {
        return super.getItem(position)
    }
}