package com.github.christianmsc.com.github.christianmsc.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.github.christianmsc.com.github.christianmsc.data.Repository
import com.github.christianmsc.com.github.christianmsc.data.database.entities.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.FavoritesEntity
import com.github.christianmsc.com.github.christianmsc.data.database.entities.RandomExerciseEntity
import com.github.christianmsc.com.github.christianmsc.models.Exercise
import com.github.christianmsc.com.github.christianmsc.models.ExerciseItem
import com.github.christianmsc.com.github.christianmsc.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE **/

    val readExercises: LiveData<List<ExercisesEntity>> =
        repository.local.readExercises().asLiveData()
    val readFavoriteExercises: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteExercises().asLiveData()
    val readRandomExercise: LiveData<List<RandomExerciseEntity>> =
        repository.local.readRandomExercise().asLiveData()

    private fun insertExercises(exercisesEntity: ExercisesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertExercises(exercisesEntity)
        }

    fun insertFavoriteExercise(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteExercise(favoritesEntity)
        }

    private fun insertRandomExercise(randomExerciseEntity: RandomExerciseEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRandomExercise(randomExerciseEntity)
        }

    fun deleteFavoriteExercise(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteExercise(favoritesEntity)
        }

    fun deleteAllFavoriteExercises() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavoriteExercises()
    }

    /** RETROFIT **/
    var exercisesResponse: MutableLiveData<NetworkResult<Exercise>> = MutableLiveData()
    var searchedExercisesResponse: MutableLiveData<NetworkResult<Exercise>> = MutableLiveData()
    var exerciseResponse: MutableLiveData<NetworkResult<ExerciseItem>> = MutableLiveData()

    fun getExercises(headers: Map<String, String>) = viewModelScope.launch {
        getExercisesSafeCall(headers)
    }

    fun searchExercises(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchExercisesSafeCall(searchQuery)
    }

    fun getExercise(id: String) = viewModelScope.launch {
        getExerciseSafeCall(id)
    }

    private suspend fun getExercisesSafeCall(headers: Map<String, String>) {
        exercisesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getExercises(headers)
                exercisesResponse.value = handleExercisesResponse(response)

                val exercises = exercisesResponse.value!!.data
                if (exercises != null) {
                    offlineCacheExercises(exercises)
                }
            } catch (e: Exception) {
                exercisesResponse.value = NetworkResult.Error("Exercises not found.")
            }
        } else {
            exercisesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun searchExercisesSafeCall(searchQuery: Map<String, String>) {
        searchedExercisesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchExercises(searchQuery)
                searchedExercisesResponse.value = handleExercisesResponse(response)
            } catch (e: Exception) {
                searchedExercisesResponse.value = NetworkResult.Error("Exercises not found.")
            }
        } else {
            searchedExercisesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private suspend fun getExerciseSafeCall(id: String) {
        exerciseResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getExercise(id)
                exerciseResponse.value = handleExerciseResponse(response)

                val randomExercise = exerciseResponse.value!!.data
                if (randomExercise != null) {
                    offlineCacheRandomExercise(randomExercise)
                }
            } catch (e: Exception) {
                exerciseResponse.value = NetworkResult.Error("Exercise not found.")
            }
        } else {
            exerciseResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheExercises(exercises: Exercise) {
        val exercisesEntity = ExercisesEntity(exercises)
        insertExercises(exercisesEntity)
    }

    private fun offlineCacheRandomExercise(randomExercise: ExerciseItem) {
        val randomExerciseEntity = RandomExerciseEntity(randomExercise)
        insertRandomExercise(randomExerciseEntity)
    }

    private fun handleExercisesResponse(response: Response<Exercise>): NetworkResult<Exercise>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.isNullOrEmpty() -> {
                return NetworkResult.Error("Exercises not found.")
            }
            response.isSuccessful -> {
                val exercises = response.body()
                return NetworkResult.Success(exercises!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun handleExerciseResponse(response: Response<ExerciseItem>): NetworkResult<ExerciseItem>? {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                NetworkResult.Error("API Key Limited.")
            }
            response.isSuccessful -> {
                val exercises = response.body()
                NetworkResult.Success(exercises!!)
            }
            else -> {
                NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}