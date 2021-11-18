package com.spacenextdoor.utils

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import com.spacenextdoor.R

class ProgressButton (context: Context, view: View) {
    @BindView(R.id.prgoressbar_Layout)
    lateinit var progressbarLayout : LinearLayout

    @BindView(R.id.progressbar_btn)
    lateinit var  progressbarBtn : ProgressBar

    @BindView(R.id.prgoressbar_text)
    lateinit var progressbarText : TextView

    fun activeButton () {
        progressbarBtn.visibility = View.VISIBLE

        progressbarText.text = "Please wait..."
    }

    fun finishButton () {
        progressbarBtn.visibility = View.GONE
    }
}