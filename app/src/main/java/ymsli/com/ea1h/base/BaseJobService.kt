package ymsli.com.ea1h.base

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   July 14, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseJobService : This abstract class is the base JobService. A JobService is responsible
 *  for running certain tasks in background and take care of multiple conditions like
 *  battery, network state, time intervals, etc
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.app.job.JobService
import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.di.component.DaggerJobServiceComponent
import ymsli.com.ea1h.di.component.JobServiceComponent
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Inject

abstract class BaseJobService: JobService() {

    @Inject lateinit var eA1HRepository: EA1HRepository

    @Inject
    lateinit var schedulerProvider: SchedulerProvider

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    abstract fun inject(jobServiceComponent: JobServiceComponent)

    override fun onCreate() {
        super.onCreate()
        inject(buildActivityComponent())
    }

    private fun buildActivityComponent() =
        DaggerJobServiceComponent
            .builder()
            .applicationComponent((application as EA1HApplication).applicationComponent)
            .build()

    /**
     * determines if internet is there or not
     */
    fun isNetworkConnected(): Boolean {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}