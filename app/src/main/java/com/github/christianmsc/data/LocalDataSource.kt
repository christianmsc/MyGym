package com.github.christianmsc.com.github.christianmsc.data

import com.github.christianmsc.com.github.christianmsc.data.database.ExercisesDao
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val exercisesDao: ExercisesDao
) {

    fun readExercises(): Flow<List<ExercisesEntity>> {
        return exercisesDao.readExercises()
    }

    fun readFavoriteExercises(): Flow<List<FavoritesEntity>> {
        return exercisesDao.readFavoriteExercises()
    }

    suspend fun insertExercises(exercisesEntity: ExercisesEntity) {
        exercisesDao.insertExercises(exercisesEntity)
    }

    suspend fun insertFavoriteExercise(favoritesEntity: FavoritesEntity) {
        exercisesDao.insertFavoriteExercise(favoritesEntity)
    }

    suspend fun deleteFavoriteExercise(favoritesEntity: FavoritesEntity) {
        exercisesDao.deleteFavoriteExercise(favoritesEntity)
    }

    suspend fun deleteAllFavoriteExercises() {
        exercisesDao.deleteAllFavoriteExercises()
    }
}