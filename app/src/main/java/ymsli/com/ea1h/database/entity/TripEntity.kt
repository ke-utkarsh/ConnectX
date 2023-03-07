/*
 * *
 *  * Project Name : EA1H
 *  * @company YMSLI
 *  * @author VE00YM129
 *  * @date   30/1/20 2:36 PM
 *  * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *  *
 *  * Description
 *  * -----------------------------------------------------------------------------------
 *  * TripEntity : This entity will be used both as model and room table storage
 *  * -----------------------------------------------------------------------------------
 *  *
 *  * Revision History
 *  * -----------------------------------------------------------------------------------
 *  * Modified By          Modified On         Description
 *  *
 *  * -----------------------------------------------------------------------------------
 *
 */

package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "trip_entity")
class TripEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tripId")
    val tripId: String,

    @ColumnInfo(name = "startAddress")
    var startAddress: String? = null,

    @ColumnInfo(name = "endAddress")
    var endAddress: String? = null,

    @ColumnInfo(name = "chassisNumber")
    var chassisNumber: String? = null,

    @ColumnInfo(name = "createdOn")
    var createdOn: Long? = null,

    @ColumnInfo(name = "startTime")
    var startTime: Long? = null,

    @ColumnInfo(name = "endTime")
    var endTime: Long? = null,

    @ColumnInfo(name = "distanceCovered")
    var distanceCovered: Float? = null,

    @ColumnInfo(name = "averageSpeed")
    var averageSpeed: Float? = null,

    @ColumnInfo(name = "breakCount")
    var breakCount: Int? = null,

    @ColumnInfo(name = "imei")
    var imei: String? = null,

    @ColumnInfo(name = "lastBatteryVoltage")
    var battery: Float? = null,

    @ColumnInfo(name = "startLat")
    var startLat: Double?=null,

    @ColumnInfo(name = "startLon")
    var startLon: Double?=null,

    @ColumnInfo(name = "endLat")
    var endLat: Double?=null,

    @ColumnInfo(name = "endLon")
    var endLon: Double?=null,

    @ColumnInfo(name = "bikeId")
    var bikeId: Int?=null,

    @ColumnInfo(name = "isActive")
    var isActive: Boolean,

    @ColumnInfo(name = "isSynced")
    var isSynced: Boolean,

    @ColumnInfo(name = "updatedOn")
    var updatedOn: Long?=null,

    @ColumnInfo(name = "userId")
    var userId: String,

    @ColumnInfo(name = "potentialLastLatitude")
    var potentialLastLatitude: Double?=null,

    @ColumnInfo(name = "potentialLastLongitude")
    var potentialLastLongitude: Double?=null,

    @ColumnInfo(name = "potentialEndTime")
    var potentialEndTime: Long? = null

) : Serializable