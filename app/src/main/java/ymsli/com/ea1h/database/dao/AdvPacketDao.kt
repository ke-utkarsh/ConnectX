package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.AdvPacketEntity

@Dao
interface AdvPacketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAdvPacket(vararg advPacketEntity: AdvPacketEntity)

    @Query("Select * FROM adv_packet")
    fun getAdvPacket(): Array<AdvPacketEntity>

    @Query("Delete from adv_packet where id=:id")
    fun deleteLogs(id: Long)
}
