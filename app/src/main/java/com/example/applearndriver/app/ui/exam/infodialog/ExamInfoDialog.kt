package com.example.applearndriver.app.ui.exam.infodialog

import android.app.AlertDialog
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.text.bold
import com.example.applearndriver.R
import com.example.applearndriver.data.model.CreateExamRules
import com.example.applearndriver.databinding.DialogExamInfoBinding
import com.example.applearndriver.utils.extensions.getResourceColor


object ExamInfoDialog {
    private var dialog: AlertDialog? = null

    fun showExamInfoDialog(context: Context?, createExamRules: CreateExamRules) {
        context?.let { context ->
            if (dialog == null) {

                val windowLayoutParams = WindowManager.LayoutParams()
                windowLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
                windowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT

                val dialogBinding = DialogExamInfoBinding.inflate(
                    LayoutInflater.from(context),
                    null,
                    false
                )

                dialogBinding.apply {
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        buttonCloseDialog.setColorFilter(context.getResourceColor(R.color.white))
                    } else {
                        buttonCloseDialog.setColorFilter(context.getResourceColor(R.color.black))
                    }

                    textNumbersOfQuestion.text = SpannableStringBuilder().apply {
                        bold { append("Tổng số câu: ") }
                        append("${createExamRules.totalNumberOfQuestion} câu")
                    }

                    textExamDuration.text = SpannableStringBuilder().apply {
                        bold { append("Thời gian làm bài: ") }
                        append("${createExamRules.examDurationByMinutes} phút")
                    }

                    textMinimumQuestionToPassExam.text = SpannableStringBuilder().apply {
                        bold { append("Số câu đúng tối thiểu: ") }
                        append("${createExamRules.minimumCorrectToPassExam} câu")
                    }

                    textTitleExamInfo.text = context.resources.getString(
                        R.string.text_title_exam_info,
                        createExamRules.licenseType.type
                    )

                    dialog = AlertDialog.Builder(context)
                        .setView(dialogBinding.root)
                        .setCancelable(false)
                        .create()
                    dialog?.window?.attributes = windowLayoutParams

                    dialogBinding.buttonCloseDialog.setOnClickListener {
                        dialog?.dismiss()
                        shutDownDialog()
                    }
                }

                dialog?.show()
            }
        }
    }
    fun shutDownDialog() {
        dialog = null
    }
}