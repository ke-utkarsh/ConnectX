package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "slider_image")
class SliderImage(
    @PrimaryKey
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    @ColumnInfo(name = "dataType")
    val dataType: String? = null
)