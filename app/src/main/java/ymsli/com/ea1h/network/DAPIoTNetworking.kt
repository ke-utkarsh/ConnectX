package ymsli.com.ea1h.network

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  (VE00YM129)
 * @date    18/07/2020 11:45 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * DAPIoTNetworking : This file sends the DAP IoT Files to the server via retrofit.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.utils.common.Constants
import java.util.concurrent.TimeUnit

object DAPIoTNetworking {

    private fun getClient(): OkHttpClient.Builder? {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(logging).addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .build()
            chain.proceed(request)
        }
    }

    fun createNetworkService(): NetworkService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.DAP_IOT_URL)
            .client(
                getClient()!!
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}
