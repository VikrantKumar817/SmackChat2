package com.example.smackchat.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smackchat.R

class loginactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)
    }

    fun loginloginbtnclicked(view:View){

    }

    fun logincreateuserclicked(view: View){
        val createuserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createuserIntent)
        finish()

    }
}
