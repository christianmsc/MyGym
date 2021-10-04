package com.github.christianmsc.com.github.christianmsc.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.FAVORITE_EXERCISES_TABLE

@Entity(tableName= FAVORITE_EXERCISES_TABLE)
class FavoritesEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var exerciseItem: ExerciseItem
)