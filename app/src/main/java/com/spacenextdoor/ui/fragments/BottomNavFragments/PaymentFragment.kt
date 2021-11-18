package com.spacenextdoor.ui.fragments.BottomNavFragments


import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ethanhua.skeleton.RecyclerViewSkeletonScreen
import com.ethanhua.skeleton.Skeleton
import com.ethanhua.skeleton.SkeletonScreen
import com.helpcrunch.library.core.HelpCrunch
import com.helpcrunch.library.core.models.user.HCUser
import com.spacenextdoor.FetchProfileQuery
import com.spacenextdoor.FetchTransactionsQuery
import com.spacenextdoor.R
import com.spacenextdoor.adapter.GetTransactionListAdapter
import com.spacenextdoor.listeners.*
import com.spacenextdoor.model.TransactionsModel
import com.spacenextdoor.service.FetchProfile
import com.spacenextdoor.service.FetchTransactions
import com.spacenextdoor.service.TransactionListItemData
import com.spacenextdoor.ui.fragments.BottomNavFragments.Payment.PaymentDetailFragment
import com.spacenextdoor.ui.fragments.BottomNavFragments.Payment.PaymentDetailFragmentInsurance
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_payment.*
import www.sanju.motiontoast.MotionToast

class PaymentFragment(chatbtnListener: ChatBtnHideListener) : Fragment(), TransactionListener,
    ProfileListener, PaymentDetailListener,
    OnBackPressedClicked {


    @BindView(R.id.edit_Payment_btn)
    lateinit var paymentEditBtn: TextView

    @BindView(R.id.payment_transaction_btn)
    lateinit var btntransaction: TextView

    @BindView(R.id.payment_method_btn)
    lateinit var btnpaymentMethod: TextView

    @BindView(R.id.layout_payments_list)
    lateinit var layoutPaymentList: LinearLayout

    @BindView(R.id.compactCreditCardInput)
    lateinit var compactCreditCardInput: CardInputWidget

    @BindView(R.id.tvPaymentMethod)
    lateinit var tvPaymentMethod: TextView

    @BindView(R.id.layout_save)
    lateinit var layout_save: LinearLayout

    @BindView(R.id.emptyStateTransactions)
    lateinit var emptyStateTransactions: LinearLayout

    @BindView(R.id.frameViewDetail)
    lateinit var frameViewDetail: FrameLayout

    @BindView(R.id.layout_with_unit_data)
    lateinit var layout_with_unit_data: LinearLayout

    @BindView(R.id.layout_transactions)
    lateinit var layout_transactions: LinearLayout


    @BindView(R.id.layout_payment_methods)
    lateinit var layout_payment_methods: LinearLayout


    var cardToSave: Card? = null
    private lateinit var stripe: Stripe

    private var paymentSkeletonScreen: RecyclerViewSkeletonScreen? = null

    private var transactionSkeletionScreen: SkeletonScreen? = null


    var onPaymentListener: PaymentDetailListener? = this

    var pDialog: SweetAlertDialog? = null

    @BindView(R.id.recyclerViewCard)
    lateinit var recyclerViewCard: RecyclerView

    var fetchTransactionsCalling = FetchTransactions()
    var transactionsListener: TransactionListener = this

    var fetchProfileCalling = FetchProfile()
    var profileListener: ProfileListener = this
    var mBackPressedClicked: OnBackPressedClicked? = this

    var chatHideListener: ChatBtnHideListener? = chatbtnListener

    var isPaymentMethodCalled: Boolean = false

    @BindView(R.id.swipeRefresh)
    lateinit var swipeRefresh: SwipeRefreshLayout
    var refresh: Boolean = false

    var tabClicked: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater!!.inflate(R.layout.fragment_payment, container, false)

        ButterKnife.bind(this, root)

        swipeRefresh.setOnRefreshListener {
            refresh = true
            settingDataInViews()
            swipeRefresh.isRefreshing = false
        }


        var transactionList = mutableListOf<TransactionsModel>()

        val adapter =
            activity?.let { GetTransactionListAdapter(it, transactionList, onPaymentListener) }

//        paymentSkeletonScreen = Skeleton.bind(recyclerViewCard)
//            .adapter(adapter)
//            .load(R.layout.toolbar)
//            .shimmer(true)
//            .show()

        transactionSkeletionScreen = Skeleton.bind(layoutPaymentList)
            .load(R.layout.toolbar)
            .shimmer(true)
            .color(R.color.white)
            .show()

        pDialog = Util.init_Progress(requireContext())

        compactCreditCardInput.postalCodeEnabled = false

        PaymentConfiguration.init(
            requireContext(),
            resources.getString(R.string.publish_key_snd_test)
        )

//        pDialog!!.show()

        settingDataInViews()

        recyclerViewCard.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        btntransaction.setSelected(true);
        btntransaction.setTypeface(null, Typeface.BOLD);
        btntransaction.setTextColor(Color.parseColor("#FFFFFF"))
        btnpaymentMethod.alpha = 0.4f
        fetchProfileCalling.setFetchProfileListener(profileListener!!)
        // setupCustomSpinner()
        return root
    }

    private fun settingDataInViews() {

        if (refresh) {
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                fetchProfileData()
                fetchTransacttionData()
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }
        } else {
            var profileData: FetchProfileQuery.Profile? =
                PrefManger.getProfileDataPayment(requireContext())

            var transactionData: FetchTransactionsQuery.Transactions? =
                PrefManger.getTransactionData(requireContext())

            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {

                if (profileData != null) {
                    settingProfileData(profileData)
                } else {
                    fetchProfileData()
                }

                if (transactionData != null) {
                    settingTranscationData(transactionData)
                } else {
                    fetchTransacttionData()
                }


            } else {

                if (profileData != null && transactionData != null) {
                    settingProfileData(profileData)
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

    private fun fetchTransacttionData() {
        fetchTransactionsCalling.setTransactionListener(transactionsListener!!)
        fetchTransactionsCalling.getTransactions(lifecycleScope, activity)
    }

    private fun fetchProfileData() {

        fetchProfileCalling.getProfile(lifecycleScope, activity)
    }

    @OnClick(R.id.payment_transaction_btn)
    fun TransactionBtn() {
        tabClicked = 0
        transactionBtnClicked()
    }

    private fun transactionBtnClicked() {
        // To select
        btnPaymentTransactionEnable()
        // To deselect.
        btnPaymentMethodDisable()
        layout_payment_methods.visibility = View.GONE
        if (!isPaymentMethodCalled) {
            layout_transactions.visibility = View.GONE
            layout_with_unit_data.visibility = View.GONE
            emptyStateTransactions.visibility = View.VISIBLE

        } else {
            layout_transactions.visibility = View.VISIBLE
            layout_with_unit_data.visibility = View.VISIBLE
            emptyStateTransactions.visibility = View.GONE

        }
    }

    @OnClick(R.id.payment_method_btn)
    fun PaymentMethosBtn() {
        tabClicked = 1
        paymentBtnClicked()
    }

    private fun paymentBtnClicked() {
        btnpaymentMethod.alpha = 0.4f
        btnPaymentMethodEnable()
        btnPaymentTransactionDisable()
        layout_transactions.visibility = View.GONE
        emptyStateTransactions.visibility = View.GONE
        layout_payment_methods.visibility = View.VISIBLE
    }

    @OnClick(R.id.edit_Payment_btn)
    fun performEdit() {
        onEditPayment()
    }

    @OnClick(R.id.btn_save)
    fun onSave() {
        notEditPayment()
        stripe = Stripe(
            requireContext(),
            PaymentConfiguration.getInstance(requireContext()).publishableKey
        )
        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            pDialog!!.show()
            saveCard()
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }
    }


    private fun saveCard() {

        cardToSave = compactCreditCardInput.card

        Log.e("check_card_0", "gbhnj " + cardToSave)

        if (cardToSave == null) {
            Util.showDialog(
                getString(R.string.validationErrors),
                "Invalid Card Data",
                requireContext()
            )
            pDialog!!.dismiss()
            return
        }

        stripe.createCardToken(cardToSave!!,
            callback = object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    Log.e("check_card_1", "gvhbn " + e);
                    Util.showDialog(
                        getString(R.string.validationErrors),
                        e.localizedMessage,
                        requireContext()
                    )
                }

                override fun onSuccess(result: Token) {
                    val cardToken: String = result.id
                    Log.e(
                        javaClass.name,
                        "Token is >>>>>>>>>>>" + result.id.toString() + "  Token card >>>>>>>>" + result.id
                    )
                    Log.e("token_id_0", "esdrf " + result.id)
                    fetchProfileCalling.updateProfileCardToken(cardToken, lifecycleScope, activity)
                }
            }
        )

    }

    @OnClick(R.id.btn_cancel)
    fun onCancel() {
        notEditPayment()
    }


    private fun notEditPayment() {
        background_tint_payment.isSelected = false
        llCardHide.visibility = View.GONE
        rlCardShow.visibility = View.VISIBLE
        llCardHide.isFocusableInTouchMode = false
        llCardHide.isFocusable = false
        layout_save.visibility = View.GONE
        //view1.visibility = View.VISIBLE
    }

    private fun onEditPayment() {
        background_tint_payment.isSelected = true
        llCardHide.visibility = View.VISIBLE
        rlCardShow.visibility = View.GONE
        llCardHide.isFocusableInTouchMode = true
        llCardHide.isFocusable = true
        llCardHide.requestFocus()
        layout_save.visibility = View.VISIBLE
//        view1.visibility = View.GONE
    }

    private fun btnPaymentTransactionEnable() {
        btntransaction.setSelected(true);
        btntransaction.setTypeface(null, Typeface.BOLD);
        btntransaction.alpha = 1f
        btntransaction.setTextColor(Color.parseColor("#FFFFFF"))
    }

    private fun btnPaymentTransactionDisable() {
        btntransaction.setSelected(false);
        btntransaction.setTypeface(null, Typeface.NORMAL);
        btntransaction.alpha = 0.4f
        btntransaction.setTextColor(Color.parseColor("#333333"))
    }

    private fun btnPaymentMethodEnable() {
        btnpaymentMethod.setSelected(true);
        btnpaymentMethod.setTypeface(null, Typeface.BOLD);
        btnpaymentMethod.alpha = 1f
        btnpaymentMethod.setTextColor(Color.parseColor("#FFFFFF"))
    }

    private fun btnPaymentMethodDisable() {
        btnpaymentMethod.setSelected(false);
        btnpaymentMethod.setTypeface(null, Typeface.NORMAL);
        btnpaymentMethod.alpha = 0.4f
        btnpaymentMethod.setTextColor(Color.parseColor("#333333"))
    }

