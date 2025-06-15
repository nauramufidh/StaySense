package com.example.staysense.ui.profile.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.staysense.database.HistoryEntity
import com.example.staysense.database.PredictionDatabase
import com.example.staysense.database.PredictionHistoryDao
import com.example.staysense.database.UploadHistoryDao
import com.example.staysense.database.UploadHistoryEntity

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val predictionHistoryDao: PredictionHistoryDao =
        PredictionDatabase.Companion.getInstance(application).predictionHistoryDao()

    private val uploadHistoryDao: UploadHistoryDao =
        PredictionDatabase.Companion.getInstance(application).uploadHistoryDao()

    private val _combinedHistory = MediatorLiveData<List<CombinedHistory>>()
    val combinedHistory: LiveData<List<CombinedHistory>> get() = _combinedHistory

    private lateinit var manualLiveData: LiveData<List<HistoryEntity>>
    private lateinit var uploadLiveData: LiveData<List<UploadHistoryEntity>>

    fun loadHistory(userId: String) {
        manualLiveData = predictionHistoryDao.getAllHistoryByUserId(userId)
        uploadLiveData = uploadHistoryDao.getAllUploadHistoryByUserId(userId)

        _combinedHistory.addSource(manualLiveData) { manualList ->
            val combined = mergeHistory(manualList, uploadLiveData.value)
            _combinedHistory.value = combined
        }

        _combinedHistory.addSource(uploadLiveData) { uploadList ->
            val combined = mergeHistory(manualLiveData.value, uploadList)
            _combinedHistory.value = combined
        }
    }

    private fun mergeHistory(
        manual: List<HistoryEntity>?,
        upload: List<UploadHistoryEntity>?
    ): List<CombinedHistory> {
        val manualMapped = manual?.map {
            CombinedHistory(it.id, it.timestamp, "Manual Input")
        } ?: emptyList()

        val uploadMapped = upload?.map {
            CombinedHistory(it.id, it.timestamp, "File Upload")
        } ?: emptyList()

        return (manualMapped + uploadMapped).sortedByDescending { it.timestamp }
    }

    suspend fun deleteHistory(userId: String){
        predictionHistoryDao.deleteAllByUser(userId)
        uploadHistoryDao.deleteAllUploadByUser(userId)
    }
}