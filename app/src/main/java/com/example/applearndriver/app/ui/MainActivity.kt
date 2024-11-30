package com.example.applearndriver.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import com.example.applearndriver.R
import com.example.applearndriver.databinding.ActivityMainBinding
import com.example.applearndriver.base.BindingActivity
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.TipsHighScore
import com.example.applearndriver.data.model.TrafficSigns
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(){
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        loadDataToFireBase()
    }
    private fun loadDataToFireBase() {
        val fireStore = FirebaseFirestore.getInstance().collection("traffic_sign")
        val jsonString =
            resources.openRawResource(R.raw.traffic_sign).bufferedReader().use {
                it.readLines().joinToString(separator = "") { it.trim() }
            }

        val typeToken = object : TypeToken<MutableList<TrafficSigns>>() {}.type
        val list = Gson().fromJson<MutableList<TrafficSigns>>(jsonString, typeToken)

        list.forEach {
            fireStore.document(it.id.toString())
                .set(it)
        }
    }

}