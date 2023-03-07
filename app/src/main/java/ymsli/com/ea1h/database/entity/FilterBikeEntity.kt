package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filter_bike")
class FilterBikeEntity(

    @PrimaryKey(autoGenerate = true)
    var filterID: Int? = null,

    @ColumnInfo(name = "bike_selected")
    var bikeSelected: String
)