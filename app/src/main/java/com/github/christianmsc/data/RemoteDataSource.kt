package com.github.christianmsc.com.github.christianmsc.data

import com.github.christianmsc.com.github.christianmsc.data.network.ExercisesApi
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val exercisesApi: ExercisesApi
) {

    suspend fun getExercises(headers: Map<String, String>): Response<Exercise> {
        return exercisesApi.getExercises(headers)
    }

    suspend fun searchExercises(searchQuery: Map<String, String>): Response<Exercise> {
        return exercisesApi.searchExercises(searchQuery)
    }

    suspend fun getExercise(id: String): Response<ExerciseItem> {
        return exercisesApi.getExercise(id)
    }
}