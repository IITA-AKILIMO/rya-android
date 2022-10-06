package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_info")
data class FieldInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "field_area_unit") var fieldAreaUnit: String = "acre",
    @ColumnInfo(name = "area_unit_index") var areaUnitIndex: Int = 0,
    @ColumnInfo(name = "selling_currency_unit") var currencyUnit: String = "tonne",
    @ColumnInfo(name = "currency_unit_index") var currencyUnitIndex: Int = 0,
    @ColumnInfo(name = "field_size") var fieldSize: Double = 1.0,
    @ColumnInfo(name = "currency") var currency: String = "USD",
    @ColumnInfo(name = "currency_name") var currencyName: String = "USD",
    @ColumnInfo(name = "area_unit") var areaUnit: String = "acre",
    @ColumnInfo(name = "selling_price") var sellingPrice: Double = 1.0,


    @ColumnInfo(name = "triangle_1_plant_count") var triangle1PlantCount: Int = 0,
    @ColumnInfo(name = "triangle_2_plant_count") var triangle2PlantCount: Int = 0,
    @ColumnInfo(name = "triangle_4_plant_count") var triangle3PlantCount: Int = 0
)
