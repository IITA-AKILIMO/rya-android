package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.FieldYieldEntity

@Dao
interface FieldYieldDao {

    @Query("SELECT * from field_yield")
    fun getAll(): List<FieldYieldEntity>

    @Query("SELECT * FROM field_yield LIMIT 1")
    fun findOne(): FieldYieldEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fieldYieldEntity: FieldYieldEntity)

    @Update
    fun update(fieldYieldEntity: FieldYieldEntity)
}
