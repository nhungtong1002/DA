package com.example.applearndriver.app.ui.instruction

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.data.model.getAllMotorbikeLicenseType
import com.example.applearndriver.utils.extensions.getCurrentLicenseType
import com.example.applearndriver.utils.extensions.isCurrentDarkMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InstructionViewModel @Inject constructor(
    sharedPreferences: SharedPreferences
): BaseViewModel(sharedPreferences){

    private val _isMotorbikeLicenseType = MutableLiveData<Boolean>()
    val isMotorbikeLicenseType: LiveData<Boolean>
        get() = _isMotorbikeLicenseType

    val isDarkModeOn: Boolean
        get() = sharedPreferences.isCurrentDarkMode()

    init {
        checkCurrentLicenseType()
    }

    private fun checkCurrentLicenseType() {
        launchTask {
            val isMotorbikeLicense = sharedPreferences.getCurrentLicenseType() in getAllMotorbikeLicenseType()
            _isMotorbikeLicenseType.value = isMotorbikeLicense
            hideLoading()
        }
    }
}
