package com.akilimo.rya

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akilimo.rya.entities.FieldInfo
import com.akilimo.rya.repos.FieldInfoDao

@Database(
    entities = [
        FieldInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fieldInfoDao(): FieldInfoDao


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
                            AppDatabase::class.java, "RYA_16_MAR_2022"
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
