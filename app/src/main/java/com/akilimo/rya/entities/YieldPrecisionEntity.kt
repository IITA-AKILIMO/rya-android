package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "yield_precision")
data class YieldPrecisionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "yield_precision")
    var yieldPrecision: String = "",
    @ColumnInfo(name = "triangle_count")
    var triangleCount: Int = -1,
    @ColumnInfo(name = "plant_count")
    var plantCount: Int = -1,
    @ColumnInfo(name = "precision_image")
    var precisionImage: Int = -1,
    @ColumnInfo(name = "precision_index")
    var selectedPrecisionIndex: Int = -1
)
