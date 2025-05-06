package com.example.applearndriver.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.applearndriver.data.model.StudyCategory

@Dao
interface StudyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewStudyCategory(studyCategory: List<StudyCategory>)

    @Query("select count(*) from ${StudyCategory.STUDY_CATEGORY_TABLE} where iconResourceID =:id")
    suspend fun checkExistItem(id: Int): Int

    @Query("select * from ${StudyCategory.STUDY_CATEGORY_TABLE}")
    suspend fun getAllInfoStudyCategory(): List<StudyCategory>
}
