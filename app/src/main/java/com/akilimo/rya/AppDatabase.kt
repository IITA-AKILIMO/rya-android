package com.akilimo.rya

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.FieldYieldEntity
import com.akilimo.rya.entities.PlantTriangleEntity
import com.akilimo.rya.entities.YieldPrecisionEntity
import com.akilimo.rya.repos.FieldInfoDao
import com.akilimo.rya.repos.FieldYieldDao
import com.akilimo.rya.repos.PlantTriangleDao
import com.akilimo.rya.repos.YieldPrecisionDao

@Database(
    entities = [
        FieldInfoEntity::class,
        YieldPrecisionEntity::class,
        PlantTriangleEntity::class,
        FieldYieldEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fieldInfoDao(): FieldInfoDao
    abstract fun yieldPrecisionDao(): YieldPrecisionDao
    abstract fun plantTriangleDao(): PlantTriangleDao
    abstract fun fieldYieldDao(): FieldYieldDao


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
                            context.applicationContext,
                            AppDatabase::class.java, "RYA_22_MAR_2022"
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
