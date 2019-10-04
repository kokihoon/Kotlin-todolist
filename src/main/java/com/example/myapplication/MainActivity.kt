package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.myapplication.Database.AppDatabase
import com.example.myapplication.Database.Helper.DatabaseWorker
import com.example.myapplication.Model.User
import com.example.myapplication.Presenter.ILoginPresenter
import com.example.myapplication.Presenter.LoginPresenter
import com.example.myapplication.View.ILoginView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btn_login
import kotlinx.android.synthetic.main.activity_main.btn_register
import kotlinx.android.synthetic.main.activity_main.edt_email
import kotlinx.android.synthetic.main.activity_main.edt_password
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login.registration_layout
import kotlinx.android.synthetic.main.user_registration.*

class MainActivity : AppCompatActivity(),ILoginView {

    private var mDb: AppDatabase? = null
    private lateinit var mDatabaseWorker: DatabaseWorker
    private val mUIHandler = Handler()

    override fun onLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onLoginSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    internal lateinit var loginPresenter: ILoginPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)

        //init
        loginPresenter = LoginPresenter(this);

        //Event
        btn_login.setOnClickListener {
            val loginCode = loginPresenter.onLogin(edt_email.text.toString(), edt_password.text.toString())

            if(loginCode == -1) {
                selectDataInDb(User(0,edt_email.toString(), edt_password.toString()))
            }
        }

        btn_register.setOnClickListener {
            showRegistration()
        }

        btn_reg.setOnClickListener {
            addUser()
            Toast.makeText(this, "잘 저장됬습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showRegistration() {
        registration_layout.visibility= View.VISIBLE
        login_layout.visibility=View.GONE
        main_layout.visibility=View.GONE
    }

    private fun insertDataInDb(user: User) {
        val task = Runnable { mDb?.userDao()?.insert(user) }
        mDatabaseWorker.postTask(task)
    }

    private fun selectDataInDb(user:User) {
        val task = Runnable{
            val userData = mDb?.userDao()?.selectUser(user.email)
            mUIHandler.post {
                if(userData == null) {
                    Toast.makeText(this, "잘안됨", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, userData.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
        mDatabaseWorker.postTask(task)

    }

    private var currentId = 1

    private fun addUser(){
        val email = reg_email.text.toString()
        val password = reg_password.text.toString()
        val checkPassword = reg_check_password.text.toString()

        insertDataInDb(User(currentId++, email, password))

    }

}
