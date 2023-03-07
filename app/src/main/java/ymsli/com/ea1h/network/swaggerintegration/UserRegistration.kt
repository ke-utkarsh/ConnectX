package ymsli.com.ea1h.network.swaggerintegration

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.swagger.client.apis.LoginControllerApi
import io.swagger.client.apis.OtpControllerApi
import io.swagger.client.apis.UserControllerApi
import io.swagger.client.infrastructure.ClientException
import io.swagger.client.models.LoginDTO
import io.swagger.client.models.OtpGenerateRequestPacket
import io.swagger.client.models.ValidateOtpDTO
import org.json.JSONObject
import ymsli.com.ea1h.database.entity.LoginResponse
import ymsli.com.ea1h.database.entity.UserEntity
import ymsli.com.ea1h.model.AppVersionResponse
import ymsli.com.ea1h.model.LoginFailureResponse
import ymsli.com.ea1h.model.UserProfileDetailResponse
import ymsli.com.ea1h.utils.common.Constants
import ymsli.com.ea1h.utils.common.Constants.DUMMY_AUTH
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRegistration @Inject constructor(
    private val otpControllerApi: OtpControllerApi,
    private val userControllerApi: UserControllerApi,
    private val loginControllerApi: LoginControllerApi,
    private val gson: Gson
){

    companion object{
        const val SUCCESS_STRING = "Success"
        const val AUTH_KEY = "na"
    }

    /** this method triggers the OTP at the requested number */
    fun triggerOtp(otpGenerateRequestPacket: OtpGenerateRequestPacket): Observable<String> {
        return Observable.create {
            try {
                val response = otpControllerApi.generateOTPUsingPOST(otpGenerateRequestPacket,
                    AUTH_KEY
                )
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    it.onNext(SUCCESS_STRING)
                } else it.onError(Exception(response.responseMessage))
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }
    }

    /**
     * this method converts the API call for user registration
     * it also parses the hashmap response to POJO
     */
    fun registerUser(validateOtpDTO: ValidateOtpDTO): Observable<UserEntity>{
        return Observable.create{
            try{
                val response = otpControllerApi.validateOTPUsingPOST(validateOtpDTO,
                    AUTH_KEY
                )
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    val jsonString = JSONObject((response.responseData as Map<String,String>).toMap()).toString()
                    val regResponse:UserEntity = gson.fromJson<UserEntity>(jsonString,UserEntity::class.java)
                    it.onNext(regResponse)
                } else it.onError(Exception(response.responseMessage))
            }
            catch (ex:Exception){
                it.onError(ex)
            }
        }
    }

    /**
     * this method registers user based on Social media platforms
     * like Google & Facebook
     */
    fun signUpUsingSocialMedia(loginDTO: LoginDTO): Observable<LoginResponse>{
        return Observable.create {
            try{
                val response = loginControllerApi.loginUsingPOST(loginDTO, AUTH_KEY)//userControllerApi.createUserUsingPOST(socialMediaSignUpDTO, AUTH_KEY)
                if(response.responseCode == HttpURLConnection.HTTP_OK){
                    val jsonString = JSONObject((response.responseData as Map<String,String>).toMap()).toString()
                    val regResponse:LoginResponse = gson.fromJson<LoginResponse>(jsonString,LoginResponse::class.java)
                    it.onNext(regResponse)
                }else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }

    fun forgotPassword(validateOtpDTO: ValidateOtpDTO): Observable<String>{
        return Observable.create {
            try{
                val response = otpControllerApi.validateOTPUsingPOST(validateOtpDTO, AUTH_KEY)
                if(response.responseCode == HttpURLConnection.HTTP_OK){
                    it.onNext(SUCCESS_STRING)
                }else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }

    fun loginUsingEmailPassword(loginDTO: LoginDTO): Observable<String>{
        return Observable.create {
            val postResponse = it
            try{
                val response = loginControllerApi.loginUsingPOST(loginDTO, DUMMY_AUTH)
                if(response.responseCode == HttpURLConnection.HTTP_OK){
                    val jsonString = JSONObject((response.responseData as Map<String,String>).toMap())
                    postResponse.onNext(jsonString.toString())
                }else it.onError(Exception(response.responseMessage))
            }
            catch (ex: ClientException){
                val jsonString = ex.message
                val statusToken = object : TypeToken<LoginFailureResponse?>() {}.type
                val result = Gson().fromJson<LoginFailureResponse>(jsonString.toString(), statusToken)
                it.onError(Exception(result.error.message))
            }
            catch(ex: Exception){
                it.onError(Exception(Constants.ERROR_TRY_AGAIN))
            }
        }
    }

    fun getUserProfileDetails(userId: String, authKey: String): Observable<UserProfileDetailResponse>{
        return Observable.create {
            try{
                val response = userControllerApi.getUserDetailsUsingGET(authKey,userId)
                if(response.responseCode==HttpURLConnection.HTTP_OK){
                    val jsonString = JSONObject((response.responseData as Map<String,String>).toMap()).toString()
                    val data = Gson().fromJson(jsonString, UserProfileDetailResponse::class.java)
                    it.onNext(data)
                }else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){ it.onError(ex) }
        }
    }
}