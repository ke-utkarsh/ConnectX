package ymsli.com.ea1h.views.yourvehicles

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author   (VE00YM129)
 * @date    24/7/20 3:10 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * YourBikesViewModel : This is the view model for bike list activity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import ymsli.com.ea1h.views.home.HomeViewModel

class YourBikesViewModel(schedulerProvider: SchedulerProvider,
                         compositeDisposable: CompositeDisposable,
                         networkHelper: NetworkHelper,
                         eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    val apiCallActive: MutableLiveData<Boolean> = MutableLiveData()
    var lastConnectedBike: BikeEntity? = null

    override fun onCreate() {}

    fun getBikes(): LiveData<Array<BikeEntity>> = eA1HRepository.getAllBikes()

    /**
     * sets the last connected bike in room
     * when any bike is selected from RV list
     */
    fun setLastConnectedBike(chassisNumber: String) {
        GlobalScope.launch(Dispatchers.IO){
            eA1HRepository.setLastConnectedBike(chassisNumber)
        }
    }

    /**
     * Retrieves the list of trips from remote server and updates
     * the local storage, UI is updated when local storage is updated.
     * @author VE00YM023
     */
    fun fetchBikesFromServer() = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
                return@launch
            }
            apiCallActive.postValue(true)
            val currentGMTMillis = Utils.getCurrentMillisInGMT()
            val bikes = eA1HRepository.getBikeList(lastUpdatedOn = 0)//hard refresh in order to get all data again
            setBikeListLastSync(currentGMTMillis)
            eA1HRepository.removeAllBikes()// delete previous data of bikes
            eA1HRepository.insertBikeData(bikes.toTypedArray())
            messageStringIdEvent.postValue(Event(Resource.success(R.string.bike_list_sync_success)))
            apiCallActive.postValue(false)
        }
        catch(cause: Exception){
            apiCallActive.postValue(false)
            messageStringIdEvent.postValue(Event(Resource.error(R.string.something_went_wrong)))
        }
    }
}