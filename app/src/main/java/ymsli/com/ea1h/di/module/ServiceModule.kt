package ymsli.com.ea1h.di.module

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 23, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ServiceModule : This is service module for service and injects object via dagger2
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import ymsli.com.ea1h.views.experiment.ForegroundOnlyLocationService

@Module
class ServiceModule (private val service: Service) {

    @Provides
    fun provideNotificationManager(): NotificationManager =
        service.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun provideLocationRequest(): LocationRequest =
        LocationRequest()

    @Provides
    fun provideFusedLocationProviderClient(): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(service)

}