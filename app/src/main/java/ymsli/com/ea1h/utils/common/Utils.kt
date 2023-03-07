/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   5/2/20 2:49 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * Utils : This class is responsible for common calculations to be performed throughout
 *              the application.
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

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import ymsli.com.ea1h.BuildConfig
import ymsli.com.ea1h.database.EA1HSharedPreferences.Companion.EMPTY_STRING
import ymsli.com.ea1h.utils.common.Constants.NA
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


object Utils {

    private const val ZERO_MIN = "0 min"
    private const val ONE_DAY_AGO = "1 day ago"
    private const val SUFFIX_DAYS_AGO = "days ago"
    private const val TIME_ZONE_KEY_GMT = "GMT"
    private const val FORMAT_HOURS_MINTS = "%02d hr, %02d min ago"
    private const val DAP_IOT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    private const val TRIP_DATE_FORMAT = "EEE, MMM dd"
    private const val PARKING_DATE_FORMAT = "EEE, MMM dd YYYY"
    private const val TRIP_TIME_FORMAT = "hh: mm a"
    private val tripHistoryDateFormatter = SimpleDateFormat("hh:mm, dd MMM")
    private const val LOG_FILE_PATH = "/sdcard/EA1H_Logs.txt"

    /** returns time in milliseconds in GMT Zone */
    fun getTimeInMilliSec(): Long{
        val cal = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE_KEY_GMT))
        return cal.timeInMillis
    }

    /**
     * returns specific format of time from timestamp
     */
    fun formatForTripDetail(millis: Long) = tripHistoryDateFormatter.format(millis)

    /**
     * returns time in the format dd MMM h:mm a
     */
    fun getTimeFormatForTrip(time: Long): String{
        val sdf = SimpleDateFormat("dd MMM hh:mm a")
        return sdf.format(time)
    }

    /**
     * checks current time and given time difference
     * this is used to create new trip or resume
     * older trips in the room
     */
    fun checkTimeDifference(inputTime: Long): Long{
        val currentTime = Date().time
        val diff = currentTime-inputTime
        return diff/1000
    }

    /**
     * generates time in a format
     * from input timestamp
     */
    fun getTimeInDateTimeFormat(currentTime: Long):String{
        val date = Date(currentTime)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh.mm aa")
        return sdf.format(date)
    }

    /**
     * generates time in a format
     * from input timestamp
     */
    fun getTimeDateForLogs(currentTime: Long):String{
        val date = Date(currentTime)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh.mm.ss aa")
        return sdf.format(date)
    }

    /**
     * Returns the day time of the date represented by the millis
     * @author VE00YM023
     */
    fun getDayTime(millis: Long): String = SimpleDateFormat("hh.mm aa").format(millis)

    /** returns time in DAPIoT format specified in DAPIoT document */
    fun getTimeForFileName(fileNameTime: String): String? {
        val formatter =
            android.icu.text.SimpleDateFormat(DAP_IOT_DATE_TIME_FORMAT)
        val formatterNew =
            android.icu.text.SimpleDateFormat("YYYYMMddHHmmssSSS")
        try {
            return formatterNew.format(formatter.parse(fileNameTime))
        }
        catch (e: ParseException) { e.printStackTrace() }
        return null
    }

    /**
     * returns current time in format
     * required to upload file to
     * DAPIoT server
     */
    fun getTimeInDAPIoTFormat():String{
        val formatter = SimpleDateFormat(DAP_IOT_DATE_TIME_FORMAT)
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(Date())
    }

    /**
     * calculates the duration of a trip
     */
    fun getTripDuration(startTime: Long, endTime: Long): String{
        val timeDiff = endTime - startTime
        if(timeDiff < 60000) return ZERO_MIN
        return String.format("%02d min",TimeUnit.MILLISECONDS.toMinutes(timeDiff))
    }

    /**
     * Given a time instance in millis, returns the difference from now.
     * based on following conditions.
     * TimeDiff from now                Returned
     * 0 days ( <86400000)              "%02d hr, %02d min ago" format
     * 1 day                            "1 day ago'
     * > 1 day                          "N days ago'
     *
     * @param  millis
     * @author VE00YM023
     */
    fun getTimeDiffFromNow(millis: Long): String{
        val timeDiff = getCurrentMillisInGMT() - millis
        return when(val days = TimeUnit.MILLISECONDS.toDays(timeDiff)){
            0L -> formatAsHoursAndMints(timeDiff)
            1L -> ONE_DAY_AGO
            else -> "$days $SUFFIX_DAYS_AGO"
        }
    }

    /**
     * Returns the current GMT Time in millis
     * @author VE00YM023
     */
    fun getCurrentMillisInGMT(): Long{
        val cal = Calendar.getInstance(TimeZone.getTimeZone(TIME_ZONE_KEY_GMT))
        return cal.timeInMillis
    }

    /**
     * Returns the given millis in "%02d hr, %02d min ago" format.
     * @author VE00YM023
     */
    private fun formatAsHoursAndMints(millis: Long): String{
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val mints = TimeUnit.MILLISECONDS.toMinutes(millis % TimeUnit.HOURS.toMillis(1))
        return String.format(FORMAT_HOURS_MINTS, hours, mints)
    }

    /**
     * returns battery value by
     * formatting it to
     * 1 decimal place
     */
    fun getBatteryVoltageFormatted(batteryVoltage: String?, suffix: String = " v"): String{
        if(batteryVoltage.isNullOrBlank() || batteryVoltage.contains("NA") || batteryVoltage.contains("null",true)) return "NA"
        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.FLOOR
        return "${df.format(batteryVoltage.toDouble())} $suffix"
    }

    /**
     * gets Elock pattern in the form of string
     * of lefts and rights
     */
    fun getElockPatternFormatted(pattern: ByteArray): String{
        var patternString: String = ""
        for(i in 2..5){
            val x = pattern.get(i)
            if(x.toInt() == 76){
                patternString += "L"
            }
            else if(x.toInt() == 82){
                patternString += "R"
            }
        }
        return patternString
    }

    /**
     * returns time in Mon, 31 July 2020 format
     */
    fun getTimeFormatTripStartParkingDetail(millis: Long): String{
        return try{ SimpleDateFormat(PARKING_DATE_FORMAT).format(millis) }
        catch (cause: Exception) { NA }
    }

    /** Returns the date part from the given date time instance. */
    fun getTripDate(millis: Long): String {
        return try{ SimpleDateFormat(TRIP_DATE_FORMAT).format(millis) }
        catch (cause: Exception) { NA }
    }

    /** Returns the date part from the given date time instance. */
    fun getTripTime(millis: Long): String {
        return try{ SimpleDateFormat(TRIP_TIME_FORMAT).format(millis) }
        catch (cause: Exception) { NA }
    }

    /**
     * Logs the message to the project log file.
     * @author Balraj VE00YM023
     */
    fun logToFile(message: String) {
        val logFile = File(LOG_FILE_PATH)
        if (!logFile.exists()) {
            try { logFile.createNewFile() }
            catch (e: IOException) { e.printStackTrace() }
        }
        try {
            val buf = BufferedWriter(FileWriter(logFile, true))
            buf.append(message)
            buf.newLine()
            buf.close()
        }
        catch (e: IOException) { e.printStackTrace() }
    }

    /**
     * Returns the current UTC time formatted in 'yyyy-MM-dd HH:mm:ss' format.
     * @author Balraj VE00YM023
     */
    fun getCurrentUTCForProjectDetails(): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.from(ZoneOffset.UTC))
        return formatter.format(Instant.now())
    }

    fun writeToFile(data: String, context: Context) {
        if(BuildConfig.DEBUG) {
            Log.e("**********Y Connect X**********", data)
            if(Build.VERSION.SDK_INT<30) {
                val logFile = File("/sdcard/ea1hLogs.txt")
                if (!logFile.exists()) {
                    try {
                        logFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                try { //BufferedWriter for performance, true to set append to file flag
                    val buf = BufferedWriter(FileWriter(logFile, true))
                    buf.append(data)
                    buf.newLine()
                    buf.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}

/**
 * Returns an UI compatible view of the String.
 * if the received String is null or empty then returns text 'NA' in red color,
 * otherwise returns the received String.
 * @author VE00YM023
 */
fun String?.toUIView(): SpannableString {
    var str = SpannableString(Constants.NA)
    if (this == null || this.trim().isEmpty()) {
        str.setSpan(ForegroundColorSpan(Color.RED), 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    else { str = SpannableString(this.trim()) }
    return str
}

/**
 * Returns an UI compatible view of the Number.
 * if the received number is null or empty then returns text 'NA' in red color,
 * otherwise returns the received number formatted to 2 decimal places.
 * @author VE00YM023
 */
fun Float?.formatForUI(suffix: String = EMPTY_STRING): SpannableString {
    var str = SpannableString(Constants.NA)
    if (this == null) {
        str.setSpan(ForegroundColorSpan(Color.RED), 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    else { str = SpannableString(this.toBigDecimal().setScale(2, RoundingMode.FLOOR).toString() + " $suffix") }
    return str
}