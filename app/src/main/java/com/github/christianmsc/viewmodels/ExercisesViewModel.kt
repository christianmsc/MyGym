package com.github.christianmsc.com.github.christianmsc.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.christianmsc.com.github.christianmsc.data.BodyPartAndTarget
import com.github.christianmsc.com.github.christianmsc.data.DataStoreRepository
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_EXERCISES_NUMBER
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_BODY_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_NUMBER
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.QUERY_SEARCH
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

    private lateinit var bodyPartAndTarget: BodyPartAndTarget

    var networkStatus = false
    var backOnline = false

    val readBodyPartAndTarget = dataStoreRepository.readBodyPartAndTarget
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveBodyPartAndTarget() {
        if (!::bodyPartAndTarget.isInitialized) {
            bodyPartAndTarget = BodyPartAndTarget(
                bodyPart,
                0,
                target,
                0
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBodyPartAndTarget(
                bodyPartAndTarget.selectedBodyPart,
                bodyPartAndTarget.selectedBodyPartId,
                bodyPartAndTarget.selectedTarget,
                bodyPartAndTarget.selectedTargetId
            )
        }
    }

    fun saveBodyPartAndTargetTemp(
        bodyPart: String,
        bodyPartId: Int,
        target: String,
        targetId: Int
    ) {
        bodyPartAndTarget = BodyPartAndTarget(
            bodyPart,
            bodyPartId,
            target,
            targetId
        )
    }

    fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        val selectedBodyPart = if(::bodyPartAndTarget.isInitialized) bodyPartAndTarget.selectedBodyPart else bodyPart
        val selectedTarget = if(::bodyPartAndTarget.isInitialized) bodyPartAndTarget.selectedTarget else target

        queries[QUERY_NUMBER] = DEFAULT_EXERCISES_NUMBER
        queries[QUERY_BODY_PART] = selectedBodyPart
        queries[QUERY_BODY_TARGET] = selectedTarget

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}