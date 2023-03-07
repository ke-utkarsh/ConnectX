package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.FilterBikeEntity

@Dao
interface FilterBikeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filterBikeEntity: FilterBikeEntity)

    @Query("Select bike_selected FROM filter_bike")
    fun getBikeInFilter():LiveData<Array<FilterBikeEntity>>

    @Query("Delete FROM filter_bike")
    fun clearSelectedBikeFromFilter()
}