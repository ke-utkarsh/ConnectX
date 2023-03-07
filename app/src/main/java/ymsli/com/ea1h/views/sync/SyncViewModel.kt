package ymsli.com.ea1h.views.sync

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    21/08/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SyncViewModel : This is view model for SyncActivity.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.TripHistoryRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import java.lang.Exception

class SyncViewModel(schedulerProvider: SchedulerProvider,
                    compositeDisposable: CompositeDisposable,
                    networkHelper: NetworkHelper,
                    repo: EA1HRepository)
        :BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, repo) {

    val noNetwork: MutableLiveData<Boolean> = MutableLiveData()
    val apiCallFailed: MutableLiveData<Boolean> = MutableLiveData()
    val syncActive: MutableLiveData<Boolean> = MutableLiveData()
    val syncFinished: MutableLiveData<Boolean> = MutableLiveData()
    private var bikeListSynced = false
    private var bikeList = listOf<BikeEntity>()
    private var tripList = listOf<TripEntity>()
    override fun onCreate() {}


    /**
     * Performs the sync operation by calling projectDetails, BikeList and TripList
     * API. it performs the following steps.
     *
     * 1. Only continue if network is available, otherwise post appropriate error
     * 2. Call project/details API and update local DB if required
     * 2. Only Sync BikeList if its not synced already (Sync may fail at TripList, in this case
     *                                                  we don't have to sync the bike list again)
     * 3. Update the last sync time of all the API's
     * 4. Once BikeList and TripList API's are synced then update the local storage
     *
     * @author VE00YM023
     */
    fun startSync() = GlobalScope.launch(Dispatchers.IO) {
        if(!checkInternetConnection()) {
            if(!getAppInitStatus()){ /*messageStringId.postValue(Resource.error(R.string.network_connection_error))*/ }
            noNetwork.postValue(true)
            return@launch
        }
        try{
            syncActive.postValue(true)
            syncProjectDetails()
            /* If bike list is already synced then we don't call it again */
            if(!bikeListSynced){
                val lastSync = getBikeListLastSync()
                val currentGMTMillis = Utils.getCurrentMillisInGMT()
                bikeList = eA1HRepository.getBikeList(lastUpdatedOn = lastSync)
                bikeListSynced = true
                setBikeListLastSync(currentGMTMillis)
            }
            val lastSync = getTripListLastSync()
            val bikeIds = bikeList.asSequence().map{b -> b.bikeId}.toCollection(arrayListOf())
            val tripHistoryRequest = TripHistoryRequestDTO(bikeIds.toTypedArray(), lastSync)
            val currentGMTMillis = Utils.getCurrentMillisInGMT()
            tripList = eA1HRepository.getTripList(tripHistoryRequest)
            setTripListLastSync(currentGMTMillis)
            updateLocalStorage()
        }
        catch(cause: Exception){
            syncActive.postValue(false)
            apiCallFailed.postValue(true)
            messageStringId.postValue(Resource.error(R.string.something_went_wrong))
        }
        finally {
            syncFinished.postValue(true)
        }
    }

    /**
     * Once both API calls finish we update the local storage with the
     * received data.
     * @author VE00YM023
     */
    private fun updateLocalStorage() = GlobalScope.launch(Dispatchers.IO) {
        eA1HRepository.insertBikeData(bikeList.toTypedArray())

        if(bikeList.size==1 && Constants.SCOOTER_STRING.equals(bikeList.get(0).vehType)){
            setIfScooter(true)
        }
        for(t in tripList){
            t.isSynced = true
        }
        eA1HRepository.insertNewTrip(tripList.toTypedArray())
       /* if(bikeList.size>0){
            eA1HRepository.setLastConnectedBike(bikeList.get(0).chasNum)
        }*/
        //syncFinished.postValue(true)
    }

    /**
     * Call project/details API and updates the local db if required.
     * if received descriptionId is already present in local database
     * then new data will replace the old data.
     * @author Balraj VE00YM023
     */
    private fun syncProjectDetails(){
        val lastSync = getProjectDetailsLastSync()
        val currentUTC = Utils.getCurrentUTCForProjectDetails()
        val apiResult = eA1HRepository.getProjectDetails(lastSync)
        if(!apiResult.isNullOrEmpty()){
            eA1HRepository.storeMiscData(apiResult.toTypedArray())
        }
        setProjectDetailsLastSync(currentUTC)
    }
}
