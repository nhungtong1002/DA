package com.example.applearndriver.utils.interfaces

interface IResponseListener<T> {
    fun onSuccess(data: T)
    fun onError(exception: Exception?)
}
