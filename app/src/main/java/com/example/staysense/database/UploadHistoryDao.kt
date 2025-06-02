package com.example.staysense.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UploadHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: UploadHistoryEntity)

    @Query("SELECT * FROM upload_history WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllUploadHistoryByUserId(userId: String): LiveData<List<UploadHistoryEntity>>

    @Query("SELECT * FROM upload_history WHERE id = :id")
    suspend fun getUploadHistoryById(id: Int): UploadHistoryEntity?

    @Query("DELETE FROM upload_history WHERE userId = :userId")
    suspend fun deleteAllUploadByUser(userId: String)

}