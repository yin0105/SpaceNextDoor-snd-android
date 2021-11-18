package com.spacenextdoor.ui.fragments.BottomNavFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.spacenextdoor.FetchProfileQuery
import com.spacenextdoor.R
import com.spacenextdoor.listeners.ProfileListener
import com.spacenextdoor.service.FetchProfile
import com.spacenextdoor.ui.activities.MainActivity
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import com.stripe.android.view.CardInputWidget
import kotlinx.android.synthetic.main.custom_dialog.view.*
import kotlinx.android.synthetic.main.fragment_account.*
import www.sanju.motiontoast.MotionToast

class AccountFragment : Fragment(), ProfileListener {


    @BindView(R.id.compactCreditCardInput)
    lateinit var compactCreditCardInput: CardInputWidget

    var cardToSave: Card? = null
    private lateinit var stripe: Stripe

    var fetchProfileCalling = FetchProfile()
    var profileListener: ProfileListener = this

    var pDialog: SweetAlertDialog? = null

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView

    @BindView(R.id.mainFrameLayout)
    lateinit var mainFrameLayout: RelativeLayout

    lateinit var loaderShownInView: RelativeLayout

    @BindView(R.id.tvFullName)
    lateinit var tvFullName: TextView

    @BindView(R.id.tvEmailAddress)
    lateinit var tvEmailAddress: TextView

    @BindView(R.id.tvPaymentMethod)
    lateinit var tvPaymentMethod: TextView

    @BindView(R.id.phone_num)
    lateinit var phone_num: TextView

    @BindView(R.id.swipeRefresh)
    lateinit var swipeRefresh: SwipeRefreshLayout
    var refresh: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = View.inflate(context, R.layout.fragment_account, null)
        ButterKnife.bind(this, root)

        swipeRefresh.setOnRefreshListener {
            refresh = true
            settingDataInViews()
            swipeRefresh.isRefreshing = false
        }
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        loaderShownInView = root.findViewById(R.id.rlLoader)
        imWaveFormation.setAnimation(R.raw.loader)

        mainFrameLayout.visibility = View.GONE
        loaderShownInView.visibility = View.VISIBLE
        pDialog = Util.init_Progress(requireContext())

        compactCreditCardInput.postalCodeEnabled = false

        PaymentConfiguration.init(
            requireContext(),
            resources.getString(R.string.publish_key_snd_test)
        )

        // imWaveFormation.visibility = View.VISIBLE

        //pDialog!!.show()

        fetchProfileCalling.setFetchProfileListener(profileListener!!)

        settingDataInViews()

        return root
    }

    private fun settingDataInViews() {

        if (refresh) {
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                fetchProfileData()
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }
        } else {
            var profileData: FetchProfileQuery.Profile? =
                PrefManger.getProfile(requireContext())

            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {

                if (profileData != null) {
                    settingProfile(profileData)
                } else {
                    fetchProfileData()
                }

            } else {
                if (profileData != null) {
                    settingProfile(profileData)
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

    private fun fetchProfileData() {

        fetchProfileCalling.getProfile(lifecycleScope, activity)
    }


    private fun notEditPayment() {
        background_tint_payment.isSelected = false
        llCardHide.visibility = View.GONE
        rlCardShow.visibility = View.VISIBLE
        llCardHide.isFocusableInTouchMode = false
        llCardHide.isFocusable = false
        layout_save.visibility = View.GONE
        view1.visibility = View.VISIBLE
    }

    private fun onEditPayment() {
        background_tint_payment.isSelected = true
        llCardHide.visibility = View.VISIBLE
        rlCardShow.visibility = View.GONE
        llCardHide.isFocusableInTouchMode = true
        llCardHide.isFocusable = true
        llCardHide.requestFocus()
        layout_save.visibility = View.VISIBLE
        view1.visibility = View.GONE
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
            // pDialog!!.show()
            loaderShownInView.visibility = View.VISIBLE
            //    imWaveFormation.visibility = View.VISIBLE
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
            // pDialog!!.dismiss()
            loaderShownInView.visibility = View.GONE
            //    imWaveFormation.visibility = View.GONE

            return
        }

        stripe.createCardToken(cardToSave!!,
            callback = object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    Log.e("check_card_1", "data " + e);
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
                    Log.e("token_id_0", "data " + result.id)
                    pDialog!!.show()
                    fetchProfileCalling.updateProfileCardToken(cardToken, lifecycleScope, activity)
                }
            }
        )

    }

    @OnClick(R.id.btn_cancel)
    fun onCancel() {
        notEditPayment()
    }

    @OnClick(R.id.singOutApplication)
    fun signOutApp() {
        val view = View.inflate(requireContext(), R.layout.lagout_dialog, null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        view.dialog_cancel.setOnClickListener {
            dialog.dismiss()
        }

        view.dialog_continue.setOnClickListener {
            mainFrameLayout.visibility = View.GONE
            loaderShownInView.visibility = View.VISIBLE

            imWaveFormation.visibility = View.VISIBLE
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                PrefManger.clearPreferences(requireContext())
                PrefManger.clearPreferencesData(requireContext())
                moveToLoginPage()
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }
            dialog.dismiss()
        }
    }

    private fun moveToLoginPage() {
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun settingProfile(profileDetails: FetchProfileQuery.Profile?) {
        if (profileDetails != null) {
            if (profileDetails!!.first_name != null) {
                tvFullName.text = profileDetails!!.first_name
            }
            if (profileDetails!!.email != null) {
                tvEmailAddress.text = profileDetails!!.email
            }
            if (profileDetails!!.customer!!.card_last_digits != null) {
                tvPaymentMethod.text = profileDetails!!.customer!!.card_last_digits
            }
            if (profileDetails!!.phone_number != null) {
                phone_num.text = profileDetails!!.phone_number
            }
        }
        imWaveFormation.visibility = View.GONE
        loaderShownInView.visibility = View.GONE
        mainFrameLayout.visibility = View.VISIBLE
    }

    override fun onSuccess(profileDetails: FetchProfileQuery.Profile?) {

        settingProfile(profileDetails)
        PrefManger.saveProfile(profileDetails, requireContext())
        // pDialog!!.dismiss()
    }

    override fun onFailure(msg: String) {
        pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        //  imWaveFormation.visibility = View.GONE
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
        pDialog!!.dismiss()
        tvPaymentMethod.text = cardToSave!!.last4.toString()
        val messageProfileSuccess = "Your card is Updated"

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
        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            PrefManger.clearPreferencesData(requireContext())
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }



    }

    override fun onUpdateProfileFailure(message: String) {
        // pDialog!!.dismiss()
        imWaveFormation.visibility = View.VISIBLE
        loaderShownInView.visibility = View.VISIBLE
        val messageProfileFailure = message

        Util.showDialog(
            "Alert",
            messageProfileFailure,
            requireContext()
        )
    }
}