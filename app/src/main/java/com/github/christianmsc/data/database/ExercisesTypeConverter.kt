package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.TypeConverter
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ExercisesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun exerciseToString(exercise: Exercise): String {
        return gson.toJson(exercise)
    }

    @TypeConverter
    fun stringToExercise(data: String): Exercise {
        val listType = object : TypeToken<Exercise>(){}.type
        return gson.fromJson(data, listType)
    }
}