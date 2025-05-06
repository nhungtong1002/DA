package com.example.applearndriver.data.datasource

import com.example.applearndriver.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

interface ITrafficSignalDataSource {
    interface Remote {
        fun getAllTrafficSignal(listener: IResponseListener<MutableList<TrafficSigns>>)
    }
}
