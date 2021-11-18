package com.spacenextdoor.ui.fragments.BottomNavFragments.Payment

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
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.spacenextdoor.R
import com.spacenextdoor.listeners.OnBackPressedClicked
import com.spacenextdoor.model.TransactionsModel
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.activity_unit_details.*
import kotlinx.android.synthetic.main.layout_toolbar_unit_details.view.*

class PaymentDetailFragmentInsurance(
    transactionsModel: TransactionsModel,
    mBackPressedClicked: OnBackPressedClicked?

) : Fragment(), OnMapReadyCallback {

    val transactionsModelDetail = transactionsModel

    @BindView(R.id.paymentType)
    lateinit var paymentType: TextView

    @BindView(R.id.paymentCompletedDate)
    lateinit var paymentCompletedDate: TextView

    @BindView(R.id.unitName)
    lateinit var unitName: TextView

    @BindView(R.id.unitArea)
    lateinit var unitArea: TextView

    @BindView(R.id.unitTotalAmount)
    lateinit var unitTotalAmount: TextView

    @BindView(R.id.siteBuildingName)
    lateinit var buildingName: TextView

    @BindView(R.id.buildingDetails)
    lateinit var buildingDetails: TextView

    @BindView(R.id.unitDeposit)
    lateinit var unitDeposit: TextView

    @BindView(R.id.unitSubtotal)
    lateinit var unitSubTotal: TextView

    @BindView(R.id.seeReceipt)
    lateinit var seeReceipt: TextView

    @BindView(R.id.layoutMap)
    lateinit var layoutMap: NestedScrollView

    private lateinit var mMap: GoogleMap

    var Latitude: Double = 0.0
    var Longitude: Double = 0.0
    var mListeners: OnBackPressedClicked? = mBackPressedClicked


//    @BindView(R.id.webViewClose)
//    lateinit var webViewClose: ImageView

    @BindView(R.id.layout_payment_unit_detail)
    lateinit var layout_payment_unit_detail: RelativeLayout

    var toolbar: Toolbar? = null

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView

    @BindView(R.id.mainFrameLayout)
    lateinit var mainFrameLayout: LinearLayout

    lateinit var loaderShownInView: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var root: View =
            inflater.inflate(R.layout.fragment_payment_details_insurance, container, false)
        ButterKnife.bind(this, root)
        mainFrameLayout.visibility = View.GONE
        loaderShownInView = root.findViewById(R.id.rlLoader)
//        mainFrameLayout.visibility = View.GONE
//        layoutMap.visibility = View.GONE
//        loaderShownInView.visibility = View.VISIBLE
//        imWaveFormation.visibility = View.VISIBLE

        var toolbar = root.findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.spaceUnitID.text =
            getString(R.string.label_unit) + " " + transactionsModelDetail.bookingDetails!!.shortId


//        var frameViewDetail = root.findViewById<View>(R.id.frameViewDetail) as FrameLayout
//
//        frameViewDetail.visibility = View.GONE


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        setData()
        Thread(Runnable {
            Thread.sleep(500)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                mainFrameLayout.visibility = View.VISIBLE
                loaderShownInView.visibility = View.GONE
            } //public void run() {
            )

        }).start()



        toolbar.image_back.setOnClickListener {
            startBackPrssed()
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startBackPrssed()

                }
            })

        return root
    }

    @OnClick(R.id.seeReceipt)
    fun openReceipt() {

        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            var userModelData = PrefManger.getUser(requireContext())

//            var URL =
//                "https://dev.spacenextdoor.com/auth?token=" + userModelData!!.accessToken + "&refresh_token=" + userModelData.refresh_token + "&redirect_url=/customer/invoice/" + transactionsModelDetail.transactionId
//            Log.e("URL", URL)

            var URL =
                "https://stag.spacenextdoor.com/auth?token=" + userModelData!!.accessToken + "&refresh_token=" + userModelData.refresh_token + "&redirect_url=/customer/invoice/" + transactionsModelDetail.transactionId
            Log.e("URL", URL)

            //   var URL = "https://dev.spacenextdoor.com/"
            FinestWebView.Builder(requireActivity()).show(URL)

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }

