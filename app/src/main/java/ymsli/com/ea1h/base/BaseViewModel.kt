package ymsli.com.ea1h.base

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   February 14, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * BaseViewModel : This abstract class is the base view-model class. It contains common
 * codebase of all the view models used throughout the application.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.database.entity.*
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.*
import ymsli.com.ea1h.utils.rx.SchedulerProvider

abstract class BaseViewModel(
    protected val schedulerProvider: SchedulerProvider,
    protected val compositeDisposable: CompositeDisposable,
    protected val networkHelper: NetworkHelper,
    protected val eA1HRepository: EA1HRepository
):ViewModel() {

    var initiateDeviceDiscovery: MutableLiveData<Boolean> = MutableLiveData()
    val messageStringId: MutableLiveData<Resource<Int>?> = MutableLiveData()
    val messageString: MutableLiveData<Resource<String>?> = MutableLiveData()
    val messageStringIdEvent: MutableLiveData<Event<Resource<Int>>?> = MutableLiveData()
    val messageStringEvent: MutableLiveData<Event<String>> = MutableLiveData()

    val message: MutableLiveData<String> = MutableLiveData()
    var showProgress: MutableLiveData<Boolean> = MutableLiveData()
    var errorAcknowledgement: MutableLiveData<Int> = MutableLiveData()
    var errorAcknowledgementEvent: MutableLiveData<Event<Int>> = MutableLiveData()
    var errorAcknowledgementMessage: MutableLiveData<String> = MutableLiveData()

    var activeTripInRoom: MutableLiveData<TripEntity> = MutableLiveData()
    //handles OK click on alert dialog
    var dialogPositiveButtonAction: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var userAckSuccessDialog: MutableLiveData<Event<String>> = MutableLiveData()
    var userAlertDialog: MutableLiveData<Event<Int>> = MutableLiveData()
    var userBlockerDialog: MutableLiveData<Event<Int>> = MutableLiveData()
    var userErrorDialog: MutableLiveData<Event<String>> = MutableLiveData()
    var disconnectECUDialog: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var showBLEInfoDialog: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var disconnectConfirmation: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var connectionStatus: MutableLiveData<Boolean> = MutableLiveData()

    var disconnectMFECUTrigger: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var shallConnectAgain: MutableLiveData<Boolean> = MutableLiveData()
    var lastConnectedBikeRemoved: MutableLiveData<Event<Boolean>> = MutableLiveData()

    var explicitDisconnect: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var activateFilter: MutableLiveData<Boolean> = MutableLiveData()

    val btAddressForLogs: MutableLiveData<String> = MutableLiveData()
    val chassisNumberForLogs: MutableLiveData<String> = MutableLiveData()

    fun getHardLogout(): Boolean = eA1HRepository.getHardLogout()

    fun setHardLogout(value: Boolean) = eA1HRepository.setHardLogout(value)

    private companion object {
        private const val MIN_TRIP_DURATION = 120
    }

    abstract fun onCreate()

    /**
     * used to unsubscribe from the observer
     * once view-model is destroyed
     */
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    /**
     * reset brake count after a trip
     */
    fun resetBrakeCount() = eA1HRepository.resetBrakeCount()

    //check internet connection
    protected fun checkInternetConnection(): Boolean = networkHelper.isNetworkConnected()

    /**
     * insert user data in room
     */
    fun insertUserEntity(userEntity: UserEntity){
        GlobalScope.launch(Dispatchers.IO){
            eA1HRepository.insertUserEntity(userEntity)
        }
    }

    /**
     * get user related data from shared prefs
     */
    fun getUserEntity(): UserEntity = eA1HRepository.getUserDataFromSharedPref()

    /** Returns a LiveData view of UserEntity stored in Room */
    fun getUserEntityLive() = eA1HRepository.getUserEntityLive()

    fun getUserProfileDetail() = eA1HRepository.getUserProfileDetail()

    fun removeUserEntity(){
        GlobalScope.launch(Dispatchers.IO) {
            eA1HRepository.removeUserEntity()
        }
    }

    /**
     * When users logs out we don't clear trip or bike data.
     * that is done only if another user logs in.
     * we only clear following flags :-
     *
     * LOGGED_IN_STATUS ----> So that user is not directed to home screen, next time app starts
     * APP_INIT_STATUS -----> So that app data is initialized next time anyone logs in
     * PROJECT_DETAILS_LAST_SYNC ---> So that project/details API return complete data on next login
     *
     * @author VE00YM023
     */
    fun logoutCustomUser() = GlobalScope.launch(Dispatchers.IO) {
        eA1HRepository.setLoggedInStatus(false)
        eA1HRepository.setAppInitStatus(false)
        eA1HRepository.setProjectDetailsLastSync(null)
    }

    /** this will encrypt the input string */
    fun encryptData(input: String): String{
        return CryptoHandler.encrypt(input)
    }

    /** this will decrypt the input string */
    fun decryptData(input: String): String{
        return CryptoHandler.decrypt(input)
    }

    fun getTripJobId(): Int? = eA1HRepository.getTripJobId()
    fun setTripJobId(id: Int) = eA1HRepository.setTripJobId(id)
    fun getBTLogJobId(): Int? = eA1HRepository.getBTLogJobId()
    fun setBTLogJobId(id: Int) = eA1HRepository.setBTLogJobId(id)

    fun getLastConnectedBike(): LiveData<BikeEntity> = eA1HRepository.getLastConnectedBike()

    /**
     * removes last connected bike while user is still logged in
     */
    fun removeLastConnectedBike() {
        compositeDisposable.addAll(
            eA1HRepository.removeLastConnectedBike()
                .subscribeOn(schedulerProvider.io())
                .subscribe({
                    eA1HRepository.removeLastConnectedBike()
                    lastConnectedBikeRemoved.postValue(Event(true))
                },{
                    eA1HRepository.removeLastConnectedBike()
                    lastConnectedBikeRemoved.postValue(Event(true))
                })
        )
        eA1HRepository.removeLastConnectedBike()
    }

    /**
     * removes last connected bike when
     * user logs out of the app
     */
    fun removeLastConnectedBikeDuringLogout() {
        compositeDisposable.addAll(
            eA1HRepository.removeLastConnectedBike()
                .subscribeOn(schedulerProvider.io())
                .subscribe()
        )
        eA1HRepository.removeLastConnectedBike()
    }

    fun getSpecificCommand(commandId: Int): String? = eA1HRepository.getSpecificCommand(commandId)

    /**
     * triggered when ignition is turned off
     */
    fun completeOnGoingTrip(geocoder: Geocoder) {
        ViewUtils.completeOnGoingTrip(geocoder,eA1HRepository)
    }

    fun setLetsStartStatus(status: Boolean) = eA1HRepository.setLetsStartStatus(status)
    fun getLetsStartStatus() = eA1HRepository.getLetsStartStatus()

    /** Getter are setters for log in status flag, we use this flag to direct user
     *  to home screen or to login screen(if not logged in) */
    fun getLoggedInStatus() = eA1HRepository.getLoggedInStatus()
    fun setLoggedInStatus(status: Boolean) = eA1HRepository.setLoggedInStatus(status)


    /** Getter and setters for TripList API last sync time and BikeList API last
     *  sync time. we store the time value millis in GMT */
    fun setTripListLastSync(millis: Long) = eA1HRepository.setTripListLastSync(millis)
    fun getTripListLastSync() = eA1HRepository.getTripListLastSync()

    fun getBikeListLastSync() = eA1HRepository.getBikeListLastSync()
    fun setBikeListLastSync(millis: Long) = eA1HRepository.setBikeListLastSync(millis)

    /**
     * Getter and setters for AppInit Status.
     * We set the AppInitStatus to true when first sync cycle completes
     * @author VE00YM023
     */
    fun setAppInitStatus(status: Boolean) = eA1HRepository.setAppInitStatus(status)
    fun getAppInitStatus() = eA1HRepository.getAppInitStatus()

    fun getUserId() = eA1HRepository.getUserId()

    /** Getter and setters for fcm token shared pref field */
    fun getFCMToken() = eA1HRepository.getFCMToken()
    fun setFCMToken(token: String) = eA1HRepository.setFCMToken(token)

    /** Getter and setters for 'User password', it is required for relogin */
    fun getUserPassword() = eA1HRepository.getUserPassword()
    fun setUserPassword(pass: String) = eA1HRepository.setUserPassword(pass)
    fun updateReLoginFailureStatus(status: Boolean) = eA1HRepository.updateReLoginFailure(status)

    fun getMiscDataByKeyLiveData(key: String): LiveData<List<MiscDataEntity>> = eA1HRepository.getMiscDataByKeyLiveData(key)
    /**
     * used to log command when triggered in SQLite
     */
    fun logBTCommandInRoom(btCommandsLogEntity: BTCommandsLogEntity){
        GlobalScope.launch(Dispatchers.IO){
            btCommandsLogEntity.deviceId = eA1HRepository.getAndroidIdFromSharedPref()
            eA1HRepository.writeBluetoothCommandInRoom(btCommandsLogEntity)
        }
    }

    fun getSubscribedToFCMTopic() = eA1HRepository.getSubscribedToFCMTopic()
    fun setSubscribedToFCMTopic(status: Boolean) = eA1HRepository.setSubscribedToFCMTopic(status)

    fun getUserEmail(): String{
        return CryptoHandler.decrypt(getUserProfileDetail().email)
    }

    fun getKeyMiscData() = eA1HRepository.getMiscDataByKey(Constants.MISC_KEY_SCRN_SHOT_ENB)

    fun getMiscDataByKey(key: String) = eA1HRepository.getMiscDataByKey(key)

    fun getAndroidIdFromSharedPref():String? = eA1HRepository.getAndroidIdFromSharedPref()

    /**
     * Getter and setter for the dont ask again checkbox value in the background
     *  settings dialog in your vehicles screen.
     */
    fun getDontAskAgain() = eA1HRepository.getDontAskAgain()
    fun setDontAskAgain(value: Boolean) = eA1HRepository.setDontAskAgain(value)

    fun getBikeBluetoothAddress() = eA1HRepository.getBikeBluetoothAddress()
    fun setBikeBluetoothAddress(btAdd: String?) = eA1HRepository.setBikeBluetoothAddress(btAdd)

    //region ELock disable parameters
    fun setIfElockCanBeDisabled(elockAbleParam: Boolean) = eA1HRepository.setIfElockCanBeDisabled(elockAbleParam)

    fun getIfElockCanBeDisabled(): Boolean = eA1HRepository.getIfElockCanBeDisabled()

    fun setIfScooter(isScooter: Boolean) = eA1HRepository.setIfScooter(isScooter)

    fun getIfScooter(): Boolean = eA1HRepository.getIfScooter()

    fun getELockEnabledStatus(): Boolean = eA1HRepository.getELockEnabledStatus()

    fun setELockEnabledStatus(status: Boolean) = eA1HRepository.setELockEnabledStatus(status)
    //endregion ELock disable parameters

    fun getProjectDetailsLastSync() = eA1HRepository.getProjectDetailsLastSync()
    fun setProjectDetailsLastSync(value: String) = eA1HRepository.setProjectDetailsLastSync(value)
}