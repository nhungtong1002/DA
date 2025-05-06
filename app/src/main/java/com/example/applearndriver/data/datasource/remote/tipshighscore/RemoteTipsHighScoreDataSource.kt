package com.example.applearndriver.data.datasource.remote.tipshighscore

import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.datasource.ITipsHighScoreDataSource
import com.example.applearndriver.data.model.TipsHighScore
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class RemoteTipsHighScoreDataSource(
    private val fireStoreDatabase: FirebaseFirestore,
) : ITipsHighScoreDataSource.Remote {

    private val tipsHighScoreCollection by lazy {
        fireStoreDatabase.collection(AppConstant.TIPS_HIGH_SCORE_COLLECTION)
    }

    override suspend fun callTipsHighScoreData(listener: IResponseListener<MutableList<TipsHighScore>>) {
        tipsHighScoreCollection.get().addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                val listTipsHighScore = mutableListOf<TipsHighScore>()

                tasks.result.documents.forEach {
                    it.toObject(TipsHighScore::class.java)
                        ?.let { notNullObject -> listTipsHighScore.add(notNullObject) }
                }

                listener.onSuccess(listTipsHighScore)
            } else {
                listener.onError(tasks.exception)
            }
        }
    }
}
