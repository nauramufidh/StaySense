package com.example.staysense.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class, UploadHistoryEntity::class],
    version = 4,
    exportSchema = false
)

abstract class PredictionDatabase : RoomDatabase() {

    abstract fun predictionHistoryDao(): PredictionHistoryDao
    abstract fun uploadHistoryDao(): UploadHistoryDao

    companion object{
        @Volatile private var INSTANCE: PredictionDatabase? = null

        fun getInstance(context: Context): PredictionDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PredictionDatabase::class.java,
                    "stay_sense_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}