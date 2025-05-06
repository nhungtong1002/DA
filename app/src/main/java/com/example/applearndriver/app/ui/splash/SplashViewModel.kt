package com.example.applearndriver.app.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.applearndriver.constant.AppConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {
    private val _loadingText = MutableLiveData<String>()
    val loadingText: MutableLiveData<String>
        get() = _loadingText

    private val _isLoadingDone = MutableLiveData<Boolean>()
    val isLoadingDone: MutableLiveData<Boolean>
        get() = _isLoadingDone

    private val loadingFlow = flow {
        for (i in MIN_LOOP..MAX_LOOP) {
            emit(LOADING + ICON.repeat(i))
            delay(AppConstant.TIME_TASK_DELAYED)
        }
    }

    init {
        loadingData()
    }

    private fun loadingData() {
        viewModelScope.launch {
            loadingFlow.collect {
                _loadingText.postValue(it)
            }

            _isLoadingDone.postValue(true)
        }
    }

    companion object {
        const val LOADING = "Đang tải"
        const val ICON = "."
        const val MIN_LOOP = 1
        const val MAX_LOOP = 3
    }
}