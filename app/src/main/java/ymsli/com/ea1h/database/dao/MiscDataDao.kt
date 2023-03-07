package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Maybe
import ymsli.com.ea1h.database.entity.MiscDataEntity

@Dao
interface MiscDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMiscData(vararg miscDataEntity: MiscDataEntity)

    @Query("Select * FROM misc_data")
    fun getMiscData(): Maybe<Array<MiscDataEntity>>

    @Query("Select descriptionValue from misc_data where discriptionId=:descriptionId")
    fun getContent(descriptionId: Int): LiveData<String>

    @Query("Select * from misc_data where descriptionKey =:key")
    fun getContentByKey(key: String): List<MiscDataEntity>

    @Query("Select * from misc_data where descriptionKey =:key")
    fun getContentByKeyLiveData(key: String): LiveData<List<MiscDataEntity>>

    @Query("Select descriptionValue from misc_data where discriptionId =:descriptionId")
    fun getContentValue(descriptionId: Int):String
}