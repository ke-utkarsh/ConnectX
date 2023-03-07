/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author  (VE00YM023)
 *  * @date   30/1/20 5:28 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * User : This data class represents an instance of the user and hold all the
 *           associated data.
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

data class User(

    @SerializedName("userName")
    val name: String?,

    @SerializedName("password")
    val password : String?
)

