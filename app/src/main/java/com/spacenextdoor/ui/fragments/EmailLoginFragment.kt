package com.spacenextdoor.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.mukesh.OnOtpCompletionListener
import com.mukesh.OtpView
import com.shashank.sony.fancytoastlib.FancyToast
import com.spacenextdoor.R
import com.spacenextdoor.R2
import com.spacenextdoor.listeners.AuthListener
import com.spacenextdoor.model.UserInfoModel
import com.spacenextdoor.service.LoginServiceCalling
import com.spacenextdoor.ui.activities.BottomNavigationActivity
import com.spacenextdoor.ui.activities.OnBoardingActivity
import com.spacenextdoor.utils.PrefManger
import com.spacenextdoor.utils.Util
import kotlinx.android.synthetic.main.fragment_email_login.*
import kotlinx.android.synthetic.main.fragment_email_login.checkBoxPrivacy
import kotlinx.android.synthetic.main.fragment_email_login.chkRemember
import kotlinx.android.synthetic.main.fragment_email_login.view.*
import kotlinx.android.synthetic.main.fragment_phone_login.*
import www.sanju.motiontoast.MotionToast


const val RC_SIGN_IN = 123

// AIzaSyBgOo-djfQEeEZaWUGB3BFRl7bm5YdW_h8 Google API KEY

class EmailLoginFragment : Fragment(), AuthListener {


    var loginServiceCalling = LoginServiceCalling()

    var authListener: AuthListener = this

    var pDialog: SweetAlertDialog? = null

    lateinit var loaderShownInView: RelativeLayout

    @BindView(R.id.imWaveFormation)
    lateinit var imWaveFormation: LottieAnimationView
    lateinit var oTP: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val root: View = View.inflate(context, R.layout.fragment_email_login, null)
        ButterKnife.bind(this, root)
        requireActivity().getWindow()
            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        loaderShownInView = root.findViewById(R.id.rlLoader)

        loginServiceCalling.setAuthListener(authListener!!)

        pDialog = Util.init_Progress(requireContext())

        var OtpView1: OtpView = root.findViewById(R.id.otp_view)

        OtpView1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length == 6) {
                    oTP = s.toString()
                    btnVerify.isEnabled = true
                    btnVerify.alpha = 1f
                } else {
                    btnVerify.alpha = 0.25f
                    btnVerify.isEnabled = false
                }
            }
        })

        return root
    }


    /**** Google Login Implementation  *******/

