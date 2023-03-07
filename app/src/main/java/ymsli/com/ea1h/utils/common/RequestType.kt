/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 11:48 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * RequestType : This determines the request type when user triggers OTP API
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.utils.common

enum class RequestType(val code: Int) {
    USER_REGISTRATION(1),
    VEHICLE_REGISTRATION(2),
    FORGOT_PASSWORD(3),
    CHANGE_PASSWORD(4),
    USER_LOGIN(5),
}