package com.example.nicolaskaplan_comp304lab_ex1.networkServices

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.FavoriteLocation

@Database(entities = [FavoriteLocation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteLocationDao(): FavoriteLocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}