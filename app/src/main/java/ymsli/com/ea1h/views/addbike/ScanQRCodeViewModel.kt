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
 * ScanQRCodeActivity : This is view model for ScanQRCodeActivity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.ValidateQRCodeDTO
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Event
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class ScanQRCodeViewModel(schedulerProvider: SchedulerProvider,
                          compositeDisposable: CompositeDisposable,
                          networkHelper: NetworkHelper,
                          eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    var qrCodeField: MutableLiveData<String> = MutableLiveData()
    var isQrCodeValid: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val apiFailed: MutableLiveData<Boolean> = MutableLiveData()
    var chassisNumber = EMPTY_STRING
    var phoneNumber = EMPTY_STRING
    var apiErrorMessage: String? = EMPTY_STRING

    override fun onCreate() {}

    /** calls the API to validate the scanned QR code */
    fun validateQRCode(){
        if(checkInternetConnection()){
            showProgress.postValue(true)
            val validateQRCodeDTO = ValidateQRCodeDTO(qrCode = qrCodeField.value)
            compositeDisposable.addAll(
                eA1HRepository.validateQRCode(validateQRCodeDTO)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        showProgress.postValue(false)
                        isQrCodeValid.postValue(Event(true))
                    },{
                        apiErrorMessage = it.message
                        showProgress.postValue(false)
                        apiFailed.postValue(true)
                    })
            )
        }
        else messageStringId.postValue(Resource.error(R.string.network_connection_error))
    }
}