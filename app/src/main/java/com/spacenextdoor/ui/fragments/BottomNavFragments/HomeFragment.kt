package com.spacenextdoor.ui.fragments.BottomNavFragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.helpcrunch.library.core.HelpCrunch
import com.helpcrunch.library.core.models.user.HCUser
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator
import com.spacenextdoor.*
import com.spacenextdoor.R2.id.textView
import com.spacenextdoor.adapter.GetBookingsListAdapter
import com.spacenextdoor.listeners.*
import com.spacenextdoor.model.TransactionsModel
import com.spacenextdoor.model.UnitDetailModel
import com.spacenextdoor.service.*
import com.spacenextdoor.ui.fragments.BottomNavFragments.Payment.PaymentDetailFragment
import com.spacenextdoor.ui.fragments.BottomNavFragments.Payment.PaymentDetailFragmentInsurance
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment(
    onBtnClicked: NavigationListener?,
    chatbtnListener: ChatBtnHideListener
) : Fragment(), ProfileListener, BookingsListener, TransactionListener, PaymentDetailListener,
    OnBackPressedClicked {

    @BindView(R.id.recyclerViewCard)
    lateinit var recyclerViewCard: RecyclerView

    @BindView(R.id.cardViewOne)
    lateinit var cardViewOne: CardView

    @BindView(R.id.cardViewTwo)
    lateinit var cardViewTwo: CardView

    @BindView(R.id.indicator)
    lateinit var indicatorCircle: IndefinitePagerIndicator

    @BindView(R.id.cardviewEmpty)
    lateinit var cardviewEmpty: CardView

    @BindView(R.id.layout_with_unit_data)
    lateinit var layout_with_unit_data: LinearLayout

    @BindView(R.id.layout_with_transaction)
    lateinit var layout_with_transaction: LinearLayout

    @BindView(R.id.mainFrameLayout)
    lateinit var mainFrameLayout: LinearLayout


    @BindView(R.id.layout_no_transaction)
    lateinit var layout_no_transaction: LinearLayout

    @BindView(R.id.cardview3)
    lateinit var cardview3: CardView
    var mListeners: NavigationListener? = onBtnClicked

    @BindView(R.id.indicators)
    lateinit var indicatorsLL: LinearLayout

    @BindView(R.id.extraSpaceRelativeLayout)
    lateinit var extraSpaceRelativeLayout: RelativeLayout

    @BindView(R.id.layout_transactions)
    lateinit var layout_transactions: RelativeLayout

    @BindView(R.id.btn_list)
    lateinit var btn_list: TextView

    @BindView(R.id.textview_see_all)
    lateinit var textview_see_all: TextView


    @BindView(R.id.webViewClose)
    lateinit var webViewClose: ImageView

    @BindView(R.id.deafultText)
    lateinit var deafultText: TextView

    @BindView(R.id.firstName)
    lateinit var firstName: TextView

    @BindView(R.id.textview_2)
    lateinit var textview_2: TextView


    @BindView(R.id.textview_3)
    lateinit var textview_3: TextView

    @BindView(R.id.textview_1)
    lateinit var textview_1: TextView

    @BindView(R.id.textview_totalamount)
    lateinit var textview_totalamount: TextView

    @BindView(R.id.textview_units)
    lateinit var textview_units: TextView

    @BindView(R.id.textview_date)
    lateinit var textview_date: TextView

    @BindView(R.id.textview_total)
    lateinit var textview_total: TextView

    @BindView(R.id.swipeRefresh)
    lateinit var swipeRefresh: SwipeRefreshLayout
    var refresh: Boolean = false

    var fetchProfileCalling = FetchProfile()
    var fetchBookingsCalling = FetchBookings()
    var fetchTransactionsCalling = FetchTransactions()

    var profileListener: ProfileListener = this
    var bookingsListener: BookingsListener = this

    var transactionsListener: TransactionListener = this
    var mBackPressedClicked: OnBackPressedClicked? = this


    var skeletonScreen: RecyclerViewSkeletonScreen? = null
    var insuranceSkeletionScreen: SkeletonScreen? = null

    var extraSpaceSkeletionScreen: SkeletonScreen? = null
    var bottomNavigationView: BottomNavigationView? = null

    lateinit var transactionsModelDetail: TransactionsModel

    var chatHideListener: ChatBtnHideListener? = chatbtnListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = View.inflate(context, R.layout.fragment_home, null)

        ButterKnife.bind(this, root)

        bottomNavigationView = root.findViewById(R.id.bottom_navigation)
        swipeRefresh.setOnRefreshListener {
            refresh = true
            settingDataInViews()
            swipeRefresh.isRefreshing = false
        }

        var bookingList = mutableListOf<UnitDetailModel>()

        val adapter = activity?.let { GetBookingsListAdapter(it, bookingList) }

        skeletonScreen = Skeleton.bind(recyclerViewCard)
            .adapter(adapter)
            .load(R.layout.skeleton_view_home)
            .color(R.color.white)
            .shimmer(true)
            .show()


        insuranceSkeletionScreen = Skeleton.bind(layout_transactions)
            .load(R.layout.skeleton_view_home_insurance_card).shimmer(false)
            .shimmer(true)
            .color(R.color.white)
            .show();

        extraSpaceSkeletionScreen = Skeleton.bind(extraSpaceRelativeLayout)
            .load(R.layout.skeleton_view_home_extraspace)
            .color(R.color.white)
            .shimmer(true)
            .show();

        indicatorsLL.visibility = View.GONE


        settingDataInViews()


        recyclerViewCard.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        val recyclerIndicator: IndefinitePagerIndicator = root.findViewById(R.id.indicator)
        indicatorCircle.attachToRecyclerView(recyclerViewCard)

        cardViewOne.setBackgroundResource(R.drawable.card_view)
        cardViewTwo.setBackgroundResource(R.drawable.card_view)

        return root
    }

    private fun settingDataInViews() {

        if (refresh) {
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                fetchProfileData()
                fetchBookingsData()
                fetchSingleTransactonData()
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }

        } else {
            var profileData: FetchProfileQuery.Profile? =
                PrefManger.getProfileData(requireContext())

            var bookingData: FetchBookingsQuery.Bookings? =
                PrefManger.getBookingData(requireContext())

            var transactionData: FetchTransactionsQuery.Transactions? =
                PrefManger.getSingleTransactionData(requireContext())

            layout_transactions.visibility = View.GONE
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {

                if (profileData != null) {
                    settingProfileData(profileData)
                } else {
                    fetchProfileData()
                }

                if (bookingData != null) {
                    settingBookingData(bookingData)
                } else {
                    fetchBookingsData()
                }

                if (transactionData != null) {
                    settingTranscationData(transactionData)
                } else {
                    fetchSingleTransactonData()
                }

            } else {
                if (profileData != null && bookingData != null && transactionData != null) {
                    settingProfileData(profileData)
                    settingBookingData(bookingData)
                    settingTranscationData(transactionData)
                } else {
                    Util.showDialog(
                        getString(R.string.alert),
                        getString(R.string.no_Internet),
                        requireActivity()
                    )
                }

            }
        }
    }

    private fun fetchSingleTransactonData() {
        fetchTransactionsCalling.setTransactionListener(transactionsListener!!)
        fetchTransactionsCalling.getTransactions(lifecycleScope, activity)
    }

    private fun fetchBookingsData() {
        fetchBookingsCalling.setBookingsListener(bookingsListener!!)
        fetchBookingsCalling.getBookings(lifecycleScope, activity)
    }

    private fun fetchProfileData() {
        fetchProfileCalling.setFetchProfileListener(profileListener!!)
        fetchProfileCalling.getProfile(lifecycleScope, activity)

    }


    override fun onSuccess(profileDetails: FetchProfileQuery.Profile?) {
        settingProfileData(profileDetails)
        PrefManger.saveProfileData(profileDetails, requireContext())
    }

    private fun settingProfileData(profileDetails: FetchProfileQuery.Profile?) {
        updateUserData(profileDetails)
        deafultText.text = resources.getString(R.string.label_hello)
        if (profileDetails!!.first_name == null) {
            firstName.text = " " + resources.getString(R.string.label_User)
        } else {
            firstName.text = " " + profileDetails!!.first_name
        }

    }

    private fun updateUserData(profileDetails: FetchProfileQuery.Profile?) {
        lateinit var name: String
        lateinit var email: String

        if (profileDetails!!.first_name != null) {
            name = profileDetails!!.first_name.toString()
            if (profileDetails.email != null) {
                email = profileDetails!!.email.toString()

                var user: HCUser = HCUser.Builder()
                    .withName(name)
                    .withEmail(email)
                    .build()
                HelpCrunch.updateUser(user)

            } else {
                var user: HCUser = HCUser.Builder()
                    .withName(name)
                    .build()

                HelpCrunch.updateUser(user)
            }
        }
    }

    @OnClick(R.id.textview_see_all)
    fun openTransactionsPage() {
        mListeners?.onBtnPressed();

        //   mainFrameLayout.visibility = View.GONE
//        val fm: FragmentManager = requireActivity().supportFragmentManager
//        fm.beginTransaction()
//            .replace(R.id.fragment_container, PaymentFragment())
//            .addToBackStack(null)
//            .commit()
    }

    @OnClick(R.id.btn_explore_storage)
    fun exploreStorage() {

        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            var userModelData = PrefManger.getUser(requireContext())

            var URL = "https://dev.spacenextdoor.com/"
            //         var URL = "https://stag.spacenextdoor.com/"

            FinestWebView.Builder(requireActivity()).show(URL)
            Log.e("URL", URL)

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }


        // mainFrameLayout.visibility = View.GONE
