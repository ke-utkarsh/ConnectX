package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.TripDetailEntity

@Dao
interface TripDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDetail(vararg trip: TripDetailEntity)

    @Query("Select * from trip_detail where tripId = :tripId")
    fun getTripDetailLive(tripId: Long): LiveData<TripDetailEntity>

    @Query("Select * from trip_detail where tripId = :tripId")
    fun getTripDetail(tripId: Long): List<TripDetailEntity>
}