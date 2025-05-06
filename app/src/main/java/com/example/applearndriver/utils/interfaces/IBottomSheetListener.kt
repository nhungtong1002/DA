package com.example.applearndriver.utils.interfaces

import com.example.applearndriver.data.model.QuestionOptions

interface IBottomSheetListener {
    fun onNextQuestion(listener: IResponseListener<Int>)
    fun onPreviousQuestion(listener: IResponseListener<Int>)
    fun onClickMoveToPosition(index: Int, data: QuestionOptions)
}
