package com.example.a213396_lingchinwei_drnazatulaini_lab5.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HelpRequest::class], version = 1, exportSchema = false)
abstract class AtlasDatabase : RoomDatabase() {
    abstract fun helpRequestDao(): HelpRequestDao

    companion object {
        @Volatile
        private var Instance: AtlasDatabase? = null

        fun getDatabase(context: Context): AtlasDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AtlasDatabase::class.java, "atlas_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
