package com.example.applearndriver.data.model.roomtypeconverter

import androidx.room.TypeConverter
import com.example.applearndriver.data.model.NewQuestion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.applearndriver.data.model.ExamHistory

class ExamTypeConverter {
    @TypeConverter
    fun convertJsonToListQuestion(json: String): MutableList<NewQuestion?> {
        val typeToken = object : TypeToken<MutableList<NewQuestion>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListQuestionToJson(listQuestions: MutableList<NewQuestion>): String? =
        Gson().toJson(listQuestions)

    @TypeConverter
    fun convertJsonToListExamHistory(json: String): MutableList<ExamHistory?> {
        val typeToken = object : TypeToken<MutableList<ExamHistory>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListExamHistoryToJson(listQuestions: MutableList<ExamHistory>): String? =
        Gson().toJson(listQuestions)
}
