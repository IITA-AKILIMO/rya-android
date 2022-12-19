package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_info")
data class FieldInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "are_unit") var areaUnit: String = "",
    @ColumnInfo(name = "field_size") var fieldSize: Double = -1.0,
    @ColumnInfo(name = "currency") var currency: String = "",
    @ColumnInfo(name = "currency_name") var currencyName: String = "",
    @ColumnInfo(name = "selling_price") var sellingPrice: Double = -1.0,

    @ColumnInfo(name = "triangle_1_plant_count") var triangle1PlantCount: Int = -1,
    @ColumnInfo(name = "triangle_2_plant_count") var triangle2PlantCount: Int = -1,
    @ColumnInfo(name = "triangle_4_plant_count") var triangle3PlantCount: Int = -1
)
