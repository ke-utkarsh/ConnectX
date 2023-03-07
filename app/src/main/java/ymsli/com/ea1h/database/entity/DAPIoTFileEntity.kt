package ymsli.com.ea1h.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zip_folder_entity")
data class DAPIoTFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long?=null,

    @ColumnInfo(name = "fileName")
    var fileName: String,

    @ColumnInfo(name = "filePath")
    var filePath: String,

    @ColumnInfo(name = "createdOn")
    val createdOn: Long,

    @ColumnInfo(name = "nextTry")
    var nextTry: Long?=null,

    @ColumnInfo(name = "retryAttempts")
    var retryAttempts: Int,

    @ColumnInfo(name = "isSent")
    var isSent: Boolean
)