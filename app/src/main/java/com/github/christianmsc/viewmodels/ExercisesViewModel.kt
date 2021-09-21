package com.github.christianmsc.com.github.christianmsc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.christianmsc.com.github.christianmsc.data.DataStoreRepository
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_EXERCISES_NUMBER
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_BODY_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_NUMBER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var bodyPart = DEFAULT_BODY_PART
    private var target = DEFAULT_TARGET

    val readBodyPartAndTarget = dataStoreRepository.readBodyPartAndTarget

    fun saveBodyPartAndTarget(bodyPart: String, bodyPartId: Int, target: String, targetId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBodyPartAndTarget(bodyPart, bodyPartId, target, targetId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readBodyPartAndTarget.collect { value ->
                bodyPart = value.selectedBodyPart
                target = value.selectedTarget
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_EXERCISES_NUMBER
        queries[QUERY_BODY_PART] = bodyPart
        queries[QUERY_BODY_TARGET] = target

        return queries
    }

}