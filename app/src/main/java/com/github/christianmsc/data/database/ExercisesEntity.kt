package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.EXERCISES_TABLE

@Entity(tableName = EXERCISES_TABLE)
class ExercisesEntity(
    var exercise: Exercise
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}