package ymsli.com.ea1h.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "adv_packet")
class AdvPacketEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    var btAdd: String?,

    var connectionString: String?,

    var currentTime:Long?


)
