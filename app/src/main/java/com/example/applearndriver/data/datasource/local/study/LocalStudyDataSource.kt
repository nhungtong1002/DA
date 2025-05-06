package com.example.applearndriver.data.datasource.local.study

import com.example.applearndriver.data.model.StudyCategory
import com.example.applearndriver.data.datasource.IStudyDataSource
import com.example.applearndriver.data.database.StudyDao

class LocalStudyDataSource(
    private val studyDao: StudyDao,
) : IStudyDataSource.Local {

    override suspend fun saveProgress(studyCategoryList: List<StudyCategory>) =
        studyDao.saveNewStudyCategory(studyCategoryList)

    override suspend fun getAllInfoStudyCategory(): List<StudyCategory> =
        studyDao.getAllInfoStudyCategory()

}

