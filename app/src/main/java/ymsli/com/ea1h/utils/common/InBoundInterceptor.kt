package ymsli.com.ea1h.utils.common

import okhttp3.*
import okio.GzipSource
import okio.Okio

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    26/08/2020 06:13 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * InBoundInterceptor : Okhttp interceptor to attach 'Accept-Encoding': 'gzip' in
 *                      every outgoing request. and then unzips the received data.
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

class InBoundInterceptor: Interceptor{

    /**
     * Intercept the outgoing request and add the 'Accept-Encoding' header
     * with valid value, so that server returns a zipped payload.
     * @author VE00YM023
     */
    override fun intercept(chain: Interceptor.Chain): Response? {
        val newRequest: Request.Builder = chain.request().newBuilder()
        newRequest.addHeader(HEADER_KEY_ACCEPT_ENCODING, GZIP)
        val response = chain.proceed(newRequest.build())
        return if (isGzipped(response)) { unzip(response) } else { response }
    }

    /**
     * Unzip the in-bound data payload, if its not null.
     * @author VE00YM023
     */
    private fun unzip(response: Response): Response? {
        if (response.body() == null) { return response }
        val gzipSource = GzipSource(response.body()!!.source())
        val bodyString = Okio.buffer(gzipSource).readUtf8()
        val responseBody = ResponseBody.create(response.body()!!.contentType(), bodyString)
        val strippedHeaders = response.headers().newBuilder()
            .removeAll(HEADER_KEY_CONTENT_ENCODING)
            .removeAll(HEADER_KEY_CONTENT_LENGTH)
            .build()
        return response.newBuilder()
            .headers(strippedHeaders)
            .body(responseBody)
            .message(response.message())
            .build()
    }

    /**
     * Check if the response contains "Content-Encoding" header with valid value.
     * @author VE00YM023
     */
    private fun isGzipped(response: Response): Boolean {
        return response.header(HEADER_KEY_CONTENT_ENCODING) != null &&
               response.header(HEADER_KEY_CONTENT_ENCODING) == GZIP
    }

    private companion object {
        private const val HEADER_KEY_ACCEPT_ENCODING = "Accept-Encoding"
        private const val HEADER_KEY_CONTENT_ENCODING = "Content-Encoding"
        private const val HEADER_KEY_CONTENT_LENGTH = "Content-Length"
        private const val GZIP = "gzip"
    }
}