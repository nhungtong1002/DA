package com.example.applearndriver.data.datasource.remote.trafficsign

import com.example.applearndriver.constant.AppConstant
import com.example.applearndriver.data.datasource.ITrafficSignalDataSource
import com.example.applearndriver.data.model.TrafficSigns
import com.google.firebase.firestore.FirebaseFirestore
import com.example.applearndriver.utils.interfaces.IResponseListener

class TrafficSignRemoteDataSource(
    private val fireStoreDB: FirebaseFirestore,
) : ITrafficSignalDataSource.Remote {

    private val trafficSignsCollections by lazy {
        fireStoreDB.collection(AppConstant.TRAFFIC_SIGN_COLLECTION)
    }

    override fun getAllTrafficSignal(listener: IResponseListener<MutableList<TrafficSigns>>) {
        trafficSignsCollections.get().addOnCompleteListener { tasks ->
            if (tasks.isSuccessful) {
                val listTrafficSign = mutableListOf<TrafficSigns>()

                tasks.result.documents.forEach {
                    it.toObject(TrafficSigns::class.java)?.let { notNullObject ->
                        listTrafficSign.add(notNullObject)
                    }
                }

                listener.onSuccess(listTrafficSign)
            } else {
                listener.onError(tasks.exception)
            }
        }
    }
}
