package com.example.applearndriver.data.datasource

import com.example.applearndriver.data.model.TipsHighScore
import com.example.applearndriver.utils.interfaces.IResponseListener

interface ITipsHighScoreDataSource {
    interface Remote {
        suspend fun callTipsHighScoreData(listener: IResponseListener<MutableList<TipsHighScore>>)
    }
}
