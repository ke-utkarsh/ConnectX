package ymsli.com.ea1h.views.splash

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM023)
 * @date    25/9/20 12:35 PM
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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.firebase.messaging.FirebaseMessaging
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseActivity
import ymsli.com.ea1h.di.component.ActivityComponent
import ymsli.com.ea1h.model.AppVersionResponse
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.views.ackdialogs.ErrorAckDialog
import ymsli.com.ea1h.views.ackdialogs.ForceAppUpdateDialog
import ymsli.com.ea1h.views.ackdialogs.OptionalAppUpdateDialog
import ymsli.com.ea1h.views.entrance.EntranceActivity
import ymsli.com.ea1h.views.sync.SyncActivity

class LaunchActivity: BaseActivity<LaunchViewModel>(), ForceAppUpdateDialog.ForceUpdateListener,
    OptionalAppUpdateDialog.OptionalAppUpdateListener {

    companion object{
        const val UPLOAD_SERVICE_ID = 901
        private const val TRANSITION_DELAY = 1000L
    }

    override fun provideLayoutId(): Int = R.layout.launch_activity

    override fun injectDependencies(activityComponent: ActivityComponent) = activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if(viewModel.getSubscribedToFCMTopic()){
            viewModel.checkAppStatus()
        }
        else { subscribeToFCMTopicAndContinue() }

        if(viewModel.getLetsStartStatus()){

        }
    }

    /**
     * Once the application initialization is done, this function can be
     * used to launch the next activity.
     * Checks if user is already logged in or not
     * @author  VE00YM023
     */
    private fun startNextActivity() {
        val status = viewModel.getLetsStartStatus()
        val nextActivityIntent: Intent
        if(!status){
            // redirect user to Lets Start screen
            nextActivityIntent = Intent(this,SplashActivity::class.java)
        }
        else{
            nextActivityIntent = when(viewModel.getLoggedInStatus()){
                true -> Intent(this, SyncActivity::class.java)
                else -> Intent(this, EntranceActivity::class.java)
            }
        }
        startActivity(nextActivityIntent)
        finish()
    }

    /**
     * Subscribes to the FCM Topic and then continues with the application launch.
     * @author VE00YM023
     */
    private fun subscribeToFCMTopicAndContinue(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FCM_TOPIC_ANDROID)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) { viewModel.setSubscribedToFCMTopic(true) }
                viewModel.checkAppStatus()
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
            if(!it.forceUpdate){
                viewModel.alterNetworkRestriction(false)
            }
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
        Handler().postDelayed({startNextActivity()}, TRANSITION_DELAY)
    }

    private lateinit var appVersionResponse: AppVersionResponse
    /**
     * Show the update available notification to the user,
     * and ask if user wants to update the application now.
     * if user selects 'OK' then open play store, otherwise move on.
     * @author VE00YM023
     */
    private fun showForceUpdateDialog(appStatus: AppVersionResponse){
        this.appVersionResponse = appStatus
        val message: String = appStatus.message ?: getString(R.string.message_force_update)
        val forceUpdate = ForceAppUpdateDialog(message)
        forceUpdate.isCancelable = false
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        forceUpdate.show(ft,null)
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
     * Show the update available notification to the user,
     * and ask if user wants to update the application now.
     * if user selects 'OK' then open play store, otherwise move on.
     * @author VE00YM023
     */
    private fun showUpdateAvailableNotification(appStatus: AppVersionResponse){
        this.appVersionResponse = appStatus
        val message = appStatus.message ?: getString(R.string.message_update_available)
        val optionalUpdate = OptionalAppUpdateDialog(message)
        optionalUpdate.isCancelable = false
        val ft = supportFragmentManager.beginTransaction().addToBackStack(null)
        optionalUpdate.show(ft,null)
    }

    override fun performForceUpdate() {
        openAppUpdateURLAndContinue(appVersionResponse.appUpdateURL, false)
    }

    override fun proceedUpdate() {
        openAppUpdateURLAndContinue(appVersionResponse.appUpdateURL)
    }

    override fun postponeUpdate() {
        checkLetsStartButtonStatusAndContinue()
    }
}