//    private fun setupCustomSsetupCustomSpinnerpinner() {
//        val adapter = context?.let { SpinnerAdapter(it, selectedMonth.list!!) }
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

    private fun setDataInViews(transactionsDetails: MutableList<TransactionsModel>) {

        if (transactionsDetails.size > 0) {
            textview_2.text =
                "Unit " + transactionsDetails.get(0).bookingDetails!!.bookingId.toString() + ", " + transactionsDetails.get(
                    0
                ).bookingDetails!!.bookingSpaceSize + " " + "sqft"

            textview_3.text = Util.getFormattedDate(transactionsDetails.get(0).createdAt.toString())

            textview_totalamount.text =
                "$" + transactionsDetails.get(0).renewalDetails!!.renewalSubTotalAmount.toString()

            textview_units.text = transactionsDetails.get(0).insuranceDetails!!.insuranceNameEn

            textview_date.text =
                Util.getFormattedDate(transactionsDetails.get(0).createdAt.toString())

            textview_total.text =
                "$ " + transactionsDetails.get(0).insuranceDetails!!.insuranceTotalAmount.toString()

        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_result_found),
                requireContext()
            )
        }

    }


    override fun onSuccess(transactionsData: FetchTransactionsQuery.Transactions) {
        settingTranscationData(transactionsData)
        if (tabClicked == 0) {
            transactionBtnClicked()
        } else {
            paymentBtnClicked()
        }
//        pDialog!!.dismiss()
    }

    private fun settingTranscationData(transactionsData: FetchTransactionsQuery.Transactions) {
        if (transactionsData.edges.isEmpty()) {
            transactionSkeletionScreen!!.hide()
            layout_transactions.visibility = View.GONE
            layout_payment_methods.visibility = View.GONE
            layout_with_unit_data.visibility = View.GONE
            emptyStateTransactions.visibility = View.VISIBLE
        } else if (transactionsData.edges != null && transactionsData.edges.isNotEmpty()) {
            PrefManger.saveTransactionData(transactionsData, requireContext())
            isPaymentMethodCalled = true
            transactionSkeletionScreen!!.hide()
            emptyStateTransactions.visibility = View.GONE
            layout_transactions.visibility = View.VISIBLE
            layoutPaymentList.visibility = View.VISIBLE
            var transactionList = TransactionListItemData.setDataInObject(transactionsData)
            recyclerViewCard.adapter =
                activity?.let { GetTransactionListAdapter(it, transactionList, onPaymentListener) }
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_result_found),
                requireContext()
            )
        }
    }

    override fun onFailure(toString: String) {

        if (isVisible) {
            val messageProfileFailure = toString
            Util.showDialog(
                "Alert",
                messageProfileFailure,
                requireContext()
            )
        }

    }

    override fun onUpdateProfileSuccess(message: String) {
        pDialog!!.dismiss()
        tvPaymentMethod.text = cardToSave!!.last4.toString()
        val messageProfileSuccess = "Your Card Is Updated"
//        Util.showDialog(
//            "Alert",
//            messageProfileSuccess,
//            requireContext()
//        )
        MotionToast.createColorToast(
            requireActivity(),
            "Hurray success",
            messageProfileSuccess,
            MotionToast.TOAST_SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )
    }

    override fun onUpdateProfileFailure(message: String) {
        pDialog!!.dismiss()
        val messageProfileFailure = message
        Util.showDialog(
            "Alert",
            messageProfileFailure,
            requireContext()
        )
    }

    override fun onSuccess(profileDetails: FetchProfileQuery.Profile?) {
        //   pDialog!!.dismiss()
        settingProfileData(profileDetails)
        PrefManger.saveProfileDataPayment(profileDetails, requireContext())

    }

    private fun settingProfileData(profileDetails: FetchProfileQuery.Profile?) {
        updateUserData(profileDetails)

        if (profileDetails!!.customer!!.card_last_digits == null) {
            tvPaymentMethod.text = "4242"
        } else {
            tvPaymentMethod.text = profileDetails!!.customer!!.card_last_digits

        }

    }

    override fun onPaymentDetailListener(transactionsModel: TransactionsModel) {
        Thread(Runnable {
            Thread.sleep(50)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                frameViewDetail.visibility = View.VISIBLE
            } //public void run() {
            )

        }).start()
        //chatHideListener!!.onHideChat()
        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.beginTransaction()
            .replace(
                R.id.frameViewDetail,
                PaymentDetailFragment(transactionsModel, mBackPressedClicked)
            )
            .addToBackStack(null)
            .commit()
    }


    override fun onPaymentDetailInsuranceListener(transactionsModel: TransactionsModel) {
        Thread(Runnable {
            Thread.sleep(50)
            // try to touch View of UI thread
            requireActivity().runOnUiThread(Runnable {
                //Your code to run in GUI thread here
                frameViewDetail.visibility = View.VISIBLE
            } //public void run() {
            )

        }).start()
        //chatHideListener!!.onHideChat()
        val fm: FragmentManager = requireActivity().supportFragmentManager
        fm.beginTransaction()
            .replace(
                R.id.frameViewDetail,
                PaymentDetailFragmentInsurance(transactionsModel, mBackPressedClicked)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onBackpressed() {
        //chatHideListener!!.onViewChat()
        frameViewDetail.visibility = View.GONE
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


//    override fun onFailure(msg: String) {
//        pDialog!!.dismiss()
//        val messageProfileFailure = msg
//        Util.showDialog(
//            "Alert",
//            messageProfileFailure,
//            requireContext()
//        )
//    }

}