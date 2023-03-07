package ymsli.com.ea1h.network.swaggerintegration

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Single
import io.swagger.client.apis.BluetoothCommandControllerApi
import io.swagger.client.apis.ProjectDescriptionDetailsControllerApi
import io.swagger.client.models.LogBluetoothCommandDTO
import org.json.JSONObject
import ymsli.com.ea1h.database.entity.BTCommandsEntity
import ymsli.com.ea1h.database.entity.MiscDataEntity
import ymsli.com.ea1h.utils.common.Constants.DONE
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MiscHandler @Inject constructor(
    private val btCommandControllerApi: BluetoothCommandControllerApi,
    private val pjDescriptionDetailsControllerApi: ProjectDescriptionDetailsControllerApi,
    private val gson: Gson
){

    fun getMiscData(authKey: String): Observable<ArrayList<MiscDataEntity>>{
        return Observable.create {
            try {
                val response = pjDescriptionDetailsControllerApi.getApiConfigurationUsingGET(authKey)
                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    val miscDataList = arrayListOf<MiscDataEntity>()
                    val dataSize = (response.responseData as java.util.ArrayList<Map<String, String>>).size
                    for(i in 0 until dataSize){
                        val jsonString = JSONObject((((response.responseData as (ArrayList<Map<String,String>>)).get(i)) as Map<String,String>).toMap()).toString(  )
                        val miscResponse: MiscDataEntity = gson.fromJson<MiscDataEntity>(jsonString,MiscDataEntity::class.java)
                        miscDataList.add(miscResponse)
                    }
                    it.onNext(miscDataList)
                }
                else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }

    fun logBluetoothCommands(logs : Array<LogBluetoothCommandDTO>, authKey: String): Single<String>{
        return Single.create {
            try{
                val response = btCommandControllerApi.logExecutedCommandUsingPOST(logs,authKey)
                if(response.responseCode == HttpURLConnection.HTTP_OK){
                    it.onSuccess(DONE)
                }else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }

    fun logBluetoothCommandsTemp(logs : Array<LogBluetoothCommandDTO>, authKey: String):Observable<String>{
        return Observable.create {
            try{
                val response = btCommandControllerApi.logExecutedCommandUsingPOST(logs,authKey)
                if(response.responseCode == HttpURLConnection.HTTP_OK){
                    it.onNext(DONE)
                }
                else it.onError(Exception(response.responseMessage))
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }

    fun getAllBluetoothCommands(authKey: String, src: String): Observable<ArrayList<BTCommandsEntity>>{
        return Observable.create {
            try{
                val response = btCommandControllerApi.getBluetoothCommandsForChasisUsingPOST(authKey,src)
                if(response.responseCode==HttpURLConnection.HTTP_OK){
                    val commandsList = arrayListOf<BTCommandsEntity>()
                    val dataSize = (response.responseData as java.util.ArrayList<Map<String, String>>).size
                    for(i in 0 until dataSize){
                        val jsonString = JSONObject((((response.responseData as (ArrayList<Map<String,String>>)).get(i)) as Map<String,String>).toMap()).toString()
                        val command: BTCommandsEntity = gson.fromJson<BTCommandsEntity>(jsonString,BTCommandsEntity::class.java)
                        commandsList.add(command)
                    }
                    it.onNext(commandsList)
                }
            }
            catch (ex: Exception){
                it.onError(ex)
            }
        }
    }
}
