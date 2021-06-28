package com.neosoft.neostore.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.neosoft.neostore.activities.LoginActivity

@SuppressLint("CommitPrefEdits")
class SessionManagement(private var con: Context) {
    private var pref :SharedPreferences
    private var editor:SharedPreferences.Editor
    private var PRIVATE_MODE:Int = 0
    init {
        pref = con.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor = pref.edit()
    }
    companion object{
        val PREF_NAME :String ="Kotlin demo"
        val IS_LOGIN :String ="isLoggedIn"
        val KEY_NAME :String ="name"
        val KEY_EMAIL :String="email"
    }
    fun createLoginSession(name:String,email:String){
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_NAME,name)
        editor.putString(KEY_EMAIL,email)
        editor.commit()
    }
    fun checkLogin()
    {
        if (!this.isLoggedIn())
        {
            val i = Intent(con,LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)
        }
    }
     fun isLoggedIn(): Boolean {
            return pref.getBoolean(IS_LOGIN,false)
    }
    fun logoutUser()
    {
        editor.clear()
        editor.commit()
        val i = Intent(con,LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)
    }
}