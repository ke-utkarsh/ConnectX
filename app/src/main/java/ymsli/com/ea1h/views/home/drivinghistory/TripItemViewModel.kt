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
 * TripItemViewModel : This is the item view model for items in trip history list
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseItemViewModel
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Inject

class TripItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    eA1HRepository: EA1HRepository
): BaseItemViewModel<TripEntity>(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository){
    override fun onCreate() {}

    /** Mappings for fields required by the list item UI */
    val source: LiveData<String> = Transformations.map(data) { it.startAddress.toString() }
    val destination : LiveData<String> = Transformations.map(data){ it.endAddress.toString() }
    val startDateTime: LiveData<Long> = Transformations.map(data) { it.startTime }
    val isActiveTrip: LiveData<Boolean> = Transformations.map(data) { it.isActive }
    val endLat: LiveData<Double> = Transformations.map(data) { it.endLat }
    val endLon: LiveData<Double> = Transformations.map(data) { it.endLon }
    val trip: MutableLiveData<TripEntity> = data
    val startLon: LiveData<Double> = Transformations.map(data) { it.startLon }
    val bikeChassisNumber: LiveData<String> = Transformations.map(data) { it.chassisNumber }

    /**
     * updates the address line of source address
     * in room db
     */
    fun updateTripSourceAddress(sourceAddress: String): Single<Int>{
        return Single.create {
            val emitter = it
            if(data.value?.tripId!=null) {
                eA1HRepository.updateTripSourceAddress(
                    data.value?.tripId!!,
                    sourceAddress
                )
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        emitter.onSuccess(it)
                    },{
                        emitter.onSuccess(0)
                    })
            }
        }
    }

    /**
     * updates the address line of destination address
     * in room db
     */
    fun updateTripDestinationAddress(destinationAddress: String): Single<Int>{
        return Single.create {
            val emitter = it
            if(data.value?.tripId!=null) {
                eA1HRepository.updateTripDestinationAddress(data.value?.tripId!!,
                    destinationAddress).subscribeOn(schedulerProvider.io()).subscribe({
                    emitter.onSuccess(it)
                },{
                    emitter.onSuccess(0)
                })

                /*compositeDisposable.addAll(
                    eA1HRepository.updateTripDestinationAddress(
                        data.value?.tripId!!,
                        destinationAddress
                    )
                        .subscribeOn(schedulerProvider.io())
                        .subscribe({
                            emitter.onSuccess(it)
                        },{
                            emitter.onSuccess(0)
                        })
                )*/
            }
        }
    }

    fun getAddress(geocoder: Geocoder): Single<String>{
        return Single.create{
            val address = geocoder.getFromLocation(trip.value?.endLat!!,trip.value?.endLon!!,1)
            it.onSuccess(address!![0].getAddressLine(0))
        }
    }

    fun getSourceAddress(geocoder: Geocoder):Single<String>{
        return Single.create{
            val address = geocoder.getFromLocation(trip.value?.startLat!!,trip.value?.startLon!!,1)
            it.onSuccess(address!![0].getAddressLine(0))
        }
    }

    fun checkInternet(): Boolean = checkInternetConnection()
}