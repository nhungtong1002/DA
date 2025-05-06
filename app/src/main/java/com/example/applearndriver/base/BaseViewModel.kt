package com.example.applearndriver.base

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applearndriver.utils.extensions.convertSecondToMillisecond
import com.example.applearndriver.utils.extensions.getCurrentLicenseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(
    protected val sharedPreferences: SharedPreferences
) : ViewModel() {
    protected val loading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = loading

    protected val exception = MutableLiveData<Exception?>()
    val hasException: LiveData<Exception?>
        get() = exception

    private val _isVisibleFinishExamButton = MutableLiveData(false)
    val isVisibleFinishExamButton: LiveData<Boolean>
        get() = _isVisibleFinishExamButton

    private val _isVisibleResetButton = MutableLiveData(false)
    val isVisibleResetButton: LiveData<Boolean>
        get() = _isVisibleResetButton

    private var _timeOutLoadingJob: Job? = null

    val currentLicenseType
        get() = sharedPreferences.getCurrentLicenseType()

    protected fun launchTask(
        onRequest: suspend CoroutineScope.() -> Unit = {},
    ) = viewModelScope.launch {
        showLoading()
        onRequest(this)
    }

    protected fun showLoading() {
        loading.value = true

        //Set timeout 7s
        _timeOutLoadingJob = viewModelScope.launch {
            delay(7L.convertSecondToMillisecond())
            loading.value = false
        }
    }

    protected fun hideLoading() {
        loading.value = false
        _timeOutLoadingJob?.cancel()
    }

    fun setVisibleFinishExamButton(isVisible: Boolean) {
        _isVisibleFinishExamButton.value = isVisible
    }

    fun setVisibleResetButton(isVisible: Boolean) {
        _isVisibleResetButton.value = isVisible
    }
}

