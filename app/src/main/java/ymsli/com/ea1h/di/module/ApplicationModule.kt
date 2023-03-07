/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   31/1/20 11:14 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ApplicationModule : This acts as the Dagger2 module of DI framework
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.apis.*
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.database.EA1HDatabase
import ymsli.com.ea1h.database.EA1HSharedPreferences
import ymsli.com.ea1h.di.ApplicationContext
import ymsli.com.ea1h.network.DAPIoTNetworking
import ymsli.com.ea1h.network.swaggerintegration.MiscHandler
import ymsli.com.ea1h.network.swaggerintegration.TripHandler
import ymsli.com.ea1h.network.swaggerintegration.UserRegistration
import ymsli.com.ea1h.network.swaggerintegration.VehicleRegistration
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.rx.RxSchedulerProvider
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import javax.inject.Singleton

@Module
class ApplicationModule (private val application: EA1HApplication){

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(): Context = application

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    fun provideSchedulerProvider(): SchedulerProvider =
        RxSchedulerProvider()

    @Singleton
    @Provides
    fun provideNetworkHelper(): NetworkHelper =
        NetworkHelper(application)

    @Singleton
    @Provides
    fun provideEA1HRepository():  EA1HRepository =
        EA1HRepository(provideEA1HDatabase(),provideSwaggerIntegration(),provideVehicleRegistration(),provideTripHandler(),provideMiscHandler(),provideEA1HSharedPreferences(),provideDAPIoTNetworking())

    @Singleton
    @Provides
    fun provideEA1HDatabase(): EA1HDatabase {
        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("Alter Table bike_entity ADD COLUMN vehType varchar DEFAULT "+Constants.BIKE_STRING)
            }

        }

        return Room.databaseBuilder(application,EA1HDatabase::class.java,"ea1h-room-database")
            .addMigrations(MIGRATION_1_2).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideSwaggerIntegration(): UserRegistration =
        UserRegistration(
            provideOTPControllerApi(),
            provideUserControllerApi(),
            provideLoginControllerApi(),
            provideGson()
        )

    @Singleton
    @Provides
    fun provideVehicleRegistration(): VehicleRegistration =
        VehicleRegistration(
            provideOTPControllerApi(),
            provideBikeControllerApi(),
            provideMfecuControllerApi(),
            provideGson()
        )

    @Singleton
    @Provides
    fun provideTripHandler(): TripHandler =
        TripHandler(provideTripControllerApi(),provideGson())

    @Singleton
    @Provides
    fun provideMiscHandler(): MiscHandler = MiscHandler(provideBluetoothCommandControllerApi(),provideProjectDescriptionDetailsControllerApi(),provideGson())

    @Singleton
    @Provides
    fun provideEncryptedSharedPreferences(): SharedPreferences {
        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        return EncryptedSharedPreferences.create("ea1h-shared-prefs-encrypted",masterKeyAlias,application,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    @Provides
    @Singleton
    fun provideEA1HSharedPreferences(): EA1HSharedPreferences = EA1HSharedPreferences(provideEncryptedSharedPreferences())

    @Provides
    @Singleton
    fun provideOTPControllerApi(): OtpControllerApi = OtpControllerApi()

    @Provides
    @Singleton
    fun provideUserControllerApi():UserControllerApi = UserControllerApi()

    @Provides
    @Singleton
    fun provideLoginControllerApi(): LoginControllerApi = LoginControllerApi()

    @Provides
    @Singleton
    fun provideMfecuControllerApi(): MfecuControllerApi = MfecuControllerApi()

    @Provides
    @Singleton
    fun provideBikeControllerApi(): BikeControllerApi = BikeControllerApi()

    @Provides
    @Singleton
    fun provideTripControllerApi(): TripControllerApi = TripControllerApi()

    @Provides
    @Singleton
    fun provideBluetoothCommandControllerApi(): BluetoothCommandControllerApi = BluetoothCommandControllerApi()

    @Provides
    @Singleton
    fun provideProjectDescriptionDetailsControllerApi(): ProjectDescriptionDetailsControllerApi = ProjectDescriptionDetailsControllerApi()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideDAPIoTNetworking() = DAPIoTNetworking.createNetworkService()
}