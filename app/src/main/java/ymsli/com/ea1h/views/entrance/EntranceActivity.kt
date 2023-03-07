/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 01:02 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * EntranceActivity : This activity contains 2 fragments for signup and signin. It handles
 *                          user entry for the core features of the app.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  * (VE00YM023)    04/02/2020          Added the SignIn/SingUp fragment integration
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.entrance

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.synthetic.main.entrance_activity.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.EntrancePagerAdapter
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.views.ackdialogs.ErrorAckDialog
import ymsli.com.ea1h.views.ackdialogs.ForgotPasswordDialog
import ymsli.com.ea1h.views.ackdialogs.GenericAckDialog
import ymsli.com.ea1h.views.sync.SyncActivity
import java.net.NetworkInterface
import java.util.*
import javax.inject.Inject

class EntranceActivity : BaseActivity<EntranceViewModel>(),ForgotPasswordDialog.ForgotPasswordDialogListener,
    GenericAckDialog.OKClickListener {
    private lateinit var sensorManager: SensorManager
    @Inject
    lateinit var ackDialogBuilder : AlertDialog.Builder
    override fun provideLayoutId(): Int = R.layout.entrance_activity
    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        window?.statusBarColor = getColor(R.color.bg_status_bar)
        window.setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN)
        setupEntranceAdapter()
        //checks if required sensor are present in phone or not
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        viewModel.isAcceleroPresent.value = (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
        viewModel.isGyroPresent.value = (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!=null)
    }

    private fun setupEntranceAdapter(){
        val entranceAdapter = EntrancePagerAdapter(this)
        view_pager.adapter = entranceAdapter
        TabLayoutMediator(entrance_tabs,view_pager){tab,position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.sign_in)
                else -> tab.text = resources.getString(R.string.sign_up)
            }
        }.attach()
    }

    /**
     * Setup LiveData observers to show messages as well as to continue to next screen.
     */
    override fun setupObservers() {
        super.setupObservers()
        viewModel.showProgress.observe(this, Observer {
            showProgress(it)
        })
        viewModel.errorAcknowledgement.observe(this, Observer {
            showErrorsViaSnackbar(it)
        })
        viewModel.errorAcknowledgementMessage.observe(this, Observer {
            showErrorsMessageViaSnackbar(it)
        })
        /* If login is successful then move to sync activity */
        viewModel.loginSuccessful.observe(this, Observer {
            val homeIntent = Intent(this, SyncActivity::class.java)
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(homeIntent)
            finish()
        })
        viewModel.signupSuccess.observe(this, Observer { it ->
            it.getIfNotHandled()?.let {
                if(it){// ask user to verify email and redirect to login
                    val intent = Intent(this, SignupSuccessActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })

        viewModel.invalidLoginMessage.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                val errorAlert = ErrorAckDialog(it)
                val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                errorAlert.show(ft,null)
                viewModel.invalidLoginMessage.value = null
            }
        })

        viewModel.forgotPasswordUserAck.observe(this, Observer {
            it.getIfNotHandled()?.let {
                val genericAckDialog = GenericAckDialog(this.resources.getString(it))
                val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                genericAckDialog.show(ft,null)
            }
        })

        viewModel.forgotPasswordUserErrorAck.observe(this, Observer {
            it.getIfNotHandled()?.let{
                val genericAckDialog = GenericAckDialog(it)
                val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                genericAckDialog.show(ft,null)
            }
        })

        viewModel.invalidSignupMessage.observe(this, Observer {
            it.getIfNotHandled()?.let {
                val errorAlert = ErrorAckDialog(it)
                val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                errorAlert.show(ft,null)
            }
        })

        viewModel.errorSignupMessage.observe(this, Observer {
            it.getIfNotHandled()?.let {
                val errorAlert = ErrorAckDialog(resources.getString(it))
                val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
                errorAlert.show(ft,null)
            }
        })
    }
    override fun onStart() {
        super.onStart()
        stopForegroundService()
    }
    override fun onResume() {
        super.onResume()
        requestDeviceId()
    }
    /**
     * sets the Android ID in shared preferences
     */
    private fun requestDeviceId(){
        val deviceId = generateUniqueID()
        if(!deviceId.isNullOrBlank()) viewModel.setAndroidIdInSharedPref(deviceId)
        else {
            viewModel.userBlockerDialog.postValue(Event(R.string.cannot_determine_unique))
        }
    }
    /**
     * generates unique ID
     * be it android id,
     * MAC Address,
     * or UUID
     */
    private fun generateUniqueID(): String?{
        val androidId: String? = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
        if(androidId.isNullOrEmpty()){
            try {
                var macAddress: String? = null
                val all: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0",true)) continue
                    val macBytes: ByteArray? = nif.hardwareAddress
                    if (macBytes!=null && macBytes.isNotEmpty()) {
                        macAddress = ""
                    }
                    val res1 = StringBuilder()
                    for (b in macBytes!!) {
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    macAddress = res1.toString()
                }
                if(!macAddress.isNullOrEmpty()) return macAddress
                else{
                    val uuid = UUID.randomUUID().toString()
                    return uuid
                }
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().recordException(ex)
            }
        }
        else return androidId
        FirebaseCrashlytics.getInstance().recordException(java.lang.Exception("Cannot determine Device uniqueness!!"))
        return null
    }
    /**
     * callback method triggered when
     * email is entered in dialog
     */
    override fun onFinishEditDialog(email: String?) {
        viewModel.changePassword(email)
    }

    override fun proceedWithAction() {
        viewModel.dialogPositiveButtonAction.postValue(Event(true))
    }
}