package com.github.christianmsc.com.github.christianmsc.util

class Constants {

    companion object {

        const val BASE_URL = "https://json-server-my-gym.herokuapp.com"

        const val EXERCISE_RESULT_KEY = "exerciseBundle"

        // API Query Keys
        const val QUERY_SEARCH = "q"
        const val QUERY_NUMBER = "_limit"
        const val QUERY_BODY_PART = "bodyPart"
        const val QUERY_BODY_TARGET = "target"
        const val QUERY_BODY_EQUIPMENT = "equipment"

        // ROOM Database
        const val DATABASE_NAME = "exercises_database"
        const val EXERCISES_TABLE = "exercises_table"

        // Bottom Sheet and preferences
        const val DEFAULT_EXERCISES_NUMBER = "50"
        const val DEFAULT_BODY_PART = "waist"
        const val DEFAULT_TARGET = "abs"

        const val PREFERENCES_NAME = "exercises_references"
        const val PREFERENCES_BODY_PART = "bodyPart"
        const val PREFERENCES_BODY_PART_ID = "bodyPartId"
        const val PREFERENCES_TARGET = "target"
        const val PREFERENCES_TARGET_ID = "targetId"
        const val PREFERENCES_BACK_ONLINE = "backOnline"
    }

}