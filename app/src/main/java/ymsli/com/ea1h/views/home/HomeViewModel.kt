package ymsli.com.ea1h.views.home

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM023)
 * @date    13/02/2020 2:00 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * HomeViewModel : This is the view model for home activity and its fragments
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import java.lang.RuntimeException
import androidx.lifecycle.LiveData
import io.swagger.client.models.RemoveBikeMappingRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gigya.android.sdk.Gigya
import com.gigya.android.sdk.GigyaCallback
import com.gigya.android.sdk.api.GigyaApiResponse
import com.gigya.android.sdk.network.GigyaError
import io.reactivex.disposables.CompositeDisposable
import io.swagger.client.apis.BikeControllerApi
import io.swagger.client.models.TripHistoryRequestDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ymsli.com.ea1h.EA1HRepository
import ymsli.com.ea1h.GigyaResponse
import ymsli.com.ea1h.R
import ymsli.com.ea1h.adapters.VehicleListAdapter
import ymsli.com.ea1h.base.BaseViewModel
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.SliderImage
import ymsli.com.ea1h.database.entity.TripEntity
import ymsli.com.ea1h.model.Pair
import ymsli.com.ea1h.model.TripHistoryFilterModel
import ymsli.com.ea1h.utils.NetworkHelper
import ymsli.com.ea1h.utils.common.*
import ymsli.com.ea1h.utils.common.Constants.MISC_KEY_HOME_HEADER_IMAGE_URL
import ymsli.com.ea1h.utils.rx.SchedulerProvider
import java.net.HttpURLConnection