//        toolbar!!.visibility = View.GONE
        //    webRl.visibility = View.VISIBLE
        //    wb_webViewHome.visibility = View.VISIBLE
        //    webViewSetupExploreBtn(URL)
    }

    private fun webViewSetupExploreBtn(URL: String) {
        wb_webViewHome.settings.javaScriptEnabled = true
        wb_webViewHome.settings.domStorageEnabled = true
//        wb_webView.settings.useWideViewPort = true
//        wb_webView.settings.allowContentAccess = true
//        wb_webView.settings.domStorageEnabled = true
        wb_webViewHome.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //val url = request?.url.toString()
                view?.loadUrl(URL)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.e("WebView", "your current url when webpage loading.." + url);

                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                Log.e("WebView", "your current url when webpage loading.. finish" + url);
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
        wb_webViewHome.loadUrl(URL)
    }

    @OnClick(R.id.btn_list)
    fun openReceipt() {
        //chatHideListener!!.onHideChat()


        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            var userModelData = PrefManger.getUser(requireContext())

            var URL = "https://dev.spacenextdoor.com/host"
            //    var URL = "https://stag.spacenextdoor.com/host"


            FinestWebView.Builder(requireActivity()).show(URL)
            //     "https://dev.spacenextdoor.com/auth?token=" + userModelData!!.accessToken + "&refresh_token=" + userModelData.refresh_token + "&redirect_url=/customer/invoice/" + transactionsModelDetail.transactionId
            Log.e("URL", URL)

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }

        //mainFrameLayout.visibility = View.GONE
