package ymsli.com.ea1h.services

import android.content.Context
import androidx.work.WorkerParameters
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.swagger.client.models.LogBluetoothCommandDTO
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.base.BaseWorkManager
import ymsli.com.ea1h.di.component.JobServiceComponent
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.CryptoHandler

class UploadBTLogsWorkManager(private val context: Context, workerParameters: WorkerParameters):
    BaseWorkManager(context,workerParameters) {

    override fun inject(jobServiceComponent: JobServiceComponent) = jobServiceComponent.inject(this)

    override fun doWork(): Result {
        init()
        if(eA1HRepository.getUserDataFromSharedPref().token.contains("null",true)){
            return Result.success()
        }
        return try{
            if(isNetworkConnected()){
                syncBTLogs()
            }
            Result.success()
        }
        catch (err: Throwable){
            Result.success()
        }
    }

    /**
     * sync bluetooth logs to the server
     * and clear them from local database
     */
    private fun syncBTLogs() {
        val noOfRecordsToFetch = getNoOfRecordsToFetch()
        val logs = eA1HRepository.getAllCommands(noOfRecordsToFetch)
        if (logs.isNotEmpty()) {
            val minimumTriggeredOn = logs[logs.size-1].triggeredOn
            val maximumTriggeredOn = logs[0].triggeredOn
            val arrLogBTCommands = arrayListOf<LogBluetoothCommandDTO>()
            for (i in logs.indices) {
                val currentLog = logs[i]
                var isSuc: Long
                if (currentLog.isSuccessful) {
                    isSuc = 1
                } else isSuc = 0

                var chasNumServer: String = Constants.NA
                if (!currentLog.chassNum.isNullOrBlank()) {
                    chasNumServer = currentLog.chassNum!!
                }
                val log = LogBluetoothCommandDTO(
                    chasNum = chasNumServer,
                    cmd = currentLog.cmdType.toLong(),
                    createdOn = currentLog.triggeredOn.toString(),
                    imei = CryptoHandler.encrypt(eA1HRepository.getAndroidIdFromSharedPref()),
                    isSuc = isSuc,
                    os = BuildConfig.OS,
                    btAdd = currentLog.btAdd
                )
                arrLogBTCommands.add(log)
            }
            compositeDisposable.addAll(
            eA1HRepository.logBluetoothCommandsTemp(arrLogBTCommands.toTypedArray())
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    if (it == Constants.DONE) {
                        eA1HRepository.deleteAllSyncedLogs(minimumTriggeredOn, maximumTriggeredOn)
                    }
                }, {
                    FirebaseCrashlytics.getInstance().recordException(it)
                })
            )
        }
    }

    /**
     * Returns the number of BLE Logs to fetch from DB.
     * this value is received from project-descriptions API,
     * if for some reason that value is not present then we return the default value.
     * @author Balraj
     * @return BLE_LOG_LIMIT key value or default if not available
     */
    private fun getNoOfRecordsToFetch(): Int{
        val list = eA1HRepository.getMiscDataByKey(Constants.MISC_KEY_BLE_LOG_LIMIT)
        if(list.isNullOrEmpty() ||
           list[0].descriptionValue.isNullOrEmpty()) {
            return Constants.DEFAULT_BLE_LOG_LIMIT
        }
        return list[0].descriptionValue.toIntOrNull() ?: Constants.DEFAULT_BLE_LOG_LIMIT
    }
}