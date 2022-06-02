package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estimate_results")
data class EstimateResultsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "tonnage_price")
    val tonnagePrice: Double,
    @ColumnInfo(name = "tonnage_estimate")
    val tonnageEstimate: Double,
    @ColumnInfo(name = "field_area")
    val fieldArea: Double,
    @ColumnInfo(name = "file_name_full")
    var fileNameFull: String,
    @ColumnInfo(name = "file_name_lean")
    var fileNameLean: String
)
