package com.example.applearndriver.data.datasource.remote.exam

import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.datasource.IExamDataSource
import com.example.applearndriver.data.model.NewQuestion
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class RemoteExamDataSource(
    private val fireStoreDB: FirebaseFirestore,
) : IExamDataSource.Remote {

    private val questionCollections by lazy {
        fireStoreDB.collection(AppConstant.NEW_QUESTION_COLLECTION)
    }

    override suspend fun getListQuestion(listener: IResponseListener<MutableList<NewQuestion>>) {
        questionCollections.get().addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                val listQuestions = mutableListOf<NewQuestion>()

                tasks.result.documents.forEach {
                    it.toObject(NewQuestion::class.java)?.let { notNullObject ->
                        listQuestions.add(notNullObject)
                    }
                }
                listQuestions.sortBy { it.id }
                listener.onSuccess(listQuestions)
            } else {
                listener.onError(tasks.exception)
            }
        }
    }
}
