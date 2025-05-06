package com.example.applearndriver.data.repository

import com.example.applearndriver.data.datasource.ITipsHighScoreDataSource
import com.example.applearndriver.data.model.TipsHighScore
import com.example.applearndriver.utils.interfaces.IResponseListener

class TipsHighScoreRepository(
    private val remote: ITipsHighScoreDataSource.Remote,
) : ITipsHighScoreDataSource.Remote {

    override suspend fun callTipsHighScoreData(listener: IResponseListener<MutableList<TipsHighScore>>) =
        remote.callTipsHighScoreData(listener)
}
