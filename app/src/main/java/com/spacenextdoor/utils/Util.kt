package com.spacenextdoor.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.TextView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.spacenextdoor.detectConnection.ConnectionDetector
import ir.androidexception.andexalertdialog.AndExAlertDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Util {

    private var font_awsome: Typeface? = null
    var pDialog: SweetAlertDialog? = null

    fun showToast(message: String, it: View) {
        val snackBar = Snackbar.make(
            it, message,
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackBar.setActionTextColor(Color.WHITE)
        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.BLACK)
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackBar.show()
    }

    fun DETECT_INTERNET_CONNECTION(context: Context): Boolean {
        val cd = ConnectionDetector(context)
        return cd.isConnectingToInternet
    }

    fun init_Progress(context: Context): SweetAlertDialog? {
        pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#00695C")
        pDialog!!.titleText = "Please Wait ..."
        pDialog!!.setCancelable(false)
        return pDialog

    }

    fun pick_date(): String {
        val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        return currentDateAndTime
    }

    fun FONT_AWSOME(context: Context, view: View) {
        if (font_awsome == null) {
            font_awsome = Typeface.createFromAsset(context.assets, "font/fontawesome_webfont.ttf")
        }
        (view as TextView).setTypeface(font_awsome)
    }


    fun showDialog(title: String, message: String, contaxt: Context) {
        AndExAlertDialog.Builder(contaxt)
            .setTitle(title)
            .setMessage(message)
            .setPositiveBtnText("ok")
            .setCancelableOnTouchOutside(false)
            .OnPositiveClicked {
            }
            .build()
    }

    fun getFormattedDate(dateString: String): String? {
        val outputPattern = "dd MMM yyyy"
        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(dateString)// it's format should be same as inputPattern
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }

    fun getFormattedDateSelected(dateString: String): String? {
        val outputPattern = "dd MMM yyyy"
        val inputPattern = "EEE MMM dd HH:mm:ss zzzz yyyy"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(dateString)// it's format should be same as inputPattern
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }


    fun getFormattedDateTimeSelected(dateString: String): String? {
        val outputPattern = "dd MMM yyyy HH:mms"
        val inputPattern = "EEE MMM dd HH:mm:ss zzzz yyyy"
        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

        var date: Date? = null
        var str: String? = null

        try {
            date = inputFormat.parse(dateString)// it's format should be same as inputPattern
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return str
    }

}

