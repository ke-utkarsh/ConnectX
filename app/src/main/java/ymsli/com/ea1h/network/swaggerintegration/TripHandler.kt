package ymsli.com.ea1h.network.swaggerintegration

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.swagger.client.apis.TripControllerApi
import io.swagger.client.models.TripHistory
import java.lang.Exception
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripHandler @Inject constructor(
    private val tripControllerApi: TripControllerApi,
    private val gson: Gson
){

    companion object{ private const val ZERO = "0" }

    fun uploadTrip(trips: Array<TripHistory>, authKey: String): Observable<Int> {
        return Observable.create {
            try {
                val response = tripControllerApi.addTripUsingPOST(trips, authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    it.onNext(response.responseCode)
                }
            } catch (ex: Exception) {
                it.onError(ex)
            }
        }
    }
}