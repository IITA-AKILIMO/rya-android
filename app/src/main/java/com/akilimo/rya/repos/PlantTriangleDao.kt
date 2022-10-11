package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.PlantTriangleEntity

@Dao
interface PlantTriangleDao {

    @Query("SELECT * from plant_triangle")
    fun getAll(): List<PlantTriangleEntity>

    @Query("SELECT * from plant_triangle LIMIT :plantCount")
    fun getAll(plantCount: Int): List<PlantTriangleEntity>

    @Query("SELECT * FROM plant_triangle LIMIT 1")
    fun findOne(): PlantTriangleEntity?

    @Query("SELECT * FROM plant_triangle WHERE triangle_name=:triangleName AND plant_name=:plantName LIMIT 1")
    fun findOneByTriangleNameAndPlantName(
        triangleName: String, plantName: String
    ): PlantTriangleEntity?


    @Query("SELECT * FROM plant_triangle WHERE triangle_name=:triangleName LIMIT :limit")
    fun findAllByTriangleName(
        triangleName: String, limit: Int = 50
    ): List<PlantTriangleEntity>

    @Query("SELECT * FROM plant_triangle LIMIT :limit")
    fun findAll(limit: Int = 50): List<PlantTriangleEntity>

    @Query("SELECT count(triangle_name) FROM plant_triangle GROUP BY triangle_name")
    fun findPlantsPerTriangle(): List<Int>

    @Query("SELECT root_weight FROM plant_triangle where triangle_name=:triangleName LIMIT :limit")
    fun findPlantRootMass(triangleName: String, limit: Int = 9): List<Double>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(fieldInfoEntity: PlantTriangleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(plantTriangleList: List<PlantTriangleEntity>)

    @Update
    fun update(fieldInfoEntity: PlantTriangleEntity)

}
