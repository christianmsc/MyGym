package com.github.christianmsc.com.github.christianmsc.data

import com.github.christianmsc.com.github.christianmsc.data.network.ExercisesApi
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val exercisesApi: ExercisesApi
) {

    suspend fun getExercises(headers: Map<String,String>) : Response<Exercise> {
        return exercisesApi.getExercises(headers)
    }
}