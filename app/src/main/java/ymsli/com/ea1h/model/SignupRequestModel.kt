/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   6/2/20 1:05 PM
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

package ymsli.com.ea1h.model

import com.google.gson.annotations.SerializedName

data class SignupRequestModel(
    @SerializedName("fullName")
    val fullName: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("rt")
    val requestType: Int?,

    @SerializedName("imei")
    val imei: String?,

    @SerializedName("otp")
    val otp: String?,

    @SerializedName("src")
    val source: Int,

    @SerializedName("createdOn")
    val createdOn: String?

)