package ymsli.com.ea1h.views.otp

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 2:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * OTPActivity : This is the OTP activity used while adding bike
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.view.MenuItem
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.widget.Chronometer
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.otp_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_CHASSIS_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_PHONE_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_QR_CODE
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_REQUEST_TYPE
import ymsli.com.ea1h.utils.common.TextWatcherAdapter
import ymsli.com.ea1h.views.addbike.ChassisNumberActivity
import ymsli.com.ea1h.views.home.HomeActivity

class OTPActivity: BaseActivity<OTPViewModel>() {

    companion object {
        const val BIKE_ADDED_SUCCESS = "Vehicle added successfully"
    }

    override fun provideLayoutId(): Int = R.layout.otp_activity
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)

    /** Overridden to handle back button click on toolbar */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /** on back pressed, we move to chassis activity */
    override fun onBackPressed() {
        val intent = Intent(this, ChassisNumberActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun setupView(savedInstanceState: Bundle?) {
        showProgress(true)
        extractIntentParams()
        setupToolbar()
        setupListeners()
        viewModel.getOTPInterval()
        window.setFlags(FLAG_NOT_TOUCHABLE,FLAG_NOT_TOUCHABLE);
    }

    /**
     * Extract the intent parameters received from the the TermsAndConditionsActivity.
     * @author VE00YM023
     */
    private fun extractIntentParams(){
        viewModel.chassisNumber = intent.getStringExtra(INTENT_KEY_CHASSIS_NO) ?: EMPTY_STRING
        viewModel.phoneNumber = intent.getStringExtra(INTENT_KEY_PHONE_NO) ?: EMPTY_STRING
        viewModel.qrCode = intent.getStringExtra(INTENT_KEY_QR_CODE) ?: EMPTY_STRING
        viewModel.requestType = intent.getIntExtra(INTENT_KEY_REQUEST_TYPE, 0)
    }

    /**
     * Sets up the toolbar with activity TITLE and configure it
     * to display the back button.
     * @author VE00YM023
     */
    private fun setupToolbar(){
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.bg_toolbar)))
    }

    /**
     * Configures the input field to post its value to view model,
     * when input value changes and sets click listeners for 'Resend OTP' label
     * and 'Verify' button.
     * @author VE00YM023
     */
    private fun setupListeners(){
        et_otp.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.otp = s?.toString()?.trim()
            }
        })

        tv_resend_otp.setOnClickListener { hideKeyboard(appbar_layout); viewModel.resendOTP() }
        btn_verify.setOnClickListener { hideKeyboard(appbar_layout); viewModel.validateOTP() }
    }

    override fun onStart() {
        super.onStart()
        tv_resend_otp.visibility = View.GONE
        chronometer.onChronometerTickListener =
            Chronometer.OnChronometerTickListener { chronometer ->
                val elapsed = SystemClock.elapsedRealtime() - chronometer!!.base
                if (elapsed >= 1000) {
                    chronometer.stop()
                    chronometer.visibility = View.GONE
                    tv_resend_otp.visibility = View.VISIBLE
                }
            }
    }

    override fun setupObservers() {
        super.setupObservers()
        /* When otp interval is received, start the chronometer */
        viewModel.otpInterval.observe(this, Observer {
            window.clearFlags(FLAG_NOT_TOUCHABLE)
            showProgress(false)
            chronometer.base = SystemClock.elapsedRealtime() + (it * 1000)
            chronometer.start()
        })

        /* When OTP is resent successfully, start the timer again */
        viewModel.resendOTPSuccess.observe(this, Observer { if(it){
            tv_resend_otp.visibility = View.GONE
            chronometer.visibility = View.VISIBLE
            chronometer.base = SystemClock.elapsedRealtime() + (viewModel.otpInterval.value!! * 1000)
            chronometer.start()
        } })

        /* When registration is successful, show the dialog */
        viewModel.registrationSuccess.observe(this, Observer {
            val message = BIKE_ADDED_SUCCESS
            showNotificationDialog(message, R.drawable.ic_success){
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        })

        /* If validations fail then show the error */
        viewModel.error.observe(this, Observer {
            et_otp.error = getString(it)
            et_otp.requestFocus()
        })

        viewModel.showProgress.observe(this, Observer { showProgress(it) })

        viewModel.successNotification.observe(this, Observer {
            showNotificationDialog(it, R.drawable.ic_success) {}
        })

        viewModel.errorNotification.observe(this, Observer {
            val message = if(it.isNullOrEmpty()) getString(R.string.something_went_wrong) else it
            showNotificationDialog(message, R.drawable.ic_error) {}
        })
    }
}