package com.example.applearndriver.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.applearndriver.app.ui.MainActivity
import com.example.applearndriver.base.BindingActivity
import com.example.applearndriver.databinding.ActivitySplashBinding
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
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
                MainActivity.start(this@SplashActivity)
            }
        }
    }

}