package com.example.staysense.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prediction_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: String,
    val source: String,
    val message: String,
    val churnProbability: String,
    val isChurn: String,
    val timestamp: Long,

    val age: Int,
    val numberOfDependents: Int,
    val city: String,
    val tenureInMonths: Int,
    val internetService: String,
    val onlineSecurity: String,
    val onlineBackup: String,
    val deviceProtectionPlan: String,
    val premiumTechSupport: String,
    val streamingTv: String,
    val streamingMovies: String,
    val streamingMusic: String,
    val unlimitedData: String,
    val contract: String,
    val paymentMethod: String,
    val monthlyCharge: Double,
    val totalCharges: Double,
    val totalRevenue: Double,
    val satisfactionScore: Double,
    val churnScore: Int,
    val cltv: Int
)
