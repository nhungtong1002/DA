package com.example.applearndriver.data.model.roomtypeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.applearndriver.data.model.WrongAnswer

class WrongAnswerObjectTypeConverter {
    @TypeConverter
    fun convertJsonToListOptionAnswer(json: String): MutableList<WrongAnswer?> {
        val typeToken = object : TypeToken<MutableList<WrongAnswer>>() {}.type
        return Gson().fromJson(json, typeToken)
    }

    @TypeConverter
    fun convertListOptionAnswerToJson(listQuestions: MutableList<WrongAnswer>): String? =
        Gson().toJson(listQuestions)
}
