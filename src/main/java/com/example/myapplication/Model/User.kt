package com.example.myapplication.Model

import android.text.TextUtils
import android.util.Patterns
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="users")
class User(
    @NonNull
    @PrimaryKey
    @ColumnInfo(name="id")
    override val id:Int =0,

    @ColumnInfo(name="email")
    override val email:String,
    @ColumnInfo(name="password")
    override val password:String

    ):IUser {


    override fun isDataValid(): Int {
        if(TextUtils.isEmpty(email))
            return 0 // 0 error code is email empty
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return 1 // 1 error code is wrong patterns
        else if(password.length <= 6)
            return 2 // 2 error code is password length must be grater than 6
        else
            return -1 // -1 is success code
    }


}