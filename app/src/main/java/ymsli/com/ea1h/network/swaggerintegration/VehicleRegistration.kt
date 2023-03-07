package ymsli.com.ea1h.network.swaggerintegration

import com.google.gson.Gson
import io.reactivex.Observable
import io.swagger.client.apis.BikeControllerApi
import io.swagger.client.apis.MfecuControllerApi
import io.swagger.client.apis.OtpControllerApi
import io.swagger.client.models.ChassisVerificationDTO
import io.swagger.client.models.EcuAdvertising
import io.swagger.client.models.ValidateOtpDTO
import io.swagger.client.models.ValidateQRCodeDTO
import org.json.JSONObject
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.utils.common.Constants
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleRegistration @Inject constructor(
    private val otpControllerApi: OtpControllerApi,
    private val bikeControllerApi: BikeControllerApi,
    private val mfecuControllerApi: MfecuControllerApi,
    private val gson: Gson
){

    companion object{
        const val SUCCESS_STRING = "Success"
        const val AUTH_KEY = "na"
        private const val JSON_KEY_PHONE_NO = "phoneNo"
    }

    /** this method triggers the OTP at the requested number */
    fun verifyChassis(chassisVerificationDTO: ChassisVerificationDTO, authKey: String): Observable<String> {
        return Observable.create {
            try {
                val response = bikeControllerApi.validateChasisUsingPOST(chassisVerificationDTO,
                    authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    val phoneNum = (response.responseData as Map<String,String>)[JSON_KEY_PHONE_NO]
                    it.onNext(phoneNum.toString())
                } else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception) { it.onError(ex) }
        }
    }

    /**
     * this verifies that QR code lies within
     * the range specified for MFECUs by IEEE
     */
    fun validateQRCode(qrCodeDTO: ValidateQRCodeDTO, authKey: String): Observable<String>{
        return Observable.create {
            try {
                val response = mfecuControllerApi.validateQRCodeUsingPOST(qrCodeDTO, authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    it.onNext(SUCCESS_STRING)
                } else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception) { it.onError(ex) }
        }
    }

    /**
     * this is responsible for bike registration
     */
    fun registerBike(validateOtpDTO: ValidateOtpDTO, authKey: String): Observable<BikeEntity>{
        return Observable.create {
            try {
                val response = otpControllerApi.validateOTPUsingPOST(validateOtpDTO,authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonString = JSONObject((response.responseData as Map<String,String>).toMap()).toString()
                    val bikeRegResponse:BikeEntity = gson.fromJson<BikeEntity>(jsonString,BikeEntity::class.java)
                    it.onNext(bikeRegResponse)
                } else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception) { it.onError(ex) }
        }
    }


    /** this method triggers the OTP at the requested number */
    fun saveAdvertisementsUsingPOST(body: kotlin.Array<EcuAdvertising>, authKey: String): Observable<String> {
        return Observable.create {
            try {
                val response = mfecuControllerApi.saveAdvertisementsUsingPOST(body,authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    it.onNext(Constants.DONE)
                } else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception) { it.onError(ex) }
        }
    }
}