package ymsli.com.ea1h.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bt_commands")
class BTCommandsEntity (
    @PrimaryKey(autoGenerate = false)
    val commandId: Int,

    var key: String,

    var commandCode: String,

    var description: String
)