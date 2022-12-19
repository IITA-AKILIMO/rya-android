package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.UserInfoEntity

@Dao
interface UserInfoDao {

    @Query("SELECT * from user_info")
    fun getAll(): List<UserInfoEntity>

    @Query("SELECT * FROM user_info LIMIT 1")
    fun findOne(): UserInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userInfoEntity: UserInfoEntity)

    @Update
    fun update(userInfoEntity: UserInfoEntity)

    @Delete
    fun deleteField(userInfoEntity: UserInfoEntity)

    @Query("DELETE FROM user_info")
    fun deleteAll()
}
