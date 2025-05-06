package com.example.applearndriver.data.repository

import com.example.applearndriver.data.datasource.IWrongAnswerDataSource
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.WrongAnswer
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

class WrongAnswerRepository(
    private val local: IWrongAnswerDataSource.Local,
    private val remote: IWrongAnswerDataSource.Remote,
) : IWrongAnswerDataSource.Local, IWrongAnswerDataSource.Remote {

    override suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer> =
        local.getAllWrongAnswerQuestion()

    override suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.insertNewWrongAnswerQuestion(wrongAnswer)

    override suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        local.updateWrongAnswerQuestion(wrongAnswer)

    override suspend fun findWrongAnswerQuestionByID(id: Int)
        = local.findWrongAnswerQuestionByID(id)

    override suspend fun getAllListQuestion(listener: IResponseListener<MutableList<NewQuestion>>) {
        remote.getAllListQuestion(listener)
    }

}
