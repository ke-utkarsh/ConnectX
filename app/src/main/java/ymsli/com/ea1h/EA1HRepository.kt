/**
 * Project Name : EA1H
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   February 24, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * EA1HRepository : This is the repository class and will act as single point of
 *                  contact to fetch data from shared preference, room or network
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

package ymsli.com.ea1h

import androidx.lifecycle.LiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.swagger.client.apis.*
import io.swagger.client.models.*
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import ymsli.com.ea1h.adapters.VehicleListAdapter
import ymsli.com.ea1h.database.EA1HDatabase
import ymsli.com.ea1h.database.EA1HSharedPreferences
import ymsli.com.ea1h.database.entity.*
import ymsli.com.ea1h.model.AppVersionResponse
import ymsli.com.ea1h.model.DAPIoTFileUploadResponse
import ymsli.com.ea1h.model.UserProfileDetailResponse
import ymsli.com.ea1h.network.NetworkService
import ymsli.com.ea1h.network.swaggerintegration.MiscHandler
import ymsli.com.ea1h.network.swaggerintegration.TripHandler
import ymsli.com.ea1h.network.swaggerintegration.UserRegistration
import ymsli.com.ea1h.network.swaggerintegration.VehicleRegistration
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Constants.API_FAILED_MESSAGE
import ymsli.com.ea1h.utils.common.Constants.AUTH_TOKEN_NOT_AVAILABLE_ERROR
import ymsli.com.ea1h.utils.common.Constants.DEFAULT_LOG_TBL_MX_SIZE
import ymsli.com.ea1h.utils.common.Constants.DUMMY_AUTH
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_APP_STATUS
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_BIKE_OBJECT
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_TRIP_DETAILS
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_TRIP_OBJECT
import ymsli.com.ea1h.utils.common.Constants.MISC_KEY_LOG_TBL_MX_SIZE
import ymsli.com.ea1h.utils.common.CryptoHandler
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EA1HRepository @Inject constructor(
    private val ea1hDatabase: EA1HDatabase,
    private val userRegistration: UserRegistration,
    private val vehicleRegistration: VehicleRegistration,
    private val tripHandler: TripHandler,
    private val miscHandler: MiscHandler,
    private val sharedPreferences: EA1HSharedPreferences,
    private val networkService: NetworkService
){
    fun getUserDataFromSharedPref(): UserEntity = sharedPreferences.getUserDataFromSharedPref()

    //region trip methods
    fun getOnGoingTrip():Array<TripEntity> = ea1hDatabase.tripDao().getOnGoingTrip()

    fun getOnGoingTripLiveData():LiveData<Array<TripEntity>> = ea1hDatabase.tripDao().getOnGoingTripLiveData()

    fun getLastTrip():Array<TripEntity> = ea1hDatabase.tripDao().getLastTrip()

    fun appendExistingTrip(tripId: Int?) = ea1hDatabase.tripDao().appendExistingTrip(tripId)

    fun insertNewTrip(tripEntity: Array<TripEntity>) {
        ea1hDatabase.tripDao().insertNewTrip(*tripEntity)
        ea1hDatabase.tripDao().setAPITripsColumns()
    }

    fun insertNewTrip(tripEntity: TripEntity) {
        ea1hDatabase.tripDao().insertNewTrip(tripEntity)
    }

    fun completeOnGoingTrip(addLine: String) = ea1hDatabase.tripDao().completeOnGoingTrip(addLine)

    fun getAllTripsLiveByUser(userId: String): LiveData<Array<TripEntity>> {
        return ea1hDatabase.tripDao().getAllTripsLive(userId)
    }

    fun getUnsyncedTrips(): Array<TripEntity> = ea1hDatabase.tripDao().getUnsyncedTrips()


    fun updateUnsyncedTrip(tripId: String) = ea1hDatabase.tripDao().updateUnsyncedTrip(tripId)

    fun syncTripsWithServer(trips: Array<TripHistory>): Observable<Int> = tripHandler.uploadTrip(trips,sharedPreferences.getUserDataFromSharedPref().token)

    fun insertNewLocations(locations: Array<LatLongEntity>) = ea1hDatabase.latLongDao().insertNewLocation(*locations)

    fun insertNewLocation(location: LatLongEntity) = ea1hDatabase.latLongDao().insertNewLocation(location)

    fun getTripLastLocation(tripId: String):LatLongEntity = ea1hDatabase.latLongDao().getTripLastLocation(tripId)

    fun getAllValues():Array<LatLongEntity> = ea1hDatabase.latLongDao().getAllValues()

    fun getLocationOfTrip(tripId: String): Array<LatLongEntity> = ea1hDatabase.latLongDao().getLocationOfTrip(tripId)

    fun getTripFirstLocation(tripId: String):LatLongEntity = ea1hDatabase.latLongDao().getTripFirstLocation(tripId)

    fun insertNewGyro(gyro: GyroEntity) =  ea1hDatabase.gyroDao().insertNewGyro(gyro)

    fun insertNewAccel(accelerometerEntity: AccelerometerEntity) = ea1hDatabase.accelerometerDao().insertNewAccel(accelerometerEntity)

    //endregion trip methods

    fun triggerOtp(otpGenerateRequestPacket: OtpGenerateRequestPacket): Observable<String> = userRegistration.triggerOtp(otpGenerateRequestPacket)

    fun setImeiNumberInSharedPref(imei: String){
        sharedPreferences.setAndroidId(imei)
    }

    fun getAndroidIdFromSharedPref():String? = sharedPreferences.getAndroidId()

    fun registerUser(validateOtpDTO: ValidateOtpDTO) = userRegistration.registerUser(validateOtpDTO)

    fun storeUserDataInSharedPref(registrationResponse: UserEntity) = sharedPreferences.storeUserDataInSharedPref(registrationResponse)

    fun storeUserEmailInSharedPref(email: String?) = sharedPreferences.storeUserEmailInSharedPref(email)

    fun getUserEmailFromSharedPref(): String? = sharedPreferences.getUserEmailFromSharedPref()

    fun signUpUsingSocialMedia(loginDTO: LoginDTO) = userRegistration.signUpUsingSocialMedia(loginDTO)

    /**
     * handles insertion for user entities
     */
    fun insertUserEntity(userEntity: UserEntity){
        ea1hDatabase.userDao().insert(userEntity)
    }

    /**
     * removes user data which is logged in
     */
    fun removeUserEntity(){
        ea1hDatabase.userDao().removeUserEntity()
    }

    fun deleteSyncedTrips() = ea1hDatabase.tripDao().deleteSyncedTrips()

    fun clearUserDataFromSharedPred(){
        sharedPreferences.clearUserDataFromSharedPred()
    }

    fun forgotPassword(validationOtpDTO: ValidateOtpDTO): Observable<String> = userRegistration.forgotPassword(validationOtpDTO)

    fun loginUsingEmailPassword(loginDTO: LoginDTO): Observable<String> = userRegistration.loginUsingEmailPassword(loginDTO)

    fun insertBikeData(bikeEntities: Array<BikeEntity>) = ea1hDatabase.bikeDao().insertBike(*bikeEntities)

    fun getAllBikes(): LiveData<Array<BikeEntity>> = ea1hDatabase.bikeDao().retrieveBikes()

    // region bike registration
    /**
     * Listed below are API calls for new Vehicle registration
     */
    fun verifyChassis(chassisVerificationDTO: ChassisVerificationDTO): Observable<String> {
        return vehicleRegistration.verifyChassis(chassisVerificationDTO,getUserDataFromSharedPref().token)
    }

    fun saveAdvertisementsUsingPOST(body: kotlin.Array<EcuAdvertising>): Observable<String>{
        return vehicleRegistration.saveAdvertisementsUsingPOST(body,getUserDataFromSharedPref().token)
    }

    fun validateQRCode(qrCodeDTO: ValidateQRCodeDTO): Observable<String> = vehicleRegistration.validateQRCode(qrCodeDTO,getUserDataFromSharedPref().token)

    fun registerBike(validateOtpDTO: ValidateOtpDTO): Observable<BikeEntity> = vehicleRegistration.registerBike(validateOtpDTO,getUserDataFromSharedPref().token)

    // endregion bike registration

    //region active bike/mfecu details
    fun setLastConnectedBike(chassisNumber: String) {
        val bikes = ea1hDatabase.bikeDao().getAllBikes()
        for(b in bikes){
            if(b.isLastConnected){
                ea1hDatabase.bikeDao().hardResetLastConnectedBike(b.chasNum)
            }
        }
        ea1hDatabase.bikeDao().setLastConnectedBike(chassisNumber)
    }

    fun removeLastConnectedBike():Single<Int> {
        return ea1hDatabase.bikeDao().resetLastConnectedBike()
    }

    fun getLastConnectedBike(): LiveData<BikeEntity> = ea1hDatabase.bikeDao().getLastConnectedBike()

    fun getLastConnectedBikeForService(): BikeEntity = ea1hDatabase.bikeDao().getLastConnectedBikeForService()
    //endregion active bike/mfecu details

    //region ignition operations

    fun logIgnition(isIgnited: Boolean) = sharedPreferences.logIgnition(isIgnited)

    fun getIgnition() = sharedPreferences.getIgnition()
    //endregion ignition operations

    //region brake count manipulation
    fun increaseBrakeCount() = sharedPreferences.increaseBrakeCount()

    fun getBrakeCount() = sharedPreferences.getBrakeCount()

    fun resetBrakeCount() = sharedPreferences.resetBrakeCount()
    //endregion brake count manipulation

    fun removeTrip(tripId: Int) = ea1hDatabase.tripDao().removeTrip(tripId)

    fun logConnection(isConnected: Boolean) = sharedPreferences.logConnection(isConnected)

    fun getConnectionStatus(): Boolean = sharedPreferences.getConnectionStatus()

    fun getMiscData(): Observable<ArrayList<MiscDataEntity>> = miscHandler.getMiscData(getUserDataFromSharedPref().token)

    fun storeMiscData(miscEntities: Array<MiscDataEntity>) = ea1hDatabase.miscDataDao().insertMiscData(*miscEntities)

    fun getMiscDataFromRoom(): Maybe<Array<MiscDataEntity>> = ea1hDatabase.miscDataDao().getMiscData()

    //endregion bt commands API and Misc Data like T&C, privacy policy

    fun getBikeChassis(make: String): List<String> = ea1hDatabase.bikeDao().getBikeChassis(make)

    fun getBikeMake(): List<String> = ea1hDatabase.bikeDao().getBikeMake()

    //region filter bike in room
    fun insertFilteredBike(filterBikeEntity: FilterBikeEntity) = ea1hDatabase.filterBikeDao().insert(filterBikeEntity)

    fun getBikeInFilter():LiveData<Array<FilterBikeEntity>> = ea1hDatabase.filterBikeDao().getBikeInFilter()

    fun clearSelectedBikeFromFilter() = ea1hDatabase.filterBikeDao().clearSelectedBikeFromFilter()

    fun removeAllBikes() = ea1hDatabase.bikeDao().removeAllBikes()
    //endregion filter bike in room

    //region user profile getter/setter

    fun getUserProfileDetails():Observable<UserProfileDetailResponse> = userRegistration.getUserProfileDetails(getUserDataFromSharedPref().userId,getUserDataFromSharedPref().token)

    fun logBluetoothCommands(logs : Array<LogBluetoothCommandDTO>): Single<String> = miscHandler.logBluetoothCommands(logs,getUserDataFromSharedPref().token)

    fun logBluetoothCommandsTemp(logs : Array<LogBluetoothCommandDTO>): Observable<String> = miscHandler.logBluetoothCommandsTemp(logs,getUserDataFromSharedPref().token)

    fun getAllBluetoothCommands(): Observable<ArrayList<BTCommandsEntity>> = miscHandler.getAllBluetoothCommands(getUserDataFromSharedPref().token,"Android")

    fun saveBluetoothCommandsInRoom(commands: Array<BTCommandsEntity>) = ea1hDatabase.btCommandsDao().insertBTCommands(*commands)

    fun getBTCommandsFromRoom(): Maybe<Array<BTCommandsEntity>> = ea1hDatabase.btCommandsDao().getBTCommands()

    fun getSpecificCommand(commandId: Int): String = ea1hDatabase.btCommandsDao().getSpecificCommand(commandId)

    fun getAllCommands(rowLimit: Int): Array<BTCommandsLogEntity> = ea1hDatabase.btCommandsLogDao().getAllCommands(rowLimit)

    fun deleteLogs() = ea1hDatabase.btCommandsLogDao().deleteLogs()

    /**
     * Deletes all the log entries having triggeredOn value greater than or equal to the
     * given parameter. this value must be smallest in all the synced logs.
     */
    fun deleteAllSyncedLogs(minimumTriggeredOn: Long, maximumTriggeredOn: Long){
        ea1hDatabase.btCommandsLogDao().deleteAllSyncedLogs(minimumTriggeredOn, maximumTriggeredOn)
    }

    fun storeELockPattern(pattern: CharArray) = sharedPreferences.storeELockPattern(pattern)

    fun getELockPattern(): CharArray? = sharedPreferences.getELockPattern()

    fun writeBluetoothCommandInRoom(btCommandsLogEntity: BTCommandsLogEntity) {
        ea1hDatabase.btCommandsLogDao().insert(btCommandsLogEntity)
        trimLogTableToMaxSize()
    }

    fun updateCommandStatus(isSuccessful: Boolean) = ea1hDatabase.btCommandsLogDao().updateCommandStatus(isSuccessful)

    fun getSliderImages() = ea1hDatabase.bikeDao().getSliderImages()

    fun insertSliderImages(images: Array<SliderImage>) = ea1hDatabase.bikeDao().insertSliderImage(*images)

    fun saveUserProfileDetailResponse(data: UserProfileDetailResponse) {
        sharedPreferences.saveUserProfileDetailResponse(data)
    }

    fun removeUserProfileDetail() = sharedPreferences.removeUserProfileDetail()

    fun getUserProfileDetail() = sharedPreferences.getUserProfileDetail()

    //region DAPIOT File methods
    fun storeFileInfo(file: DAPIoTFileEntity) = ea1hDatabase.dapIoTFileDao().storeFileInfo(file)

    fun removeFileEntry(fileName: String) = ea1hDatabase.dapIoTFileDao().removeFileEntry(fileName)

    fun getFileEntity(fileName: String): DAPIoTFileEntity? = ea1hDatabase.dapIoTFileDao().getFileEntity(fileName)

    fun updateFileEntity(id: Long,nextTry: Long,retryAttempts: Int) = ea1hDatabase.dapIoTFileDao().updateFileEntity(id,nextTry, retryAttempts)

    fun getUnsyncedFiles(): Array<DAPIoTFileEntity> = ea1hDatabase.dapIoTFileDao().getUnsyncedFiles()

    fun sentLocationDataToDAPIoTServer(xuid:String,contentDisposition: String,fileBody: RequestBody): Observable<DAPIoTFileUploadResponse> {
        if(!getRestrictNetworkCalls()){
            return networkService.sentLocationDataToDAPIoTServer("application/octet-stream",
                "application/octet-stream",xuid,contentDisposition,fileBody)
        }
        else throw kotlin.IllegalStateException("API calling is blocked due to older app version.")
    }

    fun getUnsyncedGyroEntity(): Array<GyroEntity> = ea1hDatabase.gyroDao().getUnsyncedGyroEntity()

    fun removeSyncedGyroData(id: Long) = ea1hDatabase.gyroDao().removeSyncedGyroData(id)

    fun clearGyroTable() = ea1hDatabase.gyroDao().clearGyroTable()

    fun getUnsyncedAccelEntity(): Array<AccelerometerEntity> = ea1hDatabase.accelerometerDao().getUnsyncedAccelEntity()

    fun removeSyncedAccelData(id: Long) = ea1hDatabase.accelerometerDao().removeSyncedAccelData(id)

    fun clearAccelTable() = ea1hDatabase.accelerometerDao().clearAccelTable()

    fun getCCUIDForTrip(tripId: String): String?{
        try {
            val chassisNumber = ea1hDatabase.tripDao().getChassisNumber(tripId)
            val bikeEntity = ea1hDatabase.bikeDao().getBikeModelDetails(chassisNumber)
            return bikeEntity?.ccuId
        }
        catch (ex: Exception){
            FirebaseCrashlytics.getInstance().recordException(ex)
            return null
        }
    }

    fun getUnsyncedDAPTrips(): Array<LatLongEntity> = ea1hDatabase.latLongDao().getUnsyncedTrips()

    fun updateTripParameter(locationId: Long) = ea1hDatabase.latLongDao().updateTripParameter(locationId)
    //endregion DAPIOT File methods

    fun getLastTripLiveData(userId: String): LiveData<TripEntity> = ea1hDatabase.tripDao().getLastTripLiveData(userId)
    fun getContent(descriptionId: Int): LiveData<String> = ea1hDatabase.miscDataDao().getContent(descriptionId)

        fun getContentValue(descriptionId: Int):String = ea1hDatabase.miscDataDao().getContentValue(descriptionId)

    fun getBikeRegNum(chassisNumber: String): LiveData<String> = ea1hDatabase.bikeDao().getBikeRegNum(chassisNumber)
    fun getBikeRegNumModel(chassisNumber: String): LiveData<BikeEntity> = ea1hDatabase.bikeDao().getBikeRegNumModel(chassisNumber)

    fun getBikeModelDetails(chassisNumber: String): BikeEntity = ea1hDatabase.bikeDao().getBikeModelDetails(chassisNumber)

    fun removeExtraTrips() {
        val bikeIds = ea1hDatabase.tripDao().getBikeIds()
        bikeIds.forEach {
            it?.let{ ea1hDatabase.tripDao().removeExtraRecords(it) }
        }
    }

    fun setRandomNumber(byteArray: ByteArray){
        sharedPreferences.setRandomNumber(byteArray)
    }

    fun getRandomNumber():String?{
        return sharedPreferences.getRandomNumber()
    }

    fun getMiscDataByKey(key: String) = ea1hDatabase.miscDataDao().getContentByKey(key)

    fun getMiscDataByKeyLiveData(key: String): LiveData<List<MiscDataEntity>> = ea1hDatabase.miscDataDao().getContentByKeyLiveData(key)

    fun getBikeListLastSync(): Long = sharedPreferences.getBikeListLastSync() ?: 0
    fun setBikeListLastSync(sync: Long) = sharedPreferences.setBikeListLastSync(sync)

    fun getTripListLastSync(): Long = sharedPreferences.getTripListLastSync() ?: 0
    fun setTripListLastSync(sync: Long) = sharedPreferences.setTripListLastSync(sync)

    fun getTripJobId(): Int? = sharedPreferences.getTripJobId()
    fun setTripJobId(id: Int) = sharedPreferences.setTripJobId(id)
    fun getBTLogJobId(): Int? = sharedPreferences.getBTLogJobId()
    fun setBTLogJobId(id: Int) = sharedPreferences.setBTLogJobId(id)

    fun getUserEntityLive() = ea1hDatabase.userDao().getUserEntity()
    fun updateReLoginFailure(status: Boolean) = ea1hDatabase.userDao().updateReLoginFailure(status)

    /**
     * Returns a list of all the bikes associated with this user.
     * this list is displayed in the trip history filter.
     * @author VE00YM023
     */
    fun getVehiclesForFilter(): ArrayList<VehicleListAdapter.VehicleModel>{
        val bikes = ea1hDatabase.bikeDao().getAllBikes()
        return bikes.asSequence()
            .map { VehicleListAdapter.VehicleModel(it.regNum, it.chasNum, false) }
            .toCollection(arrayListOf())
    }

    fun getChassisNoByRegistrationNumber(regNumbers: List<String>): List<String>{
        return ea1hDatabase.bikeDao().getChassisNoByRegNumbers(regNumbers)
    }

    fun setLetsStartStatus(status: Boolean) = sharedPreferences.setLetsStartStatus(status)
    fun getLetsStartStatus() = sharedPreferences.getLetsStartStatus()

    fun increaseBrakeCountInLastTrip(){
        val onGoingTrip = ea1hDatabase.tripDao().getOnGoingTrip()
        if(onGoingTrip.size>0){
            var currentCount = onGoingTrip[0].breakCount
            if(currentCount==null){
                currentCount = 1
            }
            else{
                currentCount++
            }
            ea1hDatabase.tripDao().updateBrakeCount(currentCount,onGoingTrip[0].tripId)
        }
    }

    /**
     * Getter and setter for the login status.
     * once login API returns successful response, we update the login status to true.
     * we only set it to false when user logs out of the application.
     *
     * @author VE00YM023
     */
    fun getLoggedInStatus() = sharedPreferences.getLoggedInStatus()
    fun setLoggedInStatus(status: Boolean) = sharedPreferences.setLoggedInStatus(status)

    /**
     * Getter for Auth token.
     * we receive 'Auth Token' in the response of login API and we update it in the prefs.
     *
     * @throws IllegalStateException if AuthToken doesn't exist in the prefs
     * @author VE00YM023
     */
    fun getAuthToken(): String {
        sharedPreferences.getAuthToken()?.let { return it }
        throw IllegalStateException(AUTH_TOKEN_NOT_AVAILABLE_ERROR)
    }

    /**
     * Returns list of bikes associated with this user id.
     * @param lastUpdatedOn
     * @throws JSONException if parsing of the received data fails
     *
     * @author VE00YM023
     */
    fun getBikeList(lastUpdatedOn: Long): List<BikeEntity>{
        val requestModel = BikeListRequest(BuildConfig.VERSION_NAME,
            CryptoHandler.encrypt(getAndroidIdFromSharedPref())?:Constants.NA, lastUpdatedOn)
        val result = BikeControllerApi().getListAndUpdateAppVersionUsingPOST(requestModel, getAuthToken())
        val jsonString = JSONObject((result.responseData as Map<String,String>).toMap())
        val bikesJson: JSONArray = jsonString.getJSONArray(JSON_KEY_BIKE_OBJECT)
        val bikeList = object : TypeToken<List<BikeEntity?>?>() {}.type
        return Gson().fromJson(bikesJson.toString(), bikeList)
    }


    /**
     * Returns list of trips associated with the given bikes ids.
     * @param request
     * @throws JSONException if parsing of the received data fails
     *
     * @author VE00YM023
     */
    fun getTripList(request: TripHistoryRequestDTO): List<TripEntity>{
        val result = TripControllerApi().getTripHistoryUsingPOST(request, getAuthToken())
        val jsonString = JSONObject((result.responseData as Map<String,String>).toMap())
        val tripJson: JSONArray = jsonString.getJSONArray(JSON_KEY_TRIP_OBJECT)
        val tripList = object : TypeToken<List<TripEntity?>?>() {}.type
        return Gson().fromJson(tripJson.toString(), tripList)
    }

    /**
     * Returns list of trip details(Lats, Longs) associated with the given trip ids.
     * @param request
     * @throws JSONException if parsing of the received data fails
     *
     * @author VE00YM023
     */
    fun getTripDetail(request: TripDetailRequestDTO): List<TripDetailEntity>{
        val result = TripControllerApi().getTripDetailUsingPOST(request, getAuthToken())
        val jsonString = JSONObject((result.responseData as Map<String,String>).toMap())
        val tripJson: JSONArray = jsonString.getJSONArray(JSON_KEY_TRIP_DETAILS)
        val tripList = object : TypeToken<List<TripDetailEntity?>?>() {}.type
        return Gson().fromJson(tripJson.toString(), tripList)
    }

    /**
     * Retrieves the App status (update required or not) from the remote server
     * @param request
     * @throws JSONException if parsing of the received data fails
     *
     * @author VE00YM023
     */
    fun getAppStatus(request: AppVersionRequestDTO): AppVersionResponse{
        val result = AppVersionControllerApi().getAppStatusUsingPOST(request, DUMMY_AUTH)
        val jsonString = JSONObject((result.responseData as Map<String,String>).toMap())
        val statusJson = jsonString.getJSONObject(JSON_KEY_APP_STATUS)
        val statusToken = object : TypeToken<AppVersionResponse?>() {}.type
        return Gson().fromJson(statusJson.toString(), statusToken)
    }

    /**
     * Calls the API to resend the OTP for bike registration.
     * @param request
     * @throws RuntimeException if API returns some error response
     *
     * @author VE00YM023
     */
    fun resendOTP(request: OtpGenerateRequestPacket){
        val result = OtpControllerApi().generateOTPUsingPOST(request, getAuthToken())
        if(result.responseCode != 200) { throw RuntimeException(API_FAILED_MESSAGE) }
    }

    /**
     * Inserts trip details in room database.
     * @author VE00YM023
     */
    fun insertTripDetail(trips: List<TripDetailEntity>){
        ea1hDatabase.tripDetailDao().insertDetail(*trips.toTypedArray())
    }

    fun getTripEntity(tripId: String): TripEntity? = ea1hDatabase.tripDao().getTripEntity(tripId)

    fun getPotentialLastTrip(): TripEntity? = ea1hDatabase.tripDao().getPotentialLastTrip()

    fun updatePotentialTripEndCoordinates(potentialEndTime: Long,potentialLastLatitude: Double,potentialLastLongitude: Double,distanceCovered: Float, tripId: String) =
        ea1hDatabase.tripDao().updatePotentialTripEndCoordinates(potentialEndTime,potentialLastLatitude, potentialLastLongitude,distanceCovered, tripId)

    fun updateTripSourceAddress(tripId: String, startAddress: String): Single<Int> = ea1hDatabase.tripDao().updateTripSourceAddress(tripId,startAddress)

    fun updateTripDestinationAddress(tripId: String, endAddress: String): Single<Int> = ea1hDatabase.tripDao().updateTripDestinationAddress(tripId, endAddress)
    /**
     * Returns list of bike ids for the user
     */
    fun getAllBikeIds() = ea1hDatabase.bikeDao().getAllBikeIds()

    fun getAppInitStatus() = sharedPreferences.getAppInitStatus()
    fun setAppInitStatus(status: Boolean) = sharedPreferences.setAppInitStatus(status)

    fun deleteSyncedLatLongs() = ea1hDatabase.latLongDao().deleteSyncedLatLongs()

    fun getLocationForMapPlot(tripId: String):Array<LatLongEntity> = ea1hDatabase.latLongDao().getLocationForMapPlot(tripId)

    fun getUserId() = sharedPreferences.getUserId()

    fun getFCMToken() = sharedPreferences.getFCMToken()
    fun setFCMToken(token: String) = sharedPreferences.setFCMToken(token)

    fun getUserPassword() = sharedPreferences.getUserPassword()
    fun setUserPassword(pass: String) = sharedPreferences.setUserPassword(pass)

    fun setAuthToken(auth: String) = sharedPreferences.setAuthToken(auth)

    //used for experimental trip creation
    fun setIgnitionExp(isIgnited: Boolean) = sharedPreferences.setIgnitionExp(isIgnited)

    fun getIgnitionExp(): Boolean = sharedPreferences.getIgnitionExp()
    fun getSubscribedToFCMTopic() = sharedPreferences.getSubscribedToFCMTopic()
    fun setSubscribedToFCMTopic(status: Boolean) = sharedPreferences.setSubscribedToFCMTopic(status)

    /**
     * Updates the FCM token of this device on remote server,
     * this is usually done when FCM Token expires and we are notified using onNewToken.
     * @param request
     *
     * @author VE00YM023
     */
    fun updateFCMToken(request: DeviceUpdateRequestModel): ResponseBody {
        return DeviceControllerApi().updateFCMTokenUsingPOST(request, getAuthToken())
    }

    fun insertAdvPacket(advPacketEntity: AdvPacketEntity) = ea1hDatabase.advPacketDao().insertAdvPacket(advPacketEntity)

    fun getAdvPacket(): Array<AdvPacketEntity> = ea1hDatabase.advPacketDao().getAdvPacket()

    fun deleteAdvPacket(id: Long) = ea1hDatabase.advPacketDao().deleteLogs(id)

    fun fetchAllBikes() = ea1hDatabase.bikeDao().getAllBikes()

    fun getDontAskAgain() = sharedPreferences.getDontAskAgain()
    fun setDontAskAgain(value: Boolean) = sharedPreferences.setDontAskAgain(value)

    fun getRestrictNetworkCalls(): Boolean = sharedPreferences.getRestrictNetworkCalls()

    fun setRestrictNetworkCalls(value: Boolean) = sharedPreferences.setRestrictNetworkCalls(value)

    fun getHardLogout(): Boolean = sharedPreferences.getHardLogout()

    fun setHardLogout(value: Boolean) = sharedPreferences.setHardLogout(value)

    /**
     * Trims the bt_command_logs to max table size.
     * value for maxTableSize parameter is received from the project-descriptions API.
     * if API value is not available then we use default value.
     * @author VE00YM023
     */
    private fun trimLogTableToMaxSize(){
        val maxTableSize = getMiscDataByKeyOrDefault(MISC_KEY_LOG_TBL_MX_SIZE, DEFAULT_LOG_TBL_MX_SIZE)
        ea1hDatabase.btCommandsLogDao().trimToMaxSize(maxTableSize)
    }

    /**
     * Returns the value of given project description key.
     * if for some reason that value is not present then default value is returned.
     * @param key for which value is required
     * @author Balraj
     * @return project  key value or default if not available
     */
    private fun getMiscDataByKeyOrDefault(key: String, defValue: Int): Int{
        val list = getMiscDataByKey(key)
        if(list.isNullOrEmpty() ||
            list[0].descriptionValue.isNullOrEmpty()) {
            return defValue
        }
        return list[0].descriptionValue.toIntOrNull() ?: defValue
    }

    fun getBikeBluetoothAddress() = sharedPreferences.getBikeBluetoothAddress()
    fun setBikeBluetoothAddress(btAdd: String?) = sharedPreferences.setBikeBluetoothAddress(btAdd)

    //region ELock disable parameters
    fun setIfElockCanBeDisabled(elockAbleParam: Boolean) = sharedPreferences.setIfElockCanBeDisabled(elockAbleParam)

    fun getIfElockCanBeDisabled(): Boolean = sharedPreferences.getIfElockCanBeDisabled()

    fun setIfScooter(isScooter: Boolean) = sharedPreferences.setIfScooter(isScooter)

    fun getIfScooter(): Boolean = sharedPreferences.getIfScooter()

    fun getELockEnabledStatus(): Boolean = sharedPreferences.getELockEnabledStatus()

    fun setELockEnabledStatus(status: Boolean) =  sharedPreferences.setELockEnabledStatus(status)
    //endregion ELock disable parameters

    /**
     * Getter and setter for last updated on field in project/details API request.
     * initially this value is null, and API returns complete result.
     * after that we update this value on each API call, causing API to return only the records
     * that have been updated after API call.
     */
    fun getProjectDetailsLastSync() = sharedPreferences.getProjectDetailsLastSync()
    fun setProjectDetailsLastSync(value: String?) = sharedPreferences.setProjectDetailsLastSync(value)

    /**
     * Retrieves the project description details from server.
     * @param lastSync ('2020-07-30 12:12:12), retrieve records that have been updated at or after
     *        the given timestamp value, if null retrieves complete data
     * @author VE00YM023
     */
    fun getProjectDetails(lastSync: String?): List<MiscDataEntity>{
        val result = ProjectDescriptionDetailsControllerApi().getApiConfigurationUsingGET(getAuthToken(), lastSync)
        val jsonString = JSONArray((result.responseData as List<Map<String,String>>).toList())
        val miscList = object : TypeToken<List<MiscDataEntity?>?>() {}.type
        return Gson().fromJson(jsonString.toString(), miscList)
    }
}