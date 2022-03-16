package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.FieldInfo

@Dao
interface FieldInfoDao {

    @Query("SELECT * from field_info")
    fun getAll(): List<FieldInfo>

    @Query("SELECT * FROM field_info LIMIT 1")
    fun findOne(): FieldInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fieldInfo: FieldInfo)

    @Update
    fun update(fieldInfo: FieldInfo)
}
