package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ymsli.com.ea1h.utils.common.Constants

@Entity(tableName = "bike_entity")
class BikeEntity(
    @PrimaryKey
    @ColumnInfo(name = "chassisNumber")
    val chasNum: String,

    @ColumnInfo(name = "registrationNumber")
    val regNum: String? = null,

    @ColumnInfo(name = "model")
    val mod: String? = null,

    @ColumnInfo(name = "make")
    val mk: String? = null,

    @ColumnInfo(name = "btAdd")
    var btAdd: String? = null,

    @ColumnInfo(name = "bike_pic")
    val bdp: String?=null,

    @ColumnInfo(name = "is_last_connected")
    var isLastConnected: Boolean = false,

    @ColumnInfo(name = "bikeId")
    val bikeId: Long,

    @ColumnInfo(name ="bkModNm")
    val bkModNm: String?=null,

    @ColumnInfo(name ="ccuId")
    val ccuId: String?,

    @ColumnInfo(name ="vehType",defaultValue = Constants.BIKE_STRING)
    val vehType: String?
){
    companion object {
        const val VEHICLE_TYPE_SCOOTER = "SCOOTER"
        const val VEHICLE_TYPE_BIKE = "BIKE"
    }
}