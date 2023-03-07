/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 10:07 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * LoginRequestModel : This is the request body when user will login. It is irrespective
 *                          of login methodology i.e., facebook/email/google/phone number.
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

data class LoginRequestModel(
    @SerializedName("email")
    val email: String?,

    @SerializedName("password")
    val password : String?,

    @SerializedName("requestType")
    val requestType: Int,

    @SerializedName("imei")
    val imei: String,

    @SerializedName("source")
    val source: Int,

    @SerializedName("phoneNumber")
    val phoneNumber: String?,

    @SerializedName("otp")
    val otp : String?,

    @SerializedName("idToken")
    val idToken: String?,

    @SerializedName("accountId")
    val accountId: String?
)