//        var userModelData = PrefManger.getUser(requireContext())
//
//        var URL =
//            "https://dev.spacenextdoor.com/auth?token=" + userModelData!!.accessToken + "&refresh_token=" + userModelData.refresh_token + "&redirect_url=/customer/invoice/" + transactionsModelDetail.transactionId
//        Log.e("URL", URL)
//
//        //   var URL = "https://dev.spacenextdoor.com/"
//        FinestWebView.Builder(requireActivity()).show(URL)
        //       layout_payment_unit_detail.visibility = View.GONE
//        toolbar!!.visibility = View.GONE
        //       webRl.visibility = View.VISIBLE
        //       wb_webView.visibility = View.VISIBLE
        //      webViewSetup(URL)

    }

    private fun webViewSetup(URL: String) {
        wb_webView.settings.javaScriptEnabled = true
        wb_webView.settings.domStorageEnabled = true
        wb_webView.webViewClient = object : WebViewClient() {
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
        wb_webView.loadUrl(URL)
    }

//    @OnClick(R.id.webViewClose)
//    fun closeURL() {
//        closeView()
//    }

    private fun closeView() {
        layout_payment_unit_detail.visibility = View.VISIBLE
        // toolbar!!.visibility = View.VISIBLE
        // webRl.visibility = View.GONE
    }

    private fun startBackPrssed() {
        paymentCompletedDate.text = ""
        unitName.text = ""
        unitArea.text = ""
        unitTotalAmount.text = ""
        unitSubTotal.text = ""
        unitDeposit.text = ""
        buildingName.text = ""
        paymentType.text = ""

        mListeners!!.onBackpressed()
    }

    private fun setData() {


        paymentCompletedDate.text =
            Util.getFormattedDate(transactionsModelDetail.createdAt.toString())

        unitName.text =
            requireActivity().getString(R.string.label_unit) + " " + transactionsModelDetail.bookingDetails!!.shortId

//        unitArea.text = transactionsModelDetail.spaceDetails!!.unitSize.toString() + " " +
//                transactionsModelDetail.bookingDetails!!.bookingSpaceSize.toString() + " " + transactionsModelDetail.spaceDetails!!.unitSpaceUnit.toString()
//            .toLowerCase()

        unitArea.text = transactionsModelDetail.insuranceDetails!!.insuranceNameEn

        unitTotalAmount.text =
            transactionsModelDetail.bookingDetails!!.currencySign + String.format(
                "%.2f",
                transactionsModelDetail.insuranceDetails!!.insuranceTotalAmount
            )

        if (transactionsModelDetail.renewalDetails != null) {
            unitSubTotal.text =
                transactionsModelDetail.bookingDetails!!.currencySign + String.format(
                    "%.2f", transactionsModelDetail.renewalDetails!!.renewalSubTotalAmount
                )
        } else {
            unitSubTotal.text =
                "S$0.00"
        }


        if (transactionsModelDetail.renewalDetails != null) {
            unitDeposit.text =
                transactionsModelDetail.bookingDetails!!.currencySign + String.format(
                    "%.2f", transactionsModelDetail.renewalDetails!!.renewalDepositAmount
                )
        } else {
            unitDeposit.text =
                "S$0.00"
        }

        buildingName.text =
            transactionsModelDetail.districtDetails!!.nameEn.toString()

        buildingDetails.text = transactionsModelDetail!!.countryDetails!!.nameEn.toString() + ", " +
                transactionsModelDetail.addressDetails!!.street.toString() + ", " + transactionsModelDetail.addressDetails!!.postalCode.toString()

        Latitude = transactionsModelDetail.addressDetails!!.latitude!!
        Longitude = transactionsModelDetail.addressDetails!!.longitude!!

        if (transactionsModelDetail.terminationDetails == null) {
            paymentType.text = "Paid"
        } else {
            paymentType.text = transactionsModelDetail.terminationDetails!!.paymentStatus.toString()
        }

//        loaderShownInView.visibility = View.GONE
//        imWaveFormation.visibility = View.GONE
//        layoutMap.visibility = View.VISIBLE
//        mainFrameLayout.visibility = View.VISIBLE
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        val zoomLevel = 18.0f //This goes up to 21

        // Add a marker in the map and move the camera
        val latLng = LatLng(Latitude, Longitude)
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }


//    override fun onHideChat() {
//        chatHideListener!!.onHideChat()
//
//    }
//
//    override fun onViewChat() {
//
//    }
}