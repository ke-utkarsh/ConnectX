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
 * YourBikesListItemViewModel : This is the item view model of items in
 * bikes recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseItemViewModel
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Inject

class YourBikesListItemViewModel @Inject constructor(schedulerProvider: SchedulerProvider,
                                                     compositeDisposable: CompositeDisposable,
                                                     networkHelper: NetworkHelper,
                                                     eA1HRepository: EA1HRepository)
    : BaseItemViewModel<BikeEntity>(schedulerProvider,compositeDisposable,networkHelper,eA1HRepository){


    var chasNum: LiveData<String> = Transformations.map(data){
        it.chasNum
    }

    var make: LiveData<String> = Transformations.map(data){
        it.mk
    }

    var mod: LiveData<String> = Transformations.map(data){
        it.mod
    }

    var regNum: LiveData<String> = Transformations.map(data){
        it.regNum
    }

    var bikePic: LiveData<String> = Transformations.map(data){
        it.bdp
    }

    var bikeModelName: LiveData<String> = Transformations.map(data){
        it.bkModNm
    }

    override fun onCreate() {

    }
}