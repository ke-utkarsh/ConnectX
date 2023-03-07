package ymsli.com.ea1h.base

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseItemViewModel : This abstract class is the base item view model of all the
 *                  item view model of recycler view, contains common code to all
 *                   view models of item of recycler view
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

abstract class BaseItemViewModel<T : Any>(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    eA1HRepository: EA1HRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper,eA1HRepository) {

    /* contains data to be populated in recycler view */
    val data: MutableLiveData<T> = MutableLiveData()

    fun onManualCleared() = onCleared()

    /** updates data populated in recycler view list */
    fun updateData(data: T) {
        this.data.postValue(data)
    }
}