package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_info")
data class FieldInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "field_area_unit")
    var fieldAreaUnit: String,
    @ColumnInfo(name = "field_size")
    var fieldSize: Double,
    @ColumnInfo(name = "selling_price_unit")
    var sellingPriceUnit: String,
    @ColumnInfo(name = "selling_price")
    var sellingPrice: Double
)
