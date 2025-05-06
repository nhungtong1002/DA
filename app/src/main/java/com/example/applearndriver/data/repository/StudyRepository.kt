package com.example.applearndriver.data.repository

import com.example.applearndriver.data.datasource.IStudyDataSource
import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.StudyCategory
import com.example.applearndriver.utils.interfaces.IResponseListener

class StudyRepository(
    private val local: IStudyDataSource.Local,
    private val remote: IStudyDataSource.Remote,
) : IStudyDataSource.Local, IStudyDataSource.Remote {

    override suspend fun saveProgress(studyCategoryList: List<StudyCategory>) =
        local.saveProgress(studyCategoryList)

    override suspend fun getAllInfoStudyCategory() = local.getAllInfoStudyCategory()
    
    override suspend fun getListQuestion(listener: IResponseListener<MutableList<NewQuestion>>) {
        remote.getListQuestion(listener)
    }

}
