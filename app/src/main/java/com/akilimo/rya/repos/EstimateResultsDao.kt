package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.EstimateResultsEntity

@Dao
interface EstimateResultsDao {

    @Query("SELECT * from estimate_results")
    fun getAll(): List<EstimateResultsEntity>

    @Query("SELECT * FROM estimate_results LIMIT 1")
    fun findOne(): EstimateResultsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(estimateResultsEntity: EstimateResultsEntity)

    @Update
    fun update(estimateResultsEntity: EstimateResultsEntity)

    @Delete
    fun deleteEstimate(estimate: EstimateResultsEntity)

    @Query("DELETE FROM estimate_results")
    fun deleteAll()
}
