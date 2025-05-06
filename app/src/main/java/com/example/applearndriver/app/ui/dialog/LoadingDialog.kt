package com.example.applearndriver.app.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.applearndriver.databinding.DialogLoadingLayoutBinding

object LoadingDialog {
    private var dialog: AlertDialog? = null
    fun showLoadingDialog(context: Context?) {
        if (dialog == null) {
            val windowLayoutParams = WindowManager.LayoutParams()
            windowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

            val dialogBinding = DialogLoadingLayoutBinding.inflate(
                LayoutInflater.from(context),
                null,
                false
            )
            dialog = AlertDialog.Builder(context)
                .setView(dialogBinding.root)
                .setCancelable(false)
                .create()
            dialog?.window?.attributes = windowLayoutParams
        }

        dialog?.show()
    }

    fun hideLoadingDialog() {
        dialog?.hide()
    }

    fun shutDownDialog() {
        dialog = null
    }
}
