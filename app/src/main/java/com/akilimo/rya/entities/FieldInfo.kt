package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_info")
data class FieldInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "field_area_unit")
    val fieldAreaUnit: String,
    @ColumnInfo(name = "field_size")
    val fieldSize: Double,
    @ColumnInfo(name = "selling_price_unit")
    val sellingPriceUnit: String,
    @ColumnInfo(name = "selling_price")
    val sellingPrice: Double
)
