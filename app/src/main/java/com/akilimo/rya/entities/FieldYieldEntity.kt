package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_yield")
data class FieldYieldEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "image_id")
    val imageId: Int,
    @ColumnInfo(name = "yield_amount_label")
    val fieldYieldAmountLabel: String,
    @ColumnInfo(name = "yield_label")
    val yieldLabel: String,
    @ColumnInfo(name = "yield_amount")
    val yieldAmount: Double,
    @ColumnInfo(name = "yield_desc")
    val fieldYieldDesc: String
)