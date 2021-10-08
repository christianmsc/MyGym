package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.RandomExerciseEntity

@Database(
    entities = [ExercisesEntity::class, FavoritesEntity::class, RandomExerciseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExercisesTypeConverter::class)
abstract class ExercisesDatabase: RoomDatabase() {

    abstract fun exercisesDao(): ExercisesDao
}