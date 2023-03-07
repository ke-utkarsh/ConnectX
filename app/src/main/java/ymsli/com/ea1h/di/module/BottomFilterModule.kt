package ymsli.com.ea1h.di.module

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BottomFilterModule : This is the module and injects object for bottom filter module.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.FilterFragment
import ymsli.com.ea1h.model.TripHistoryFilterModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.ViewModelProviderFactory
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import ymsli.com.ea1h.views.home.drivinghistory.TripHistoryBluetoothViewModel

@Module
class BottomFilterModule(private val fragment: FilterFragment<*>) {

    @Provides
    fun provideTripHistoryBluetoothViewModel(
        schedulerProvider: SchedulerProvider, compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper, ea1hRepository: EA1HRepository
    ): TripHistoryBluetoothViewModel =
        ViewModelProviders.of(fragment.activity!!,
            ViewModelProviderFactory(TripHistoryBluetoothViewModel::class) {
                TripHistoryBluetoothViewModel(
                    schedulerProvider,
                    compositeDisposable,
                    networkHelper,
                    ea1hRepository
                )
            }).get(TripHistoryBluetoothViewModel::class.java)

    @Provides
    fun provideTripHistoryFilterModel(): TripHistoryFilterModel = TripHistoryFilterModel()

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

}