package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.Database.AppDatabase
import com.example.myapplication.Database.Helper.DatabaseWorker
import com.example.myapplication.Model.User
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private var mDb: AppDatabase? = null
    private lateinit var mDatabaseWorker: DatabaseWorker
    private val mUIHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mDatabaseWorker = DatabaseWorker("dbWorkerThread")
        mDatabaseWorker.start()

        mDb = AppDatabase.getInstance(this)


        btn_reg1.setOnClickListener {
            addUser()
            Toast.makeText(this, "잘 저장됬습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private var currentId = 1

    private fun addUser(){
        val email = reg_email.text.toString()
        val password = reg_password.text.toString()
        val checkPassword = reg_check_password.text.toString()
        if(password != checkPassword) {
            showMessage("비밀번호가 다릅니다.")
        }
        if(!checkEmail(User(0, email, password))) {
            insertDataInDb(User(currentId++, email, password))
        } else{
            showMessage("중복된 이메일이 있습니다.")
        }
    }

    private fun showMessage(message:String) {
        AlertDialog
            .Builder(this)
            .setMessage(message)
            .create()
            .show()
    }

    private fun checkEmail(user:User):Boolean {
        var check = true
        lifecycleScope.launch(Dispatchers.IO) { //비동기 처리 안됨

            val task = Runnable {
                val userData = mDb?.userDao()?.checkEmail(user.email)
                if (userData == null) {
                    check = false
                }
            }
            mDatabaseWorker.postTask(task)
        }
        return check
    }


    private fun insertDataInDb(user: User) {
        val task = Runnable {
            mDb?.userDao()?.insert(user)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        mDatabaseWorker.postTask(task)
    }

}
