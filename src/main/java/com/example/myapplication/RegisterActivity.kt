package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.Database.AppDatabase
import com.example.myapplication.Database.Helper.DatabaseWorker
import com.example.myapplication.Model.User
import com.example.myapplication.Presenter.ILoginPresenter
import com.example.myapplication.Presenter.IRegisterPresenter
import com.example.myapplication.Presenter.RegisterPresenter
import com.example.myapplication.View.IRegisterView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), IRegisterView {

    override fun onRegisterSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRegisterError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private var mDb: AppDatabase? = null
    private lateinit var mDatabaseWorker: DatabaseWorker

    internal lateinit var registerPresenter: IRegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)

        //init
        registerPresenter = RegisterPresenter(this)

        btn_reg.setOnClickListener {
            val email = reg_email.text.toString()
            val password = reg_password.text.toString()
            val checkPassword = reg_check_password.text.toString()

            var registerCode = registerPresenter.onRegister(email,password,checkPassword) // 회원가입 값 체크

            if(registerCode == -1) {
                addUser(email, password)
            }
        }
    }

    private var currentId = 1

    private fun addUser(email:String, password:String){

        checkEmail(email, password)
    }

    private fun checkEmail(email:String, password:String) {
        val task = Runnable {
            val userData = mDb?.userDao()?.checkEmail(email)
            if (userData == null) {
                mDb?.userDao()?.insert(User(currentId++, email, password))
                Toast.makeText(this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "중복된 이메일이 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        mDatabaseWorker.postTask(task)
    }
}
