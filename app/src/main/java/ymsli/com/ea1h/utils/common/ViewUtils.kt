package ymsli.com.ea1h.utils.common

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    20/02/2020 12:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ViewUtils : This class contains utilities related specifically to UI elements.
 *             Ex. (Different types of dialog boxes etc.)
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.database.EA1HSharedPreferences
import ymsli.com.ea1h.database.entity.AccelerometerEntity
import ymsli.com.ea1h.database.entity.GyroEntity
import ymsli.com.ea1h.database.entity.LatLongEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.services.ECUParameters
import ymsli.com.ea1h.services.ZipUploadService
import ymsli.com.ea1h.utils.common.Constants.ERROR_UNSUPPORTED_OPERATION
import ymsli.com.ea1h.services.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit


object ViewUtils {

    private const val EMPTY_STRING = ""
    private const val ZIP_UPLOAD_SERVICE_JOB_ID = 56
    private const val TRIP_UPLOAD_SERVICE_JOB_ID = 59
    private const val TRIP_UPLOAD_UNSYNCED_FILES_SERVICE_JOB_ID = 61
    private const val TAG_ACCEL_SENSOR = "AccelSensorvalues"
    private const val TAG_GYRO_SENSOR = "GyroSensorvalues"

    /**
     * InputFilter implementation to disallow any space character input.
     * this filter can be used by any input to disallow any space char as input.
     *
     * @author VE00YM023
     */
    val spaceFilter = object : InputFilter {
        override fun filter(source: CharSequence, start: Int, end: Int,
                            dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            for (i in start until end) {
                if (Character.isSpaceChar(source[i])) return EMPTY_STRING
            }
            return null
        }
    }

