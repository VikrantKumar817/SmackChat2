package com.example.smackchat.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smackchat.R
import com.example.smackchat.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smackchat.services.AuthService
import com.example.smackchat.services.UserDataService
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(UserDataChangeReciever,
            IntentFilter(BROADCAST_USER_DATA_CHANGE))
    }
    private val UserDataChangeReciever = object :BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (AuthService.isLoggedIn){
                usernameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userimageNavHeader.setImageResource(resourceId)
                userimageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
                loginbtnNavHeader.text = "Logout"

            }

        }


    }

    fun loginbtnNavClicked(view: View) {
        if (AuthService.isLoggedIn){
            UserDataService.logout()
            usernameNavHeader.text = "Login"
            userEmailNavHeader.text = ""
            userimageNavHeader.setImageResource(R.drawable.defaultprofile)
            userimageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginbtnNavHeader.text = "Login"

        } else{

            val loginIntent = Intent(this, loginactivity::class.java)
            startActivity(loginIntent)


        }


    }

    fun addchannelclicked(view: View){

    }

    fun sendmsgbtnclicked(view: View){

    }
}
