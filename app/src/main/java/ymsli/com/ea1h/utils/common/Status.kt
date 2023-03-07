package ymsli.com.ea1h.utils.common

/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 10:02 AM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Status : ENUM for status code of validations at client-side
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  * (VE00YM023)   31/1/2020           Added validator methods for name, email
 *                                             and password fields.
 *  * -----------------------------------------------------------------------------------
 *
 */

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    UNKNOWN
}