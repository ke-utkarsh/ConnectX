package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.GyroEntity

@Dao
interface GyroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewGyro(gyro: GyroEntity)

    @Query("Select * from gyro_entity where isFileCreated = 0 limit 60")
    fun getUnsyncedGyroEntity(): Array<GyroEntity>

    @Query("Delete from gyro_entity where id =:id")
    fun removeSyncedGyroData(id: Long)

    @Query("Delete from gyro_entity")
    fun clearGyroTable()

}