package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * Project Name : EA1H
 * @company YMSLI
 * @author  VE00YM023
 * @date    21/08/2020 06:57 PM
 * Copyright (c) 2020, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * TripDetailEntity : This entity is used to store the trip details (Lats, Long)
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 * -----------------------------------------------------------------------------------
 */

@Entity(tableName = "trip_detail")
class TripDetailEntity (
    @PrimaryKey
    val tripId: String,

    @ColumnInfo(name = "lats")
    var lats: String? = null,

    @ColumnInfo(name = "longs")
    var longs: String? = null
)