//        toolbar!!.visibility = View.GONE
//        webRl.visibility = View.VISIBLE
//        wb_webViewHome.visibility = View.VISIBLE
//        webViewSetup(URL)


    }

    private fun webViewSetup(URL: String) {

        wb_webViewHome.settings.mixedContentMode = 0
        wb_webViewHome.settings.javaScriptEnabled = true
        wb_webViewHome.settings.domStorageEnabled = true
        wb_webViewHome.settings.loadWithOverviewMode = true

        wb_webViewHome.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                //val url = request?.url.toString()
                view?.loadUrl(URL)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                Log.e("WebView", "your current url when webpage loading.." + url);

                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {

                Log.e("WebView", "your current url when webpage loading.. finish" + url);
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
        wb_webViewHome.loadUrl(URL)
    }


    @OnClick(R.id.webViewClose)
    fun closeURL() {
        chatHideListener!!.onViewChat()
        closeView()
    }

    private fun closeView() {
        mainFrameLayout.visibility = View.VISIBLE
        // toolbar!!.visibility = View.VISIBLE
        webRl.visibility = View.GONE
    }

    override fun onSuccess(bookingsData: FetchBookingsQuery.Bookings) {

        settingBookingData(bookingsData)


    }

    private fun settingBookingData(bookingsData: FetchBookingsQuery.Bookings) {
        insuranceSkeletionScreen?.hide()
        extraSpaceSkeletionScreen?.hide()
        layout_transactions.visibility = View.VISIBLE
        indicatorsLL.visibility = View.VISIBLE
        if (bookingsData.edges == null || bookingsData.edges.size == 0) {
            skeletonScreen!!.hide()
            cardviewEmpty.visibility = View.VISIBLE
            layout_no_transaction.visibility = View.VISIBLE
            layout_with_transaction.visibility = View.GONE
            indicatorsLL.visibility = View.GONE
            cardview3.visibility = View.VISIBLE
        } else {
            if (bookingsData.edges != null) {
                cardviewEmpty.visibility = View.GONE
                layout_no_transaction.visibility = View.GONE
                indicatorsLL.visibility = View.VISIBLE
                layout_with_transaction.visibility = View.GONE
                layout_with_unit_data.visibility = View.VISIBLE
                if (bookingsData.edges.isNotEmpty()) {

                    PrefManger.saveBookingData(bookingsData, requireContext())
                    var bookingList = BookingListItemData.setDataInObject(bookingsData)
                    recyclerViewCard.adapter =
                        activity?.let { GetBookingsListAdapter(it, bookingList) }
                }
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_result_found),
                    requireContext()
                )
            }
        }

    }

    override fun onSuccess(transactionsData: FetchTransactionsQuery.Transactions) {

        settingTranscationData(transactionsData)

    }

    private fun settingTranscationData(transactionsData: FetchTransactionsQuery.Transactions) {

        if (transactionsData.edges == null || transactionsData.edges.size == 0) {
            skeletonScreen!!.hide()
            insuranceSkeletionScreen!!.hide()
            cardviewEmpty.visibility = View.VISIBLE
            layout_no_transaction.visibility = View.VISIBLE
            layout_with_transaction.visibility = View.GONE
            indicatorsLL.visibility = View.GONE
            cardview3.visibility = View.VISIBLE

        } else {
            if (transactionsData.edges != null) {
                cardviewEmpty.visibility = View.GONE
                layout_no_transaction.visibility = View.GONE
                layout_with_unit_data.visibility = View.VISIBLE
                indicatorsLL.visibility = View.VISIBLE
                layout_with_transaction.visibility = View.VISIBLE

                if (transactionsData.edges.isNotEmpty()) {
                    PrefManger.saveSingleTransactionData(transactionsData, requireContext())
                    var transactionsDetails =
                        TransactionListItemData.setDataInObject(transactionsData)
                    setDataInViews(transactionsDetails)
                }
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_result_found),
                    requireContext()
                )
            }
        }
        cardview3.visibility = View.VISIBLE

    }

    private fun setDataInViews(transactionsDetails: MutableList<TransactionsModel>) {

        if (transactionsDetails.size > 0) {

            transactionsModelDetail = transactionsDetails[0]
            textview_2.text =
                getString(R.string.label_unit) + " " + transactionsDetails[0].bookingDetails!!.shortId.toString() + ", " + transactionsDetails[0].bookingDetails!!.bookingSpaceSize + " " + transactionsDetails[0].bookingDetails!!.bookingSpaceSizeUnit.toLowerCase()

            textview_3.text =
                Util.getFormattedDate(transactionsDetails[0].bookingDetails!!.bookingCreatedAt.toString())

            textview_totalamount.text =
                transactionsDetails[0].bookingDetails!!.currencySign + String.format(
                    "%.2f", transactionsDetails[0].bookingDetails!!.totalAmount
                )

//            transactionsDetails[0].insuranceDetails!!.insuranceNameEn + " " + "S$" + String.format(
//                "%.2f", transactionsDetails[0].insuranceDetails!!.insurancePricePerDay
//            ) + " " + "Per day"

            textview_units.text = transactionsDetails[0].insuranceDetails!!.insuranceNameEn + ", " +
                    transactionsDetails[0].bookingDetails!!.currencySign + " " + transactionsDetails[0].insuranceDetails!!.insurancePricePerDay.toString() + " " + "Per day"

            textview_date.text =
                Util.getFormattedDate(transactionsDetails[0].insuranceDetails!!.insuranceCreatedAt.toString())

            textview_total.text =
                transactionsDetails[0].bookingDetails!!.currencySign + String.format(
                    "%.2f", transactionsDetails[0].insuranceDetails!!.insuranceTotalAmount
                )

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_result_found),
                requireContext()
            )
        }

    }

    override fun onFailure(msg: String) {
        // Profile Listener Method
        if (isVisible) {
            val messageProfileFailure = msg
            Util.showDialog(
                "Alert",
                messageProfileFailure,
                requireContext()
            )
        }
    }

    override fun onUpdateProfileSuccess(message: String) {
        // For booking Get BY ID success
    }

    override fun onUpdateProfileFailure(message: String) {
        // For booking Get BY ID success
        if (isVisible) {
            val messageProfileFailure = message
            Util.showDialog(
                "Alert",
                messageProfileFailure,
                requireContext()
            )
        }
    }


    @OnClick(R.id.cardViewOne)
    fun openUnitDetailPage() {
        mainHomell.visibility = View.GONE
        // chatHideListener!!.onHideChat()

        Thread(Runnable {
            Thread.sleep(50)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                frameViewDetail.visibility = View.VISIBLE
            } //public void run() {
            )

        }).start()

        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.beginTransaction()
            .replace(
                R.id.frameViewDetail,
                PaymentDetailFragment(transactionsModelDetail, mBackPressedClicked)
            )
            .addToBackStack(null)
            .commit()
    }


    @OnClick(R.id.cardViewTwo)
    fun openInsuranceDetailPage() {
        mainHomell.visibility = View.GONE
        // chatHideListener!!.onHideChat()

        Thread(Runnable {
            Thread.sleep(50)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                frameViewDetail.visibility = View.VISIBLE
            } //public void run() {
            )

        }).start()

        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.beginTransaction()
            .replace(
                R.id.frameViewDetail,
                PaymentDetailFragmentInsurance(transactionsModelDetail, mBackPressedClicked)
            )
            .addToBackStack(null)
            .commit()
    }

    @OnClick(R.id.layout_with_transaction)
    fun openPage() {

        mainHomell.visibility = View.GONE
        // chatHideListener!!.onHideChat()

        Thread(Runnable {
            Thread.sleep(50)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                frameViewDetail.visibility = View.VISIBLE
            } //public void run() {
            )

        }).start()

        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.beginTransaction()
            .replace(
                R.id.frameViewDetail,
                PaymentDetailFragment(transactionsModelDetail, mBackPressedClicked)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onGetByIdSuccess(bookingsDataById: FetchBookingDetailsQuery.Booking) {
        // For booking Get BY ID success
    }

    override fun onGetByIdFailure(toString: String) {
        // For booking Get BY ID Failure
    }

    override fun onRequestTerminationSuccess(requestTerminationData: RequestTerminationMutation.RequestTermination) {
        // Nothing required yet
    }

    override fun onRequestTerminationFailure(message: String) {
        // Nothing required yet
    }

    override fun onCancellationReasonsSuccess(cancellationReasonsData: CancellationReasonsQuery.Cancellation_reasons) {
    }

    override fun onCancellationReasonsFailure(message: String) {
    }

    override fun onCancelBookingFailure(message: String) {
    }

    override fun onCancelBookingSuccess(cancelBookingData: CancelBookingMutation.CancelBooking) {
    }

    override fun onPaymentDetailListener(transactionsModel: TransactionsModel) {

    }

    override fun onPaymentDetailInsuranceListener(transactionsModel: TransactionsModel) {

    }

    override fun onBackpressed() {
        chatHideListener!!.onViewChat()
        frameViewDetail.visibility = View.GONE
        mainHomell.visibility = View.VISIBLE
    }

}
