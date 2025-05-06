package com.example.applearndriver.data.datasource

import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.utils.interfaces.IResponseListener

interface IWrongAnswerDataSource {
    interface Local {
        suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer>
        suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer)
        suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer)
        suspend fun findWrongAnswerQuestionByID(id: Int): WrongAnswer?
    }

    interface Remote {
        suspend fun getAllListQuestion(listener: IResponseListener<MutableList<NewQuestion>>)
    }
}
