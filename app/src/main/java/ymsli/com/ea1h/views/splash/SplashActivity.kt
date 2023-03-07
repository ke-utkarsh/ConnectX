/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM023)
 * @date    10/2/20 12:35 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SplashActivity : This activity is the launcher activity for EA1H.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splash.*
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.model.AppVersionResponse
import ymsli.com.ea1h.utils.common.Constants.FCM_TOPIC_ANDROID
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.views.entrance.EntranceActivity
import ymsli.com.ea1h.views.sync.SyncActivity

class SplashActivity : BaseActivity<SplashViewModel>() {

    companion object{
        const val UPLOAD_SERVICE_ID = 901
        private const val TRANSITION_DELAY = 2000L
    }

    override fun provideLayoutId() = R.layout.activity_splash

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setLetsStartContentStatus()
        setupClickListeners()
        if(viewModel.getSubscribedToFCMTopic()){
            //viewModel.checkAppStatus()
        }
        else { subscribeToFCMTopicAndContinue() }

        //show background app running pop-up
        if(!viewModel.getLetsStartStatus()){
            openPowerSettings(null)
        }
    }

    /**
     * Set the visibility status of 'LET'S START' button and text.
     * we only show this section when user launches the app for first time.
     * after first launch this section is never displayed again until user clears the app
     * data or re-installs the app.
     * @author VE00YM023
     */
    private fun setLetsStartContentStatus(){
        val status = viewModel.getLetsStartStatus()
        btn_start_splash.visibility = if(status) View.GONE else View.VISIBLE
        tv_splash_yamaha.visibility = if(status) View.GONE else View.VISIBLE
    }

    private fun setupClickListeners() {
        btn_start_splash.setOnClickListener {
            viewModel.setLetsStartStatus(true)
            startNextActivity()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        /*  When network connection is not available or AppVersion API
         *  call fails, then we simply move to next screen, without version check */
        viewModel.continueToNextScreen.observe(this, Observer {
            checkLetsStartButtonStatusAndContinue()
        })

        /* Observe the AppVersion response, and perform appropriate operation (show dialog, etc) */
        viewModel.appVersionApiResponse.observe(this, Observer {
            when{
                it == null -> checkLetsStartButtonStatusAndContinue()
                it.forceUpdate -> showForceUpdateDialog(it)
                it.updateAvailable -> showUpdateAvailableNotification(it)
                else -> checkLetsStartButtonStatusAndContinue()
            }
        })
    }

    /**
     * If 'LET'S START button is visible then we enable it
     * when App version API call finishes. otherwise we move the user to next activity.
     * @author VE00YM023
     */
    private fun checkLetsStartButtonStatusAndContinue(){
        if (btn_start_splash.visibility == View.VISIBLE) {
            btn_start_splash.isEnabled = true
        } else Handler().postDelayed({startNextActivity()}, TRANSITION_DELAY)
    }

    /**
     * Show the update available notification to the user,
     * and ask if user wants to update the application now.
     * if user selects 'OK' then open play store, otherwise move on.
     * @author VE00YM023
     */
    private fun showUpdateAvailableNotification(appStatus: AppVersionResponse){
        val message = appStatus.message ?: getString(R.string.message_update_available)
        ViewUtils.getDoubleActionConfirmationDialog(this, message, {
            checkLetsStartButtonStatusAndContinue()
        },{ openAppUpdateURLAndContinue(appStatus.appUpdateURL) }).show()
    }

    /**
     * Show the update available notification to the user,
     * and ask if user wants to update the application now.
     * if user selects 'OK' then open play store, otherwise move on.
     * @author VE00YM023
     */
    private fun showForceUpdateDialog(appStatus: AppVersionResponse){
        val message = appStatus.message ?: getString(R.string.message_force_update)
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ACTION_OK)) { _, _ ->
                openAppUpdateURLAndContinue(appStatus.appUpdateURL, false)
            }
            .setCancelable(false)
            .show()
    }

    /**
     * Opens the update URL in play store.
     * if play store is not available then tries to start the browser.
     * @author VE00YM023
     */
    private fun openAppUpdateURLAndContinue(url: String?, moveForward: Boolean = true){
        if(url.isNullOrEmpty()) {
            showMessage(getString(R.string.update_url_not_found))
            if(moveForward) { checkLetsStartButtonStatusAndContinue() }
            else { finish() }
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if(intent.resolveActivity(packageManager) != null){
            startActivity(intent)
            finish()
        }
        else {
            showMessage(getString(R.string.message_no_app_found))
            if(moveForward) checkLetsStartButtonStatusAndContinue()
            else finish()
        }
    }

    /**
     * Once the application initialization is done, this function can be
     * used to launch the next activity.
     * Checks if user is already logged in or not
     * @author  VE00YM023
     */
    private fun startNextActivity() {
        val intent = when(viewModel.getLoggedInStatus()){
            true -> Intent(this, SyncActivity::class.java)
            else -> Intent(this, EntranceActivity::class.java)
        }
        startActivity(intent)
        finish()
    }

    /**
     * Subscribes to the FCM Topic and then continues with the application launch.
     * @author VE00YM023
     */
    private fun subscribeToFCMTopicAndContinue(){
        FirebaseMessaging.getInstance().subscribeToTopic(FCM_TOPIC_ANDROID)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { viewModel.setSubscribedToFCMTopic(true) }
                //viewModel.checkAppStatus()
            }
    }
}