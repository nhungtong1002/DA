package com.example.applearndriver.utils.extensions

import android.util.TypedValue
import androidx.viewbinding.ViewBinding
import com.example.applearndriver.utils.extensions.getResourceColor

fun ViewBinding.getCurrentThemeTextColor() : Int {
    val typedValue = TypedValue()
    this.root.context.theme.resolveAttribute(android.R.attr.textColor, typedValue, true)
    return typedValue.data
}

fun ViewBinding.getCurrentThemeBackgroundColor(): Int {
    val typedValue = TypedValue()
    this.root.context.theme.resolveAttribute(android.R.attr.colorBackground, typedValue, true)
    return typedValue.data
}

fun ViewBinding.getCurrentThemeCardColor() : Int {
    val typedValue = TypedValue()
    this.root.context.theme.resolveAttribute(android.R.attr.colorBackgroundFloating, typedValue, true)
    return typedValue.data
}

fun ViewBinding.getSelectedColor(color: Int) =
    this.root.context.getResourceColor(color)