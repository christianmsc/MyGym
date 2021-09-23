package com.github.christianmsc.com.github.christianmsc.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.DEFAULT_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_BODY_PART
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_BODY_PART_ID
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_NAME
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_TARGET
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.PREFERENCES_TARGET_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = PREFERENCES_NAME)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedBodyPart = stringPreferencesKey(PREFERENCES_BODY_PART)
        val selectedBodyPartId = intPreferencesKey(PREFERENCES_BODY_PART_ID)
        val selectedTarget = stringPreferencesKey(PREFERENCES_TARGET)
        val selectedTargetId = intPreferencesKey(PREFERENCES_TARGET_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveBodyPartAndTarget(
        bodyPart: String,
        bodyPartId: Int,
        target: String,
        targetId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedBodyPart] = bodyPart
            preferences[PreferenceKeys.selectedBodyPartId] = bodyPartId
            preferences[PreferenceKeys.selectedTarget] = target
            preferences[PreferenceKeys.selectedTargetId] = targetId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readBodyPartAndTarget: Flow<BodyPartAndTarget> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedBodyPart = preferences[PreferenceKeys.selectedBodyPart] ?: DEFAULT_BODY_PART
            val selectedBodyPartId = preferences[PreferenceKeys.selectedBodyPartId] ?: 0
            val selectedTarget = preferences[PreferenceKeys.selectedTarget] ?: DEFAULT_TARGET
            val selectedTargetId = preferences[PreferenceKeys.selectedTargetId] ?: 0
            BodyPartAndTarget(
                selectedBodyPart,
                selectedBodyPartId,
                selectedTarget,
                selectedTargetId
            )
        }

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}

data class BodyPartAndTarget(
    val selectedBodyPart: String,
    val selectedBodyPartId: Int,
    val selectedTarget: String,
    val selectedTargetId: Int
)