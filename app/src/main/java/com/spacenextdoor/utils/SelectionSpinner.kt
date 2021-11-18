package com.spacenextdoor.utils

import com.spacenextdoor.R

data class SelectionSpinner(val image: Int, val name: String)

object selectedMonth {

    private val images = intArrayOf(
        R.drawable.bag_icon,
        R.drawable.bag_icon,
        R.drawable.bag_icon,
        R.drawable.bag_icon,
    )

    private val selectedMonth = arrayOf(
        "This week",
        "Last 3 weeks",
        "Previous month",
        "Calendar"
    )

    var list: ArrayList<SelectionSpinner>? = null
        get() {

            if (field != null)
                return field

            field = ArrayList()
            for (i in images.indices) {

                val imageId = images[i]
                val monthSelected = selectedMonth[i]

                val selectedOption = SelectionSpinner(imageId, monthSelected)
                field!!.add(selectedOption)
            }

            return field
        }
}