package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "plant_triangle",
    indices = [Index(value = ["plant_name", "triangle_name"], unique = true)]
)
data class PlantTriangleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "triangle_name")
    var triangleName: String,
    @ColumnInfo(name = "plant_name")
    var plantName: String,
    @ColumnInfo(name = "root_weight")
    var rootWeight: Double,
)
