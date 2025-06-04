package com.example.applearndriver.app.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.utils.extensions.isCurrentDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    sharedPreferences: SharedPreferences,
) : BaseViewModel(sharedPreferences) {

    private val _isDarkModeOn = MutableLiveData<Boolean>()

    init {
        _isDarkModeOn.postValue(sharedPreferences.isCurrentDarkMode())
    }

    val isDarkModeOn: LiveData<Boolean>
        get() = _isDarkModeOn

    fun turnOnDarkMode() {
        _isDarkModeOn.value = true
    }

    fun turnOffDarkMode() {
        _isDarkModeOn.value = false
    }

    fun saveDarkModeState() =
        sharedPreferences.edit()
            .putBoolean(AppConstant.DARK_MODE, _isDarkModeOn.value ?: false)
            .apply()
}
