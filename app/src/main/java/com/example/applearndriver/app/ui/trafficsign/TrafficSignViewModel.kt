package com.example.applearndriver.app.ui.trafficsign

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.data.model.TrafficSigns
import com.example.applearndriver.data.repository.TrafficRepository
import com.example.applearndriver.utils.extensions.processDoubleQuotes
import com.example.applearndriver.utils.extensions.processEndline
import com.example.applearndriver.utils.interfaces.IResponseListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrafficSignViewModel @Inject constructor(
    private val trafficRepository: TrafficRepository,
    sharedPreferences: SharedPreferences,
) : BaseViewModel(sharedPreferences) {

    private val _listTrafficSign = MutableLiveData<MutableList<TrafficSigns>>()

    val listTrafficSign: LiveData<MutableList<TrafficSigns>>
        get() = _listTrafficSign

    init {
        getTrafficSignListFromRemote()
    }

    private fun getTrafficSignListFromRemote() {
        launchTask {
            trafficRepository.getAllTrafficSignal(
                object : IResponseListener<MutableList<TrafficSigns>> {
                    override fun onSuccess(data: MutableList<TrafficSigns>) {
                        data.forEach {
                            it.title = it.title.processDoubleQuotes()
                            it.description = it.description.processEndline()
                        }
                        _listTrafficSign.postValue(data)
                        hideLoading()
                    }

                    override fun onError(exception: Exception?) {
                        this@TrafficSignViewModel.exception.postValue(exception)
                        hideLoading()
                    }
                }
            )
        }
    }

    fun getListByCategory(category: String) =
        _listTrafficSign.value
            ?.filter { return@filter it.category.lowercase() == category.lowercase() }
            ?.toMutableList()
}
