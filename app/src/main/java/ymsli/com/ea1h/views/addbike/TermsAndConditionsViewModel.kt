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
 * TermsAndConditionsViewModel : This is view model for TermsAndConditionsActivity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.OtpGenerateRequestPacket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.RequestType
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class TermsAndConditionsViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    eA1HRepository: EA1HRepository
): BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    var chassisNumber = EMPTY_STRING
    var phoneNumber = EMPTY_STRING
    var qrCode = EMPTY_STRING
    var requestType = 0
    val termsAndConditions: MutableLiveData<String?> = MutableLiveData()
    val apiResponseSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private companion object { private const val DATA_KEY = "AGREEMENT" }

    override fun onCreate() {}

    /** gets terms and conditions from the room db */
    fun getTermsAndConditions() = viewModelScope.launch(Dispatchers.IO){
        val data = eA1HRepository.getMiscDataByKey(DATA_KEY)
        if(data.isNotEmpty() && !data[0].descriptionValue.isNullOrEmpty()){
            termsAndConditions.postValue(data[0].descriptionValue)
        }
    }

    /**
     * Validate Chassis API is called for resending the OTP.
     * @author VE00YM023
     */
    fun generateOTP() = GlobalScope.launch(Dispatchers.IO){
        if (!checkInternetConnection()) {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            return@launch
        }
        try{
            showProgress.postValue(true)
            eA1HRepository.resendOTP(getResendOTPRequestModel())
            apiResponseSuccess.postValue(true)
            showProgress.postValue(false)
        }
        catch(cause: Exception){
            showProgress.postValue(false)
            messageStringId.postValue(Resource.error(R.string.failure_otp_resend))
        }
    }

    /** Utility method to generate the request model for API call */
    private fun getResendOTPRequestModel() = OtpGenerateRequestPacket(
        imei = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!),
        phone = encryptData(phoneNumber),
        rt = RequestType.VEHICLE_REGISTRATION.code
    )
}