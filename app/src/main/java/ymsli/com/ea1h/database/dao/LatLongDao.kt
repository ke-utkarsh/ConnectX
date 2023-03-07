package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.LatLongEntity

@Dao
interface LatLongDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewLocation(vararg latLong: LatLongEntity)

    @Query("Select *,max(locationId) from lat_long_entity where tripId =:tripId")
    fun getTripLastLocation(tripId: String):LatLongEntity

    @Query("Select *,min(locationId) from lat_long_entity where tripId =:tripId")
    fun getTripFirstLocation(tripId: String):LatLongEntity

    @Query("Select * from lat_long_entity where isFileCreated = 0 limit 40")
    fun getUnsyncedTrips(): Array<LatLongEntity>

    @Query("Update lat_long_entity Set isFileCreated = 1 where locationId =:locationId")
    fun updateTripParameter(locationId: Long)

    @Query("Select * from lat_long_entity")
    fun getAllValues():Array<LatLongEntity>

    @Query("Select * from lat_long_entity where tripId =:tripId order by tripId")
    fun getLocationOfTrip(tripId: String): Array<LatLongEntity>

    @Query("DELETE FROM lat_long_entity where isFileCreated = 1")
    fun deleteSyncedLatLongs()

    @Query("Select * from lat_long_entity where tripId =:tripId order by locationId")
    fun getLocationForMapPlot(tripId: String):Array<LatLongEntity>
}