package com.example.applearndriver.data.repository

import com.example.applearndriver.data.datasource.IExamDataSource
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.utils.interfaces.IResponseListener

class ExamRepository(
    private val local: IExamDataSource.Local,
    private val remote: IExamDataSource.Remote,
) : IExamDataSource.Local, IExamDataSource.Remote {
    override suspend fun insertNewExam(exam: Exam) = local.insertNewExam(exam)

    override suspend fun updateExam(exam: Exam) = local.updateExam(exam)
    override suspend fun getAllExamByLicenseType(licenseType: String)
        = local.getAllExamByLicenseType(licenseType)

    override suspend fun deleteExam(exam: Exam) = local.deleteExam(exam)

    override suspend fun getAllExam(): MutableList<Exam> = local.getAllExam()

    override suspend fun getDetailExamByID(id: Int): Exam = local.getDetailExamByID(id)
    override suspend fun getListQuestion(listener: IResponseListener<MutableList<NewQuestion>>) {
        remote.getListQuestion(listener)
    }

}
