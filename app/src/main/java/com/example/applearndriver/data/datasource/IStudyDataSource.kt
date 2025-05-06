package com.example.applearndriver.data.datasource

import com.example.applearndriver.data.model.NewQuestion
import com.example.applearndriver.data.model.StudyCategory
import com.nguyennhatminh614.motobikedriverlicenseapp.utils.interfaces.IResponseListener

interface IStudyDataSource {
    interface Local {
        suspend fun saveProgress(studyCategoryList: List<StudyCategory>)
        suspend fun getAllInfoStudyCategory(): List<StudyCategory>
    }

    interface Remote {
        suspend fun getListQuestion(listener: IResponseListener<MutableList<NewQuestion>>)
    }
}
