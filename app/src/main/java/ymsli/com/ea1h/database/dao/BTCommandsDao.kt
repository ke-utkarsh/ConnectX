package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe
import ymsli.com.ea1h.database.entity.BTCommandsEntity

@Dao
interface BTCommandsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBTCommands(vararg btCommandsEntity: BTCommandsEntity)

    @Query("Select * FROM bt_commands")
    fun getBTCommands(): Maybe<Array<BTCommandsEntity>>

    @Query("Select commandCode from bt_commands where commandId = :commandId")
    fun getSpecificCommand(commandId: Int): String
}