package ymsli.com.ea1h

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    10/02/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * EA1HApplication : This is application class whose callbacks are triggered
 * when as per application lifecycle
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.app.Application
import android.util.Log
import com.gigya.android.sdk.Gigya
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity
import ymsli.com.ea1h.di.component.ApplicationComponent
import ymsli.com.ea1h.di.component.DaggerApplicationComponent
import ymsli.com.ea1h.di.module.ApplicationModule
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Utils
import ymsli.com.ea1h.utils.common.ViewUtils
import javax.inject.Inject

class EA1HApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var eA1HRepository: EA1HRepository

    override fun onCreate() {
        super.onCreate()
        Gigya.setApplication(this)
        Gigya.getInstance(GigyaResponse::class.java)
        try {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
        catch (ex: java.lang.Exception){
            Log.d("Firebase initialization Error",ex.localizedMessage?:Constants.NA)
        }
        injectDependencies()
        resetMFECUConnectivity()
        ViewUtils.startZipUploadService(baseContext)//start file upload service to sync local sensor and location data to DAP Server
        ViewUtils.startTripsSyncService(baseContext)//start data sync service to sync local trips to YMSLI server
        ViewUtils.startDataSyncService(baseContext)//start data sync service to sync local trips to YMSLI server
        ea1hRepo = eA1HRepository
        eA1HRepository.setIgnitionExp(false)
        eA1HRepository.setIfElockCanBeDisabled(false)
        logCommand(18)
    }

    /**
     * injects parameters in application instance
     */
    private fun injectDependencies(){
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

    fun setComponent(applicationComponent: ApplicationComponent) {
        this.applicationComponent = applicationComponent
    }

    /**
     * reset connectivity parameters on application creation
     */
    private fun resetMFECUConnectivity(){
        eA1HRepository.logConnection(false)
        eA1HRepository.logIgnition(false)
    }

    companion object {
        lateinit var ea1hRepo: EA1HRepository
    }

    private fun logCommand(cmdType: Int) {
        try {
            var devId: String = Constants.NA
            if(eA1HRepository.getAndroidIdFromSharedPref()!=null){
                devId = eA1HRepository.getAndroidIdFromSharedPref()?:Constants.NA
            }

            val btCommandsLogEntity = BTCommandsLogEntity(cmdType = cmdType,chassNum = Constants.NA,btAdd = Constants.NA,triggeredOn = Utils.getTimeInMilliSec(),
                isSuccessful = true,isCommit = true,deviceId = devId)

            eA1HRepository.writeBluetoothCommandInRoom(btCommandsLogEntity)
        } catch (ex: Exception) {
            Log.d("Logging error",ex.localizedMessage?:Constants.NA)
        }
    }
}
