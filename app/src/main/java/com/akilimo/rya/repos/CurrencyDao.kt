package com.akilimo.rya.repos

import androidx.room.*
import com.akilimo.rya.entities.CurrencyEntity

@Dao
interface CurrencyDao {

    @Query("SELECT * from currencies")
    fun getAll(): MutableList<CurrencyEntity>


    @Query("SELECT * FROM currencies WHERE country_code =:countryCode")
    fun findOne(countryCode: String): CurrencyEntity?

    @Query("SELECT * FROM currencies WHERE country_name =:countryName")
    fun findByCountryName(countryName: String): CurrencyEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(currencyEntity: CurrencyEntity)

    @Update
    fun update(currencyEntity: CurrencyEntity)
}
