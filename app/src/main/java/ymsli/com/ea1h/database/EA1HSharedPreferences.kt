package ymsli.com.ea1h.database

/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   31/1/20 11:14 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * EA1HSharedPreferences : This holds the value of shared preferences and sole resposibility
 * is to read/write/clear the shared preferences.
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *               10/02/2020          Added provider for SplashViewModel
 *  * -----------------------------------------------------------------------------------
 *
 */

import android.content.SharedPreferences
import ymsli.com.ea1h.database.entity.UserEntity
import ymsli.com.ea1h.model.UserProfileDetailResponse
import java.lang.StringBuilder
import javax.inject.Inject

class EA1HSharedPreferences @Inject constructor(
    private val prefs: SharedPreferences
) {
    companion object {
        const val IMEI_TAG = "IMEI"
        const val USER_ID_TAG = "USER_ID"
        const val EMAIL_TAG = "EMAIL"
        const val NAME_TAG = "NAME"
        const val AUTH_TOKEN_TAG = "AUTH_TOKEN"
        const val IS_IGNITION_ON = "IS_IGNITION_ON"
        const val BRAKE_COUNT = "BRAKE_COUNT"
        const val IS_CONNECTED_TAG = "IS_CONNECTED_TAG"
        const val ELOCK_CHAR_1 = "ELOCK_CHAR_1"
        const val ELOCK_CHAR_2 = "ELOCK_CHAR_2"
        const val ELOCK_CHAR_3 = "ELOCK_CHAR_3"
        const val ELOCK_CHAR_4 = "ELOCK_CHAR_4"

        const val USER_FULL_NAME = "userFullName"
        const val USER_EMAIL = "userEmail"
        const val USER_PROFILE_PIC = "userProfilePic"
        const val EMPTY_STRING = ""

        const val RANDOM_NUMBER = "RANDOM_NUMBER"
        const val BIKE_LIST_LAST_SYNC = "BIKE_LIST_LAST_SYNC"
        const val TRIP_JOB_ID = "TRIP_JOB_ID"
        const val BT_LOG_JOB_ID = "BT_LOG_JOB_ID"
        const val LETS_START_STATUS = "LETS_START_STATUS"
        const val LOGGED_IN_STATUS = "LOGGED_IN_STATUS"
        const val TRIP_LIST_LAST_SYNC = "TRIP_LIST_LAST_SYNC"
        const val APP_INIT_STATUS = "APP_INIT_STATUS"
        const val PASSWORD = "PASSWORD"
        const val FCM_TOKEN = "FCM_TOKEN"
        const val BIKE_BT_ADDRESS = "BIKE_BT_ADDRESS"
        const val FCM_TOPIC_SUBSCRIPTION_STATUS = "FCM_TOPIC_SUBSCRIPTION_STATUS"
        const val DONT_ASK_AGAIN = "DONT_ASK_AGAIN"

        const val RESTRICT_NETWORK_CALL_FLAG = "RESTRICT_NETWORK_CALL_FLAG"
        const val RESTRICT_USER_LOGIN_FLAG = "RESTRICT_USER_LOGIN_FLAG"

        const val ELOCK_TOGGLE_KEY = "ELOCK_TOGGLE_KEY"
        const val SCOOTER_KEY = "SCOOTER_KEY"
        const val ELOCK_STATUS_KEY = "ELOCK_STATUS_KEY"
        const val PROJECT_DETAILS_LAST_UPDATED_ON = "PD_LAST_UPDATED_ON"
    }

    fun setRandomNumber(byteArray: ByteArray){
        prefs.edit().putString(RANDOM_NUMBER,byteArray.toString())
    }

    fun getRandomNumber():String?{
        return prefs.getString(RANDOM_NUMBER,null)
    }

    fun setAndroidId(imei: String) {
        prefs.edit().putString(IMEI_TAG, imei).commit()
    }

    fun getAndroidId(): String? = prefs.getString(IMEI_TAG, null)

    fun storeUserDataInSharedPref(registrationResponse: UserEntity) {
        prefs.edit().putString(USER_ID_TAG, registrationResponse.userId).apply()
        prefs.edit().putString(AUTH_TOKEN_TAG, registrationResponse.token).apply()
        prefs.edit().putString(NAME_TAG, registrationResponse.name).apply()
    }

    fun storeUserEmailInSharedPref(email: String?){
        prefs.edit().putString(EMAIL_TAG, email).apply()
    }

    fun getUserEmailFromSharedPref():String = prefs.getString(EMAIL_TAG, null).toString()

    fun getUserDataFromSharedPref(): UserEntity {
        return UserEntity(
            prefs.getString(AUTH_TOKEN_TAG, null).toString(),
            prefs.getString(USER_ID_TAG, null).toString(),
            reLoginFailure = false
        )
    }

    fun clearUserDataFromSharedPred() {
        prefs.edit().putString(USER_ID_TAG, null).apply()
        prefs.edit().putString(AUTH_TOKEN_TAG, null).apply()
        prefs.edit().putString(NAME_TAG, null).apply()
    }

    fun logIgnition(isIgnited: Boolean){
        prefs.edit().putBoolean(IS_IGNITION_ON, isIgnited).apply()
    }

    fun getIgnition(): Boolean = prefs.getBoolean(IS_IGNITION_ON,false)

    fun logConnection(isConnected: Boolean){
        prefs.edit().putBoolean(IS_CONNECTED_TAG, isConnected).apply()
    }

    fun getConnectionStatus(): Boolean = prefs.getBoolean(IS_CONNECTED_TAG,false)

    //region brake count manipulation
    fun increaseBrakeCount() {
        var currentCount = getBrakeCount()
        currentCount++
        prefs.edit().putInt(BRAKE_COUNT,currentCount).apply()
    }

    fun resetBrakeCount() = prefs.edit().putInt(BRAKE_COUNT,0).apply()

    fun getBrakeCount(): Int = prefs.getInt(BRAKE_COUNT,0)
    //endregion brake count manipulation

    //region elock pattern local operations
    fun storeELockPattern(pattern: CharArray){
        prefs.edit().putString(ELOCK_CHAR_1,pattern[0].toString()).commit()
        prefs.edit().putString(ELOCK_CHAR_2,pattern[1].toString()).commit()
        prefs.edit().putString(ELOCK_CHAR_3,pattern[2].toString()).commit()
        prefs.edit().putString(ELOCK_CHAR_4,pattern[3].toString()).commit()
    }

    fun getELockPattern(): CharArray?{
        val sb = StringBuilder()
        if(prefs.getString(ELOCK_CHAR_1,null)==null){
            return null
        }
        sb.append(prefs.getString(ELOCK_CHAR_1,null))
        sb.append(prefs.getString(ELOCK_CHAR_2,null))
        sb.append(prefs.getString(ELOCK_CHAR_3,null))
        sb.append(prefs.getString(ELOCK_CHAR_4,null))
        return sb.toString().toCharArray()
    }

    /**
     * Stores the user profile details information in the prefs.
     * this data is saved on the successful response of the getUserDetails API.
     *
     * @author VE00YM023
     */
    fun saveUserProfileDetailResponse(data: UserProfileDetailResponse) {
        //prefs.edit().putString(USER_FULL_NAME, data.fullName).apply()
        prefs.edit().putString(USER_FULL_NAME, data.fullName).commit()
        prefs.edit().putString(USER_EMAIL, data.email).apply()
        prefs.edit().putString(USER_PROFILE_PIC, data.profilePic).apply()
    }

    fun removeUserProfileDetail(){
        prefs.edit().putString(USER_FULL_NAME, null).apply()
        prefs.edit().putString(USER_EMAIL, null).apply()
        prefs.edit().putString(USER_PROFILE_PIC, null).apply()
    }

    fun getUserProfileDetail() = UserProfileDetailResponse(
        prefs.getString(USER_EMAIL, null),
        null,
        prefs.getString(USER_FULL_NAME, null),
        null, null, null, null, null,
        prefs.getString(USER_PROFILE_PIC, null),
        null, null, null
    )

    fun getBikeListLastSync(): Long? = prefs.getLong(BIKE_LIST_LAST_SYNC, 0)
    fun setBikeListLastSync(syncTime: Long) = prefs.edit().putLong(BIKE_LIST_LAST_SYNC, syncTime).apply()

    fun getTripListLastSync(): Long? = prefs.getLong(TRIP_LIST_LAST_SYNC, 0)
    fun setTripListLastSync(syncTime: Long) = prefs.edit().putLong(TRIP_LIST_LAST_SYNC, syncTime).apply()

    fun getTripJobId(): Int? = prefs.getInt(TRIP_JOB_ID, 0)
    fun setTripJobId(id: Int) = prefs.edit().putInt(TRIP_JOB_ID, id).apply()
    fun getBTLogJobId(): Int? = prefs.getInt(BT_LOG_JOB_ID, 0)
    fun setBTLogJobId(id: Int) = prefs.edit().putInt(BT_LOG_JOB_ID, id).apply()
    fun getLetsStartStatus() = prefs.getBoolean(LETS_START_STATUS, false)
    fun setLetsStartStatus(status: Boolean) = prefs.edit().putBoolean(LETS_START_STATUS, status).apply()

    fun getLoggedInStatus() = prefs.getBoolean(LOGGED_IN_STATUS, false)
    fun setLoggedInStatus(status: Boolean) = prefs.edit().putBoolean(LOGGED_IN_STATUS, status).commit()
    fun getAuthToken() = prefs.getString(AUTH_TOKEN_TAG, null)

    fun getAppInitStatus() = prefs.getBoolean(APP_INIT_STATUS, false)
    fun setAppInitStatus(status: Boolean) = prefs.edit().putBoolean(APP_INIT_STATUS, status).apply()

    fun getUserId() = prefs.getString(USER_ID_TAG, null)

    fun getFCMToken() = prefs.getString(FCM_TOKEN, null)
    fun setFCMToken(token: String) = prefs.edit().putString(FCM_TOKEN, token).apply()

    fun getUserPassword() = prefs.getString(PASSWORD, null)
    fun setUserPassword(pass: String) = prefs.edit().putString(PASSWORD, pass).apply()

    fun setAuthToken(auth: String) = prefs.edit().putString(AUTH_TOKEN_TAG, auth).apply()

    //used for experimental trip creation
    fun setIgnitionExp(isIgnited: Boolean) = prefs.edit().putBoolean("IGNITION_EXP",isIgnited).apply()

    fun getIgnitionExp(): Boolean = prefs.getBoolean("IGNITION_EXP",false)
    fun getSubscribedToFCMTopic() = prefs.getBoolean(FCM_TOPIC_SUBSCRIPTION_STATUS, false)
    fun setSubscribedToFCMTopic(status: Boolean){
        prefs.edit().putBoolean(FCM_TOPIC_SUBSCRIPTION_STATUS, status).apply()
    }

    fun getDontAskAgain(): Boolean = prefs.getBoolean(DONT_ASK_AGAIN, false)
    fun setDontAskAgain(value: Boolean) = prefs.edit().putBoolean(DONT_ASK_AGAIN, value).apply()

    fun getRestrictNetworkCalls(): Boolean = prefs.getBoolean(RESTRICT_NETWORK_CALL_FLAG, false)
    fun setRestrictNetworkCalls(value: Boolean) = prefs.edit().putBoolean(RESTRICT_NETWORK_CALL_FLAG, value).commit()

    fun getHardLogout(): Boolean = prefs.getBoolean(RESTRICT_USER_LOGIN_FLAG, false)
    fun setHardLogout(value: Boolean) = prefs.edit().putBoolean(RESTRICT_USER_LOGIN_FLAG, value).commit()

    fun getBikeBluetoothAddress() = prefs.getString(BIKE_BT_ADDRESS, null)
    fun setBikeBluetoothAddress(btAdd: String?) = prefs.edit().putString(BIKE_BT_ADDRESS, btAdd).apply()

    //region ELock disable parameters
    fun setIfElockCanBeDisabled(elockAbleParam: Boolean) = prefs.edit().putBoolean(ELOCK_TOGGLE_KEY,elockAbleParam).commit()

    fun getIfElockCanBeDisabled(): Boolean = prefs.getBoolean(ELOCK_TOGGLE_KEY,false)

    fun setIfScooter(isScooter: Boolean) = prefs.edit().putBoolean(SCOOTER_KEY,isScooter).commit()

    fun getIfScooter(): Boolean = prefs.getBoolean(SCOOTER_KEY,false)

    fun getELockEnabledStatus(): Boolean = prefs.getBoolean(ELOCK_STATUS_KEY,false)

    fun setELockEnabledStatus(status: Boolean) =prefs.edit().putBoolean(ELOCK_STATUS_KEY,status).commit()

    /**
     * Getter and setter last updated on field for project/detail API request.
     */
    fun getProjectDetailsLastSync() = prefs.getString(PROJECT_DETAILS_LAST_UPDATED_ON, null)
    fun setProjectDetailsLastSync(value: String?){
        prefs.edit().putString(PROJECT_DETAILS_LAST_UPDATED_ON, value).commit()
    }
}