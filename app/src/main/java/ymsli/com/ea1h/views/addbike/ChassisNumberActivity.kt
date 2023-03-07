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
 * ChassisNumberActivity : This is class which will perform all the chassis number
 * related operations
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
import android.text.Editable
import android.view.MenuItem
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.add_vehicle_fragment.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.TextWatcherAdapter

class ChassisNumberActivity : BaseActivity<ChassisNumberViewModel>() {

    companion object{
        const val CHASSIS_NUMBER_TAG = "CHASSIS_NUMBER_TAG"
        const val PHONE_NUMBER_TAG = "PHONE_NUMBER_TAG"
    }

    override fun provideLayoutId(): Int = R.layout.add_vehicle_fragment
    override fun injectDependencies(ac: ActivityComponent) = ac.inject(this)

    /** Overridden to handle the toolbar back button press */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        setupSupportActionBar()
        setupListeners()
    }

    /**
     * Sets up the support action bar with Activity Title and
     * configures the home button as back button on the toolbar.
     * @author VE00YM023
     */
    private fun setupSupportActionBar(){
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.bg_toolbar)))
    }

    /**
     * Setup listeners for edit text as well as 'Next Step' Button.
     * @author VE00YM023
     */
    private fun setupListeners(){
        et_chassis.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.chassisNumber.postValue(s.toString().trim())
            }
        })
        btn_next.setOnClickListener { hideKeyboard(appbar_layout); viewModel.validateChassisNumber() }
    }

    /**
     * Setup Observers for different LiveData objects ( showProgress, chassisNo etc)
     * @author VE00YM023
     */
    override fun setupObservers() {
        super.setupObservers()
        val scanIntent = Intent(this, ScanQRCodeActivity::class.java)
        viewModel.isChassisValid.observe(this, Observer {
                if(!it.isBlank()){
                    scanIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//open QR scanner once chassis number is validated
                    scanIntent.putExtra(CHASSIS_NUMBER_TAG,viewModel.chassisNumber.value)
                    scanIntent.putExtra(PHONE_NUMBER_TAG,it)
                    startActivity(scanIntent)
                }
                else{
                    showNotificationDialog(getString(R.string.invalid_chassis_number), R.drawable.ic_error){}
                }
        })

        /* When validations fail post error to the input field */
        viewModel.showError.observe(this, Observer {
            et_chassis.error = this.resources.getString(it)
            et_chassis.requestFocus()
        })

        /* Show progress when API call is active */
        viewModel.showProgress.observe(this, Observer { showProgress(it) })

        viewModel.errorNotification.observe(this, Observer {
            val message = it ?: getString(R.string.something_went_wrong)
            showNotificationDialog(message, R.drawable.ic_error){}
        })
    }
}