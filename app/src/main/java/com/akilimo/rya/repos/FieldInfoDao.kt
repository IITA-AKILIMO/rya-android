package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.FieldInfoEntity

@Dao
interface FieldInfoDao {

    @Query("SELECT * from field_info")
    fun getAll(): List<FieldInfoEntity>

    @Query("SELECT * FROM field_info LIMIT 1")
    fun findOne(): FieldInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fieldInfoEntity: FieldInfoEntity)

    @Update
    fun update(fieldInfoEntity: FieldInfoEntity)
}
