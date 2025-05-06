package com.example.applearndriver.utils.extensions

import android.content.Context
import android.graphics.Color
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat

val isCurrentDarkModeByDefault : Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

val isCurrentLightModeByDefault : Boolean
    get() = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO

fun Context.getResourceColor(@ColorRes colorID: Int)
    = ContextCompat.getColor(this, colorID)

fun Context.showToast(message: String, isLongDuration: Boolean = false) {
    Toast.makeText(
        this,
        message,
        if (isLongDuration) Toast.LENGTH_LONG
        else Toast.LENGTH_SHORT
    ).show()
}