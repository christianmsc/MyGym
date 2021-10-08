package com.github.christianmsc.com.github.christianmsc.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.RANDOM_EXERCISE_TABLE

@Entity(tableName = RANDOM_EXERCISE_TABLE)
class RandomExerciseEntity(
    @Embedded
    var randomExercise: ExerciseItem
) {
    @PrimaryKey(autoGenerate = false)
    var id2: Int = 0
}