/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 1:26 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Validator : This Activity is initializes the application
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*
import ymsli.com.ea1h.model.DAPIoTFileUploadResponse
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @POST("0002/iot-data/")
    fun sentLocationDataToDAPIoTServer(@Header("Content-Type")contentHeader: String,
                                       @Header("Accept")acceptHeader: String,
                                       @Header("x-uid")xuid: String,
                                       @Header("Content-Disposition")contentDisposition: String,
                                       @Body body: RequestBody): Observable<DAPIoTFileUploadResponse>
}