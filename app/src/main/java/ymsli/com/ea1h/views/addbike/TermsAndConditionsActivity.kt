package ymsli.com.ea1h.views.addbike

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    10/02/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TermsAndConditionsActivity : This is class which will show terms and conditions of the app
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
import android.view.MenuItem
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.terms_conditions_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_CHASSIS_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_PHONE_NO
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_QR_CODE
import ymsli.com.ea1h.utils.common.Constants.INTENT_KEY_REQUEST_TYPE
import ymsli.com.ea1h.views.otp.OTPActivity

class TermsAndConditionsActivity: BaseActivity<TermsAndConditionsViewModel>() {

    override fun provideLayoutId(): Int  = R.layout.terms_conditions_activity
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){ onBackPressed() }
        return super.onOptionsItemSelected(item)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        extractIntentParams()
        setupToolbar()
        viewModel.getTermsAndConditions()
        btn_accept.setOnClickListener { viewModel.generateOTP() }
    }

    /**
     * Extract the intent parameters received from the the ScanQRCodeActivity.
     * this parameters are passed to OTPActivity.
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

    override fun setupObservers() {
        super.setupObservers()
        /* Load the terms and conditions from API Response stored in room */
        viewModel.termsAndConditions.observe(this, Observer { tv_text_container.text = it })
        viewModel.showProgress.observe(this, Observer { showProgress(it) })

        /* When Generate OTP API succeeds, redirect to OTP Screen */
        viewModel.apiResponseSuccess.observe(this, Observer {
            val otpIntent = Intent(this, OTPActivity::class.java)
            otpIntent.putExtra(INTENT_KEY_CHASSIS_NO, viewModel.chassisNumber)
            otpIntent.putExtra(INTENT_KEY_QR_CODE, viewModel.qrCode)
            otpIntent.putExtra(INTENT_KEY_REQUEST_TYPE, viewModel.requestType)
            otpIntent.putExtra(INTENT_KEY_PHONE_NO, viewModel.phoneNumber)
            otpIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(otpIntent)
            finish()
        })
    }
}