package io.swagger.client.infrastructure

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.swagger.client.apis.LoginControllerApi
import io.swagger.client.models.LoginDTO
import okhttp3.*
import org.json.JSONObject
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.EA1HApplication
import ymsli.com.ea1h.database.entity.UserEntity
import ymsli.com.ea1h.utils.common.Constants.DUMMY_AUTH
import ymsli.com.ea1h.utils.common.Constants.HEADER_KEY_AUTH
import ymsli.com.ea1h.utils.common.Constants.JSON_KEY_USER_OBJECT
import ymsli.com.ea1h.utils.common.Constants.LOGIN_URL
import ymsli.com.ea1h.utils.common.Constants.NA
import ymsli.com.ea1h.utils.common.Constants.RE_LOGIN_FAILURE
import ymsli.com.ea1h.utils.common.CryptoHandler
import ymsli.com.ea1h.utils.common.InBoundInterceptor
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

open class ApiClient(val baseUrl: String) {
    companion object {
        protected const val ContentType = "Content-Type"
        protected const val Accept = "Accept"
        protected const val JsonMediaType = "application/json"
        protected const val FormDataMediaType = "multipart/form-data"
        protected const val XmlMediaType = "application/xml"

        @JvmStatic
        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(InBoundInterceptor())
            .connectTimeout(60,TimeUnit.SECONDS)
            .readTimeout(60,TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build()

        @JvmStatic
        var defaultHeaders: Map<String, String> by ApplicationDelegates.setOnce(mapOf(ContentType to JsonMediaType, Accept to JsonMediaType))

        @JvmStatic
        val jsonHeaders: Map<String, String> = mapOf(ContentType to JsonMediaType, Accept to JsonMediaType)
    }

    protected inline fun <reified T> requestBody(content: T, mediaType: String = JsonMediaType): RequestBody =
            when {
                content is File -> RequestBody.create(MediaType.parse(mediaType), content)

                mediaType == FormDataMediaType -> {
                    var builder = FormBody.Builder()
                    // content's type *must* be Map<String, Any>
                    @Suppress("UNCHECKED_CAST")
                    (content as Map<String, String>).forEach { key, value ->
                        builder = builder.add(key, value)
                    }
                    builder.build()
                }
                mediaType == JsonMediaType -> RequestBody.create(
                        MediaType.parse(mediaType), Serializer.moshi.adapter(T::class.java).toJson(content)
                )
                mediaType == XmlMediaType -> TODO("xml not currently supported.")

                // : this should be extended with other serializers
                else -> TODO("requestBody currently only supports JSON body and File body.")
            }

    protected inline fun <reified T : Any?> responseBody(body: ResponseBody?, mediaType: String = JsonMediaType): T? {
        if (body == null) return null
        return when (mediaType) {
            JsonMediaType -> Serializer.moshi.adapter(T::class.java).fromJson(body.source())
            else -> TODO()
        }
    }

    protected inline fun <reified T : Any?> request(requestConfig: RequestConfig, body: Any? = null): ApiInfrastructureResponse<T?> {
        val httpUrl = HttpUrl.parse(BuildConfig.BASE_URL) ?: throw IllegalStateException("baseUrl is invalid.")

        var urlBuilder = httpUrl.newBuilder()
                .addPathSegments(requestConfig.path.trimStart('/'))

        requestConfig.query.forEach { query ->
            query.value.forEach { queryValue ->
                urlBuilder = urlBuilder.addQueryParameter(query.key, queryValue)
            }
        }

        val url = urlBuilder.build()
        val shallRestrict = EA1HApplication.ea1hRepo.getRestrictNetworkCalls()
        if(shallRestrict){
            if(url.toString().contains("/app/version",true) || (url.toString().contains("/project/details",true))){
                //do nothing
            }
            else{
                throw kotlin.IllegalStateException("API calling is blocked due to older app version.")
            }
        }
        val headers = requestConfig.headers + defaultHeaders

        if (headers[ContentType] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Content-Type header. This is required.")
        }

        if (headers[Accept] ?: "" == "") {
            throw kotlin.IllegalStateException("Missing Accept header. This is required.")
        }

        // : support multiple contentType,accept options here.
        val contentType = (headers[ContentType] as String).substringBefore(";").toLowerCase()
        val accept = (headers[Accept] as String).substringBefore(";").toLowerCase()

        var request: Request.Builder = when (requestConfig.method) {
            RequestMethod.DELETE -> Request.Builder().url(url).delete()
            RequestMethod.GET -> Request.Builder().url(url)
            RequestMethod.HEAD -> Request.Builder().url(url).head()
            RequestMethod.PATCH -> Request.Builder().url(url).patch(requestBody(body, contentType))
            RequestMethod.PUT -> Request.Builder().url(url).put(requestBody(body, contentType))
            RequestMethod.POST -> Request.Builder().url(url).post(requestBody(body, contentType))
            RequestMethod.OPTIONS -> Request.Builder().url(url).method("OPTIONS", null)
        }

        headers.forEach { header -> request = request.addHeader(header.key, header.value) }

        val realRequest = request.build()
        val response = client.newCall(realRequest).execute()

        // : handle specific mapping types. e.g. Map<int, Class<?>>
        when {
            response.isRedirect -> return Redirection(
                    response.code(),
                    response.headers().toMultimap()
            )
            response.isInformational -> return Informational(
                    response.message(),
                    response.code(),
                    response.headers().toMultimap()
            )
            response.isSuccessful -> return Success(
                    responseBody(response.body(), accept),
                    response.code(),
                    response.headers().toMultimap()
            )
            response.isClientError -> {
                /* If an API call (Other than Login) has failed, then try to re-login */
                if (!(realRequest.url().toString().contains(LOGIN_URL, true)) &&
                    (HttpURLConnection.HTTP_FORBIDDEN == response.code() || HttpURLConnection.HTTP_UNAUTHORIZED == response.code()) &&
                     !getUserId().isNullOrEmpty() ) {

                    var originalRequestFailedDueToClientError = request
                    var userData: UserEntity? = null
                    var reLoginResponse: io.swagger.client.models.ResponseBody? = null
                    try {
                        reLoginResponse = LoginControllerApi().loginUsingPOST(getLoginDTO(), DUMMY_AUTH)
                        if (reLoginResponse.responseCode == HttpURLConnection.HTTP_OK) {
                            val jsonString = JSONObject((reLoginResponse.responseData as Map<String, String>).toMap())
                            val userJson: JSONObject = jsonString.getJSONObject(JSON_KEY_USER_OBJECT)
                            val userToken = object : TypeToken<UserEntity?>() {}.type
                            userData = Gson().fromJson(userJson.toString(), userToken)
                            setAuthToken(userData?.token!!)
                        } else {
                            /* If re-login has failed, then redirect user to login activity */
                            setReLoginFailureStatus()
                            throw java.lang.IllegalStateException(RE_LOGIN_FAILURE)
                        }
                    }
                    catch (ex: Exception) {
                        setReLoginFailureStatus()
                        return ClientError(response.body()?.string(), response.code(), response.headers().toMultimap())
                    }
                        originalRequestFailedDueToClientError.header(HEADER_KEY_AUTH, userData?.token)
                        val responseOfOriginalRequestAfterReLogin = client.newCall(originalRequestFailedDueToClientError.build()).execute()
                        when {
                            responseOfOriginalRequestAfterReLogin.isRedirect -> return Redirection(
                                responseOfOriginalRequestAfterReLogin.code(),
                                responseOfOriginalRequestAfterReLogin.headers().toMultimap()
                            )
                            responseOfOriginalRequestAfterReLogin.isInformational -> return Informational(
                                responseOfOriginalRequestAfterReLogin.message(),
                                responseOfOriginalRequestAfterReLogin.code(),
                                responseOfOriginalRequestAfterReLogin.headers().toMultimap()
                            )
                            responseOfOriginalRequestAfterReLogin.isSuccessful -> return Success(
                                responseBody(responseOfOriginalRequestAfterReLogin.body(), accept),
                                responseOfOriginalRequestAfterReLogin.code(),
                                responseOfOriginalRequestAfterReLogin.headers().toMultimap()
                            )
                            responseOfOriginalRequestAfterReLogin.isClientError -> {
                                return ClientError(
                                    responseOfOriginalRequestAfterReLogin.body()?.string(),
                                    responseOfOriginalRequestAfterReLogin.code(),
                                    responseOfOriginalRequestAfterReLogin.headers().toMultimap()
                                )
                            }
                            else -> return ServerError(
                                null,
                                responseOfOriginalRequestAfterReLogin.body()?.string(),
                                responseOfOriginalRequestAfterReLogin.code(),
                                responseOfOriginalRequestAfterReLogin.headers().toMultimap()
                            )
                        }
                }
                return ClientError(
                    response.body()?.string(),
                    response.code(),
                    response.headers().toMultimap()
                )
            }
            else -> return ServerError(
                    null,
                    response.body()?.string(),
                    response.code(),
                    response.headers().toMultimap()
            )
        }
    }

    /** Helper functions for the relogin flow,
     *  we need to retrieve the login parameter's and after login we have to
     *  save the Auth. */
    protected inline fun getUserId() = EA1HApplication.ea1hRepo.getUserId()

    protected inline  fun getLoginDTO(): LoginDTO {
        val repo = EA1HApplication.ea1hRepo
        val userEmail = repo.getUserEmailFromSharedPref()
        val androidId = repo.getAndroidIdFromSharedPref() ?: NA
        return LoginDTO(
            appVersion = BuildConfig.VERSION_NAME,
            email = CryptoHandler.encrypt(userEmail),
            imei = CryptoHandler.encrypt(androidId),
            os = BuildConfig.OS,
            fcmToken = repo.getFCMToken(),
            pwd = CryptoHandler.encrypt(repo.getUserPassword())
        )
    }

    protected inline fun setAuthToken(auth: String){
        EA1HApplication.ea1hRepo.setAuthToken(auth)
    }

    /** Updates the user_entity table, it has LiveData observer attached in
     *  base activity, which allow us to redirect user to sign in screen.
     *  @author VE00YM023
     */
    protected inline fun setReLoginFailureStatus(){
        EA1HApplication.ea1hRepo.updateReLoginFailure(true)
    }
}