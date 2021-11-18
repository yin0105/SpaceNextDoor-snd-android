package com.spacenextdoor.ui.activities.Termination

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.spacenextdoor.CalculateTerminationDuesMutation
import com.spacenextdoor.PayTerminationMutation
import com.spacenextdoor.R
import com.spacenextdoor.RequestTerminationMutation
import com.spacenextdoor.listeners.TerminationListener
import com.spacenextdoor.model.BookingDetailsModel
import com.spacenextdoor.model.TerminationModel
import com.spacenextdoor.service.RequestTerminationBooking
import com.spacenextdoor.service.SetCalculationTerminationDuesData
import com.spacenextdoor.service.SetTerminationData
import com.spacenextdoor.ui.activities.EventDecoratorColor
import com.spacenextdoor.ui.activities.UnitDetailsActivity
import com.spacenextdoor.utils.EventDecorator
import com.spacenextdoor.utils.Util
import ir.androidexception.andexalertdialog.AndExAlertDialog
import kotlinx.android.synthetic.main.activity_request_termination.*
import kotlinx.android.synthetic.main.layout_toolbar_unit_details.view.*
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RequestTermination : AppCompatActivity(), TerminationListener {

    var terminationListener: TerminationListener = this
    var requestTerminationBooking = RequestTerminationBooking()

    var BookingId: Int? = null
    var moveOutDateRequest: String? = ""
    var requestTerminationCurrentDate: String? = ""
    var requestTerminationDate: String = ""

    var moveOutDateForTermination: String = ""
    val calander = Calendar.getInstance()
    var pDialog: SweetAlertDialog? = null
    var moveOutLabelDate: String? = null
    var globaleDate: CalendarDay? = null
    var terminationDateWhenNoValue: CalendarDay? = null
    var bookinIdPre: String? = null
    var terminationRequestDetails: BookingDetailsModel? = null
    var calculateTerminationDetails: TerminationModel? = null
    var Id: Int = 0
    var mMoveInDate: String? = null
    var shortId: String? = null
    var urlImage: String? = ""

    @BindView(R.id.btnCancel)
    lateinit var btnCancel: TextView

    @BindView(R.id.layoutBeforeLoader)
    lateinit var layoutBeforeLoader: RelativeLayout

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView

    @BindView(R.id.imageData)
    lateinit var imageData: ImageView

    lateinit var loaderShownInView: RelativeLayout
    var moveOutDateNew: String = ""

    var moveOutSelectedDate: String = ""
    var monthlyOngoing: Boolean = false

    var URLLoad: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_termination)

        ButterKnife.bind(this)
        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        loaderShownInView = findViewById(R.id.rlLoader)
        loaderShownInView.visibility = View.VISIBLE
        imWaveFormation.setAnimation(R.raw.loader)
        imWaveFormation.visibility = View.VISIBLE

        // pDialog = Util.init_Progress(this)
        gettingDataFromPreviousActivity()
        toolbar.image_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun gettingDataFromPreviousActivity() {
        val bundle = intent.getBundleExtra("Bundle")
//        urlImage =
//            "https://assets.spacenextdoor.com/uploads/image/image/272/8e11aa93a3f1256677728f57d7ef6b15150dac5b17dfa2555169bcc220b1942a.jpg"
//
//        Glide.with(this)
//            .load(urlImage)
//            .into(imageData);


        terminationRequestDetails =
            bundle!!.getParcelable<BookingDetailsModel>("terminationDetails")//its mean not null*/

        calculateTerminationDetails = bundle!!.getParcelable("calculateTerminationDetails")

        BookingId = terminationRequestDetails!!.bookingId

        if (terminationRequestDetails!!.unitLoadImageUrl != null) {

            if (terminationRequestDetails!!.unitLoadImageUrl!!.size > 0) {
                URLLoad = terminationRequestDetails!!.unitLoadImageUrl!![0].urlImage
                Glide.with(this)
                    .load(URLLoad)
                    .into(imageData);
            }
        } else {
            imageData.setImageResource(R.drawable.no_image_placeholder)
        }



        Id = BookingId!!
        bookinIdPre = intent.getStringExtra("bookingID")
        shortId = intent.getStringExtra("shortID")

        moveOutSelectedDate = intent.getStringExtra("moveOutDateSelected")!!
        var moveoutDate = intent.getStringExtra("moveOutDate")
        requestTerminationCurrentDate = intent.getStringExtra("currentDate")
        mMoveInDate = intent.getStringExtra("moveInDate")
        monthlyOngoing = intent.getBooleanExtra("monthlyOnGoing", false)

        moveOutDateForTermination = moveoutDate!!
        moveOutLabelDate = moveoutDate

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.spaceUnitID.text = getString(R.string.label_unit) + " " + shortId

        requestTerminationBooking.setRequestTerminationListener(terminationListener!!)

        setTerminationDetails(terminationRequestDetails)

        setDuesDataInViews(calculateTerminationDetails!!)

        // callingDataShowAPI(moveoutDate)
    }

    private fun callingDataShowAPI(mDate: String?) {

        //setTerminationDetails(terminationRequestDetails)
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
//            pDialog!!.show()
            //requestTerminationBooking.requestTerminationForBooking(lifecycleScope, this, moveoutDate , Id.toInt())
            requestTerminationBooking.calculateTerminationDues(
                lifecycleScope,
                this,
                mDate!!,
                Id
            )

        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
    }

    private fun setTerminationDetails(terminationRequestDetails: BookingDetailsModel?) {

        buildingName.text = terminationRequestDetails!!.siteName
        bookingID.text = "Booking ID" + " " + terminationRequestDetails.bookingId.toString()
        streetName.text =
            terminationRequestDetails.districtDetailModel!!.nameEn + " , " + terminationRequestDetails.countryDetailModel!!.nameEn

    }

    @OnClick(R.id.changeDate)
    fun changeDate() {
        openCustomCalendar()
    }

    @OnClick(R.id.btnCancel)
    fun moveBack() {
        intent.putExtra("shortID", shortId)
        onBackPressed()
    }


    private fun openCustomCalendar() {
        val bundle = intent.getBundleExtra("Bundle")

        var terminationRequestDetails =
            bundle!!.getParcelable<BookingDetailsModel>("terminationDetails")//its mean not null*/
        BookingId = terminationRequestDetails!!.bookingId
        // var Id: String = bookingId.toString()
        var Id: String = BookingId.toString()

        var calenderOngoing: Calendar? = null
        val currentDay: Date = Calendar.getInstance().time
        val currentDate: CalendarDay = CalendarDay.from(currentDay)

        val view = View.inflate(this, R.layout.calendar_dialog, null)
        val moveoutDate = view.findViewById<MaterialCalendarView>(R.id.moveout_date)
        val dialogOk = view.findViewById<TextView>(R.id.dialog_ok)
        val cancelIconDialog = view.findViewById<ImageView>(R.id.canel_icon_dialog)
        var clearDate: TextView = view.findViewById<TextView>(R.id.date_clear)

        if (!requestForTerminationDate.text.equals(getString(R.string.label_monthly_ongoing))) {

            if (!moveOutDateNew.equals("")) {
                calenderOngoing = gettingDateExist(moveOutDateNew.toString())
                settingDecoratorCurrent(calenderOngoing.time.toString(), moveoutDate)
                moveoutDate.selectedDate = CalendarDay.from(calenderOngoing.time)
            } else {
                calenderOngoing = gettingDateExist(moveOutSelectedDate.toString())
                settingDecoratorCurrent(calenderOngoing.time.toString(), moveoutDate)
                moveoutDate.selectedDate = CalendarDay.from(calenderOngoing.time)

            }

            if (!monthlyOngoing) {
                var calendermax = gettingDateExist(moveOutLabelDate.toString())
                moveoutDate.state().edit().setMaximumDate(CalendarDay.from(calendermax.time))
                    .commit()
            } else {

                val calender = gettingDate(currentDate.toString())
                calender.add(Calendar.DATE, 30)

                moveoutDate.state().edit()
                    .setMaximumDate(CalendarDay.from(calender!!.time))
                    .commit()

            }
        } else {
            settingDecorator(currentDate.toString(), moveoutDate)
        }

        setCurrentDayDecoder(currentDate, moveoutDate)

        requestTerminationDate = Util.getFormattedDateSelected(currentDate.toString()).toString()


        moveoutDate.state().edit().setMinimumDate(currentDay).commit()

        moveoutDate.setOnDateChangedListener { widget, date, selected ->
            if (date != currentDate) {
                globaleDate = date
            }

            if (date != currentDate) {

                widget.removeDecorators()

                if (!requestForTerminationDate.text.equals(getString(R.string.label_monthly_ongoing))) {

                    val terminateDate: CalendarDay = CalendarDay.from(calenderOngoing!!.time)

                    when {
                        date == terminateDate -> {
                            settingDecoratorCurrent(
                                widget.selectedDate.date.toString(),
                                moveoutDate
                            )
                        }
                        date.isAfter(terminateDate) -> {
                            settingDecoratorCurrent(
                                widget.selectedDate.date.toString(),
                                moveoutDate
                            )
                        }
                        date.isBefore(terminateDate) -> {

                            val date2: CalendarDay = settingDate(mMoveInDate!!)

                            if (date.isInRange(currentDate, date2)) {
                                settingDecorator(currentDate.toString(), moveoutDate)
                            } else {
                                settingDecoratorCurrent(
                                    widget.selectedDate.date.toString(),
                                    moveoutDate
                                )
                            }

                        }
                        else -> {
                            settingDecorator(widget.selectedDate.date.toString(), moveoutDate)
                        }
                    }

                } else {

                    terminationDateWhenNoValue = getTerminateDate(currentDate.toString())

                    if (date.isAfter(terminationDateWhenNoValue!!)) {
                        settingDecoratorCurrent(widget.selectedDate.date.toString(), moveoutDate)
                    } else {
                        settingDecorator(currentDate.toString(), moveoutDate)
                    }
                }

                var day: String = widget.selectedDate.date.toString()
                setCurrentDayDecoder(currentDate, moveoutDate)
                moveOutDateForTermination = Util.getFormattedDateSelected(day).toString()
            } else {
                if (globaleDate != null && globaleDate != currentDate) {
                    moveoutDate.selectedDate = globaleDate
                } else {
                    if (!requestForTerminationDate.text.equals(getString(R.string.label_monthly_ongoing))) {
                        val terminateDate: CalendarDay = CalendarDay.from(calenderOngoing!!.time)
                        moveoutDate.selectedDate = terminateDate
                    } else {
                        moveoutDate.selectedDate = null
                    }

                }
            }
        }

        val builder = AlertDialog.Builder(this@RequestTermination)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogOk.setOnClickListener {
            requestForTerminationDate.text = moveOutDateForTermination

            if (requestForTerminationDate.text == "") {
                val messageTermination =
                    "Please choose a Move Out Date before calling the Request Termination"
                Util.showDialog(
                    "Alert",
                    messageTermination,
                    this
                )
            } else {
                callingDataShowAPI(moveOutDateForTermination)
                dialog.dismiss()
            }
        }

        cancelIconDialog.setOnClickListener {
            dialog.dismiss()
        }

        clearDate.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onBackPressed() {
        startBackAcitvity()
    }

    private fun startBackAcitvity() {
        val intent = Intent(this, UnitDetailsActivity::class.java)
        intent.putExtra("moveOutDateOld", moveOutLabelDate)
        intent.putExtra("moveOutDate", moveOutDateForTermination)
        intent.putExtra("bookingID", bookinIdPre)
        intent.putExtra("shortID", shortId)
        intent.putExtra("monthlyOnGoing", monthlyOngoing)
        startActivity(intent)
        finish()
    }


    private fun setCurrentDayDecoder(currentDay: CalendarDay, moveoutDate: MaterialCalendarView?) {
        moveoutDate!!.addDecorator(
            EventDecoratorColor(
                Color.parseColor("#00A0E3"),
                currentDay
            )
        )
    }

    private fun gettingDate(Day: String): Calendar {

        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val c = Calendar.getInstance()
        try {
            Log.e("", "aaaa " + sdf.parse(Day))
            var date = sdf.parse(Day)
            c.time = sdf.parse(Day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return c
    }

    private fun gettingDateExist(Day: String): Calendar {
        val sdf = SimpleDateFormat("dd MMM yyyy")
        val c = Calendar.getInstance()
        try {
            Log.e("", "aaaa " + sdf.parse(Day))
            var date = sdf.parse(Day)
            c.time = sdf.parse(Day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return c
    }


    private fun settingDate(
        currentDay: String
    ): CalendarDay {

        val calender = gettingDate(currentDay)
        calender.add(Calendar.DATE, 14)

        return CalendarDay.from(calender.time)
    }

    private fun settingDecorator(
        currentDay: String,
        moveoutDate: MaterialCalendarView
    ) {
        val calender = gettingDate(currentDay)
        calender.add(Calendar.DATE, 14)

        val terminateDate: CalendarDay = CalendarDay.from(calender.time)
        moveoutDate.addDecorator(
            EventDecorator(
                Color.parseColor("#EA5B21"),
                terminateDate
            )
        )
    }

    private fun getTerminateDate(currentDay: String): CalendarDay? {

        val calender = gettingDate(currentDay)
        calender.add(Calendar.DATE, 14)

        val terminateDate: CalendarDay = CalendarDay.from(calender.time)
        return terminateDate
    }

    private fun settingDecoratorCurrent(currentDay: String, moveoutDate: MaterialCalendarView?) {
        val calender = gettingDate(currentDay)

        val terminateDate: CalendarDay = CalendarDay.from(calender.time)
        moveoutDate!!.addDecorator(
            EventDecorator(
                Color.parseColor("#EA5B21"),
                terminateDate
            )
        )

    }

    private fun payForBooking(terminationId: Int) {
        var Id = terminationId
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            // pDialog!!.show()
            requestTerminationBooking.payForTheBooking(lifecycleScope, this, Id)
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                this
            )
        }

    }

    private fun setDuesDataInViews(calculateTerminationDetails: TerminationModel) {

        moveOutDate.text =
            Util.getFormattedDate(calculateTerminationDetails.moveOutDate.toString())

        contractTerminatingDate.text =
            Util.getFormattedDate(calculateTerminationDetails.terminationDate.toString())
//        promotionConditionFee.text = "S$" + String.format(
//            "%.2f", calculateTerminationDetails.promotionAmount
//        )
        TotalAmount.text = "S$" + String.format(
            "%.2f", calculateTerminationDetails.totalAmount
        )
        requestForTerminationDate.text =
            requestTerminationCurrentDate.toString()

        renewalAmount.text = "S$" + String.format(
            "%.2f", calculateTerminationDetails.failedRenewalsAmount
        )
        noticePeriodAmount.text = "S$" + String.format(
            "%.2f", calculateTerminationDetails.noticePeriodAmount
        )
        remainingDaysAmount.text = "S$" + String.format(
            "%.2f", calculateTerminationDetails.remainingDaysAmount
        )

        noticePeriodDate.text =
            requestTerminationCurrentDate.toString() + " - " + Util.getFormattedDate(
                calculateTerminationDetails.terminationDate.toString()
            )

        imWaveFormation.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        loaderShownInView.visibility = View.VISIBLE


    }

    @OnClick(R.id.btnConfirmAndPay)
    fun sendTerminationRequest() {

        var Id = BookingId
        var moveoutDate = intent.getStringExtra("moveOutDate")

        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            imWaveFormation.visibility = View.VISIBLE
            layoutBeforeLoader.visibility = View.GONE
            // pDialog!!.show()
            requestTerminationBooking.requestTerminationForBooking(
                lifecycleScope,
                this,
                moveoutDate!!,
                Id!!
            )
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                this
            )
        }
    }

    override fun onSuccessCalculateTerminationDues(calculateTerminationDuesData: CalculateTerminationDuesMutation.CalculateTerminationDues) {
        // pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        loaderShownInView.visibility = View.VISIBLE
        moveOutDateNew = moveOutDateForTermination
        if (calculateTerminationDuesData != null) {
            var calculateTerminationDuesDetails =
                SetCalculationTerminationDuesData.setDataInObject(calculateTerminationDuesData)
            setDuesDataInViews(calculateTerminationDuesDetails)
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_result_found), this)
        }
    }

    override fun onFailureCalculateTerminationDues(message: String) {
        // on Failure of a function
        //pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        val getDetailsMessage = message
        Util.showDialog(
            "Alert",
            getDetailsMessage,
            this
        )
    }

    override fun onFailurePayForTheBooking(message: String) {
        // pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        val getDetailsMessage = message
        Util.showDialog(
            "Alert",
            getDetailsMessage,
            this
        )
    }

    override fun onSuccessPayForTheBookingData(payForTheBookingData: PayTerminationMutation.PayTerminationAmount) {
        // pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        val getDetailsMessage = "Payment is successful"
        if (payForTheBookingData.success) {
            showOkDialog(
                "Alert",
                getDetailsMessage,
                this
            )
        }
    }

    fun showOkDialog(title: String, message: String, contaxt: Context) {
        AndExAlertDialog.Builder(contaxt)
            .setTitle(title)
            .setMessage(message)
            .setPositiveBtnText("ok")
            .setCancelableOnTouchOutside(false)
            .OnPositiveClicked {
                startBackAcitvity()
            }
            .build()
    }

    override fun onFailure(message: String) {
        // on Failure of a function
        // pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE
        loaderShownInView.visibility = View.GONE
        layoutBeforeLoader.visibility = View.VISIBLE
        val getDetailsMessage = message
        Util.showDialog(
            "Alert",
            getDetailsMessage,
            this
        )
    }

    override fun onSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination) {
        if (requestTerminationData != null) {
            var terminationRequestDetails =
                SetTerminationData.setDataInObject(requestTerminationData)
            var terminationId = requestTerminationData.id
            payForBooking(terminationId)
            //setDataInViews(terminationRequestDetails)
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_result_found), this)
        }
    }

    /// Unused Method..
    private fun setDataInViews(terminationRequestDetails: TerminationModel) {
        moveOutDate.text = Util.getFormattedDate(terminationRequestDetails.moveOutDate.toString())
        noticePeriodDate.text =
            Util.getFormattedDate(terminationRequestDetails.moveOutDate.toString()) + " " + Util.getFormattedDate(
                terminationRequestDetails.terminationDate.toString()
            )
        contractTerminatingDate.text = terminationRequestDetails.terminationDate.toString()
//        promotionConditionFee.text = terminationRequestDetails.promotionAmount.toString()
        TotalAmount.text = terminationRequestDetails.totalAmount.toString()
        requestForTerminationDate.text =
            Util.getFormattedDate(terminationRequestDetails.terminationDate.toString())
    }

}