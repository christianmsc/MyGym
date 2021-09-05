package com.github.christianmsc.com.github.christianmsc.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.christianmsc.com.github.christianmsc.util.Constants
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.API_HOST
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.API_KEY
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.HEADER_API_HOST
import com.github.christianmsc.com.github.christianmsc.util.Constants.Companion.HEADER_API_KEY

class ExercisesViewModel(application: Application): AndroidViewModel(application) {

    fun applyHeaders(): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()

        headers[HEADER_API_HOST] = API_HOST
        headers[HEADER_API_KEY] = API_KEY

        return headers
    }

}