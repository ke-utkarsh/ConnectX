/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 10:17 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ApplicationComponent : This acts as the Dagger2 component of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.component

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.apis.*
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.database.EA1HSharedPreferences
import ymsli.com.ea1h.di.ApplicationContext
import ymsli.com.ea1h.di.module.ApplicationModule
import ymsli.com.ea1h.network.swaggerintegration.UserRegistration
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(eA1HApplication: EA1HApplication)

    fun getApplication(): Application

    @ApplicationContext
    fun getContext(): Context

    fun getCompositeDisposable(): CompositeDisposable

    fun getSharedPreferences(): SharedPreferences

    fun getNetworkHelper(): NetworkHelper

    fun getEA1HRepository(): EA1HRepository

    fun getSchedulerProvider(): SchedulerProvider

    fun getSwaggerIntegration(): UserRegistration

    fun getEA1HSharedPreferences(): EA1HSharedPreferences

    fun getOTPControllerAPI(): OtpControllerApi

    fun userControllerAPI(): UserControllerApi

    fun getLoginControllerAPI(): LoginControllerApi

    fun getBikeControllerAPI(): BikeControllerApi

    fun getMfecuControllerAPI(): MfecuControllerApi

    fun getTripControllerApi(): TripControllerApi

    fun getBluetoothCommandControllerApi() : BluetoothCommandControllerApi

    fun getProjectDescriptionDetailsControllerApi(): ProjectDescriptionDetailsControllerApi

    fun getGson():Gson
}