package com.spacenextdoor.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.spacenextdoor.*
import com.spacenextdoor.adapter.GetSiteFeatureListAdapter
import com.spacenextdoor.adapter.SpinnerAdapter1
import com.spacenextdoor.listeners.BookingsListener
import com.spacenextdoor.listeners.TerminationListener
import com.spacenextdoor.model.BookingDetailsModel
import com.spacenextdoor.model.CancelBookingModel
import com.spacenextdoor.model.CancelReasonsModel
import com.spacenextdoor.service.*
import com.spacenextdoor.ui.activities.Bookings.CancelBooking
import com.spacenextdoor.ui.activities.Termination.RequestTermination
import com.spacenextdoor.utils.EventDecorator
import com.spacenextdoor.utils.Util
import kotlinx.android.synthetic.main.activity_unit_details.*
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.layout_toolbar_unit_details.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UnitDetailsActivity : AppCompatActivity(), BookingsListener, TerminationListener {

    var bookingsListener: BookingsListener = this
    var fetchBookingsById = FetchBookings()
    var Latitude = null
    var Longitude = null
    var LatLng = null
    var moveOutDateForTermination: String = ""
    var requestTerminationDate: String = ""
    val calander = Calendar.getInstance()
    var pDialog: SweetAlertDialog? = null
    var moveOutLabelDate: String? = null
    var moveInLabelDate: String? = null
    var globaleDate: CalendarDay? = null
    var terminationDateWhenNoValue: CalendarDay? = null
    var bookingStatus: String? = null
    var bookingId: String? = null
    var shortId: String? = null
    var isOngoingData: Boolean = false

    @BindView(R.id.siteRecycler)
    lateinit var recyclerViewSite: RecyclerView


    @BindView(R.id.collapsing_toolbar_layout)
    lateinit var wholeLayoutUnitDetails: CollapsingToolbarLayout

    private var unitDetailsSkeletionScreen: SkeletonScreen? = null

    var bookingTerminionDetail: BookingDetailsModel? = null

    var cancelBookingDataDetails: CancelBookingModel? = null

    @BindView(R.id.cardViewTwo)
    lateinit var cardViewTwo: CardView

    @BindView(R.id.cardViewThree)
    lateinit var cardViewThree: CardView

    @BindView(R.id.cardViewFour)
    lateinit var cardViewFour: CardView

    @BindView(R.id.tvUnitDetailsId)
    lateinit var tvUnitDetailsId: TextView

    @BindView(R.id.tvUnitAccess)
    lateinit var tvUnitAccess: TextView

    @BindView(R.id.tvRequestTermination)
    lateinit var tvRequestTermination: TextView

    @BindView(R.id.alreadyRequestedTerminationBtn)
    lateinit var alreadyRequestedTerminationBtn: TextView

    @BindView(R.id.alreadyRequestedTermination)
    lateinit var alreadyRequestedTermination: TextView

    @BindView(R.id.bookingSqft)
    lateinit var bookingSqft: TextView

    @BindView(R.id.bookingSize)
    lateinit var bookingSize: TextView

    @BindView(R.id.siteName)
    lateinit var siteName: TextView

    @BindView(R.id.streetName)
    lateinit var streetName: TextView

    @BindView(R.id.moveInDate)
    lateinit var moveInDate: TextView

    @BindView(R.id.moveOutDateLabel)
    lateinit var moveOutDateLabel: TextView


    @BindView(R.id.postalCode)
    lateinit var postalCode: TextView

    @BindView(R.id.renewalInsuranceAmount)
    lateinit var renewalInsuranceAmount: TextView

    @BindView(R.id.RenewalTotalAmount)
    lateinit var RenewalTotalAmount: TextView

    @BindView(R.id.InsurancePricePerDay)
    lateinit var InsurancePricePerDay: TextView

    @BindView(R.id.Insurancecoverage)
    lateinit var Insurancecoverage: TextView

    @BindView(R.id.cardLastFourDigits)
    lateinit var cardLastFourDigits: TextView

    @BindView(R.id.renewalSubtotal)
    lateinit var renewalSubtotal: TextView

    @BindView(R.id.webViewClose)
    lateinit var webViewClose: ImageView

    @BindView(R.id.cancelBooking)
    lateinit var cancelBooking: TextView

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView
    var isCanceled: Boolean = false

    var terminationListener: TerminationListener = this
    var requestTerminationBooking = RequestTerminationBooking()

    //    var toolbar: Toolbar? = null
    var selectedItem: Int = 0
    var cancelReasonsListData = mutableListOf<CancelReasonsModel>()

    var moveOutDateOld: String = ""
    var monthlyOngoing: Boolean = false

    @BindView(R.id.layoutscroll)
    lateinit var layoutscroll: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unit_details)
        ButterKnife.bind(this)

        var bar: RelativeLayout = findViewById(R.id.barUnit)
        bar.bringToFront()

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        toolbar.image_back.setOnClickListener {
            super.onBackPressed()
        }
        var cancelReasonsList = mutableListOf<CancelReasonsModel>()


