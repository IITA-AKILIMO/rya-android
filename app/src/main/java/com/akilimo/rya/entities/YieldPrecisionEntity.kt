package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yield_precision")
data class YieldPrecisionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "yield_precision")
    var yieldPrecision: String,
    @ColumnInfo(name = "triangle_count")
    var triangleCount: Int,
    @ColumnInfo(name = "plant_count")
    var plantCount: Int,
)
