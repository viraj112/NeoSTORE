package com.neosoft.neostore.utilities

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class Validations {
    companion object{
        fun isValidemail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

    }

}