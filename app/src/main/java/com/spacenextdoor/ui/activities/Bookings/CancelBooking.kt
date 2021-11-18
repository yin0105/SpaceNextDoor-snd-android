package com.spacenextdoor.ui.activities.Bookings

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.spacenextdoor.R
import com.spacenextdoor.model.BookingDetailsModel
import com.spacenextdoor.model.CancelBookingModel
import com.spacenextdoor.ui.activities.BottomNavigationActivity
import com.spacenextdoor.ui.activities.UnitDetailsActivity
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.layout_toolbar_unit_details.view.*
import org.w3c.dom.Text

class CancelBooking : AppCompatActivity() {

    @BindView(R.id.cardViewFour)
    lateinit var cardViewFour: CardView

    @BindView(R.id.bookingCancelStatus)
    lateinit var bookingCancelStatus: TextView

    @BindView(R.id.bookingCancelTime)
    lateinit var bookingCancelTime: TextView

    @BindView(R.id.bookingShortId)
    lateinit var bookingShortId: TextView

    @BindView(R.id.bookingCancelSize)
    lateinit var bookingCancelSize: TextView

//    @BindView(R.id.refundedAmount)
//    lateinit var refundedAmount: TextView

    @BindView(R.id.plentyApplied)
    lateinit var plentyApplied: TextView


    @BindView(R.id.TotalAmountPaid)
    lateinit var TotalAmountPaid: TextView

    @BindView(R.id.cardLastFourDigits)
    lateinit var cardLastFourDigits: TextView

    @BindView(R.id.findAnotherStorage)
    lateinit var findAnotherStorage: TextView

    @BindView(R.id.paymentRefund)
    lateinit var paymentRefund: TextView

    @BindView(R.id.backToHome)
    lateinit var backToHome: TextView

    @BindView(R.id.subtotalAmount)
    lateinit var subtotalAmount: TextView

    @BindView(R.id.insuranceAmount)
    lateinit var insuranceAmount: TextView

    var totalAmountForCancelBooking: Double = 0.0

//    @BindView(R.id.plentyPercent)
//    lateinit var plentyPercent : TextView

    var bookingRequestDetails: BookingDetailsModel? = null
    var cancelBookingDetails: CancelBookingModel? = null
    var shortId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_booking)
        ButterKnife.bind(this)
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.image_back.setOnClickListener {
            onBackPressed()
        }
        settingCardViewBackground()
        gettingDataFromPreviousActivity()
    }

    private fun gettingDataFromPreviousActivity() {
        val bundle = intent.getBundleExtra("Bundle")
        shortId = intent.getStringExtra("shortID")
        bookingRequestDetails = bundle!!.getParcelable("bookingDetailsData")
        cancelBookingDetails = bundle!!.getParcelable("cancelBookingDetailsResponse")
        shortId = intent.getStringExtra("shortID")

        setBookingDataInViews(bookingRequestDetails!!)
        setCancelBookingResponse(cancelBookingDetails!!)
    }

    private fun setCancelBookingResponse(cancelBookingDetails: CancelBookingModel) {

//        totalAmountForCancelBooking =
//            cancelBookingDetails.cancelRefundedAmount!!.toInt() + cancelBookingDetails.cancelPenaltyApplied!!.toInt()


//        refundedAmount.text = "S$" + String.format(
//            "%.2f", cancelBookingDetails.cancelRefundedAmount
//        )
        plentyApplied.text = "S$" + String.format(
            "%.2f", cancelBookingDetails.cancelPenaltyPercent
        )
        if (cancelBookingDetails.cancelPenaltyApplied == true) {
            paymentRefund.text = "Partial refund"
        } else {
            paymentRefund.text = "Full refund"
        }


        TotalAmountPaid.text = "S$" + String.format(
            "%.2f", cancelBookingDetails.cancelRefundedAmount
        )

    }

    private fun setBookingDataInViews(bookingRequestDetails: BookingDetailsModel) {
        bookingCancelStatus.text = bookingRequestDetails.Status
        bookingCancelTime.text =
            Util.getFormattedDate(bookingRequestDetails.unitMoveOutDate.toString())
        bookingShortId.text = getString(R.string.label_unit) + " " + shortId
        bookingCancelSize.text =
            bookingRequestDetails.unitDetailModel!!.unitSpaceSize.toString() + " " + bookingRequestDetails.unitDetailModel!!.unitSpaceUnit.toString()
                .toLowerCase()

        if (bookingRequestDetails.transactionDetailModel!!.transactionCardLastDigits != null) {
            cardLastFourDigits.text = "**** **** **** " +
                    bookingRequestDetails.transactionDetailModel!!.transactionCardLastDigits.toString()
        }

        subtotalAmount.text = "S$" + String.format(
            "%.2f", bookingRequestDetails.subTotalAmount
        )

        insuranceAmount.text = "S$" + String.format(
            "%.2f", bookingRequestDetails.insuranceAmount
        )

    }

    private fun settingCardViewBackground() {
        // cardViewOne.setBackgroundResource(R.drawable.card_view)
        cardViewFour.setBackgroundResource(R.drawable.card_view)
    }


    @OnClick(R.id.findAnotherStorage)
    fun openAddAnotherStorage() {
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            var URL = "https://dev.spacenextdoor.com/"
            FinestWebView.Builder(this).show(URL)
            Log.e("URL", URL)

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                Activity()
            )
        }
    }

    @OnClick(R.id.backToHome)
    fun backHome() {
        onBackPressed()
    }

    override fun onBackPressed() {
        startBackAcitvity()
    }

    private fun startBackAcitvity() {
        val intent = Intent(this, BottomNavigationActivity::class.java)
        startActivity(intent)
        finish()
    }


}