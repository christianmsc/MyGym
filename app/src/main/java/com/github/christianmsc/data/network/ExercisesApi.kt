package com.github.christianmsc.com.github.christianmsc.data.network

import com.github.christianmsc.com.github.christianmsc.models.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface ExercisesApi {
    @GET("/exercises")
    suspend fun getExercises(
        @HeaderMap headers : Map<String, String>
    ): Response<Exercise>

}