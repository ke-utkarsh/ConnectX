package ymsli.com.ea1h.views.home.drivinghistory

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    22/2/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripHistoryBluetoothViewModel : This is the view-model of TripHistoryActivity
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
import io.swagger.client.models.TripHistoryRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.VehicleListAdapter
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.model.Pair
import ymsli.com.ea1h.model.TripHistoryFilterModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class TripHistoryBluetoothViewModel(schedulerProvider: SchedulerProvider,
                                    compositeDisposable: CompositeDisposable,
                                    networkHelper: NetworkHelper,
                                    eA1HRepository: EA1HRepository
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    val apiCallActive: MutableLiveData<Boolean> = MutableLiveData()
    var applyFilter: MutableLiveData<TripHistoryFilterModel> = MutableLiveData()
    var filterState = TripHistoryFilterModel()

    val bikeEntityMap: MutableMap<String, BikeEntity> = mutableMapOf()

    private companion object { private const val MAX_RANGE_VALUE = 100 }


    /**
     * Only retrieve trips associated with this userId,
     * trips for another user can exist in the Room, because we only remove synced data.
     */
    fun getLastTripsOfUser():LiveData<Array<TripEntity>>{
        return eA1HRepository.getAllTripsLiveByUser(getUserId()!!)
    }

    /**
     * Retrieves the list of vehicles to be displayed in the Filter Dialog.
     * BikeName of these vehicles can be used to filter the trip list.
     * @author VE00YM023
     */
    fun getVehicleListForFilter() = GlobalScope.launch(Dispatchers.IO) {
        val data = eA1HRepository.getVehiclesForFilter()
        if(filterState.vehicles == null){
            filterState.vehicles = data
        }
    }

    /**
     * Filters the trip list, using the criteria chosen by the user in Filter Dialog.
     * @author VE00YM023
     */
    fun filterTrips(trips: ArrayList<TripEntity>, filterModel: TripHistoryFilterModel): List<TripEntity>{
        var filteredList = filterByDate(trips, filterModel.date)
        filteredList = filterByDistance(filteredList, filterModel.distance)
        filteredList = filterByBreakCount(filteredList, filterModel.brakeCount)
        filteredList = filterBySpeed(filteredList, filterModel.avgSpeed)
        filteredList = filterByRegNo(filteredList, filterModel.vehicles!!.toList())
        return filteredList
    }

    /** Filters the trips list by the from date and to date */
    private fun filterByDate(trips: List<TripEntity>, date: Pair<Long, Long>) = trips.filter {
            if(it.startTime == null) true
            else if(date.from == 0L && date.to == 0L) true
            else if(date.from != 0L && date.to != 0L) {
                it.startTime!! >= date.from && it.startTime!! <= date.to
            }
            else if(date.from != 0L) it.startTime!! >= date.from
            else if(date.to != 0L) it.startTime!! <= date.to
            else true
    }

    /**
     * Filters the trips based on the 'Distance' filter item.
     * @author VE00YM023
     */
    private fun filterByDistance(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        val tripDistance = it.distanceCovered?.div(1000)
        filterByRange(tripDistance, predicate)
    }

    /**
     * Filters the trips based on the 'Break Count' filter item.
     * @author VE00YM023
     */
    private fun filterByBreakCount(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        filterByRange(it.breakCount?.toFloat(), predicate)
    }

    /**
     * Filters the trips based on the 'Average Speed' filter item.
     * @author VE00YM023
     */
    private fun filterBySpeed(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        filterByRange(it.averageSpeed, predicate)
    }

    /**
     * Filters the trip list by the registration number selected in the filter dialog.
     * as TripEntity doesn't contain the model name of Vehicle,
     * we first retrieve chassis numbers for the selected model names and then
     * we perform the comparison based on the chassis number.
     * @author VE00YM023
     */
    private fun filterByRegNo(trips: List<TripEntity>,
                              bikes: List<VehicleListAdapter.VehicleModel>): List<TripEntity>{
        val models = bikes.filter { it.selected }.map { it.regNo ?: EMPTY_STRING }.toCollection(arrayListOf())
        val chassisList = eA1HRepository.getChassisNoByRegistrationNumber(models.toList())
        return trips.filter {
            if(models.isNullOrEmpty()) true
            else when (it.chassisNumber) {
                null -> true
                else -> it.chassisNumber!! in chassisList
            }
        }
    }

    /**
     * Returns true if the given field value satisfies the range predicate.
     * @author VE00YM023
     */
    private fun filterByRange(fieldValue: Float?, predicate: Pair<Int, Int>) = when {
        fieldValue == null -> true
        predicate.from > predicate.to -> true
        predicate.to >= MAX_RANGE_VALUE -> fieldValue >= predicate.from
        predicate.from == predicate.to -> fieldValue == predicate.from.toFloat()
        else -> fieldValue >= predicate.from && fieldValue <= predicate.to
    }

    override fun onCreate() {}

    /**
     * Retrieves the list of trips from remote server and updates
     * the local storage, UI is updated when local storage is updated.
     * @author VE00YM023
     */
    fun fetchTripsFromServer() = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
                return@launch
            }
            apiCallActive.postValue(true)
            val bikeIds = eA1HRepository.getAllBikeIds()
            val lastSync = 0L//getTripListLastSync()
            val tripHistoryRequest = TripHistoryRequestDTO(bikeIds.toTypedArray(), lastSync)
            val currentGMTMillis = Utils.getCurrentMillisInGMT()
            val data = eA1HRepository.getTripList(tripHistoryRequest)
            setTripListLastSync(currentGMTMillis)
            eA1HRepository.insertNewTrip(data.toTypedArray())
            messageStringId.postValue(Resource.success(R.string.trip_sync_success))
            apiCallActive.postValue(false)
        }
        catch(cause: Exception){
            apiCallActive.postValue(false)
            messageStringId.postValue(Resource.error(R.string.something_went_wrong))
        }
    }

    fun getBikeEntityMap(){
        val bikes = eA1HRepository.fetchAllBikes()
        bikes.forEach { bike -> bikeEntityMap[bike.chasNum] = bike }
    }

}