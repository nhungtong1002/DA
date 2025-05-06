package com.example.applearndriver.data.datasource

import com.example.applearndriver.data.model.TrafficSigns
import com.example.applearndriver.utils.interfaces.IResponseListener

interface ITrafficSignalDataSource {
    interface Remote {
        fun getAllTrafficSignal(listener: IResponseListener<MutableList<TrafficSigns>>)
    }
}
