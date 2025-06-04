package com.example.applearndriver.app.ui.tiphighscores

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.applearndriver.base.BaseViewModel
import com.example.applearndriver.data.model.TipsHighScore
import com.example.applearndriver.data.repository.TipsHighScoreRepository
import com.example.applearndriver.utils.interfaces.IResponseListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TipsHighScoreViewModel @Inject constructor(
    private val repository: TipsHighScoreRepository,
    sharedPreferences: SharedPreferences,
) : BaseViewModel(sharedPreferences) {
    private val _listTipsHighScore = MutableLiveData<MutableList<TipsHighScore>>()

    val listTipsHighScore: LiveData<MutableList<TipsHighScore>>
        get() = _listTipsHighScore

    init {
        fetchData()
    }

    private fun fetchData() {
        launchTask {
            repository.callTipsHighScoreData(
                object : IResponseListener<MutableList<TipsHighScore>> {
                    override fun onSuccess(data: MutableList<TipsHighScore>) {
                        _listTipsHighScore.postValue(data)
                        hideLoading()
                    }

                    override fun onError(exception: Exception?) {
                        this@TipsHighScoreViewModel.exception.postValue(exception)
                        hideLoading()
                    }
                }
            )
        }
    }

}