open class HomeViewModel(schedulerProvider: SchedulerProvider,
                         compositeDisposable: CompositeDisposable,
                         private val gigya: Gigya<GigyaResponse>,
                         networkHelper: NetworkHelper, eA1HRepository: EA1HRepository)
    : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper, eA1HRepository) {

    private companion object {
        private const val IMAGE_URL = "imageUrl"
        private const val DATA_TYPE = "dataType"
        private const val MAX_RANGE_VALUE = 100
    }
    var hazardActivateField: MutableLiveData<Boolean> = MutableLiveData()
    var hazardDeactivateField: MutableLiveData<Boolean> = MutableLiveData()
    var activeBikeEntity: MutableLiveData<BikeEntity> = MutableLiveData()
    var profilePicBitmap: MutableLiveData<Bitmap?> = MutableLiveData()

    //button click listener
    var answerBackButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var eLockButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var hazardButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var locateBikeButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var parkingRecordButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var drivingHistoryButton: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var isHazardActive: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var isConnectionEstablished: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var isConnectionDestablished: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var eLockPattern: MutableLiveData<Event<String>> = MutableLiveData()
    var eLockWritten: MutableLiveData<Event<Boolean>> = MutableLiveData()
    //header image URL variable
    var headerImageURL: MutableLiveData<String> = MutableLiveData()

    var iconColorChangeOnConnected: MutableLiveData<Event<Boolean>> = MutableLiveData()
    var isBikeAdded: MutableLiveData<Boolean> = MutableLiveData()

    var setELockPattern:MutableLiveData<Event<IntArray>> = MutableLiveData()
    var getELockPattern:MutableLiveData<Event<Boolean>> = MutableLiveData()
    var resetPasswordEmail: String? = EMPTY_STRING
    var applyFilter: MutableLiveData<TripHistoryFilterModel> = MutableLiveData()
    var filterState = TripHistoryFilterModel()

    var proceedBikeConnection: MutableLiveData<Event<String>> = MutableLiveData()
    val bikeEntityMap: MutableMap<String, BikeEntity> = mutableMapOf()
    val changePasswordResponse: MutableLiveData<String> = MutableLiveData()
    val showBackgroundSettingsDialog: MutableLiveData<Event<Boolean>> = MutableLiveData()

    //parameters based on new connection methodology
    var bikeConnected:MutableLiveData<Event<Boolean>> = MutableLiveData()
    var batteryVoltage: MutableLiveData<Event<String>> = MutableLiveData()
    var brakeCount: MutableLiveData<Int> = MutableLiveData()

    //parameter to enable/disable ELock
    var enableELock:MutableLiveData<Event<Boolean>> = MutableLiveData()
    var eLockStatus:MutableLiveData<Event<Boolean>> = MutableLiveData()
    var initialELockStatus:MutableLiveData<Event<Boolean>> = MutableLiveData()

    var setScooterImage:MutableLiveData<Event<Boolean>> = MutableLiveData()
    var removeELockIcon:MutableLiveData<Event<Boolean>> = MutableLiveData()

    val loadScooterIcons: MutableLiveData<Boolean> = MutableLiveData()
    val loadBikeIcons: MutableLiveData<Boolean> = MutableLiveData()
    val elockSettingState: MutableLiveData<Int> = MutableLiveData()// 0 -> disabling, 1 -> enabling, 2 -> changing pattern
    val bikeDeletionAck: MutableLiveData<Event<Boolean>> = MutableLiveData()

    val isConnectionTimeout: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val setScooterUI: MutableLiveData<Event<Boolean>> = MutableLiveData()

    override fun onCreate() {

    }

    fun increaseBrakeCountInLastTrip(){
        GlobalScope.launch(Dispatchers.IO){
            eA1HRepository.increaseBrakeCountInLastTrip();
        }
    }
    /**
     * Return a LiveData object containing all the entries in the slider_image table.
     * @author VE00YM023
     */
    fun getViewPagerData() = eA1HRepository.getSliderImages()

    fun getLastTripLiveData(): LiveData<TripEntity> = eA1HRepository.getLastTripLiveData(getUserId()?:"")

    /**
     * Wrapper to call the API if the network connection is available.
     * @author VE00YM023
     */
    fun updateViewPagerData() {
        if(checkInternetConnection()) callApi()
    }

    /**
     * Calls the api to retrieve the data for slider images,
     * if the response is successful then stores the retrieved data in
     * the local storage.
     * @author VE00YM023
     */
    private fun callApi() = viewModelScope.launch(Dispatchers.IO) {
        try{
            val user = getUserEntity()
            val result = BikeControllerApi().getBikeImagesUsingGET(user.token)
            if(result.responseCode == HttpURLConnection.HTTP_OK){
                val data = (result.responseData as ArrayList<Map<String, String>>).toImageEntityList()
                eA1HRepository.insertSliderImages(data)
            }
        }
        catch (e: Exception){}
    }

    private fun ArrayList<Map<String, String>>.toImageEntityList(): Array<SliderImage>{
        val result = arrayListOf<SliderImage>()
        for(entry in this){
            val imageUrl = entry[IMAGE_URL].toString()
            val dataType = entry[DATA_TYPE].toString()
            result.add(SliderImage(imageUrl, dataType))
        }
        return result.toTypedArray()
    }

    /**
     * if user profile details are not there
     * in shared prefs, it pulls them from
     * server and store in Shared prefs
     */
    fun getUserProfileDetails() {
        if (checkInternetConnection()) {
            compositeDisposable.addAll(
                eA1HRepository.getUserProfileDetails()
                    .subscribeOn(schedulerProvider.io())
                    .subscribe({
                        if (it != null) eA1HRepository.saveUserProfileDetailResponse(it)
                    }, {
                        Log.d("Error", it.message?:Constants.NA)
                    })
            )
        }
    }

    /**
     * if bt commands are not stored in room,
     * API is called to pull BT Commands and store in room
     */
    fun getAllBluetoothCommands(){
        compositeDisposable.addAll(
            eA1HRepository.getBTCommandsFromRoom()
                .subscribeOn(schedulerProvider.computation())
                .subscribe({
                    if(it.size==0 && checkInternetConnection()){
                        compositeDisposable.addAll(
                            eA1HRepository.getAllBluetoothCommands()
                                .subscribeOn(schedulerProvider.io())
                                .subscribe({
                                    Log.d("Error","World")
                                    eA1HRepository.saveBluetoothCommandsInRoom(it.toTypedArray())
                                },{
                                    Log.d("Error",it.message?:Constants.NA)
                                })
                        )
                    }
                },{
                    Log.d("Error",it.message?:Constants.NA)
                })
        )
    }

    /**
     * gets content from MiscDataEntity
     */
    fun getContent(descriptionId: Int): LiveData<String> = eA1HRepository.getContent(descriptionId)

    /**
     * parses base64 string and show it to image view
     * of the user
     */
    fun parseBase64String(base64: String) = viewModelScope.launch(Dispatchers.Default) {
        try {
            val byteArr = Base64.decode(base64, Base64.DEFAULT)
            val parsedBitmap = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.size)
            profilePicBitmap.postValue(parsedBitmap)
        }
        catch (e: Exception) { profilePicBitmap.postValue(null)}
    }

    /**
     * loads image url in the header image of
     * nav drawer
     */
    fun getHeaderImageURL() = viewModelScope.launch(Dispatchers.IO) {
        val data = eA1HRepository.getMiscDataByKey(MISC_KEY_HOME_HEADER_IMAGE_URL)
        if(data.isNotEmpty() && !data[0].descriptionValue.isNullOrEmpty()){
            headerImageURL.postValue(data[0].descriptionValue)
        }
    }

    /**
     * gets bike registration number
     * based on its chassis number
     */
    fun getBikeRegNum(chassisNumber: String): LiveData<String> = eA1HRepository.getBikeRegNum(chassisNumber)

    fun getOnGoingTripLiveData():LiveData<Array<TripEntity>> = eA1HRepository.getOnGoingTripLiveData()

    //used for experimental trip creation
    fun setIgnitionExp(isIgnited: Boolean) = eA1HRepository.setIgnitionExp(isIgnited)
    fun getIgnitionExp(): Boolean = eA1HRepository.getIgnitionExp()
    fun getLastConnectedBikeForService() : BikeEntity? = eA1HRepository.getLastConnectedBikeForService()



    val apiCallActive: MutableLiveData<Boolean> = MutableLiveData()
    var lastConnectedBike: BikeEntity? = null

    fun getBikes(): LiveData<Array<BikeEntity>> = eA1HRepository.getAllBikes()

    /**
     * sets the last connected bike in room
     * when any bike is selected from RV list
     */
    fun setLastConnectedBike(chassisNumber: String) {
        GlobalScope.launch(Dispatchers.IO){
            eA1HRepository.setLastConnectedBike(chassisNumber)
        }
    }

    /**
     * Retrieves the list of trips from remote server and updates
     * the local storage, UI is updated when local storage is updated.
     * @author VE00YM023
     */
    fun fetchBikesFromServer(isCalledAfterDeletion: Boolean = false) = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
                return@launch
            }
            apiCallActive.postValue(true)
            val currentGMTMillis = Utils.getCurrentMillisInGMT()
            val bikes = eA1HRepository.getBikeList(lastUpdatedOn = 0)//hard refresh in order to get all data again
            setBikeListLastSync(currentGMTMillis)
            val lastConnectedBike = eA1HRepository.getLastConnectedBikeForService()
            eA1HRepository.removeAllBikes()// delete previous data of bikes
            eA1HRepository.insertBikeData(bikes.toTypedArray())

            if(isCalledAfterDeletion){
                val scooters = bikes.filter { it.vehType.equals(Constants.SCOOTER_STRING) }
                if(bikes.size==0){
                    setScooterUI.postValue(Event(false))
                }
                else setScooterUI.postValue(Event(scooters.size == bikes.size)) // update UI for scooter or bike
            }
            lastConnectedBike?.let {
                eA1HRepository.setLastConnectedBike(it.chasNum)
            }
            messageStringIdEvent.postValue(Event(Resource.success(R.string.bike_list_sync_success)))
            apiCallActive.postValue(false)
        }
        catch(cause: Exception){
            apiCallActive.postValue(false)
            messageStringIdEvent.postValue(Event(Resource.error(R.string.something_went_wrong)))
        }
    }

    fun updateCommandStatus(isSuccessful: Boolean) {
        GlobalScope.launch(Dispatchers.IO){
            eA1HRepository.updateCommandStatus(isSuccessful)
        }
    }

    /**
     * This function performs the password change action.
     * it first validates the both the password fields and then continues with
     * api call if all the validations are passed.
     * @author VE00YM023
     */
    fun changePassword(){
        if(!checkInternetConnection()){
            errorAcknowledgement.postValue(R.string.network_connection_error)
            return
        }
        showProgress.postValue(true)
        gigya.forgotPassword(resetPasswordEmail, object : GigyaCallback<GigyaApiResponse>() {
            override fun onSuccess(response: GigyaApiResponse?) {
                showProgress.postValue(false)
                changePasswordResponse.postValue(Constants.SUCCESS)
            }

            override fun onError(error: GigyaError?) {
                changePasswordResponse.postValue(error?.localizedMessage?: Constants.SOMETHING_WRONG)
                showProgress.postValue(false)
            }
        })
    }


    /**
     * Only retrieve trips associated with this userId,
     * trips for another user can exist in the Room, because we only remove synced data.
     */
    fun getLastTripsOfUser():LiveData<Array<TripEntity>>{
        return eA1HRepository.getAllTripsLiveByUser(getUserId()!!)
    }

    /**
     * Retrieves the list of vehicles to be displayed in the Filter Dialog.
     * BikeName of these vehicles can be used to filter the trip list.
     * @author VE00YM023
     */
    fun getVehicleListForFilter() = GlobalScope.launch(Dispatchers.IO) {
        val data = eA1HRepository.getVehiclesForFilter()
        if(filterState.vehicles == null){
            filterState.vehicles = data
        }
    }

    /**
     * Filters the trip list, using the criteria chosen by the user in Filter Dialog.
     * @author VE00YM023
     */
    fun filterTrips(trips: ArrayList<TripEntity>, filterModel: TripHistoryFilterModel): List<TripEntity>{
        var filteredList = filterByDate(trips, filterModel.date)
        filteredList = filterByDistance(filteredList, filterModel.distance)
        filteredList = filterByBreakCount(filteredList, filterModel.brakeCount)
        filteredList = filterBySpeed(filteredList, filterModel.avgSpeed)
        filteredList = filterByRegNo(filteredList, filterModel.vehicles!!.toList())
        return filteredList
    }

    /** Filters the trips list by the from date and to date */
    private fun filterByDate(trips: List<TripEntity>, date: Pair<Long, Long>) = trips.filter {
        if(it.startTime == null) true
        else if(date.from == 0L && date.to == 0L) true
        else if(date.from != 0L && date.to != 0L) {
            it.startTime!! >= date.from && it.startTime!! <= date.to
        }
        else if(date.from != 0L) it.startTime!! >= date.from
        else if(date.to != 0L) it.startTime!! <= date.to
        else true
    }

    /**
     * Filters the trips based on the 'Distance' filter item.
     * @author VE00YM023
     */
    private fun filterByDistance(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        val tripDistance = it.distanceCovered?.div(1000)
        filterByRange(tripDistance, predicate)
    }

    /**
     * Filters the trips based on the 'Break Count' filter item.
     * @author VE00YM023
     */
    private fun filterByBreakCount(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        filterByRange(it.breakCount?.toFloat(), predicate)
    }

    /**
     * Filters the trips based on the 'Average Speed' filter item.
     * @author VE00YM023
     */
    private fun filterBySpeed(trips: List<TripEntity>, predicate: Pair<Int, Int>) = trips.filter {
        filterByRange(it.averageSpeed, predicate)
    }

    /**
     * Filters the trip list by the registration number selected in the filter dialog.
     * as TripEntity doesn't contain the model name of Vehicle,
     * we first retrieve chassis numbers for the selected model names and then
     * we perform the comparison based on the chassis number.
     * @author VE00YM023
     */
    private fun filterByRegNo(trips: List<TripEntity>,
                              bikes: List<VehicleListAdapter.VehicleModel>): List<TripEntity>{
        val models = bikes.filter { it.selected }.map { it.regNo ?: EMPTY_STRING }.toCollection(arrayListOf())
        val chassisList = eA1HRepository.getChassisNoByRegistrationNumber(models.toList())
        return trips.filter {
            if(models.isNullOrEmpty()) true
            else when (it.chassisNumber) {
                null -> true
                else -> it.chassisNumber!! in chassisList
            }
        }
    }

    /**
     * Returns true if the given field value satisfies the range predicate.
     * @author VE00YM023
     */
    private fun filterByRange(fieldValue: Float?, predicate: Pair<Int, Int>) = when {
        fieldValue == null -> true
        predicate.from > predicate.to -> true
        predicate.to >= MAX_RANGE_VALUE -> fieldValue >= predicate.from
        predicate.from == predicate.to -> fieldValue == predicate.from.toFloat()
        else -> fieldValue >= predicate.from && fieldValue <= predicate.to
    }


    /**
     * Retrieves the list of trips from remote server and updates
     * the local storage, UI is updated when local storage is updated.
     * @author VE00YM023
     */
    fun fetchTripsFromServer() = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) {
                messageStringIdEvent.postValue(Event(Resource.error(R.string.network_connection_error)))
                return@launch
            }
            apiCallActive.postValue(true)
            val bikeIds = eA1HRepository.getAllBikeIds()
            val lastSync = 0L//making it 0 so to pull all the 20 trips and avoid inconsistency in multiple devices
            val tripHistoryRequest = TripHistoryRequestDTO(bikeIds.toTypedArray(), lastSync)
            val currentGMTMillis = Utils.getCurrentMillisInGMT()
            val data = eA1HRepository.getTripList(tripHistoryRequest)
            setTripListLastSync(currentGMTMillis)
            eA1HRepository.insertNewTrip(data.toTypedArray())
            messageStringIdEvent.postValue(Event(Resource.success(R.string.trip_sync_success)))
            apiCallActive.postValue(false)
        }
        catch(cause: Exception){
            apiCallActive.postValue(false)
            messageStringIdEvent.postValue(Event(Resource.error(R.string.something_went_wrong)))
        }
    }

    fun getBikeEntityMap(){
        val bikes = eA1HRepository.fetchAllBikes()
        bikes.forEach { bike -> bikeEntityMap[bike.chasNum] = bike }
    }

    fun getContentValue(descriptionId: Int): String{
        return eA1HRepository.getContentValue(descriptionId)
    }

    /**
     * Delete the bike from user account (Remove User-Bike association).
     * @author VE00YM023
     */
    fun removeBikeMapping(bike: BikeEntity) = GlobalScope.launch(Dispatchers.IO) {
        try{
            if(!checkInternetConnection()) {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
                return@launch
            }
            apiCallActive.postValue(true)
            val encryptedIMEI = encryptData(eA1HRepository.getAndroidIdFromSharedPref()!!)
            val request = RemoveBikeMappingRequest(arrayOf(bike.bikeId), encryptedIMEI)
            val result = BikeControllerApi().removeMappingUsingPOST(request, eA1HRepository.getAuthToken())
            if(HttpURLConnection.HTTP_OK == result.responseCode) {
                // ack user about deletion and refresh bikes list
                bikeDeletionAck.postValue(Event(true))
                fetchBikesFromServer(true)
            }
            else { throw RuntimeException("API RETURNED NON 200 RESPONSE CODE") }
        }
        catch(cause: Exception){
            apiCallActive.postValue(false)
            messageStringIdEvent.postValue(Event(Resource.error(R.string.something_went_wrong)))
        }
    }

    fun getVehicleByChassisNo(chassisNumber: String) = eA1HRepository.getBikeModelDetails(chassisNumber)
}
