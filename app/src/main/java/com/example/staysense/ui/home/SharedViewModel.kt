package com.example.staysense.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.jvm.internal.Ref.BooleanRef

class SharedViewModel: ViewModel() {
    private val _uploadLiveData = MutableLiveData<Boolean>()
    val uploadLiveData: LiveData<Boolean> get() = _uploadLiveData

    fun setUploadSuccess(value: Boolean){
        _uploadLiveData.value = value
    }
}