/*    @OnClick(R.id.llLoginGoogle)
    fun google_signin() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        Log.e("show us", "Google SignIn method called.")
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val acct = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (acct != null) {
            val personName = acct.displayName
            val personGivenName = acct.givenName
            val personFamilyName = acct.familyName
            val personEmail = acct.email
            val personId = acct.id
            val personPhoto: Uri? = acct.photoUrl
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Log.e("sing in successfully", "done sign in")
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("not login successfully", "Not done")

        }
    }


    // SendOTP text again if you Didnt get it
    @OnClick(R.id.tvSendText)
    fun sendOtpEmail() {
        login()
    }

    // Verify OTP for email
    @OnClick(R.id.btnVerify)
    fun verifyLogin() {

        val email: String = etEmail.text.toString()

        if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
            //pDialog!!.show()
            loaderShownInView.visibility = View.VISIBLE

            imWaveFormation.setAnimation(R.raw.loader)
            imWaveFormation.visibility = View.VISIBLE
            loginServiceCalling.verifyCodeAndEmail(email, oTP, lifecycleScope)
        } else {
            Util.showDialog(
                getString(R.string.alert),
                getString(R.string.no_Internet),
                requireActivity()
            )
        }
    }

    // Send OTP for Email
    @OnClick(R.id.btnLogin)
    fun login() {

        val email: String = etEmail.text.toString()

        if (validate(email)) {
            if (Util.DETECT_INTERNET_CONNECTION(requireActivity())) {
                // pDialog!!.show()
                loaderShownInView.visibility = View.VISIBLE
                // imWaveFormation = R.id.imWaveFormation
                imWaveFormation.setAnimation(R.raw.loader)
//        imWaveFormation.playAnimation()
                imWaveFormation.visibility = View.VISIBLE

                loginServiceCalling.sendOtp(email, lifecycleScope)

            } else {
                Util.showDialog(
                    getString(R.string.alert),
                    getString(R.string.no_Internet),
                    requireActivity()
                )
            }

        } else if (!validate(email)) {
            onSetFields()
            return
        }
    }

    // Edit email
    @OnClick(R.id.email_edit)
    fun editEmail() {

        llLogin.visibility = View.VISIBLE
        llVerify.visibility = View.GONE
        val email: String = etEmail.text.toString()
        if (!validate(email)) {
            btnLogin.isEnabled = true
            return
        }
        chkRemember.toggle()
    }

    @OnClick(R.id.chkRemember)
    fun checked() {
        checkBoxPrivacy.setTextColor(resources.getColor(R.color.check_box_color))
    }

    // Validation Check
    private fun validate(email: String): Boolean {
        var valid = false

        if (TextUtils.isEmpty(email)) {
            tvEmailError.text = resources.getString(R.string.enter_email)
            etEmail.setBackgroundResource(R.drawable.error_background)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tvEmailError.text = resources.getString(R.string.invalid_email)
        } else if (!chkRemember.isChecked) {
            settingEditTextAndCheckBox(resources.getColor(R.color.check_box_unchecked));
        } else {
            valid = true
            settingEditTextAndCheckBox(resources.getColor(R.color.check_box_color));
        }
        return valid
    }

    private fun settingEditTextAndCheckBox(color: Int) {
        tvEmailError.text = "";
        etEmail.setBackgroundResource(R.drawable.textview_round)
        checkBoxPrivacy.setTextColor(color)
    }


    private fun moveToNextActivity() {
        val intent = Intent(activity, BottomNavigationActivity::class.java)
        startActivity(intent)
        imWaveFormation.visibility = View.GONE
        activity?.finish()
    }

    private fun moveToOnBoarding() {
        val intent = Intent(activity, OnBoardingActivity::class.java)
        startActivity(intent)
    }

    private fun onSetFields() {
        btnLogin.isEnabled = true
    }

    override fun onSuccess(accessToken: String, refresh_token: String) {
        // pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE

        val refreshLoginToken = refresh_token
        val accessLoginToken = accessToken
        val user = UserInfoModel()
//        user.accessToken = "Bearer" + " " + accessLoginToken
        user.accessToken = accessLoginToken
        user.refresh_token = refreshLoginToken
        PrefManger.saveUser(user, requireActivity())

        if (user != null) {
            moveToNextActivity()
        } else {
            moveToOnBoarding()
        }
    }

    override fun onFailure(msg: String) {
        // pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        val errorMessage = msg
//        FancyToast.makeText(
//            requireContext(),
//            errorMessage,
//            FancyToast.LENGTH_LONG,
//            FancyToast.ERROR,
//            true
//        ).show()


        MotionToast.createColorToast(

            requireActivity(),
            "Failed ☹",
            errorMessage,
            MotionToast.TOAST_ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(requireContext(), R.font.helvetica_regular)
        )

//        Util.showDialog(
//            "Alert",
//            errorMessage,
//            requireContext()
//        )
    }

    override fun onFailurePhone(msg: String) {
        // pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        val errorMessage = msg
        Util.showDialog(
            "Alert",
            errorMessage,
            requireContext()
        )
    }

    override fun onSendOTPSuccess(email: String) {
        //pDialog!!.dismiss()
        loaderShownInView.visibility = View.GONE
        imWaveFormation.visibility = View.GONE
        llLogin.visibility = View.GONE
        llVerify.visibility = View.VISIBLE
        val msg =
            requireContext().getString(R.string.message_otp, email)
        pin_detail.text = msg
        etEmailVerify.setText(email)
    }


}