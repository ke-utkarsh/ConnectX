package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single
import ymsli.com.ea1h.database.entity.BikeEntity
import ymsli.com.ea1h.database.entity.SliderImage
import ymsli.com.ea1h.utils.common.Event

@Dao
interface BikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBike(vararg trip:BikeEntity)

    @Query("Select * From bike_entity")
    fun retrieveBikes(): LiveData<Array<BikeEntity>>

    @Query("Select * From bike_entity where is_last_connected = 1 limit 1")
    fun getLastConnectedBike(): LiveData<BikeEntity>

    @Query("Select * From bike_entity where is_last_connected = 1 limit 1")
    fun getLastConnectedBikeForService(): BikeEntity

    @Query("Update bike_entity Set is_last_connected=0 ")
    fun resetLastConnectedBike():Single<Int>

    @Query("Update bike_entity Set is_last_connected=1 where chassisNumber =:chassisNumber")
    fun setLastConnectedBike(chassisNumber: String)

    @Query("Update bike_entity Set is_last_connected=0 where chassisNumber =:chassisNumber")
    fun hardResetLastConnectedBike(chassisNumber: String)

    @Query("Select chassisNumber from bike_entity where make =:make")
    fun getBikeChassis(make: String): List<String>

    @Query("Select make from bike_entity")
    fun getBikeMake(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSliderImage(vararg images: SliderImage)

    @Query("Select * From slider_image")
    fun getSliderImages(): LiveData<Array<SliderImage>>

    @Query("Select registrationNumber from bike_entity where chassisNumber =:chassisNumber")
    fun getBikeRegNum(chassisNumber: String): LiveData<String>

    @Query("Select * from bike_entity where chassisNumber =:chassisNumber")
    fun getBikeRegNumModel(chassisNumber: String): LiveData<BikeEntity>

    @Query("Select DISTINCT bkModNm from bike_entity")
    fun getAllModelNames(): List<String>

    @Query("Select DISTINCT chassisNumber from bike_entity where bkModNm IN (:models)")
    fun getChassisNoByModelName(models: List<String>): List<String>
    @Query("Select * from bike_entity where chassisNumber =:chassisNumber")
    fun getBikeModelDetails(chassisNumber: String): BikeEntity

    @Query("Select bikeId From bike_entity")
    fun getAllBikeIds(): List<Long>

    @Query("Select DISTINCT registrationNumber from bike_entity")
    fun getAllRegNumbers(): List<String?>

    @Query("Select DISTINCT chassisNumber from bike_entity where registrationNumber IN (:regNumbers)")
    fun getChassisNoByRegNumbers(regNumbers: List<String>): List<String>

    @Query("Select * From bike_entity")
    fun getAllBikes(): Array<BikeEntity>

    @Query("DELETE FROM bike_entity")
    fun removeAllBikes()
}