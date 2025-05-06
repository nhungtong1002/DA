package com.example.applearndriver.data.datasource.local.wronganswer

import com.example.applearndriver.data.model.WrongAnswer
import com.example.applearndriver.data.datasource.IWrongAnswerDataSource
import com.example.applearndriver.data.database.WrongAnswerDao

class LocalWrongAnswerDataSource(
    private val wrongAnswerDao: WrongAnswerDao,
) : IWrongAnswerDataSource.Local {

    override suspend fun getAllWrongAnswerQuestion(): MutableList<WrongAnswer> =
        wrongAnswerDao.getAllWrongAnswerQuestion()

    override suspend fun insertNewWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        wrongAnswerDao.insertNewWrongAnswerQuestion(wrongAnswer)

    override suspend fun updateWrongAnswerQuestion(wrongAnswer: WrongAnswer) =
        wrongAnswerDao.updateWrongAnswerQuestion(wrongAnswer)

    override suspend fun findWrongAnswerQuestionByID(id: Int) =
        wrongAnswerDao.findWrongAnswerQuestionByID(id)
}
