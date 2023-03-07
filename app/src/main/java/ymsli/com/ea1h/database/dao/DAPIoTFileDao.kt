package ymsli.com.ea1h.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.DAPIoTFileEntity

@Dao
interface DAPIoTFileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeFileInfo(file: DAPIoTFileEntity)

    @Query("Delete from zip_folder_entity where fileName =:fileName")
    fun removeFileEntry(fileName: String)

    @Query("Select * from zip_folder_entity where fileName =:fileName")
    fun getFileEntity(fileName: String): DAPIoTFileEntity

    @Query("Update zip_folder_entity Set nextTry =:nextTry,retryAttempts =:retryAttempts where id = :id")
    fun updateFileEntity(id: Long,nextTry: Long,retryAttempts: Int)

    @Query("Select * from zip_folder_entity where isSent =0")
    fun getUnsyncedFiles(): Array<DAPIoTFileEntity>

}