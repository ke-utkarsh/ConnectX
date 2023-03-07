package ymsli.com.ea1h.views.splash

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    10/2/20 2:49 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * SplashViewModel : This is view model for splash activity.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.AppVersionRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.model.AppVersionResponse
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants.ANDROID
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class SplashViewModel(schedulerProvider: SchedulerProvider,
                        compositeDisposable: CompositeDisposable,
                        networkHelper: NetworkHelper,
                        eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    val appVersionApiResponse: MutableLiveData<AppVersionResponse> = MutableLiveData()
    val continueToNextScreen: MutableLiveData<Boolean> = MutableLiveData()
    override fun onCreate() {}

    /**
     * Executes the api call for app version check. if the call completes
     * successfully then posts the response in live data object.
     * @author VE00YM023
     */
    fun checkAppStatus() = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) { continueToNextScreen.postValue(true); return@launch }
            val request = AppVersionRequestDTO(ANDROID, BuildConfig.VERSION_NAME)
            val appStatus = eA1HRepository.getAppStatus(request)
            appVersionApiResponse.postValue(appStatus)
        }
        catch (e: Exception){
            continueToNextScreen.postValue(true)
        }
    }
}