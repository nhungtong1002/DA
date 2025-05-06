package com.example.applearndriver.data.repository

import com.example.applearndriver.data.datasource.ITrafficSignalDataSource
import com.example.applearndriver.data.model.TrafficSigns
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class TrafficRepository(
    private val remote: ITrafficSignalDataSource.Remote,
) : ITrafficSignalDataSource.Remote {
    override fun getAllTrafficSignal(listener: IResponseListener<MutableList<TrafficSigns>>) =
        remote.getAllTrafficSignal(listener)
}
