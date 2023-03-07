package ymsli.com.ea1h.views.home.unregisteruser

import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class UnregisterUserViewModel(schedulerProvider: SchedulerProvider,
                       compositeDisposable: CompositeDisposable,
                       networkHelper: NetworkHelper,
                       eA1HRepository: EA1HRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository){

    override fun onCreate() {}

    fun networkAvailable() = checkInternetConnection()
}