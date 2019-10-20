package com.example.smackchat.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.example.smackchat.R

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


    }

    fun loginbtnNavClicked(view: View) {

        val loginIntent = Intent(this, loginactivity::class.java)
        startActivity(loginIntent)

    }

    fun addchannelclicked(view: View){

    }

    fun sendmsgbtnclicked(view: View){
    }
}
