/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    28/02/2020 11:45 AM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * UserProfileViewModel : View model for the UserProfile UI element
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h.views.home.userprofile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.apis.UserControllerApi
import io.swagger.client.models.UserProfileDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.R
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.model.UserProfileDetailResponse
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.Constants.NULL_STRING
import ymsli.com.ea1h.utils.common.Resource
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection

class UserProfileViewModel(schedulerProvider: SchedulerProvider,
                       compositeDisposable: CompositeDisposable,
                       networkHelper: NetworkHelper,
                       eA1HRepository: EA1HRepository
)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository){

    val profileData: MutableLiveData<UserProfileDetailResponse?> = MutableLiveData()
    val apiCallSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val imageBitmap: MutableLiveData<Bitmap> = MutableLiveData()

    val apiSuccessMessage: MutableLiveData<String> = MutableLiveData()
    val apiErrorMessage: MutableLiveData<String> = MutableLiveData()

    var bitmap: Bitmap? = null
    var userName: String? = null

    private companion object {
        private const val TAG = "UserProfileViewModel"
        private const val COMPRESSION_RATIO = 30
        private const val PARSING_ERROR = "Bitmap parsing failed"
        private const val UPDATE_SUCCESS_MESSAGE = "Profile update successful"
        private const val UPDATE_FAILED_MESSAGE = "Profile update failed, Please try again"
        private const val ERROR_SOMETHING_WENT_WRONG = "Something went wrong!! Try again"
    }

    override fun onCreate() {}

    /**
     * Retrieves the user profile data from local storage.
     * @author VE00YM023
     */
    fun loadDetailsFromLocalStorage() = viewModelScope.launch(Dispatchers.Default) {
        val userDetails = getUserProfileDetail()
        userDetails.profilePic?.let { parseBase64String(it) }
        profileData.postValue(userDetails)
    }

    /**
     * If network is available then retrieves the user profile data (possibly updated)
     * from remote and updates the local storage.
     * @author VE00YM023
     */
    fun tryToUpdateDetailsFromRemote() {
        val userId = getUserEntity().userId
        if(checkInternetConnection() && (userId != NULL_STRING && userId.isNotEmpty())){
            fetchFromRemote()
        }
    }

    /**
     * Calls the API to retrieve the user profile detail information.
     * This information is displayed in the user profile section.
     *
     * @author VE00YM023
     */
    private fun fetchFromRemote() = viewModelScope.launch(Dispatchers.IO){
        try{
            showProgress.postValue(true)
            val user = getUserEntity()
            val result = UserControllerApi().getUserDetailsUsingGET(user.token, user.userId)
            var data: UserProfileDetailResponse? = null
            if(result.responseCode == HttpURLConnection.HTTP_OK){
                val jsonString = JSONObject((result.responseData as Map<String,String>).toMap()).toString()
                data = Gson().fromJson(jsonString, UserProfileDetailResponse::class.java)
                data?.profilePic?.let { parseBase64String(it) }
                eA1HRepository.saveUserProfileDetailResponse(data)
            }
            profileData.postValue(data)
            showProgress.postValue(false)
        }
        catch(e: Exception){ showProgress.postValue(false) }
    }


    /** If network connection is available then calls the update API,
     *  otherwise notify the user.
     *  @author VE00YM023
     */
    fun performUpdate(){
        when(checkInternetConnection()){
            true -> updateUserProfile()
            else -> messageStringId.postValue(Resource.error(R.string.network_connection_error))
        }
    }

    private fun updateUserProfile() = viewModelScope.launch(Dispatchers.IO){
        try{
            showProgress.postValue(true)
            val user = getUserEntity()
            val request = UserProfileDetails(bitmap.toBase64(), userName, user.userId)
            val result = UserControllerApi().updateUserDetailsUsingPOST1(request, user.token)
            var data: UserProfileDetailResponse? = null
            if(result.responseCode == HttpURLConnection.HTTP_OK){
                val jsonString = JSONObject((result.responseData as Map<String,String>).toMap()).toString()
                data = Gson().fromJson(jsonString, UserProfileDetailResponse::class.java)
                apiCallSuccess.postValue(true)
                eA1HRepository.saveUserProfileDetailResponse(data)
                apiSuccessMessage.postValue(UPDATE_SUCCESS_MESSAGE)
            }
            else{
                apiCallSuccess.postValue(false)
                apiErrorMessage.postValue(UPDATE_FAILED_MESSAGE)
            }
            showProgress.postValue(false)
        }
        catch(e: Exception){
            apiCallSuccess.postValue(false)
            showProgress.postValue(false)
            apiErrorMessage.postValue(ERROR_SOMETHING_WENT_WRONG)
        }
    }

    /**
     * Tries to parse bitmap from given string, and updates the imageBitmap LiveData object.
     * if parsing fails then error is logged and LiveData object is updated with null.
     * @author VE00YM023
     */
    private fun parseBase64String(base64: String) = viewModelScope.launch(Dispatchers.Default) {
        try {
            val byteArr = Base64.decode(base64, Base64.DEFAULT)
            val parsedBitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size)
            imageBitmap.postValue(parsedBitmap)
        }
        catch (e: Exception) {
            Log.e(TAG, PARSING_ERROR)
            imageBitmap.postValue(null)
        }
    }

    /**
     * Converts this Bitmap to the base64 string representation
     */
    private fun Bitmap?.toBase64(): String? {
        if(this == null) return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_RATIO, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}