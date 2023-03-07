/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   8/2/20 10:11 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * ForgotPasswordViewModel : This is the associated view model for ForgotPasswordActivity.
 *                              This is responsible for data rendering on UI.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.password.forgot

import androidx.lifecycle.MutableLiveData
import com.gigya.android.sdk.Gigya
import com.gigya.android.sdk.GigyaCallback
import com.gigya.android.sdk.api.GigyaApiResponse
import com.gigya.android.sdk.network.GigyaError
import io.reactivex.disposables.CompositeDisposable
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.*
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class ForgotPasswordViewModel(schedulerProvider: SchedulerProvider,
                              compositeDisposable: CompositeDisposable,
                              networkHelper: NetworkHelper,
                              private val gigya: Gigya<GigyaResponse>,
                              eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    var emailField : MutableLiveData<String> = MutableLiveData()
    //ack user validation error
    val emailFieldError: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate() {

    }

    /**
     * triggers when user has forgotten his/her
     * password and now want to reset it using
     * phone number and otp
     */
    fun changePassword(){
        if (checkInternetConnection()) {
            val validation = Validator.validateEmail(emailField.value)
            val failedValidation = validation.filter { it.resource.status == Status.ERROR }
            if (failedValidation.isNotEmpty()) {
                // ack user about validation error
                emailFieldError.postValue(failedValidation[0].resource.data)
            } else {
                showProgress.postValue(true)
                gigya.forgotPassword(emailField.value, object : GigyaCallback<GigyaApiResponse>() {
                    override fun onSuccess(response: GigyaApiResponse?) {
                        showProgress.postValue(false)
                        userAlertDialog.postValue(Event(R.string.email_check))// ask user to check mail and reset password
                    }

                    override fun onError(error: GigyaError?) {
                        userErrorDialog.postValue(Event(error?.localizedMessage?:Constants.SOMETHING_WRONG))// ack user about the error
                        showProgress.postValue(false)
                    }
                })
            }
        } else errorAcknowledgement.postValue(R.string.network_connection_error) // ack user that there is no internet connection
    }
}