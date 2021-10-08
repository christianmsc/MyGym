package com.github.christianmsc.com.github.christianmsc.data.network

import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ExercisesApi {
    @GET("/exercises")
    suspend fun getExercises(
        @QueryMap queries: Map<String, String>
    ): Response<Exercise>

    @GET("/exercises")
    suspend fun searchExercises(
        @QueryMap queries: Map<String, String>
    ): Response<Exercise>

    @GET("/exercises/{id}")
    suspend fun getExercise(
        @Path("id") id: String
    ): Response<ExerciseItem>

}