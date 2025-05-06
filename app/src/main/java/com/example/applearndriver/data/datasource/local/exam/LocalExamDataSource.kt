package com.example.applearndriver.data.datasource.local.exam

import com.example.applearndriver.data.datasource.IExamDataSource
import com.example.applearndriver.data.model.Exam
import com.example.applearndriver.data.database.ExamDao

class LocalExamDataSource(
    private val examDao: ExamDao,
) : IExamDataSource.Local {

    override suspend fun insertNewExam(exam: Exam) = examDao.insertNewExam(exam)

    override suspend fun updateExam(exam: Exam) = examDao.updateExam(exam)
    override suspend fun getAllExamByLicenseType(licenseType: String)
        = examDao.getAllExamByLicenseType(licenseType)

    override suspend fun deleteExam(exam: Exam) = examDao.deleteExam(exam)

    override suspend fun getAllExam(): MutableList<Exam> = examDao.getAllExam()

    override suspend fun getDetailExamByID(id: Int): Exam = examDao.getDetailExamByID(id)

}
