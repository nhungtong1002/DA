package com.example.applearndriver.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.applearndriver.app.ui.main.MainActivity
import com.example.applearndriver.base.BindingActivity
import com.example.applearndriver.databinding.ActivitySplashBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.applearndriver.R
import com.example.applearndriver.data.model.TrafficSigns
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>() {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        initData()
        handleClicks()
        handleObserver()
    }

    private val viewModel by viewModels<SplashViewModel>()

    private fun initData() {
//        lifecycleScope.launch {
//            loadDataToFireBase()
//        }
    }

    private fun handleClicks() {
        supportActionBar?.hide()
    }

    private fun handleObserver() {
        lifecycleScope.launch {
            viewModel.loadingText.observe(this@SplashActivity) {
                binding.textLoading.text = it
            }

            viewModel.isLoadingDone.observe(this@SplashActivity) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
        }
    }


//    private fun loadDataToFireBase() {
//        val fireStore = FirebaseFirestore.getInstance().collection("question_new_ver")
//
//        val jsonString =
//            resources.openRawResource(R.raw.traffic_sign).bufferedReader().use {
//                it.readLines().joinToString(separator = "") { it.trim() }
//            }
//
//        val typeToken = object : TypeToken<MutableList<TrafficSigns>>() {}.type
//        val list = Gson().fromJson<MutableList<TrafficSigns>>(jsonString, typeToken)
//
//        list.forEach {
//            fireStore.document(it.id)
//                .set(it)
//        }
//    }

}