//        toolbar!!.setOnClickListener() {
//
//            if (wb_webView.isVisible) {
//                layout_top.visibility = View.VISIBLE
//                wb_webView.visibility = View.GONE
//            } else {
//finish()
//                /*startActivity(
//                    Intent(
//                        this,
//                        BottomNavigationActivity::class.java
//                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                )*/
//            }
//        }

        recyclerViewSite.layoutManager = GridLayoutManager(this, 2)

        unitDetailsSkeletionScreen = Skeleton.bind(wholeLayoutUnitDetails)
            .load(R.layout.skleton_payment)
            .shimmer(true)
            .color(R.color.white)
            .show()


        pDialog = Util.init_Progress(this)

//        var imWaveFormation: LottieAnimationView = findViewById(R.id.imWaveFormation)
        imWaveFormation.setAnimation(R.raw.loader)
        imWaveFormation.visibility = View.GONE

        // setupCustomSpinner()

        settingCardViewBackground()

        gettingDataFromPreviousActivity()
        tvUnitDetailsId.setTextColor(Color.parseColor("#FFFFFF"))

        tvUnitDetailsId.setSelected(true);
        requestTerminationBooking.setRequestTerminationListener(terminationListener!!)

        clickListeners()
    }


    private fun gettingDataFromPreviousActivity() {
        bookingId = intent.getStringExtra("bookingID")
        shortId = intent.getStringExtra("shortID")
        moveOutLabelDate = intent.getStringExtra("moveOutDate")

        if (intent.getStringExtra("moveOutDateOld") != null) {
            moveOutDateOld = intent.getStringExtra("moveOutDateOld")!!
        }

        if (intent.getBooleanExtra("monthlyOnGoing", false) != null) {
            monthlyOngoing = intent.getBooleanExtra("monthlyOnGoing", false)
        }



        if (!moveOutLabelDate.equals("")) {
            moveOutDateLabel.text = moveOutLabelDate
        } else {
            moveOutDateLabel.text = getString(R.string.label_monthly_ongoing)
            isOngoingData = true
        }

        var toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.spaceUnitID.text = getString(R.string.label_unit) + " " + shortId

        fetchBookingsById.setBookingsListener(bookingsListener!!)

        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            // pDialog!!.show()
            fetchBookingsById.getBookingById(lifecycleScope, this, bookingId!!.toInt())
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
    }

    private fun settingCardViewBackground() {
        // cardViewOne.setBackgroundResource(R.drawable.card_view)
        cardViewTwo.setBackgroundResource(R.drawable.card_view)
        cardViewThree.setBackgroundResource(R.drawable.card_view)
        cardViewFour.setBackgroundResource(R.drawable.card_view)
    }


    fun selectUnitDetails() {
        tvUnitAccess.setSelected(false);
        tvUnitAccess.alpha = 0.4f
        tvUnitAccess.setTextColor(Color.parseColor("#333333"))
        tvUnitAccess.setTypeface(null, Typeface.NORMAL)

        tvUnitDetailsId.setSelected(true);
        tvUnitDetailsId.setTextColor(Color.parseColor("#FFFFFF"))
        tvUnitDetailsId.alpha = 1f
        tvUnitDetailsId.setTypeface(null, Typeface.BOLD)
    }

    fun selectAccessHistory() {
        // To select
        tvUnitAccess.setSelected(true);
        tvUnitAccess.setTextColor(Color.parseColor("#FFFFFF"))
        tvUnitAccess.setTypeface(null, Typeface.BOLD)
        tvUnitAccess.alpha = 1f

        // To deselect.
        tvUnitDetailsId.setSelected(false);
        tvUnitDetailsId.setTextColor(Color.parseColor("#333333"))
        tvUnitDetailsId.setTypeface(null, Typeface.NORMAL)
        tvUnitDetailsId.alpha = 0.4f
    }

    override fun onResume() {
        super.onResume()

    }


    private fun clickListeners() {

        tvUnitAccess.alpha = 0.4f


        tvUnitDetailsId.setOnClickListener {

            rlAccessDetails.visibility = View.GONE
            rlUnitDetails.visibility = View.VISIBLE
            selectUnitDetails()
        }

        tvUnitAccess.setOnClickListener {

            rlAccessDetails.visibility = View.VISIBLE
            rlUnitDetails.visibility = View.GONE
            unit_size.visibility = View.VISIBLE
            //unit_size.setVisibility(View.VISIBLE);

            selectAccessHistory()
        }


        tvRequestTermination.setOnClickListener {

            fetchBookingsById.setBookingsListener(bookingsListener!!)

            val view = View.inflate(this, R.layout.custom_dialog, null)
            val builder = AlertDialog.Builder(this@UnitDetailsActivity)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            view.dialog_cancel.setOnClickListener {
                dialog.dismiss()
            }

            view.dialog_continue.setOnClickListener {
                openCustomCalendar()
                dialog.dismiss()
            }

            view.canel_icon_dialog.setOnClickListener {
                dialog.dismiss()
            }
        }
//
//        tvRequestAccessCode.setOnClickListener {
//
//            val view = View.inflate(this, R.layout.custom_dialog_request_access, null)
//            val builder = AlertDialog.Builder(this@UnitDetailsActivity)
//            builder.setView(view)
//            val dialog = builder.create()
//            dialog.show()
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//            view.dialog_cancel.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            view.dialog_continue.setOnClickListener {
//                dialog.dismiss()
//            }
//
//            view.canel_icon_dialog.setOnClickListener {
//                dialog.dismiss()
//            }
//        }

        webViewClose.setOnClickListener {
            closeView()
        }
    }

    private fun closeView() {
        //   layout_top.visibility = View.VISIBLE
        toolbar!!.visibility = View.VISIBLE
        webRl.visibility = View.GONE
    }


    private fun openCustomCalendar() {

        val bookingId = intent.getCharSequenceExtra("bookingID")
        var Id: String = bookingId.toString()

        var calenderOngoing: Calendar? = null
        val currentDay: Date = Calendar.getInstance().time
        val currentDate: CalendarDay = CalendarDay.from(currentDay)
        var calenderMoveIn = gettingDateExist(moveInLabelDate.toString())

        val view = View.inflate(this, R.layout.calendar_dialog, null)
        val moveoutDate = view.findViewById<MaterialCalendarView>(R.id.moveout_date)
        val dialogOk = view.findViewById<TextView>(R.id.dialog_ok)
        val cancelIconDialog = view.findViewById<ImageView>(R.id.canel_icon_dialog)
        var clearDate: TextView = view.findViewById<TextView>(R.id.date_clear)




        if (!moveOutDateLabel.text.equals(getString(R.string.label_monthly_ongoing))) {

            calenderOngoing = gettingDateExist(moveOutLabelDate.toString())
            settingDecoratorCurrent(calenderOngoing.time.toString(), moveoutDate)
            moveoutDate.selectedDate = CalendarDay.from(calenderOngoing.time)


            if (!monthlyOngoing) {
                if (!moveOutDateOld.equals("")) {
                    var calenderOngoingOldDate = gettingDateExist(moveOutDateOld.toString())
                    moveoutDate.state().edit()
                        .setMaximumDate(CalendarDay.from(calenderOngoingOldDate.time))
                        .commit()

                } else {
                    moveoutDate.state().edit()
                        .setMaximumDate(CalendarDay.from(calenderOngoing.time))
                        .commit()

                }
            } else {
                val calender = gettingDate(currentDate.toString())
                calender.add(Calendar.DATE, 30)

                moveoutDate.state().edit()
                    .setMaximumDate(CalendarDay.from(calender!!.time))
                    .commit()
            }

            moveOutDateForTermination = moveOutLabelDate.toString()

        } else {
            settingDecorator(currentDate.toString(), moveoutDate)
            val calender = gettingDate(currentDate.toString())
            calender.add(Calendar.DATE, 30)

            moveoutDate.state().edit()
                .setMaximumDate(CalendarDay.from(calender!!.time))
                .commit()
        }

        setCurrentDayDecoder(currentDate, moveoutDate, "#00A0E3")


        //setCurrentDayDecoder(CalendarDay.from(calenderMoveIn.time), moveoutDate, "#E3599A")


        requestTerminationDate =
            Util.getFormattedDateSelected(currentDate.date.toString()).toString()


        moveoutDate.state().edit().setMinimumDate(currentDay).commit()

        moveoutDate.setOnDateChangedListener { widget, date, selected ->
            if (date != currentDate) {
                globaleDate = date
            }
            if (date != currentDate && date.isBefore(CalendarDay.from(calenderMoveIn.time))) {
                val getDetailsMessage =
                    "MoveOut Date should be greater than MoveIn Date"
                Util.showDialog(
                    "Alert",
                    getDetailsMessage,
                    this
                )
            } else {
                if (date != currentDate) {

                    widget.removeDecorators()

                    if (!moveOutDateLabel.text.equals(getString(R.string.label_monthly_ongoing))) {

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

                                val date2: CalendarDay = settingDate(moveInDate.text.toString())

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
                            settingDecoratorCurrent(
                                widget.selectedDate.date.toString(),
                                moveoutDate
                            )
                        } else {
                            settingDecorator(currentDate.toString(), moveoutDate)
                        }
                    }

                    var day: String = widget.selectedDate.date.toString()
                    setCurrentDayDecoder(currentDate, moveoutDate, "#00A0E3")
                    moveOutDateForTermination = Util.getFormattedDateSelected(day).toString()
                } else {
                    if (globaleDate != null && globaleDate != currentDate) {
                        moveoutDate.selectedDate = globaleDate
                    } else {
                        if (!moveOutDateLabel.text.equals(getString(R.string.label_monthly_ongoing))) {
                            val terminateDate: CalendarDay =
                                CalendarDay.from(calenderOngoing!!.time)
                            moveoutDate.selectedDate = terminateDate
                        } else {
                            moveoutDate.selectedDate = null
                        }

                    }
                }
            }
        }

        val builder = AlertDialog.Builder(this@UnitDetailsActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialogOk.setOnClickListener {
            moveOutDateLabel.text = moveOutDateForTermination

            if (moveOutDateLabel.text == "") {
                val messageTermination =
                    "Please choose a Move Out Date before calling the Request Termination"
                Util.showDialog(
                    "Alert",
                    messageTermination,
                    this
                )

            } else {
                callingDataShowAPI(moveOutDateForTermination, Id.toInt())
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

    private fun callingDataShowAPI(moveOutDateForTermination: String, Id: Int) {
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            imWaveFormation.visibility = View.VISIBLE
            requestTerminationBooking.calculateTerminationDues(
                lifecycleScope,
                this,
                moveOutDateForTermination!!,
                Id
            )

        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
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


    private fun setCurrentDayDecoder(
        currentDay: CalendarDay,
        moveoutDate: MaterialCalendarView?,
        color: String
    ) {

        moveoutDate!!.addDecorator(
            EventDecoratorColor(
                Color.parseColor(color),
                currentDay
            )
        )
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

//    private fun setupCustomSpinner() {
//        val adapter = SpinnerAdapter(this, selectedMonth.list!!)
//        sp_months.adapter = adapter
//
//        sp_months.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val selectedItem = parent!!.getItemAtPosition(position)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//            }
//        }
//    }

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

    private fun setDataInViews(bookingDetails: BookingDetailsModel) {

        bookingTerminionDetail = bookingDetails

        bookingSqft.text =
            bookingDetails.unitDetailModel!!.unitSpaceSize.toString() + " " + bookingDetails.unitDetailModel!!.unitSpaceUnit.toString()
                .toLowerCase()

        bookingSize.text = bookingDetails.unitDetailModel!!.unitSize
        siteName.text = bookingDetails.addressDetailsModel!!.street
        streetName.text = bookingDetails.countryDetailModel!!.nameEn
        moveInDate.text =
            Util.getFormattedDate(bookingDetails.unitDetailModel!!.unitMovieInDate.toString())

        if (moveOutLabelDate.equals("")) {
            if (bookingDetails.unitDetailModel!!.unitMoveOutDate != null && bookingDetails.unitDetailModel!!.unitMoveOutDate != "") {

                moveOutLabelDate =
                    Util.getFormattedDate(bookingDetails.unitDetailModel!!.unitMoveOutDate.toString())

                moveOutDateLabel.text =
                    Util.getFormattedDate(bookingDetails.unitDetailModel!!.unitMoveOutDate.toString())
            } else {
                moveOutDateLabel.text = getString(R.string.label_monthly_ongoing)
                isOngoingData = true
            }
        }


        if (bookingDetails.unitDetailModel!!.unitMovieInDate != null && bookingDetails.unitDetailModel!!.unitMovieInDate != "") {
            moveInLabelDate =
                Util.getFormattedDate(bookingDetails.unitDetailModel!!.unitMovieInDate.toString())
        }

        if (bookingDetails.Status != null && bookingDetails.Status != "") {

            bookingStatus = bookingDetails.Status.toString()
        }

        postalCode.text =
            bookingDetails.addressDetailsModel!!.postalCode + " " + bookingDetails.countryDetailModel!!.nameEn

        renewalInsuranceAmount.text =
            "S$" + String.format(
                "%.2f", bookingDetails.renewalDetailModel!!.renewalInsuranceAmount
            )

        renewalSubtotal.text =
            "S$" + String.format(
                "%.2f", bookingDetails.renewalDetailModel!!.renewalSubTotalAmount
            )

        RenewalTotalAmount.text =
            "S$" + String.format(
                "%.2f", bookingDetails.renewalDetailModel!!.renewalTotalAmount
            ) + " "

        InsurancePricePerDay.text =
            "S$ " + String.format(
                "%.2f", bookingDetails.insuranceDetailsModel!!.insurancePricePerDay
            ) + " Per Day"

        Insurancecoverage.text =
            "Essential up to " + "S$" + bookingDetails.insuranceDetailsModel!!.insuranceNameEn.toString()
        cardLastFourDigits.text =
            "**** **** **** " + bookingDetails.transactionDetailModel!!.transactionCardLastDigits

        if (bookingDetails.siteFeatureModel != null && bookingDetails.siteFeatureModel!!.size > 0) {
            recyclerViewSite.adapter =
                this.let { GetSiteFeatureListAdapter(it, bookingDetails.siteFeatureModel!!, this) }
        }
    }

    @OnClick(R.id.btnInterested)
    fun webViewcall() {
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            //   layout_top.visibility = View.GONE
            toolbar!!.visibility = View.GONE
            webRl.visibility = View.VISIBLE
            wb_webView.visibility = View.VISIBLE
            webViewSetup()
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
    }

    fun clearWebView() {
        wb_webView.clearCache(true)
        wb_webView.clearHistory()
        // wb_webView.destroy()
        wb_webView.loadUrl("")
        WebStorage.getInstance().deleteAllData()
        //  layout_top.visibility = View.VISIBLE
        wb_webView.visibility = View.GONE
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    private fun webViewSetup() {
        var isLast: Boolean = false
        wb_webView.settings.javaScriptEnabled = true

        wb_webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                Log.e(
                    "APP",
                    "when you click on any interlink on webview that time you got url :-" + url
                );
                if (url.toString().contains("https://spacenextdoor.typeform.com/to/LPkTaVjA/")) {
                    clearWebView()

                }
                isLast = true
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.e("WebView", "your current url when webpage loading.." + url);
                if (url.toString().contains("https://spacenextdoor.typeform.com/to/LPkTaVjA/")) {
                    clearWebView()
                }
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                Log.e("WebView", "your current url when webpage loading.. finish" + url);
                if (url.toString().contains("https://spacenextdoor.typeform.com/to/LPkTaVjA/")) {
                    clearWebView()
                }
                if (isLast) {
                    isLast = false
                    closeView()
                }
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {

                super.onReceivedError(view, request, error)
            }
        }
        wb_webView.loadUrl("https://spacenextdoor.typeform.com/to/LPkTaVjA")
    }

    override fun onGetByIdSuccess(bookingsDataByIdData: FetchBookingDetailsQuery.Booking) {

        // imWaveFormation.visibility = View.GONE
        wholeLayoutUnitDetails.visibility = View.VISIBLE
        unitDetailsSkeletionScreen!!.hide()
        layoutscroll.visibility = View.VISIBLE

        if (bookingsDataByIdData != null) {
            var bookingDetails = SettingBookingData.setDataInObject(bookingsDataByIdData)
            unitDetailsSkeletionScreen!!.hide()

            if (bookingDetails.isTerminationRequested == true) {
                tvRequestTermination.visibility = View.GONE
                alreadyRequestedTermination.visibility = View.VISIBLE
                alreadyRequestedTerminationBtn.visibility = View.VISIBLE
            }

            if (bookingDetails.isTerminationRequested == false && bookingDetails.Status.equals("CONFIRMED")) {
                tvRequestTermination.visibility = View.GONE
                alreadyRequestedTerminationBtn.visibility = View.GONE
                alreadyRequestedTermination.visibility = View.GONE
                cancelBooking.visibility = View.VISIBLE
            }

            if (isCanceled) {
                isCanceled = false
                openCancelBookingDetailsActivity(bookingDetails)
            } else {
                setDataInViews(bookingDetails)
            }
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_result_found), this)
        }
    }

    @OnClick(R.id.cancelBooking)
    fun openCancelBooking() {

        val view = View.inflate(this, R.layout.cancellation_dialog, null)
        val dialogCancelClick = view.findViewById<ImageView>(R.id.canel_icon_dialog)
        val dialogKeepBooking = view.findViewById<TextView>(R.id.dialogKeepBooking)
        val dialogConfirm = view.findViewById<TextView>(R.id.dialog_confirm)

        val builder = AlertDialog.Builder(this@UnitDetailsActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialogCancelClick.setOnClickListener {
            dialog.dismiss()
        }

        dialogKeepBooking.setOnClickListener {
            openCancelBookingDialogNEW()
        }

        dialogConfirm.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun openCancelBookingDialogNEW() {
        fetchBookingsById.setBookingsListener(bookingsListener!!)

        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            pDialog!!.show()
            fetchBookingsById.getCancellationReasons(lifecycleScope, this)

        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
    }

    private fun openCancelBookingDialog(cancelReasonsList: MutableList<CancelReasonsModel>) {

        //val bookingId = intent.getCharSequenceExtra("bookingID")
        //var Id: Int = bookingId.toInt()
        val view = View.inflate(this, R.layout.cancel_booking_dialog, null)
        val dialogContinue = view.findViewById<TextView>(R.id.dialog_ok)
        val cancelIconDialog = view.findViewById<ImageView>(R.id.canel_icon_dialog)
        val cancelReasonsRecycler: Spinner = view.findViewById(R.id.cancelReasonsRecycler)

        val adapter = SpinnerAdapter1(this, cancelReasonsList)
        cancelReasonsRecycler.adapter = adapter

        cancelReasonsRecycler.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = cancelReasonsList[position].id!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        pDialog!!.dismiss()

        val builder = AlertDialog.Builder(this@UnitDetailsActivity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        var hostString: String = "Will let you know later"

        dialogContinue.setOnClickListener {

            fetchBookingsById.setBookingsListener(bookingsListener!!)

            if (Util.DETECT_INTERNET_CONNECTION(this)) {
                pDialog!!.show()
                fetchBookingsById.requestForCancelBooking(
                    lifecycleScope,
                    this,
                    bookingId!!.toInt(),
                    selectedItem
                )

            } else {
                Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
            }
        }

        cancelIconDialog.setOnClickListener {
            dialog.dismiss()
        }
    }


    private fun callCancelBookingApi(Id: Int, reasonId: Int, hostString: String) {
        if (Util.DETECT_INTERNET_CONNECTION(this)) {
            pDialog!!.show()
            fetchBookingsById.requestForCancelBooking(lifecycleScope, this, Id, reasonId)

        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
        }
    }

    override fun onGetByIdFailure(toString: String) {
        // For booking getById failure
        pDialog!!.dismiss()
        val getDetailsMessage = toString
        Util.showDialog(
            "Alert",
            getDetailsMessage,
            this
        )
    }

    override fun onRequestTerminationSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination) {
    }

    override fun onRequestTerminationFailure(message: String) {
        val messageTermination = message
        Util.showDialog(
            "Alert",
            messageTermination,
            this
        )
    }


    override fun onSuccess(bookingsData: FetchBookingsQuery.Bookings) {
        //For Booking List Success
    }

    override fun onSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination) {
    }

    override fun onFailure(toString: String) {
    }

    override fun onSuccessCalculateTerminationDues(calculateTerminationDuesData: CalculateTerminationDuesMutation.CalculateTerminationDues) {
        //pDialog!!.dismiss()
        imWaveFormation.visibility = View.GONE

        if (calculateTerminationDuesData != null) {
            var calculateTerminationDuesDetails =
                SetCalculationTerminationDuesData.setDataInObject(calculateTerminationDuesData)
            val intent = Intent(this, RequestTermination::class.java)
            val bundle = Bundle()
            bundle.putParcelable("terminationDetails", bookingTerminionDetail)
            bundle.putParcelable("calculateTerminationDetails", calculateTerminationDuesDetails)
            intent.putExtra("Bundle", bundle)

            intent.putExtra("moveOutDateSelected", moveOutDateForTermination)

            if (!moveOutLabelDate.equals("")) {
                intent.putExtra("moveOutDate", moveOutLabelDate)
                intent.putExtra("monthlyOnGoing", false)
            } else {
                intent.putExtra("monthlyOnGoing", true)
                intent.putExtra("moveOutDate", moveOutDateForTermination)
            }

            intent.putExtra("currentDate", requestTerminationDate)
            intent.putExtra("moveInDate", moveInDate.text.toString())
            intent.putExtra("bookingID", bookingId)
            intent.putExtra("shortID", shortId)


            startActivity(intent)
            finish()
        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_result_found), this)
        }

    }

    override fun onFailureCalculateTerminationDues(message: String) {
        //pDialog!!.dismiss()
        val getDetailsMessage = message
        Util.showDialog(
            "Alert",
            getDetailsMessage,
            this
        )
        if (isOngoingData) {
            moveOutDateLabel.text = getString(R.string.label_monthly_ongoing)
        }
        imWaveFormation.visibility = View.GONE

    }

    override fun onFailurePayForTheBooking(message: String) {
    }

    override fun onSuccessPayForTheBookingData(payForTheBookingData: PayTerminationMutation.PayTerminationAmount) {
    }

    override fun onCancellationReasonsSuccess(cancellationReasonsData: CancellationReasonsQuery.Cancellation_reasons) {
        if (cancellationReasonsData != null) {
            var cancelReasonsList = CancelReasonsItemData.setDataInObject(cancellationReasonsData)
            openCancelBookingDialog(cancelReasonsList)

        } else {
            Util.showDialog(getString(R.string.alert), getString(R.string.no_result_found), this)
        }
    }

    override fun onCancellationReasonsFailure(message: String) {
        val messageTermination = message
        Util.showDialog(
            "Alert",
            messageTermination,
            this
        )
    }

    override fun onCancelBookingFailure(message: String) {
        pDialog!!.dismiss()
        val messageTermination = message
        Util.showDialog(
            "Alert",
            messageTermination,
            this
        )
    }

    override fun onCancelBookingSuccess(cancelBookingData: CancelBookingMutation.CancelBooking) {

        if (cancelBookingData != null) {

            cancelBookingDataDetails =
                SetCancelBookingData.setDataInObject(cancelBookingData)

            fetchBookingsById.setBookingsListener(bookingsListener!!)

            if (Util.DETECT_INTERNET_CONNECTION(this)) {
                // pDialog!!.show()
                isCanceled = true
                fetchBookingsById.getBookingById(lifecycleScope, this, bookingId!!.toInt())
            } else {
                Util.showDialog(getString(R.string.alert), getString(R.string.no_Internet), this)
            }


        }
    }

    private fun openCancelBookingDetailsActivity(bookingDetails: BookingDetailsModel) {

        pDialog!!.dismiss()
        bookingTerminionDetail = bookingDetails
        val intent = Intent(this, CancelBooking::class.java)
        val bundle = Bundle()
        bundle.putParcelable("cancelBookingDetailsResponse", cancelBookingDataDetails)
        bundle.putParcelable("bookingDetailsData", bookingTerminionDetail)
        intent.putExtra("Bundle", bundle)
        intent.putExtra("shortID", shortId)
        startActivity(intent)
        finish()
    }

}