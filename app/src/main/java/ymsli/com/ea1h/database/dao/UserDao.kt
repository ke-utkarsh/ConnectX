package ymsli.com.ea1h.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ymsli.com.ea1h.database.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registrationResponse: UserEntity)

    @Query("Select * FROM user_entity")
    fun getUserEntity(): LiveData<Array<UserEntity>>

    @Query("Delete FROM user_entity")
    fun removeUserEntity()

    @Query("UPDATE user_entity set reLoginFailure = :status")
    fun updateReLoginFailure(status: Boolean)
}