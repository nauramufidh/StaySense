package com.example.staysense.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    @Query("SELECT * FROM prediction_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllHistoryByUserId(userId: String): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM prediction_history WHERE id = :id")
    suspend fun getHistoryById(id: Int): HistoryEntity?

    @Query("DELETE FROM prediction_history WHERE userId = :userId")
    suspend fun deleteAllByUser(userId: String)
}