package com.neosoft.neostore.utilities

import android.text.TextUtils
import android.util.Patterns

class Validations {
    companion object{
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }
}