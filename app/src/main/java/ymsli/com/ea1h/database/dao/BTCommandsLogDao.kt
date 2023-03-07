package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.BTCommandsLogEntity

@Dao
interface BTCommandsLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(btCommandsLogEntity: BTCommandsLogEntity)

    @Query("Update bt_command_logs Set isSuccessful =:isSuccessful, isCommit = 1  where isCommit = 0")
    fun updateCommandStatus(isSuccessful: Boolean)

    @Query("Select * from bt_command_logs Where isSuccessful=0")
    fun getFailedCommands(): Array<BTCommandsLogEntity>

    @Query("Delete from bt_command_logs")
    fun deleteLogs()

    @Query("Select * from bt_command_logs order by triggeredOn desc limit :rowLimit")
    fun getAllCommands(rowLimit: Int): Array<BTCommandsLogEntity>

    @Query("DELETE FROM bt_command_logs where triggeredOn >= :minTrig and triggeredOn <= :maxTrig")
    fun deleteAllSyncedLogs(minTrig: Long, maxTrig: Long)

    @Query("DELETE FROM bt_command_logs WHERE triggeredOn IN (SELECT triggeredOn FROM(SELECT triggeredOn FROM bt_command_logs ORDER BY triggeredOn DESC LIMIT 2000 OFFSET :maxSize) a)")
    fun trimToMaxSize(maxSize: Int): Int
}