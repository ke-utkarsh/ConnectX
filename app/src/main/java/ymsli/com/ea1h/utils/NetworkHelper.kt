package ymsli.com.ea1h.utils

/**
 * Project Name : EA1H
 * @company YMSLI
 * @author VE00YM129
 * @date   August 20, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * NetworkHelper : This is network helper to determine if network is present or not
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Singleton

@Singleton
class NetworkHelper constructor(private val context: Context){

    companion object{
        private const val TAG = "NetworkHelper"
    }

    fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}