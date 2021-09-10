package com.github.christianmsc.com.github.christianmsc.data

import com.github.christianmsc.com.github.christianmsc.data.database.ExercisesDao
import com.github.christianmsc.com.github.christianmsc.data.database.ExercisesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val exercisesDao: ExercisesDao
) {

    fun readDatabase(): Flow<List<ExercisesEntity>> {
        return exercisesDao.readExercises()
    }

    suspend fun insertExercises(exercisesEntity: ExercisesEntity){
        exercisesDao.insertExercises(exercisesEntity)
    }
}