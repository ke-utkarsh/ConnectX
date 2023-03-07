package ymsli.com.ea1h.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "misc_data")
class MiscDataEntity (
    @PrimaryKey
    var discriptionId: Int,//spelling mistake from API end

    val descriptionKey: String,

    var descriptionValue: String = "",

    var updatedOn: String = ""
)