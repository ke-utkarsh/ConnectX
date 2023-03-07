package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lat_long_entity")
class LatLongEntity (

    @PrimaryKey(autoGenerate = true)
    val locationId: Long?=null,

    @ColumnInfo(name = "tripId")
    val tripId: String,

    @ColumnInfo(name = "latitude")
    val latitude: String,

    @ColumnInfo(name = "longitude")
    val longitude: String,

    @ColumnInfo(name = "locationCaptureTime")
    val locationCaptureTime:String,

    @ColumnInfo(name = "isFileCreated")
    val isFileCreated: Boolean
)
