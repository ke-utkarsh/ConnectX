package ymsli.com.ea1h.views.otp

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 2:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * OTPViewModel : This is the View model for OTPActivity
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
import io.swagger.client.models.RegisterBike
import io.swagger.client.models.ValidateOtpDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.*
import ymsli.com.ea1h.utils.common.Constants.MISC_KEY_OTP_INTERVAL
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class OTPViewModel (
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    eA1HRepository: EA1HRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    /* Parameters required for OTP validation API, received from previous Activity */
    var chassisNumber = EMPTY_STRING
    var phoneNumber = EMPTY_STRING
    var qrCode = EMPTY_STRING
    var requestType = 0

    var otp: String? = EMPTY_STRING
    var otpInterval: MutableLiveData<Int> = MutableLiveData()
    val error: MutableLiveData<Int> = MutableLiveData()
    val resendOTPSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val registrationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val errorNotification: MutableLiveData<String> = MutableLiveData()
    val successNotification: MutableLiveData<String> = MutableLiveData()

    override fun onCreate() {}

    private companion object {
        private const val OTP_SENT_SUCCESS = "OTP sent successfully"
        private const val OTP_SENT_FAILED = "Failed to send OTP, Please try again!!"
    }

    /**
     * Retrieves the OTP interval time, this value is used for the chronometer
     * @author VE00YM023
     */
    fun getOTPInterval() = viewModelScope.launch(Dispatchers.IO) {
        val result = eA1HRepository.getMiscDataByKey(MISC_KEY_OTP_INTERVAL)
        if(result != null && result.isNotEmpty()){
            otpInterval.postValue(result[0].descriptionValue.toInt())
        }
        else otpInterval.postValue(30)
    }

    /**
     * Tries to validate the entered OTP for 'Null, Blank and invalid values'
     * if validations fail then updates the error LiveData Object
     */
    fun validateOTP() {
        val validations = Validator.validateOTPInput(otp)
        val failedValidation = validations.filter { it.resource.status == Status.ERROR }
        if (failedValidation.isNotEmpty()) {
            error.postValue(failedValidation[0].resource.data)
            return
        }
        if (!checkInternetConnection()) {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            return
        }
        registerNewVehicle()
    }

    /**
     * this method registers new bike via OTP which is sent to that mobile number
     * which was shared with dealer at the time of bike purchase
     */
    private fun registerNewVehicle() {
        val request = getValidateOTPModel()
        showProgress.postValue(true)
        compositeDisposable.addAll(
            eA1HRepository.registerBike(request)
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    val isScooter = it.vehType.equals(Constants.SCOOTER_STRING,true)
                    if(isScooter){
                        eA1HRepository.setIfScooter(true)
                    }
                    eA1HRepository.insertBikeData(arrayOf(it))
                    showProgress.postValue(false)
                    registrationSuccess.postValue(true)
                }, {
                    showProgress.postValue(false)
                    errorNotification.postValue(it.message)
                })
        )
    }

    /**
     * Validate Chassis API is called for resending the OTP.
     * @author VE00YM023
     */
    fun resendOTP() = GlobalScope.launch(Dispatchers.IO){
        if (!checkInternetConnection()) {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            return@launch
        }
        try{
            showProgress.postValue(true)
            eA1HRepository.resendOTP(getResendOTPRequestModel())
            showProgress.postValue(false)
            resendOTPSuccess.postValue(true)
            successNotification.postValue(OTP_SENT_SUCCESS)
        }
        catch(cause: Exception){
            showProgress.postValue(false)
            errorNotification.postValue(OTP_SENT_FAILED)
        }
    }

    /** Utility methods to generate the request models for different API calls */

    private fun getResendOTPRequestModel() = OtpGenerateRequestPacket(
        imei = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!),
        phone = encryptData(phoneNumber),
        rt = RequestType.VEHICLE_REGISTRATION.code
    )

    private fun getRegisterBikeModel() = RegisterBike(
        agreed = true, /* If we are here that means terms are accepted */
        chasNum = encryptData(chassisNumber),
        qrCode = qrCode,
        userId = getUserId()
    )

    private fun getValidateOTPModel() = ValidateOtpDTO(
        oregisterBike = getRegisterBikeModel(),
        imei = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!),
        phone = encryptData(phoneNumber),
        os = BuildConfig.OS,
        rt = RequestType.VEHICLE_REGISTRATION.code,
        otp = otp
    )
}