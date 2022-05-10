package com.jakey.exoplayertutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _seekTimeLiveData = MutableLiveData(0L)
    val seekTimeLiveData: LiveData<Long> = _seekTimeLiveData

    private val _restoreMediaLiveData = MutableLiveData(0)
    val restoreMediaLiveData: LiveData<Int> = _restoreMediaLiveData

    fun setSeekTimeLiveData(l: Long) {
            _seekTimeLiveData.postValue(l)
    }
    fun setRestoreMediaLiveData(i: Int) =
        _restoreMediaLiveData.postValue(i)
}