package ymsli.com.ea1h.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bt_command_logs")
class BTCommandsLogEntity(
    @PrimaryKey(autoGenerate = false)
    var triggeredOn: Long,

    var cmdType: Int,

    var chassNum: String?=null,

    var deviceId: String?=null,

    var isSuccessful: Boolean,

    var isCommit: Boolean,

    var btAdd: String?
)