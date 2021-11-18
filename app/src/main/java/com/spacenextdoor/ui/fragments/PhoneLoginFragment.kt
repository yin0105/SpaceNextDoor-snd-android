package com.spacenextdoor.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.hbb20.CountryCodePicker
import com.mukesh.OtpView
import com.spacenextdoor.R
import com.spacenextdoor.listeners.AuthListener
import com.spacenextdoor.model.UserInfoModel
import com.spacenextdoor.service.LoginServiceCalling
import com.spacenextdoor.ui.activities.BottomNavigationActivity
import com.spacenextdoor.ui.activities.OnBoardingActivity
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import kotlinx.android.synthetic.main.fragment_email_login.*
import kotlinx.android.synthetic.main.fragment_phone_login.*
import kotlinx.android.synthetic.main.fragment_phone_login.checkBoxPrivacy
import kotlinx.android.synthetic.main.fragment_phone_login.chkRemember
import www.sanju.motiontoast.MotionToast

class PhoneLoginFragment : Fragment(), AuthListener {


    @BindView(R.id.etCcp)
    lateinit var setCountryCode: EditText

    @BindView(R.id.ccpCounteryPicker)
    lateinit var ccpCounteryPicker: CountryCodePicker

    @BindView(R.id.rlPhone)
    lateinit var relativeLayoutPhone: RelativeLayout

    @BindView(R.id.tvPhoneError)
    lateinit var PhoneError: TextView

    lateinit var loaderShownInView: RelativeLayout

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView

    private var countryCode: String? = null
    private var countryName: String? = null
    private val ccpText: String? = null

    var loginServiceCalling = LoginServiceCalling()
    var authListener: AuthListener = this
    var pDialog: SweetAlertDialog? = null
    lateinit var oTP: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val root: View = View.inflate(context, R.layout.fragment_phone_login, null)
        ButterKnife.bind(this, root)

        loaderShownInView = root.findViewById(R.id.rlLoader)

        loginServiceCalling.setAuthListener(authListener!!)

        pDialog = Util.init_Progress(requireContext())


        onCountrySelected()
        //   get Country Code
        ccpCounteryPicker.setOnCountryChangeListener {
            onCountrySelected()
        }

        var OtpView1: OtpView = root.findViewById(R.id.otp_view)

        OtpView1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length == 6) {
                    oTP = s.toString()
                    btnVerifyPhone.isEnabled = true
                    btnVerifyPhone.alpha = 1f
                } else {
                    btnVerifyPhone.alpha = 0.25f
                    btnVerifyPhone.isEnabled = false
                }
            }
        })

        return root
    }


    // Resend the OTP if not Received
    @OnClick(R.id.tvSendOtpPhone)
    fun sendOtpPhone() {
        onLogin()
    }

    //Verify OTP
    @OnClick(R.id.btnVerifyPhone)
    fun verifyLogin() {

        val phone: String = etPhoneEdit.text.toString()
        val resultText = countryCode + phone

        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            // pDialog!!.show()
            loaderShownInView.visibility = View.VISIBLE

            imWaveFormation.setAnimation(R.raw.loader)
            imWaveFormation.visibility = View.VISIBLE
            loginServiceCalling.verifyCodeAndEmail(resultText, oTP, lifecycleScope)
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }
    }

    //// SendOTP to the Phone
    @OnClick(R.id.btnLoginPhone)
    fun onLogin() {

        val phone: String = etPhoneEdit.text.toString()
        val ccp: String = etCcp.text.toString()
        val resultText = countryCode + phone


        if (phone.isNotEmpty() && ccp.isNotEmpty() && chkRemember.isChecked) {
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                //  pDialog!!.show()
                loaderShownInView.visibility = View.VISIBLE
                imWaveFormation.setAnimation(R.raw.loader)
                imWaveFormation.visibility = View.VISIBLE
                loginServiceCalling.sendOtpPhone(resultText, lifecycleScope)
            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }
        } else if (!onValidate(phone)) {
            onSetfields()
            return
        } else if (TextUtils.isEmpty(ccpText)) {
            tvPhoneError.text = resources.getString(R.string.select_country)
            rlPhone.setBackgroundResource(R.drawable.error_background)
        }
    }

    //// Edit Phone Number
    @OnClick(R.id.tvEditPhoneView)
    fun clickEditEmail() {

        llLoginPhone.visibility = View.VISIBLE
        llVerifyPhone.visibility = View.GONE

        val phone: String = etPhoneEdit.text.toString()
        if (!onValidate(phone)) {
            onSetfields()
            return
        }
        chkRemember.toggle()
    }

    @OnClick(R.id.chkRemember)
    fun onChecked() {
        checkBoxPrivacy.setTextColor(resources.getColor(R.color.check_box_color))
    }

    // Validation check
    private fun onValidate(phone: String): Boolean {

        var valid = true
        val ccp: String = etCcp.text.toString()

        if (TextUtils.isEmpty(phone)) {
            tvPhoneError.text = resources.getString(R.string.enter_phone_number)
            rlPhone.setBackgroundResource(R.drawable.error_background)
            valid = false
        } else {
            tvPhoneError.text = ""
            rlPhone.setBackgroundResource(R.drawable.bg_round)
        }

        if (chkRemember.isChecked) {
            checkBoxPrivacy.setTextColor(resources.getColor(R.color.check_box_color))
        } else {
            checkBoxPrivacy.setTextColor(resources.getColor(R.color.check_box_unchecked))
            valid = false
        }
        return valid
    }

    private fun onSetfields() {
        btnLoginPhone.isEnabled = true
    }

