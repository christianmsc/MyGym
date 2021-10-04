package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity

@Database(
    entities = [ExercisesEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExercisesTypeConverter::class)
abstract class ExercisesDatabase: RoomDatabase() {

    abstract fun exercisesDao(): ExercisesDao
}