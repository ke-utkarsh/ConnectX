package ymsli.com.ea1h.di.component

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 23, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ServiceComponent : This is service component and inject methods for service module
 * Part of dagger2 framework
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import dagger.Component
import ymsli.com.ea1h.di.IntentServiceScope
import ymsli.com.ea1h.di.module.ServiceModule
import ymsli.com.ea1h.services.FCMService
import ymsli.com.ea1h.views.experiment.ForegroundOnlyLocationService

@IntentServiceScope
@Component(dependencies = [ApplicationComponent::class],modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(foregroundOnlyLocationService: ForegroundOnlyLocationService)
    fun inject(fcmService: FCMService)
}