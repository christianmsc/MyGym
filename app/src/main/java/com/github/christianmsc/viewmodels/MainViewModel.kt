package com.github.christianmsc.com.github.christianmsc.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.github.christianmsc.com.github.christianmsc.data.Repository
import com.github.christianmsc.com.github.christianmsc.data.database.ExercisesEntity
import com.github.christianmsc.com.github.christianmsc.models.Exercise
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
        repository.local.readDatabase().asLiveData()

    private fun insertExercises(exercisesEntity: ExercisesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertExercises(exercisesEntity)
        }

    /** RETROFIT **/
    var exercisesResponse: MutableLiveData<NetworkResult<Exercise>> = MutableLiveData()

    fun getExercises(headers: Map<String, String>) = viewModelScope.launch {
        getExercisesSafeCall(headers)
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

    private fun offlineCacheExercises(exercises: Exercise) {
        val exercisesEntity = ExercisesEntity(exercises)
        insertExercises(exercisesEntity)
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