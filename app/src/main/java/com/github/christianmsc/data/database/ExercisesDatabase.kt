package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ExercisesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ExercisesTypeConverter::class)
abstract class ExercisesDatabase: RoomDatabase() {

    abstract fun exercisesDao(): ExercisesDao
}