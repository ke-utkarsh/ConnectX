package ymsli.com.ea1h.services

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 20, 2020
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * UploadTripsWorkManager : This WorkManager is responsible for uploading trips in
 *  user device to the server
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.swagger.client.models.TripHistory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.base.BaseWorkManager
import ymsli.com.ea1h.di.component.JobServiceComponent
import ymsli.com.ea1h.utils.common.CryptoHandler
import ymsli.com.ea1h.utils.common.Utils
import java.lang.Exception
import java.net.HttpURLConnection
import java.util.*

class UploadTripsWorkManager(private val context: Context, workerParameters: WorkerParameters):
    BaseWorkManager(context,workerParameters) {

    private var isSyncSuccessful: Boolean = false

    override fun inject(jobServiceComponent: JobServiceComponent) {
        jobServiceComponent.inject(this)
    }


    override fun doWork(): Result {
        init()
        return try{
            if(isNetworkConnected()){
                syncTripsWithServer() //start syncing trips with server
            }
            Result.success()
        }
        catch (throwable: Throwable){
            Result.success()
        }
    }

    /** responsible to push trips in device to the server */
    private fun syncTripsWithServer() {
        //GlobalScope.launch(Dispatchers.IO) {
            val trips = eA1HRepository.getUnsyncedTrips()
            if (trips.isNotEmpty()) {
                for (trip in trips) {
                    if(trip.startAddress.isNullOrBlank()){
                        trip.startAddress = getAddress(trip.startLat,trip.startLon)
                    }
                    if(trip.endAddress.isNullOrBlank()){
                        trip.endAddress = getAddress(trip.endLat?:trip.potentialLastLatitude,trip.endLon?:trip.potentialLastLongitude)
                    }
                    val tripEndTime = trip.endTime?:trip.potentialEndTime
                    if(checkShallUpload(tripEndTime)) {
                        val tripHis = TripHistory(
                            tripId = trip.tripId,
                            averageSpeed = trip.averageSpeed?.toDouble(),
                            battery = trip.battery?.toDouble(),
                            bikeId = trip.bikeId?.toLong(),
                            breakCount = trip.breakCount?.toLong(),
                            chassisNumber = trip.chassisNumber,
                            distanceCovered = trip.distanceCovered?.toDouble(),
                            endAddress = trip.endAddress,
                            endLat = trip.endLat ?: trip.potentialLastLatitude,
                            endLon = trip.endLon ?: trip.potentialLastLongitude,
                            endTime = tripEndTime,
                            imei = CryptoHandler.encrypt(eA1HRepository.getAndroidIdFromSharedPref()),
                            startAddress = trip.startAddress,
                            startLat = trip.startLat,
                            startLon = trip.startLon,
                            startTime = trip.startTime,
                            userId = trip.userId,
                            updatedOn = trip.endTime
                        )
                        try {
                            compositeDisposable.add(
                            eA1HRepository.syncTripsWithServer(arrayOf(tripHis))
                                .subscribeOn(schedulerProvider.io())
                                .subscribe({
                                    if (it == HttpURLConnection.HTTP_OK) {
                                        eA1HRepository.updateUnsyncedTrip(trip.tripId)
                                        isSyncSuccessful = true
                                    }
                                },{
                                    Log.d("Trips sync","Error")
                                }))
                            /* change isSynced parameter in room */

                        } catch (ex: Exception) {
                            FirebaseCrashlytics.getInstance().recordException(ex)
                        }
                    }
                }
            }
            eA1HRepository.removeExtraTrips() //clears trips later than 20th trip for particular bikes
            removeExtraLatLong()
        //}
    }

    /**
     * removes latitudes and longitudes if there
     * are more than 20k entries in the table
     * This will ensure app from using more memory
     * and keep it smooth and running.
     */
    private fun removeExtraLatLong(){
        val tripDataList = eA1HRepository.getAllValues()
        if(tripDataList.size>50000){
            eA1HRepository.deleteSyncedLatLongs()
        }
    }

    /**
     * checks if trip being uploaded is older than 2 minutes or threshold value
     */
    private fun checkShallUpload(tripEndTime: Long?): Boolean{
        if(tripEndTime==null) return true
        val currentTime = Utils.getTimeInMilliSec()
        val timeDiff = currentTime - tripEndTime
        var timeThreshold = eA1HRepository.getContentValue(4).toLong()
        if(timeThreshold==null || timeThreshold==0L) timeThreshold = 120
        if(timeDiff>(timeThreshold*1000)) return true
        return false
    }

    private fun getAddress(lat: Double?,lon: Double?): String?{
        if(lat  ==null || lon == null) return null
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            //possible exception if internet is not there
            val addresses = geocoder.getFromLocation(lat,
                lon,1)
            return addresses!![0].getAddressLine(0)
        }
        catch (ex: Exception) {
            return null
        }
    }
}