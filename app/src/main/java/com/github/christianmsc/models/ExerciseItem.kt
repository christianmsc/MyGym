package com.github.christianmsc.com.github.christianmsc.models


import com.google.gson.annotations.SerializedName

data class ExerciseItem(
    @SerializedName("bodyPart")
    val bodyPart: String,
    @SerializedName("equipment")
    val equipment: String,
    @SerializedName("gifUrl")
    val gifUrl: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("target")
    val target: String
)