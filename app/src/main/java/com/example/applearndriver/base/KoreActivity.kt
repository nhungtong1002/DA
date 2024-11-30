package com.example.applearndriver.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat

abstract class KoreActivity : AppCompatActivity() {
    protected abstract fun updateUI(savedInstanceState: Bundle?)
    protected abstract fun createContentView(savedInstanceState: Bundle?): View

    @ColorRes
    open fun getStatusBarColor(): Int {
        return android.R.color.white
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ContextCompat.getColor(this, getStatusBarColor()))
        val viewRoot = createContentView(savedInstanceState)
        setContentView(viewRoot)
        supportActionBar?.hide()
        actionBar?.hide()
        updateUI(savedInstanceState)
    }

    fun setStatusBarColor(@ColorInt color: Int) {
        if (window.statusBarColor == color) return
        window.statusBarColor = color
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = isColorLight(color)
        }
    }

    private fun isColorDark(@ColorInt color: Int): Boolean {
        val darkness =
            1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }

    private fun isColorLight(@ColorInt color: Int): Boolean {
        return !isColorDark(color)
    }

    fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}