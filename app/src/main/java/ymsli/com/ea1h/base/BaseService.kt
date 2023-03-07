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
 * BaseService : This abstract class is the base Service. A Service is responsible
 *  for running certain tasks in background
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.app.Service
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.di.component.DaggerServiceComponent
import ymsli.com.ea1h.di.component.ServiceComponent
import ymsli.com.ea1h.di.module.ServiceModule
import javax.inject.Inject

abstract class BaseService: Service() {

    @Inject lateinit var eA1HRepository: EA1HRepository

    protected abstract fun injectDependencies(serviceComponent: ServiceComponent)

    override fun onCreate() {
        injectDependencies(buildServiceComponent())
    }

    private fun buildServiceComponent() =
        DaggerServiceComponent
            .builder()
            .applicationComponent((application as EA1HApplication).applicationComponent)
            .serviceModule(ServiceModule(this))
            .build()
}