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
 *  * Resource : Used at view model in order to acknowledge user
 *      about validation message or response from API
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

data class Resource <out T> private constructor(val status: Status, val data: T?) {

    companion object {
        fun <T> success(data: T? = null): Resource<T> =
            Resource(
                Status.SUCCESS,
                data
            )

        fun <T> error(data: T? = null): Resource<T> =
            Resource(
                Status.ERROR,
                data
            )

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(
                Status.LOADING,
                data
            )

        fun <T> unknown(data: T? = null): Resource<T> =
            Resource(
                Status.UNKNOWN,
                data
            )
    }
}