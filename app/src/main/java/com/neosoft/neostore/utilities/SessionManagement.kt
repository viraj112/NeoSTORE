package com.neosoft.neostore.utilities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.neosoft.neostore.activities.LoginActivity

public class SessionManagement {
    lateinit var pref :SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    lateinit var con :Context
    var PRIVATE_MODE:Int = 0

    constructor(con:Context){
        this.con =con
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
            var i : Intent= Intent(con,LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            con.startActivity(i)

        }

        fun getUserDetails():HashMap<String,String>
        {
            var user:Map<String,String> = HashMap<String,String>()
            (user as HashMap).put(KEY_NAME, pref.getString(KEY_NAME,null)!!)
            (user as HashMap).put(KEY_NAME, pref.getString(KEY_EMAIL,null)!!)
        return user
        }



    }

     fun isLoggedIn(): Boolean {
            return pref.getBoolean(IS_LOGIN,false)

    }
    fun logoutUser()
    {
        editor.clear()
        editor.commit()
        var i : Intent= Intent(con,LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        con.startActivity(i)

    }
}