//    private fun onCountrySelected() {
//
//        countryCode = ccpCounteryPicker!!.selectedCountryCodeWithPlus
//        countryName = ccpCounteryPicker!!.selectedCountryName
//
//        etCcp.setText(countryCode)
//        rlPhone.setBackgroundResource(R.drawable.bg_round)
//        tvPhoneError.text = ""
//    }

    private fun onCountrySelected() {

        countryCode = ccpCounteryPicker!!.selectedCountryCodeWithPlus
        countryName = ccpCounteryPicker!!.selectedCountryName

        setCountryCode.setText(countryCode)
        relativeLayoutPhone.setBackgroundResource(R.drawable.bg_round)
        PhoneError.text = ""
    }


    private fun moveToNextActivity() {
        val intent = Intent(activity, BottomNavigationActivity::class.java)
        startActivity(intent)
        activity?.finish()
        loaderShownInView.visibility = View.GONE
    }

    private fun moveToOnBoarding() {
        val intent = Intent(activity, OnBoardingActivity::class.java)
        startActivity(intent)
    }

    override fun onSuccess(accessToken: String, refresh_token: String) {
        // pDialog!!.dismiss()
        val refreshLoginToken = refresh_token
        val accessLoginToken = accessToken
        val user = UserInfoModel()
        user.accessToken = "Bearer" + " " + accessLoginToken
        user.refresh_token = refreshLoginToken
        PrefManger.saveUser(user, requireActivity())

        if (user != null) {
            moveToNextActivity()
        } else {
            moveToOnBoarding()
            loaderShownInView.visibility = View.GONE
        }
    }

    override fun onFailure(msg: String) {
        // pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        //tvOtpPhoneError.text = msg
        MotionToast.createColorToast(
            requireActivity(),
            "Failed ☹",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )
    }

    override fun onFailurePhone(msg: String) {
        //  pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        // tvPhoneError.text = msg
        MotionToast.createColorToast(
            requireActivity(),
            "Failed ☹",
            msg,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )
    }

    override fun onSendOTPSuccess(email: String) {
        // pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        llLoginPhone.visibility = View.GONE
        llVerifyPhone.visibility = View.VISIBLE
        val msg =
            requireContext().getString(R.string.phone_message_otp, email)
        etPhoneVerify.setText(email)
        pin_detail_phone.text = msg
    }

}