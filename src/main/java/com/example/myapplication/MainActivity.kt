package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.Presenter.ILoginPresenter
import com.example.myapplication.Presenter.LoginPresenter
import com.example.myapplication.View.ILoginView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),ILoginView {

    override fun onLoginResult(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    internal lateinit var loginPresenter: ILoginPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        loginPresenter = LoginPresenter(this);

        //Event
        btn_login.setOnClickListener {
            loginPresenter.onLogin(edt_email.text.toString(), edt_password.text.toString())
        }
    }
}
