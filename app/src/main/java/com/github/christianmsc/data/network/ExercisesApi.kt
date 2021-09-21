package com.github.christianmsc.com.github.christianmsc.data.network

import com.github.christianmsc.com.github.christianmsc.models.Exercise
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

interface ExercisesApi {
    @GET("/exercises")
    suspend fun getExercises(
        @QueryMap queries : Map<String, String>
    ): Response<Exercise>

}