package ymsli.com.ea1h.views.addbike

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    10/02/2020 2:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ChassisNumberViewModel : This is view model for ChassisNumberActivity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.ChassisVerificationDTO
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.common.Status
import ymsli.com.ea1h.utils.common.Validator
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class ChassisNumberViewModel(schedulerProvider: SchedulerProvider,
                             compositeDisposable: CompositeDisposable,
                             networkHelper: NetworkHelper,
                             eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    var chassisNumber: MutableLiveData<String> = MutableLiveData()
    var isChassisValid: MutableLiveData<String> = MutableLiveData()
    var showError: MutableLiveData<Int> = MutableLiveData()
    val errorNotification: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {}

    /**
     * Performs client side validations on the chassis number given by user.
     * if validations pass then check of network connection and continue with API call.
     * @author VE00YM023
     */
    fun validateChassisNumber() {
        val validations = Validator.validateChassisNumber(chassisNumber.value)
        val successValidations = validations.filter { it.resource.status == Status.SUCCESS }
        if (successValidations.size != validations.size) {
            showError.postValue(validations[0].resource.data)
            return
        }
        if (!checkInternetConnection()) {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            return
        }
        callValidateAPI()
    }

    /** Calls validate API on the remote server. */
    private fun callValidateAPI(){
        showProgress.postValue(true)
        compositeDisposable.addAll(eA1HRepository.verifyChassis(getRequest())
            .subscribeOn(schedulerProvider.io())
            .subscribe({
                showProgress.postValue(false)
                isChassisValid.postValue(decryptData(it))
            },{
                showProgress.postValue(false)
                errorNotification.postValue(it.message)
            })
        )
    }

    /** Returns API request model for the chassis validation API. */
    private fun getRequest() = ChassisVerificationDTO(
        chasNum = encryptData(chassisNumber.value!!),
        imei = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!)
    )
}