package com.example.staysense.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upload_history")
data class UploadHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val source: String,
    val totalCustomers: Int,
    val churnCount: Int ,
    val notChurnCount: Int,
    val churnRate: String,
    val timestamp: Long
)
