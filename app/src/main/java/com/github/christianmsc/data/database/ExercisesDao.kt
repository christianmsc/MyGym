package com.github.christianmsc.com.github.christianmsc.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercisesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertExercises(exercisesEntity: ExercisesEntity)

    @Query("SELECT * FROM exercises_table ORDER BY id ASC")
    fun readExercises(): Flow<List<ExercisesEntity>>
}