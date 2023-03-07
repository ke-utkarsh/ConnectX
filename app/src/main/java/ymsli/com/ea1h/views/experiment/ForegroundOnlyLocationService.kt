package ymsli.com.ea1h.views.experiment

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 24, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ForegroundOnlyLocationService : This is responsible for continuously storing location
 *                                  in room database. It fetches location while
 *                                   app is in background or foreground.
 *                                   Service tracks location when requested and updates Activity via binding. If Activity is
 *                                   stopped/unbinds and tracking is enabled, the service promotes itself to a foreground service to
 *                                   ensure location updates aren't interrupted.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseService
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity
import ymsli.com.ea1h.di.component.ServiceComponent
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.ViewUtils
import ymsli.com.ea1h.views.home.HomeActivity
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class ForegroundOnlyLocationService : BaseService(),SensorEventListener {
    companion object {
        private const val PACKAGE_NAME = "ymsli.com.ea1h"
        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"
        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"
        private const val EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION =
            "$PACKAGE_NAME.extra.CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION"
        private const val ERROR_MISSING_LOCATION = "Location missing in callback."
        private const val TWO_MINTS_MILLIS = 120000
        private const val MAX_LOCATION_RESULTS = 1
    }
    private var configurationChange = false
    private var serviceRunningInForeground = false
    private val localBinder = LocalBinder()
    private var lastExecution: Long = Calendar.getInstance().timeInMillis
    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var locationRequest: LocationRequest
    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    /* sensor parameters */
    private lateinit var sensorManager: SensorManager
    private var accelSensor: Sensor? = null
    private var gyroSensor: Sensor? = null

    /* MFECU parameters */
    private lateinit var ecuParameters: ECUParameters
    override fun onCreate() {
        super.onCreate()
        locationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        Log.e("Observable Log","Foreground Location Service")
        locationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.setSmallestDisplacement(5f)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!=null){
            gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        }
        ecuParameters = ECUParameters.getEcuParametersInstance()
        /* Used for receiving notifications from the FusedLocationProviderApi */
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult?.lastLocation != null) {
                    onNewLocation(locationResult.lastLocation)
                }
                else { Log.d(TAG, ERROR_MISSING_LOCATION) }
            }
        }
    }
    override fun injectDependencies(serviceComponent: ServiceComponent) = serviceComponent.inject(this)

    override fun onDestroy() {
        super.onDestroy()
        logCommand(19)
        Process.killProcess(Process.myPid())
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val cancelLocationTrackingFromNotification =
            intent?.getBooleanExtra(EXTRA_CANCEL_LOCATION_TRACKING_FROM_NOTIFICATION, false)
        if (cancelLocationTrackingFromNotification==true) {
            stopTrackingLocation()
            stopSelf()
        }
        //return START_STICKY // Tells the system to recreate the service if it's been killed by OS
        return START_REDELIVER_INTENT // Tells the system to recreate the service if it's been killed by OS
    }
    /* Activity comes into foreground and binds to service, so the service can
       become a background services. */
    override fun onBind(intent: Intent?): IBinder? {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }
    /* Activity returns to the foreground and rebinds to service, so the service
       can become a background services. */
    override fun onRebind(intent: Intent?) {
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }
    /* Activity leaves foreground, so service needs to become a foreground service
       to maintain the 'while-in-use' label. */
    override fun onUnbind(intent: Intent?): Boolean {
        if (!configurationChange && SharedPreferenceUtil.getLocationTrackingPref(this)) {
            val notification =
                generateNotification()
            startForeground(NOTIFICATION_ID, notification)
            logCommand(20)
            serviceRunningInForeground = true
        }
        return true
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }
    /* Binding to this service doesn't actually trigger onStartCommand(). That is needed to
       ensure this Service can be promoted to a foreground service, i.e., the service needs to
       be officially started. */
    fun startTrackingLocation() {
        SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        try {
            if(ecuParameters.isConnected && ecuParameters.isIgnited && checkGPS()){
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback,Looper.getMainLooper())
                if(accelSensor!=null) sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_NORMAL)
                if(gyroSensor!=null) sensorManager.registerListener(this,gyroSensor,SensorManager.SENSOR_DELAY_NORMAL)
                lastExecutionTime = Utils.getTimeInMilliSec()
            }
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        }
    }
    private fun checkGPS(): Boolean{
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return ViewUtils.checkIsGPSEnabledForForegroundService(locationManager)
    }
    /**
     * callback method triggered when app is killed in background and foreground service is active.
     */
    override fun onTaskRemoved(rootIntent: Intent?) {
        ViewUtils.completeOnGoingTrip(Geocoder(this, Locale.getDefault()),eA1HRepository)
        stopTrackingLocation()
        logCommand(19)
        stopForeground(true)
        stopSelf()
    }

    fun stopTrackingLocation() {
        try {
            sensorManager.unregisterListener(this)
            if(accelSensor!=null) sensorManager.unregisterListener(this,accelSensor)
            if(gyroSensor!=null) sensorManager.unregisterListener(this,gyroSensor)
            ecuParameters.isIgnited = false
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    ViewUtils.startZipUploadService(baseContext)
                }
            }
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, true)
        }
    }
    private var lastExecutionTime: Long? = null
    /** Update the location data in room DB */
    private fun addData(latitude: Double, longitude: Double, addressLine:String){
        if(eA1HRepository.getLastConnectedBikeForService()==null) return
        ViewUtils.storeTripLocations(eA1HRepository, latitude, longitude,
            addressLine, eA1HRepository.getLastConnectedBikeForService().chasNum)
        /* run ZipUploadService every 2 minutes */
        val currentTime = Utils.getTimeInMilliSec()
        if(lastExecutionTime!=null) {
            /* ZipUploadService will be triggered periodically for 2 minutes */
            if ((currentTime - lastExecutionTime!!) > (TWO_MINTS_MILLIS)) {
                ViewUtils.startZipUploadService(baseContext)
                lastExecutionTime = Utils.getTimeInMilliSec()
            }
        }
    }
    /**
     * When new location is captured, try to get the address with geo coder
     * and then update the Room DB locations DB.
     */
    private fun onNewLocation(location: Location) {
        currentLocation = location
        /* Notify anyone listening for broadcasts about the new location. */
        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
        /* Update notification content if running as a foreground service. */
        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(location.latitude,
                location.longitude, MAX_LOCATION_RESULTS)
            val addressLine = addresses!![0].getAddressLine(0)
            addData(currentLocation!!.latitude, currentLocation!!.longitude, addressLine)
        } catch (ex: Exception) {
            addData(currentLocation!!.latitude, currentLocation!!.longitude, EMPTY_STRING)
        }
    }
    /** Post a notification to the system notification try to keep the service running */
    private fun generateNotification(): Notification {
        var foregroundText: String = this.resources.getString(R.string.foreground_service_notif_message)
        try {
            foregroundText = eA1HRepository.getContentValue(19)
        }
        catch (ex: Exception){
            FirebaseCrashlytics.getInstance().recordException(ex)
        }
        val titleText = getString(R.string.app_name)
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, titleText,
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.setSound(null,null)
        /* Adds NotificationChannel to system. Attempting to create an
           existing notification channel with its original values performs
           no operation, so it's safe to perform the below sequence. */
        notificationManager.createNotificationChannel(notificationChannel)
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(foregroundText)
            .setBigContentTitle(titleText)
        val notificationCompatBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
        /* below intent helps to launch respective activity when user taps notification */
        val mainIntent = Intent(this, HomeActivity::class.java)
     /*   val homeActivityPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)*/
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_ONE_SHOT)
        }
        return notificationCompatBuilder
            .setStyle(bigTextStyle)
            .setContentTitle(titleText)
            .setContentIntent(pendingIntent)
            .setContentText(foregroundText)
            .setSmallIcon(R.mipmap.ic_connect_round)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setOngoing(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }
    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: ForegroundOnlyLocationService
            get() = this@ForegroundOnlyLocationService
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent?) {
        if(event!=null) {
            ViewUtils.addSensorData(lastExecution,eA1HRepository,event)
            val lastExec = Calendar.getInstance().timeInMillis
            lastExecution = lastExec
        }
    }
    fun ignitionHandler(isIgnited: Boolean) {
        try {
            if(isIgnited){
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback,Looper.getMainLooper())
                if(accelSensor!=null) sensorManager.registerListener(this,accelSensor,SensorManager.SENSOR_DELAY_NORMAL)
                if(gyroSensor!=null) sensorManager.registerListener(this,gyroSensor,SensorManager.SENSOR_DELAY_NORMAL)
                lastExecutionTime = Utils.getTimeInMilliSec()
            }
            else{
                try {
                    ecuParameters.isIgnited = false
                    sensorManager.unregisterListener(this)
                    if(accelSensor!=null) sensorManager.unregisterListener(this,accelSensor)
                    if(gyroSensor!=null) sensorManager.unregisterListener(this,gyroSensor)
                    val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    val geoCoder = Geocoder(this, Locale.getDefault())
                    ViewUtils.completeOnGoingTrip(geocoder = geoCoder,eA1HRepository = eA1HRepository)
                    removeTask.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                        }
                    }
                    SharedPreferenceUtil.saveLocationTrackingPref(this, false)
                } catch (unlikely: SecurityException) {
                    SharedPreferenceUtil.saveLocationTrackingPref(this, true)
                }
            }
        } catch (unlikely: SecurityException) {
            SharedPreferenceUtil.saveLocationTrackingPref(this, false)
        }
    }
    fun connectionHandler(isConnected: Boolean) {
        if(!isConnected){
            try {
                sensorManager.unregisterListener(this)
                if(accelSensor!=null) sensorManager.unregisterListener(this,accelSensor)
                if(gyroSensor!=null) sensorManager.unregisterListener(this,gyroSensor)
                val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                removeTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                    }
                }
                SharedPreferenceUtil.saveLocationTrackingPref(this, false)
            } catch (unlikely: SecurityException) {
                SharedPreferenceUtil.saveLocationTrackingPref(this, true)
            }
        }
    }
    private fun logCommand(cmdType: Int) {
        try {
            val ob2: BTCommandsLogEntity
            ob2 = BTCommandsLogEntity( Date().time, cmdType, Constants.NA, eA1HRepository.getAndroidIdFromSharedPref(), true, true, Constants.NA)
            eA1HRepository.writeBluetoothCommandInRoom(ob2)
        } catch (ex: Exception) {
        }
    }
}
private const val TAG = "ForegroundOnlyLocationService"
/*
 * The desired interval for location updates. Inexact. Updates may be
 * more or less frequent.
 */
private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = TimeUnit.SECONDS.toMillis(3)
/*
 * The fastest rate for active location updates. Updates will never be
 * more frequent than this value.
 */
private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS
private const val NOTIFICATION_ID = 12345678
private const val NOTIFICATION_CHANNEL_ID = "while_in_use_channel_01"