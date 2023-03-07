package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ymsli.com.ea1h.database.entity.TripEntity
import javax.inject.Singleton

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewTrip(vararg trip:TripEntity)

    @Query("Update trip_entity Set isActive = 0")
    fun setAPITripsColumns()

    @Query("Select * from trip_entity where isActive = 1 Order by tripId DESC Limit 1")
    fun getOnGoingTrip():Array<TripEntity>

    @Query("Select * from trip_entity where isActive = 1 Order by tripId DESC Limit 1")
    fun getOnGoingTripLiveData():LiveData<Array<TripEntity>>

    @Query("Update trip_entity Set breakCount =:breakCount where tripId =:tripId")
    fun updateBrakeCount(breakCount: Int,tripId: String)

    @Query("Select * from trip_entity Order by tripId DESC Limit 1")
    fun getLastTrip(): Array<TripEntity>

    @Query("Select * from trip_entity where isActive =0 and userId =:userId Order by startTime DESC Limit 1")
    fun getLastTripLiveData(userId: String): LiveData<TripEntity>

    @Query("Update trip_entity Set endAddress=:addLine, isActive = 0 Where isActive = 1")
    fun completeOnGoingTrip(addLine: String)

    @Query("Select * FROM trip_entity where userId = :userId Order By startTime DESC Limit 20")
    fun getAllTripsLive(userId: String): LiveData<Array<TripEntity>>

    @Query("Delete FROM trip_entity where tripId =:tripId")
    fun removeTrip(tripId: Int?)

    @Query("Update trip_entity Set isActive = 1 where tripId = :tripId")
    fun appendExistingTrip(tripId: Int?)

    @Query("Select * FROM trip_entity where isSynced=0 and isActive=0")
    fun getUnsyncedTrips(): Array<TripEntity>

    @Query("Update trip_entity set isSynced =1 where tripId =:tripId")
    fun updateUnsyncedTrip(tripId: String)

    @Query("SELECT DISTINCT bikeId from trip_entity")
    fun getBikeIds(): List<Int?>

    @Query("DELETE FROM trip_entity WHERE tripId IN (SELECT tripId FROM(SELECT tripId FROM trip_entity WHERE bikeId =:bikeId ORDER BY tripId DESC LIMIT 2000 OFFSET 20) a)")
    fun removeExtraRecords(bikeId: Int): Int

    @Query("Delete from trip_entity where isSynced=1")
    fun deleteSyncedTrips()

    @Query("Select chassisNumber from trip_entity where tripId =:tripId")
    fun getChassisNumber(tripId: String): String

    @Query("Select * from trip_entity where tripId =:tripId")
    fun getTripEntity(tripId: String): TripEntity?

    @Query("Select * from trip_entity order by startTime desc limit 1")
    fun getPotentialLastTrip(): TripEntity?

    @Query("Update trip_entity set potentialEndTime =:potentialEndTime, potentialLastLatitude = :potentialLastLatitude, potentialLastLongitude =:potentialLastLongitude, distanceCovered =:distanceCovered where tripId =:tripId")
    fun updatePotentialTripEndCoordinates(potentialEndTime: Long,potentialLastLatitude: Double,potentialLastLongitude: Double,distanceCovered: Float, tripId: String)

    @Query("Update trip_entity set startAddress =:startAddress where tripId =:tripId")
    fun updateTripSourceAddress(tripId: String, startAddress: String): Single<Int>

    @Query("Update trip_entity set endAddress =:endAddress where tripId =:tripId")
    fun updateTripDestinationAddress(tripId: String, endAddress: String): Single<Int>
}
