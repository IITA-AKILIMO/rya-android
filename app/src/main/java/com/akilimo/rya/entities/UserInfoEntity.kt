package com.akilimo.rya.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "full_names") var fullNames: String,
    @ColumnInfo(name = "country") var countryCode: String,
    @ColumnInfo(name = "currency") var currencyCode: String,
    @ColumnInfo(name = "designation") var designation: String,
    @ColumnInfo(name = "phone_number") var phoneNumber: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "language") var language: String,
    @ColumnInfo(name = "area_unit") var areaUnit: String,
    @ColumnInfo(name = "area_unit_text") var areaUnitText: String
)
