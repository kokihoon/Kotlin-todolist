package com.example.myapplication.Utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.myapplication.LoginActivity
import com.example.myapplication.MainActivity

class SessionManager {

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context

    var PRIVATE_MODE:Int = 0

    constructor(con:Context) {
        this.con =con
        pref = con.getSharedPreferences(KEY_EMAIL, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object {
        val IS_LOGIN:String ="isLoginIn"
        val KEY_EMAIL: String = "email"
    }

    fun createLoginSession(email: String, islogin:Boolean) {
        editor.putBoolean(IS_LOGIN, islogin)
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }

    fun checkLogin() {
        if(!this.isLoggedIn()) {
            var intent = Intent(con, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            con.startActivity(intent)
        }
    }

    fun getUserDetails():HashMap<String, String> {
        var user:Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_EMAIL, pref.getString(KEY_EMAIL, null).toString())

        return user
    }

    fun LoggoutUser() {
        //editor.clear()
        editor.putBoolean(IS_LOGIN, false)
        editor.commit()

        var intent = Intent(con, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        con.startActivity(intent)
    }

    fun isLoggedIn():Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
}