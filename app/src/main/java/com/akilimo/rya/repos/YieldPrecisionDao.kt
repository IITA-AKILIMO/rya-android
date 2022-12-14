package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.YieldPrecisionEntity

@Dao
interface YieldPrecisionDao {

    @Query("SELECT * from yield_precision")
    fun getAll(): List<YieldPrecisionEntity>

    @Query("SELECT * FROM yield_precision LIMIT 1")
    fun findOne(): YieldPrecisionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fieldInfoEntity: YieldPrecisionEntity)

    @Update
    fun update(fieldInfoEntity: YieldPrecisionEntity)
}
