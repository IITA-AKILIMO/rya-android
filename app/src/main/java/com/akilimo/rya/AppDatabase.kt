package com.akilimo.rya

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akilimo.rya.entities.*
import com.akilimo.rya.repos.*

@Database(
    entities = [FieldInfoEntity::class, YieldPrecisionEntity::class, PlantTriangleEntity::class, FieldYieldEntity::class, EstimateResultsEntity::class, CurrencyEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fieldInfoDao(): FieldInfoDao
    abstract fun yieldPrecisionDao(): YieldPrecisionDao
    abstract fun plantTriangleDao(): PlantTriangleDao
    abstract fun fieldYieldDao(): FieldYieldDao
    abstract fun estimateResultsDao(): EstimateResultsDao
    abstract fun currencyDao(): CurrencyDao


    companion object {
        // For Singleton instantiation
        @Volatile
        private var database: AppDatabase? = null
        private const val NUMBER_OF_THREADS = 4

        @JvmStatic
        @Synchronized
        fun getDatabase(context: Context): AppDatabase? {
            if (database == null) {
                synchronized(AppDatabase::class.java) {
                    if (database == null) {
                        database = Room.databaseBuilder(
                            context.applicationContext, AppDatabase::class.java, "RYA_15_DEC_2022"
                        )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return database
        }
    }
}
