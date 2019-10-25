package com.example.smackchat.controller

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smackchat.R
import com.example.smackchat.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smackchat.services.AuthService
import com.example.smackchat.services.UserDataService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var useravatar = "profile"
    var avatarcolor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createspinner.visibility = View.INVISIBLE

    }

    fun generateuseravatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(4)
        if (color == 0) {
            useravatar = "light$avatar"
        } else {
            useravatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(useravatar, "drawable", packageName)
        createavatarimageView.setImageResource(resourceId)
    }

    fun generatecolorclicked(view: View) {
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createavatarimageView.setBackgroundColor(Color.rgb(r, g, b))
        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarcolor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createuserbtnclicked(view: View) {
        enablespinner(true)

        val userName = createusernametxt.text.toString()
        val email = createemailtxt.text.toString()
        val password = createpasswordtxt.text.toString()
        if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {

            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createuser(
                                this,
                                userName,
                                email,
                                useravatar,
                                avatarcolor
                            ) { createSuccess ->
                                if (createSuccess) {

                                    val userdatachange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this)
                                        .sendBroadcast(userdatachange)
                                    enablespinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(
                this,
                "make sure username, email and password is correct",
                Toast.LENGTH_LONG
            ).show()
            enablespinner(false)
        }

    }
    fun errorToast() {
        Toast.makeText(this, "something went wrong,please try again", Toast.LENGTH_LONG).show()
        enablespinner(false)
    }

    fun enablespinner(enable: Boolean) {
        if (enable) {
            createspinner.visibility = View.VISIBLE

        } else {
            createspinner.visibility = View.INVISIBLE
        }

        createuserbtn.isEnabled = !enable
        createavatarimageView.isEnabled = !enable
        backgroundcolorbtn.isEnabled = !enable
    }
}



