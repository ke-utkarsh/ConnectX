package ymsli.com.ea1h.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.DeviceUpdateRequestModel
import org.json.JSONObject
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.di.component.DaggerServiceComponent
import ymsli.com.ea1h.di.component.ServiceComponent
import ymsli.com.ea1h.di.module.ServiceModule
import ymsli.com.ea1h.utils.common.Constants.ALERT_KEY
import ymsli.com.ea1h.utils.common.Constants.ALERT_TYPE
import ymsli.com.ea1h.utils.common.Constants.CHECK_VERSION
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_API_NAME
import ymsli.com.ea1h.utils.common.Constants.LOGOUT_RESTRICT
import ymsli.com.ea1h.utils.common.Constants.MESSAGE_KEY
import ymsli.com.ea1h.utils.common.Constants.MIN_STABLE_VERSION
import ymsli.com.ea1h.utils.common.Constants.NETWORK_RESTRICT
import ymsli.com.ea1h.utils.common.Constants.NOTIFICATION_DATA_KEY
import ymsli.com.ea1h.utils.common.Constants.SILENT_KEY
import ymsli.com.ea1h.utils.common.Constants.TARGET_VERSION
import ymsli.com.ea1h.utils.common.Constants.TITLE_KEY
import ymsli.com.ea1h.utils.common.CryptoHandler
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Inject

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author  Singh (VE00YM023)
 * @date   JUL 22, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * FCMService : triggered using FCM and responsible for notification in the device
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

class FCMService : FirebaseMessagingService() {

    @Inject lateinit var eA1HRepository: EA1HRepository
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var compositeDisposable: CompositeDisposable

    private val PRIMARY_NOTIFICATION_CHANNEL_ID = "primary_notification_channel_12"
    private var notificationManager: NotificationManager? = null
    private val NOTIFICATION_ID = 1

    override fun onCreate() {
        injectDependencies(buildServiceComponent())
    }

    private fun buildServiceComponent() =
        DaggerServiceComponent
            .builder()
            .applicationComponent((application as EA1HApplication).applicationComponent)
            .serviceModule(ServiceModule(this))
            .build()

    fun injectDependencies(sc: ServiceComponent) = sc.inject(this)

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised.
     *
     * @author VE00YM023
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "------- NEW FCM TOKEN --------------")
        Log.d(TAG, token)
        updateFCMTokenOnServer(token)
    }

    /** Called when notification is received by the device. */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.i(TAG, "Notification Received")
        val data = remoteMessage?.data?.get(NOTIFICATION_DATA_KEY) ?: return
        try {
            val apiName = JSONObject(data).get(JSON_KEY_API_NAME)
            if(PROJECT_DESCRIPTION == apiName) {
                compositeDisposable.addAll(eA1HRepository.getMiscData()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        if (it != null && it.isNotEmpty()) {
                            eA1HRepository.storeMiscData(it.toTypedArray())
                        }
                    }, { Log.d(TAG, MSG_API_CALL_FAILED) })
                )
            }
            else if(RESTRICT_USER == apiName){
                val shallLogout = JSONObject(data).get(LOGOUT_RESTRICT) as Boolean
                val restrictNetwork = JSONObject(data).get(NETWORK_RESTRICT) as Boolean
                val checkVersion = JSONObject(data).get(CHECK_VERSION) as Boolean
                val minStableVersion = JSONObject(data).get(MIN_STABLE_VERSION) as String
                restrictUser(shallLogout,restrictNetwork,checkVersion,minStableVersion)
            }

            else if(RESTRICT_APP_VERSION == apiName){
                val type = JSONObject(data).get(ALERT_TYPE) as String //determine type of notification
                if(type.equals(ALERT_KEY,true)){ // check if type is alert
                    val targetVersion: String = (JSONObject(data).get(TARGET_VERSION) as String)
                    if(BuildConfig.VERSION_NAME.toDouble() == targetVersion.toDouble()){
                        val title = JSONObject(data).get(TITLE_KEY) as String
                        val message = JSONObject(data).get(MESSAGE_KEY) as String
                        createNotificationChannel()
                        val notifBuilder = getNotificationBuilder(message,title)
                        notificationManager?.notify(NOTIFICATION_ID, notifBuilder!!.build())
                    }
                }
                else if(type.equals(SILENT_KEY,true)){
                    val shallLogout = JSONObject(data).get(LOGOUT_RESTRICT) as Boolean
                    val restrictNetwork = JSONObject(data).get(NETWORK_RESTRICT) as Boolean
                    val targetVersion: String = (JSONObject(data).get(TARGET_VERSION) as String)
                    restrictApp(shallLogout,restrictNetwork,targetVersion)
                }
            }
        }
        catch(cause: Exception){ Log.d(TAG, MSG_API_CALL_FAILED) }
    }

    /**
     * Updates the FCM Token on the server, if user is logged in.
     * @author VE00YM023
     */
    private fun updateFCMTokenOnServer(token: String){
        val isLoggedIn = eA1HRepository.getLoggedInStatus()
        val androidId = eA1HRepository.getAndroidIdFromSharedPref()
        if(isLoggedIn && androidId != null){
            Log.d(TAG, "------- UPDATING FCM TOKEN ON SERVER --------------")
            val request = DeviceUpdateRequestModel(token, CryptoHandler.encrypt(androidId))
            eA1HRepository.updateFCMToken(request)
        }
    }

    private fun restrictUser(shallLogout: Boolean, restrictNetwork: Boolean, checkVersion: Boolean, minStableVersion: String){
        if(checkVersion){
            // execute only if app version is less than minstableversion
            if(BuildConfig.VERSION_NAME.toDouble() < minStableVersion.toDouble()){
                //do logout
                eA1HRepository.setHardLogout(shallLogout)
                // do network restrict
                eA1HRepository.setRestrictNetworkCalls(restrictNetwork)
            }
        }
        else{
            // execute irrespective of the app version
            //do logout
            eA1HRepository.setHardLogout(shallLogout)
        }
    }

    private fun restrictApp(shallLogout: Boolean, restrictNetwork: Boolean, targetVersion: String){
        if(BuildConfig.VERSION_NAME.toDouble() == targetVersion.toDouble()){
            //do logout
            eA1HRepository.setHardLogout(shallLogout)
            // do network restrict
            eA1HRepository.setRestrictNetworkCalls(restrictNetwork)
        }
    }

    private fun getNotificationBuilder(message: String,title: String): NotificationCompat.Builder? {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        return NotificationCompat.Builder(this, PRIMARY_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_connect_round)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentText(message)
            .setSound(soundUri)
            .setAutoCancel(true)
    }

    /**
     * creates notification channel here for devices have Oreo or later
     */
    fun createNotificationChannel() {
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create notification channel here
            val notificationChannel = NotificationChannel(
                PRIMARY_NOTIFICATION_CHANNEL_ID,
                "Notification First", NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.enableLights(true)
            notificationChannel.description = "Notification from India Yamaha Motors"
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val TAG = "FCMService"
        private const val PROJECT_DESCRIPTION = "projectDescription"
        private const val RESTRICT_USER = "restrictUser"
        private const val RESTRICT_APP_VERSION = "versionSpecific"
        private const val MSG_API_CALL_FAILED = "MISC Data API Call failed"
    }
}