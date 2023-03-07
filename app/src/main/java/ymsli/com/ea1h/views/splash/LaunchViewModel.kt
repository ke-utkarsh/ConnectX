package ymsli.com.ea1h.views.splash

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
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class LaunchViewModel(schedulerProvider: SchedulerProvider,
                      compositeDisposable: CompositeDisposable, networkHelper: NetworkHelper,
                      eA1HRepository: EA1HRepository): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    val appVersionApiResponse: MutableLiveData<AppVersionResponse> = MutableLiveData()
    val continueToNextScreen: MutableLiveData<Boolean> = MutableLiveData()

    fun alterNetworkRestriction(restrictNetwork: Boolean) = eA1HRepository.setRestrictNetworkCalls(restrictNetwork)

    override fun onCreate() {

    }

    /**
     * Executes the api call for app version check. if the call completes
     * successfully then posts the response in live data object.
     * @author VE00YM023
     */
    fun checkAppStatus() = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) { continueToNextScreen.postValue(true); return@launch }
            val request = AppVersionRequestDTO(Constants.ANDROID, BuildConfig.VERSION_NAME)
            val appStatus = eA1HRepository.getAppStatus(request)
            appVersionApiResponse.postValue(appStatus)
        }
        catch (e: Exception){
            continueToNextScreen.postValue(true)
        }
    }
}