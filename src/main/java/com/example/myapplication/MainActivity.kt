package com.example.myapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.Utils.SessionManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var session:SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = SessionManager(applicationContext)

        session.checkLogin()

        var user: HashMap<String, String> = session.getUserDetails()

        var email: String = user.get(SessionManager.KEY_EMAIL)!!

        username.text = email

        btn_logout.setOnClickListener {
            session.LoggoutUser()
        }


    }
}
