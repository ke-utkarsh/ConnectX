package ymsli.com.ea1h.views.home.drivinghistory


/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    22/7/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripHistoryDetailViewModel : This is the viewmodel for TripHistoryDetailActivity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.TripDetailRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.LatLongEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants.COMMA
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class TripHistoryDetailViewModel(schedulerProvider: SchedulerProvider,
                                 compositeDisposable: CompositeDisposable,
                                 networkHelper: NetworkHelper,
                                 eA1HRepository: EA1HRepository)
    :BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    lateinit var tripId :String
    var tripLocations : MutableLiveData<Array<LatLongEntity>> = MutableLiveData()
    var graphError: MutableLiveData<Event<Boolean>> = MutableLiveData()

    fun getBikeRegNumModel(chassisNumber: String): LiveData<BikeEntity> = eA1HRepository.getBikeRegNumModel(chassisNumber)

    override fun onCreate() {}

    fun getTripDetailFromRoom() = GlobalScope.launch(Dispatchers.IO) {
        val data = eA1HRepository.getTripEntity(tripId)
        if(data!=null){
            val locations = eA1HRepository.getLocationOfTrip(tripId)
            if(locations.isNotEmpty()) { tripLocations.postValue(locations) }
             else { fetchTripDetailFromServer() }
        }
    }

    /**
     * Retrieves the trip detail from the remove server,
     * only if the network connection is available.
     *
     * @author VE00YM023
     */
    private fun fetchTripDetailFromServer() = GlobalScope.launch(Dispatchers.IO) {
        if(!checkInternetConnection()) { return@launch }
        try{
            val request = TripDetailRequestDTO(arrayOf(tripId))
            val tripCoordinatesList = eA1HRepository.getTripDetail(request)
            val latitudes = tripCoordinatesList[0].lats?.split(COMMA)
            val longitudes = tripCoordinatesList[0].longs?.split(COMMA)
            val locationList = ArrayList<LatLongEntity>()
            if(longitudes!=null)
            for(i in longitudes.indices){
                if(!latitudes?.get(i).isNullOrBlank() && !longitudes[i].isNullOrBlank()){
                    val latLongEntity = LatLongEntity(tripId = tripId, latitude = latitudes!![i],
                        longitude = longitudes[i], locationCaptureTime = EMPTY_STRING,
                        isFileCreated = true)
                    locationList.add(latLongEntity)
                }
            }
            eA1HRepository.insertNewLocations(locationList.toTypedArray())
            tripLocations.postValue(eA1HRepository.getLocationForMapPlot(tripId))
        }
        catch(cause: Exception){
            graphError.postValue(Event(true))
        }
    }
}