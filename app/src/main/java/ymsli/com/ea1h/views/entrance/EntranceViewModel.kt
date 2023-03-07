/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 2:49 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * EntranceViewModel : This class is the view model for entrance activity and is
 *                          responsible for data rendering over EntranceActivity
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.views.entrance

import android.hardware.SensorManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.gigya.android.sdk.Gigya
import com.gigya.android.sdk.GigyaCallback
import com.gigya.android.sdk.GigyaLoginCallback
import com.gigya.android.sdk.api.GigyaApiResponse
import com.gigya.android.sdk.network.GigyaError
import com.gigya.socialize.GSObject
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.models.LoginDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.entity.UserEntity
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.*
import ymsli.com.ea1h.utils.common.Constants.ERROR_TRY_AGAIN
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_USER_OBJECT
import ymsli.com.ea1h.utils.common.Constants.NA
import ymsli.com.ea1h.utils.rx.SchedulerProvider

class EntranceViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val gigya: Gigya<GigyaResponse>,
    eA1HRepository: EA1HRepository,
    private val gson: Gson
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    companion object{
        //Gigya parameters
        const val EMAIL_KEY = "email"
        const val PASSWORD_KEY = "password"
        const val FIRST_NAME_KEY = "firstName"
        const val COUNTRY_KEY = "country"
        const val NUMBER_KEY = "number"
        const val PHONE_KEY = "phones"
        const val COUNTRY_VALUE = "IN"
        const val PROFILE_KEY = "profile"
        const val SESSION_EXP_KEY = "sessionExpiration"
        const val VERIFICATION_PENDING_KEY = "Account Pending Verification"
        const val VALIDATION_ERROR_KEY = "validationErrors"
        const val VALIDATION_MESSAGE_KEY = "message"
    }

    //parameters for signin using Phone number or email
    var signinParameter: MutableLiveData<String> = MutableLiveData()

    //parameters for signup
    var fullName: MutableLiveData<String> = MutableLiveData()
    var email: MutableLiveData<String> = MutableLiveData()
    var password: MutableLiveData<String> = MutableLiveData()
    var phone: MutableLiveData<String> = MutableLiveData()
    var signupSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()
    //error acknowledgement at Signin fragment
    var invalidUserEmailPhone: MutableLiveData<Int> = MutableLiveData()
    val emptyPassword: MutableLiveData<Boolean> = MutableLiveData()

    /** Validation error live data, if any validation fails for sign up fragment,
     *  then failed validation is posted to this field, UI element can observe it
     *  to check field has failed the validation */
    val fieldError: MutableLiveData<Validation> = MutableLiveData()
    var loginSuccessful: MutableLiveData<Boolean> = MutableLiveData()

    var isGyroPresent: MutableLiveData<Boolean> = MutableLiveData()
    var isAcceleroPresent: MutableLiveData<Boolean> = MutableLiveData()
    var invalidLoginMessage: MutableLiveData<String> = MutableLiveData()
    var invalidSignupMessage: MutableLiveData<Event<String>> = MutableLiveData()
    var errorSignupMessage: MutableLiveData<Event<Int>> = MutableLiveData()
    var forgotPasswordUserAck: MutableLiveData<Event<Int>> = MutableLiveData()
    var forgotPasswordUserErrorAck: MutableLiveData<Event<String>> = MutableLiveData()

    private lateinit var fcmToken: String

    override fun onCreate() {}

    /**
     * Tries to login the user by performing following steps.
     * 1.  Perform required validation on the user input
     * 2.  Check if network connection is available
     * 3.  if network is available and validations are passed then
     *     get the FCM token and call Login API.
     *
     * @author VE00YM023
     */
    fun getFCMTokenAndLogin(){
        val validations = Validator.validateEmail(email.value)
        val successValidations = validations.filter { it.resource.status == Status.SUCCESS }
        if (successValidations.size != validations.size) {
            showError(R.string.invalid_phone_number_email)
            return
        }
        if(password.value.isNullOrEmpty()){
            emptyPassword.postValue(true)
            return
        }
        if(!checkInternetConnection()) {
            messageStringId.postValue(Resource.error(R.string.network_connection_error))
            return
        }
        /* If network is available and validations are passed then get the FCM
           token, store it in the shared prefs and then call the login API */
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(it.isSuccessful){
                fcmToken = it.result?.token!!
                setFCMToken(fcmToken)
                callLoginApi()
            }
        }
    }

    /**
     * Calls the login API on the remote server.
     * result of this API contains the user information and the 'Auth Token'
     * we store all this information in the shared preferences for later use.
     *
     * Note: All this data must be cleared at logout
     *
     * @author VE00YM023
     */
    private fun callLoginApi() {
        showProgress.postValue(true)
        val request = getLoginRequestDTO()
        compositeDisposable.addAll(
            eA1HRepository.loginUsingEmailPassword(request)
                .subscribeOn(schedulerProvider.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val jsonString = JSONObject(it)
                    val userJson = jsonString.getJSONObject(JSON_KEY_USER_OBJECT).toString()
                    val user: UserEntity = gson.fromJson(userJson, UserEntity::class.java)
                    setupAppState(user)
                }, {
                    showProgress.postValue(false)
                    invalidLoginMessage.postValue(it.message ?: ERROR_TRY_AGAIN)
                })
        )
    }

    /**
     * Returns the Login request DTO, with required fields populated.
     * @author VE00YM023
     */
    private fun getLoginRequestDTO() = LoginDTO(
        appVersion = BuildConfig.VERSION_NAME,
        email = encryptData(email.value!!),
        imei = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!),
        os = "${BuildConfig.OS} ${ Build.VERSION.RELEASE}",
        fcmToken = fcmToken,
        pwd = encryptData(password.value!!),
        accelero = isAcceleroPresent.value?:false,
        gyro = isGyroPresent.value?:false,
        mdNm = getModelName()
    )

    private fun getModelName():String? = "${Build.MANUFACTURER} ${Build.MODEL}"
    /**
     * responsible for signing up new user via Gigya
     */
    fun signUp() {
        if (checkInternetConnection()) {
            // validate user input
            val signUpValidationModel =
                SignUpValidationModel(fullName.value, email.value, password.value,phone.value)
            val validations = Validator.validateSignUpForm(signUpValidationModel)
            when (validations.responseCode) {
                Status.ERROR -> fieldError.postValue(validations.validation)
                else -> {
                    showProgress.postValue(true)
                    val params = GSObject()//GSObject created and passed on to the Gigya API to create new user
                    params.put(EMAIL_KEY, email.value)
                    params.put(PASSWORD_KEY, password.value)

                    val profile = GSObject()
                    profile.put(FIRST_NAME_KEY, fullName.value)
                    profile.put(COUNTRY_KEY,COUNTRY_VALUE)
                    val tempMap = GSObject()
                    tempMap.put(NUMBER_KEY,phone.value)
                    profile.put(PHONE_KEY,tempMap)
                    params.put(PROFILE_KEY, profile)

                    val map = HashMap<String,Any>()
                    map.put(PROFILE_KEY,profile)

                    gigya.register(
                        email.value,
                        password.value,
                        map, //mutableMapOf<String, Any>(SESSION_EXP_KEY to 10),
                        object : GigyaLoginCallback<GigyaResponse>() {
                            override fun onSuccess(response: GigyaResponse?) {
                                // save data locally & sync with server
                                showProgress.postValue(false)
                                // ask user to verify email and redirect to login
                                signupSuccess.postValue(Event(true))
                            }

                            override fun onError(error: GigyaError?) {
                                /* If verification pending error message is received then
                                   we take it as success and move to the sync success screen. */
                                if (error?.localizedMessage.equals(VERIFICATION_PENDING_KEY)) {
                                    signupSuccess.postValue(Event(true))
                                }
                                else {
                                    try {
                                        val errorString = ((JSONObject(error?.data)).getJSONArray(VALIDATION_ERROR_KEY)[0] as JSONObject).get(VALIDATION_MESSAGE_KEY).toString()
                                        //userErrorDialog.postValue(Event(errorString))
                                        if(errorString.contains("does not match to required regex pattern",true)){
                                            invalidSignupMessage.postValue(Event("Use strong password"))
                                        }
                                        else{
                                            invalidSignupMessage.postValue(Event(errorString))
                                        }
                                    }
                                    catch (ex: Exception){
                                        //userAlertDialog.postValue(Event(R.string.use_strong_password))
                                        errorSignupMessage.postValue(Event(R.string.use_strong_password))
                                    }
                                }
                                showProgress.postValue(false)
                            }
                        })
                }
            }
        } else errorAcknowledgementEvent.postValue(Event(R.string.network_connection_error)) // ack user that there is no internet connection
    }

    /**
     * show validation error to the user
     */
    private fun showError(@StringRes errorMessage: Int) {
        invalidUserEmailPhone.postValue(errorMessage)
    }

    /**
     * stores android ID of device in shared prefs
     */
    fun setAndroidIdInSharedPref(imei: String) {
        eA1HRepository.setImeiNumberInSharedPref(imei)
    }

    /**
     * When log in operation completes. we check if same user has logged in again.
     * if so then we don't have to clear the old data. (Trips, bikes etc)
     * @author VE00YM023
     */
    private fun setupAppState(newUser: UserEntity) = GlobalScope.launch(Dispatchers.IO) {
        /* Different user has logged in, clear data of old user */
        if(newUser.userId != getUserId()){
            eA1HRepository.clearUserDataFromSharedPred()
            eA1HRepository.removeUserEntity()
            eA1HRepository.removeUserProfileDetail()
            eA1HRepository.setDontAskAgain(false)
            eA1HRepository.removeAllBikes()
            eA1HRepository.deleteSyncedTrips()
            eA1HRepository.deleteSyncedLatLongs()
            eA1HRepository.setBikeListLastSync(0)
            eA1HRepository.setTripListLastSync(0)
        }
        newUser.reLoginFailure = false
        eA1HRepository.storeUserEmailInSharedPref(email.value)
        eA1HRepository.storeUserDataInSharedPref(newUser)
        eA1HRepository.insertUserEntity(newUser)
        setUserPassword(password.value!!)
        setLoggedInStatus(true)
        showProgress.postValue(false)
        loginSuccessful.postValue(true)
    }

    /**
     * triggers when user has forgotten his/her
     * password and now want to reset it using
     * phone number and otp
     */
    fun changePassword(email: String?){
        if (checkInternetConnection()) {
            showProgress.postValue(true)
            gigya.forgotPassword(email, object : GigyaCallback<GigyaApiResponse>() {
                override fun onSuccess(response: GigyaApiResponse?) {
                    showProgress.postValue(false)
                    forgotPasswordUserAck.postValue(Event(R.string.email_check))// ask user to check mail and reset password
                }

                override fun onError(error: GigyaError?) {
                    forgotPasswordUserErrorAck.postValue(Event(error?.localizedMessage?:Constants.SOMETHING_WRONG))// ack user about the error
                    showProgress.postValue(false)
                }
            })
        } else errorAcknowledgement.postValue(R.string.network_connection_error) // ack user that there is no internet connection
    }
}