    /**
     * Returns Float counter part of the given DP value.
     * @author VE00YM023
     */
    fun dpToFloat(context: Context, dp: Float): Float{
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics)
    }


    /**
     * Returns a configured alert dialog which can be used
     * to show some action message to the user.
     * this dialog only performs single operation, which is executed
     * on the click of OK button.
     *
     * @param context required to show dialog
     * @param message message to be shown in confirmation dialog
     * @param onConfirm listener to be executed when user clicks the OK button
     *
     * @author VE00YM023
     */
    fun getSingleActionConfirmationDialog(context: Context,
                              message: String,
                              callback: () -> Unit): AlertDialog{
        var onCancel   = DialogInterface.OnClickListener { _, _ -> }
        var onConfirm  = DialogInterface.OnClickListener { _, _ -> callback()}

        return getConfirmationDialog(context, R.style.AlertDialogTheme,
            message, onConfirm, onCancel)
    }

    /**
     * Returns a configured alert dialog which can be used
     * to show some action message to the user, with both 'OK' and 'Cancel'
     * button actions.
     *
     * @param context required to show dialog
     * @param message message to be shown in confirmation dialog
     * @param onCancel listener to be executed when user clicks the Cancel button
     * @param onConfirm listener to be executed when user clicks the OK button
     *
     * @author VE00YM023
     */
    fun getDoubleActionConfirmationDialog(context: Context,
                                          message: String,
                                          onCancel: () -> Unit,
                                          onConfirm: () -> Unit): AlertDialog{
        val onCancel   = DialogInterface.OnClickListener { _, _ -> onCancel() }
        val onConfirm  = DialogInterface.OnClickListener { _, _ -> onConfirm() }

        return getConfirmationDialog(context, R.style.AlertDialogTheme,
            message, onConfirm, onCancel)
    }


    /**
     * Returns a configured alert dialog which can be used
     * to show some notification to the user.
     * this dialog has only one button with text 'OK'.
     *
     * @author VE00YM023
     */
    fun getNotificationDialog(
        context: Context, theme: Int, message: String,
        callback: () -> Unit
    ): AlertDialog {
        val onConfirm  = DialogInterface.OnClickListener { _, _ -> callback()}
        var alterDialogBuilder = AlertDialog.Builder(context, theme)
        alterDialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.ACTION_OK), onConfirm)
        return alterDialogBuilder.create()
    }

    /**
     * Returns a configured alert dialog which can be used
     * to show some action message to the user.
     * this dialog has two action buttons, 'OK' and 'Cancel' respectively.
     *
     * @author VE00YM023
     */
    fun getConfirmationDialog(
        context: Context, theme: Int, message: String,
        onConfirm: DialogInterface.OnClickListener,
        onCancel: DialogInterface.OnClickListener
    ): AlertDialog {

        var alterDialogBuilder = AlertDialog.Builder(context, theme)
        alterDialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.ACTION_OK), onConfirm)
            .setNegativeButton(context.getString(R.string.ACTION_CANCEL), onCancel)
        return alterDialogBuilder.create()
    }

    /**
     * responsible for adding sensor data to the active trip
     * This data is being stored as a comma separated
     * string in the room database
     */
    fun addSensorData(
        lastExecution: Long,
        eA1HRepository: EA1HRepository,
        event: SensorEvent
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            val diff = (Calendar.getInstance().timeInMillis - lastExecution) / 1000
            val tripList = eA1HRepository.getOnGoingTrip()
            if (tripList.size > 0) {
                val trip = tripList[0]
                if (diff >= 2) {//This is responsible for a minimum period of 2 seconds for capturing sensor data
                    val df =
                        DecimalFormat("#.######") //Need to format sensor response upto 6 decimal places
                    //If the accelerometer sensor responds
                    if (event.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]
                        Log.d(TAG_ACCEL_SENSOR, "${df.format(x)},${df.format(y)},${df.format(z)}")
                        val sensorTime = Utils.getTimeInDAPIoTFormat()
                        //update the trip accelerometer values in the room
                        //eA1HRepository.updateOnGoingTripWithAccelerometerData(trip.tripId!!, trip.accelerometerReadings!!,trip.accelerometerReadingTimings!!)
                        val accelerometerEntity = AccelerometerEntity(
                            x = df.format(x),
                            y = df.format(y),
                            z = df.format(z),
                            sensorTime = sensorTime,
                            isFileCreated = false,
                            tripId = trip.tripId
                        )
                        eA1HRepository.insertNewAccel(accelerometerEntity)
                    }

                    //If the gyrometer sensor responds
                    else if (event.sensor?.type == Sensor.TYPE_GYROSCOPE) {
                        val x: Float = event.values[0]
                        val y: Float = event.values[1]
                        val z: Float = event.values[2]
                        Log.d(TAG_GYRO_SENSOR, "${df.format(x)},${df.format(y)},${df.format(z)}")
                        val sensorTime = Utils.getTimeInDAPIoTFormat()
                        //insert the trip gyrometer values in the room
                        //eA1HRepository.updateOnGoingTripWithGyrometerData(trip.tripId!!, trip.gyroReadings!!,trip.gyrometerReadingTimings!!)
                        val gyroEntity = GyroEntity(
                            x = df.format(x),
                            y = df.format(y),
                            z = df.format(z),
                            sensorTime = sensorTime,
                            isFileCreated = false,
                            tripId = trip.tripId
                        )
                        eA1HRepository.insertNewGyro(gyroEntity)
                    }
                    //below paramter is being returned in order to maintain current sensor values recorded time
                    //device need to maintain a period of 2 seconds before storing another set of sensor data
                    //return Calendar.getInstance().timeInMillis
                }
            }}
    }

    /**
     * used for storing trip locations in room
     * Locations are stored in new trip or in
     * an existing trip.
     */
    fun storeTripLocations(eA1HRepository: EA1HRepository,latitude: Double, longtitude: Double,addressLine:String,chassisNumber: String?=null){
        val decimalFormat = DecimalFormat("#.#####") //location data is corrected upto 5 decimal places
        decimalFormat.roundingMode = RoundingMode.CEILING

        var  activeTrip = eA1HRepository.getOnGoingTrip()
        if(!activeTrip.isNotEmpty()){
            // pull older trip matching threshold criteria
            val potentialTrip = eA1HRepository.getPotentialLastTrip()
            if(potentialTrip!=null && potentialTrip.endTime!=null && potentialTrip.endLat!=null && potentialTrip.endLon!=null){
                //find time difference
                val t1 =  potentialTrip.endTime
                val t2 = Utils.getTimeInMilliSec()
                val timeThreshold = eA1HRepository.getContentValue(4).toLong()
                var distanceThreashold: Int
                try{
                    distanceThreashold = eA1HRepository.getContentValue(16).toInt()
                }
                catch (ex: java.lang.Exception){
                    distanceThreashold = 200
                }

                //find distance difference
                val distance = getDistance(latitude,longtitude,potentialTrip.endLat,potentialTrip.endLon)

                if(!(t2-t1!! > (timeThreshold*1000) || (distance>distanceThreashold))){
                    //use older trip
                    potentialTrip.isActive = true
                    potentialTrip.endTime = null
                    potentialTrip.endLat = null
                    potentialTrip.endLon = null
                    potentialTrip.endAddress = null
                    eA1HRepository.insertNewTrip(potentialTrip)
                    activeTrip = arrayOf(potentialTrip)
                    //Crashlytics.logException(java.lang.Exception("Using previous trip(below 2 minutes threshold) with ID: "+potentialTrip.tripId+ " at "+Utils.getTimeInDAPIoTFormat()))
                }
            }
        }
        if(activeTrip.size!=0){
            Log.d("Location is ","${decimalFormat.format(latitude)}, ${decimalFormat.format(longtitude)}")
            //store location in lat_long_entity
            val latLongEntity = LatLongEntity(tripId = activeTrip[0].tripId?:"${eA1HRepository.getLastConnectedBikeForService().ccuId}_${Utils.getTimeInMilliSec()}",latitude = latitude.toString(),longitude = longtitude.toString(),
                locationCaptureTime = Utils.getTimeInDAPIoTFormat(),isFileCreated = false)
            eA1HRepository.insertNewLocation(latLongEntity)

            //update potential trip end coordindates and distance in TripEntity table of SQLite
            var tempDistance = getDistance(activeTrip[0].potentialLastLatitude,activeTrip[0].potentialLastLongitude,latitude,longtitude)
            tempDistance = tempDistance + (activeTrip[0].distanceCovered?:0f)
            eA1HRepository.updatePotentialTripEndCoordinates(Utils.getTimeInMilliSec(),latitude,longtitude,tempDistance,activeTrip[0].tripId)
        }
        else{
            //Create new trip in local storage with injected locations
            val ccuId = eA1HRepository.getLastConnectedBikeForService().ccuId
            val currentTime = Utils.getTimeInMilliSec()
            val timeInDapIoTFormat = Utils.getTimeInDAPIoTFormat()
            val tripId = "${ccuId}_${currentTime}" //tripId created using CCUID and trip creation timestamp
            val newTrip = TripEntity(tripId = tripId,
                startAddress = addressLine,
                endAddress = null,
                chassisNumber = chassisNumber,
                createdOn = currentTime,
                startTime = currentTime,
                endTime = Utils.getTimeInMilliSec(),
                distanceCovered = 0f,
                averageSpeed = 0f,
                breakCount = 0,
                imei = eA1HRepository.getAndroidIdFromSharedPref(),
                startLat = decimalFormat.format(latitude).toDouble(),
                startLon = decimalFormat.format(longtitude).toDouble(),
                isActive = true,
                isSynced = false,
                userId = eA1HRepository.getUserDataFromSharedPref().userId,
                potentialLastLatitude = decimalFormat.format(latitude).toDouble(),
                potentialLastLongitude = decimalFormat.format(longtitude).toDouble())
            eA1HRepository.insertNewTrip(newTrip)

            //store location in lat_long_entity
            val latLongEntity = LatLongEntity(tripId = tripId,latitude = latitude.toString(),longitude = longtitude.toString(),
            locationCaptureTime = timeInDapIoTFormat,isFileCreated = false)
            eA1HRepository.insertNewLocation(latLongEntity)
            //Crashlytics.logException(java.lang.Exception("New trip started with ID: "+tripId+ " at "+Utils.getTimeInDAPIoTFormat()))
        }
    }

    /**
     * file upload service to sync local sensor
     * and location data to DAP Server
     */
    fun startZipUploadService(baseContext: Context){
        val componentName = ComponentName(baseContext, ZipUploadService::class.java)
        val info = JobInfo.Builder(ZIP_UPLOAD_SERVICE_JOB_ID, componentName)
        info.setMinimumLatency(1*1000)// minimum latency is 1 seconds
        info.setOverrideDeadline(60*1000) // override deadline caused by another instance of service is 60 seconds
        val scheduler = baseContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(info.build())
    }

    /**
     * file upload service to sync local sensor
     * and location data to DAP Server for unsynced
     * files. This will run periodically which is
     * passed as a parameter in method
     */
    fun startZipUploadServicePeriodically(baseContext: Context,timePeriodInMinutes: Long){
        val componentName = ComponentName(baseContext, ZipUploadService::class.java)
        val info = JobInfo.Builder(TRIP_UPLOAD_UNSYNCED_FILES_SERVICE_JOB_ID,componentName)
        info.setPeriodic(timePeriodInMinutes*60*1000)
        info.setPersisted(true)
        val scheduler = baseContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.schedule(info.build())
    }

    /**
     * sync data service which sync
     * logs to the server
     */
    fun startDataSyncService(baseContext: Context){
        val uploadTripWork: WorkRequest = OneTimeWorkRequest.Builder(UploadBTLogsWorkManager::class.java).setInitialDelay(30,TimeUnit.SECONDS).build()
        WorkManager.getInstance(baseContext).enqueue(uploadTripWork)
    }

    /**
     * sync data service which sync
     * trips  to the server
     */
    fun startTripsSyncService(baseContext: Context){
        val uploadTripWork: WorkRequest = OneTimeWorkRequest.Builder(UploadTripsWorkManager::class.java).setInitialDelay(1,TimeUnit.SECONDS).build()
        WorkManager.getInstance(baseContext).enqueue(uploadTripWork)
    }

    /**
     * generates ELock command for
     * user input
     */
    const val CHAR_R = 'R'
    const val CHAR_L = 'L'
    fun generateELockCommand(indexArray: Array<Int>): IntArray{
        val patternArray = arrayOfNulls<Char>(4)
        if (indexArray[0] == 0) patternArray[0] = CHAR_L
        else patternArray[0] = CHAR_R

        if (indexArray[1] == 0) patternArray[1] = CHAR_L
        else patternArray[1] = CHAR_R

        if (indexArray[2] == 0) patternArray[2] = CHAR_L
        else patternArray[2] = CHAR_R

        if (indexArray[3] == 0) patternArray[3] = CHAR_L
        else patternArray[3] = CHAR_R

        val finalArray = charArrayOf(patternArray[0]!!, patternArray[1]!!, patternArray[2]!!, patternArray[3]!!)

        val bytesQuery = IntArray(8)
        bytesQuery[0] = 0x43
        bytesQuery[1] = 0x33
        bytesQuery[6] = 0x00
        bytesQuery[7] = 0x00
        for (i in 0..3) {
            if (finalArray[i] == CHAR_L) bytesQuery[(i + 2)] = 0x4C
            else bytesQuery[(i + 2)] = 0x52
        }
        return bytesQuery
    }

    fun prepareMFECUCommand(answerBackCommand: String): IntArray{
        val command = answerBackCommand.split(",")
        val commandArray = intArrayOf(
            (command[0].trim()).toInt(),
            (command[1].trim()).toInt(),
            (command[2].trim()).toInt(),
            (command[3].trim()).toInt(),
            (command[4].trim()).toInt(),
            (command[5].trim()).toInt(),
            (command[6].trim()).toInt(),
            (command[7].trim()).toInt()
        )
        return commandArray
    }

    fun prepareELockPatterSetCommand(command: IntArray): ByteArray{
        try {
            val commandArray = ByteArray(8)
            commandArray[0] = command[0].toByte()
            commandArray[1] = command[1].toByte()
            commandArray[2] = command[2].toByte()
            commandArray[3] = command[3].toByte()
            commandArray[4] = command[4].toByte()
            commandArray[5] = command[5].toByte()
            commandArray[6] = command[6].toByte()
            commandArray[7] = command[7].toByte()
            return commandArray
        }
        catch (ex: Exception){
            Log.d("Error preparing command",ex.localizedMessage)
            return byteArrayOf(0,0,0,0,0,0,0,0)
        }
    }

    fun prepareMFECUCommandNonEncrypted(command: String): ByteArray{
        try {
            val command = command.split(",")
            val commandArray = ByteArray(8)
            commandArray[0] = command[0].trim().toByte()
            commandArray[1] = command[1].trim().toByte()
            commandArray[2] = command[2].trim().toByte()
            commandArray[3] = command[3].trim().toByte()
            commandArray[4] = command[4].trim().toByte()
            commandArray[5] = command[5].trim().toByte()
            commandArray[6] = command[6].trim().toByte()
            commandArray[7] = command[7].trim().toByte()
            return commandArray
        }
        catch (ex: Exception){
            Log.d("Error preparing command",ex.localizedMessage)
            return byteArrayOf(0,0,0,0,0,0,0,0)
        }
    }

    private const val MIN_TRIP_DURATION = 120
    private const val MAX_GEO_CODER_RESULTS = 1
    private const val START_POINT = "startPoint"
    private const val END_POINT = "endPoint"

    /**
     * triggered when ignition is turned off
     */
    fun completeOnGoingTrip(geocoder: Geocoder,eA1HRepository: EA1HRepository) {
        GlobalScope.launch(Dispatchers.IO) {
            val activeTrip = eA1HRepository.getOnGoingTrip()
            if (activeTrip.isNotEmpty()) {
                //Get battery voltage and brake count
                    val tripEntity = activeTrip[0]
                    try {
                        tripEntity.endTime = Utils.getTimeInMilliSec()
                        val lastLocation = eA1HRepository.getTripLastLocation(tripEntity.tripId)

                        val tempEndLat = lastLocation.latitude
                        val tempEndLong = lastLocation.longitude
                        var addresses: List<Address>?
                        var googleAddress = EA1HSharedPreferences.EMPTY_STRING
                        try {
                            //possible exception if internet is not there
                            addresses = geocoder.getFromLocation(tempEndLat.toDouble(),
                                tempEndLong.toDouble(), MAX_GEO_CODER_RESULTS
                            )
                            googleAddress = addresses!![0].getAddressLine(0)
                        }
                        catch (ex: Exception) {
                            Log.d("Logging error",ex.localizedMessage?:Constants.NA)
                        }

                        tripEntity.endAddress = googleAddress
                        tripEntity.isActive = false
                        tripEntity.imei = eA1HRepository.getAndroidIdFromSharedPref()
                        //Calculate avg speed in metre/seconds
                        val timeTravelledInSeconds =
                            TimeUnit.MILLISECONDS.toSeconds(tripEntity.endTime!! - tripEntity.startTime!!)

                        if (tripEntity.distanceCovered != null) {
                            tripEntity.averageSpeed =
                                ((tripEntity.distanceCovered!!*18) / (5*timeTravelledInSeconds))
                        }

                        tripEntity.startLat = activeTrip[0].startLat
                        tripEntity.startLon = activeTrip[0].startLon
                        tripEntity.endLat = lastLocation.latitude.toDouble()
                        tripEntity.endLon = lastLocation.longitude.toDouble()
                        tripEntity.battery = ECUParameters.getEcuParametersInstance().battery
                        tripEntity.userId = eA1HRepository.getUserDataFromSharedPref().userId
                        eA1HRepository.insertNewTrip(tripEntity)
                    }
                    catch (ex: Exception) {
                        Log.d("Logging error",ex.localizedMessage?:Constants.NA)
                    }
                eA1HRepository.resetBrakeCount()
            }
        }
    }

    /**
     * checks if GPS is enabled or not
     */
    fun checkIsGPSEnabledForForegroundService(locationManager: LocationManager): Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * calculates distance between
     * 2 coordinates
     */
    private fun getDistance(startLat: Double?, startLon: Double?, endLat: Double?,endLong: Double?): Float{
        if(startLat!=null && startLon!=null && endLat!=null && endLong!=null){
            val df = DecimalFormat("0.00")

            val loc1 = Location("loc1")
            loc1.latitude = startLat
            loc1.longitude = startLon
            val loc2 = Location("loc2")
            loc2.latitude = endLat
            loc2.longitude = endLong
            val dist =  loc1.distanceTo(loc2)
            return df.format(dist).toFloat()
        }
        return 0f
    }

    /**
     * Tries to open given link in a browser application,
     * if no application is found that can perform this action then user is notified.
     * @author VE00YM023
     */
    fun openLink(ctx: Context, link: String) {
        val intent = Intent(ACTION_VIEW, Uri.parse(link))
        if (intent.resolveActivity(ctx.packageManager) == null) {
            Toast.makeText(ctx, ERROR_UNSUPPORTED_OPERATION, Toast.LENGTH_LONG).show()
        } else ctx.startActivity(intent)
    }
}