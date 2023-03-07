package ymsli.com.ea1h.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_entity")
data class UserEntity(
    val token: String,
    @PrimaryKey
    val userId: String,
    val name: String?=null,
    var reLoginFailure: Boolean
)