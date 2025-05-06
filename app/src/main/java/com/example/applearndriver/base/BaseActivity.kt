package com.example.applearndriver.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding>(
    private val bindingInflater: (LayoutInflater) -> T,
) : AppCompatActivity() {

    private var _binding: T? = null

    protected val viewBinding: T
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater(layoutInflater)
        setContentView(viewBinding.root)

        initData(savedInstanceState)
        handleEvent()
        bindData()
    }

    open fun showLoadingDialog() {}

    open fun hideLoadingDialog() {}

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun handleEvent()

    abstract fun bindData()

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
