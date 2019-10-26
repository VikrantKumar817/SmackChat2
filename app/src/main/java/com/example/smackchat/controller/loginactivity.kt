package com.example.smackchat.controller

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.smackchat.R
import com.example.smackchat.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_loginactivity.*

class loginactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)

        loginSpinner.visibility = View.INVISIBLE
    }

    fun loginloginbtnclicked(view:View){

        enablespinner(true)

        val email = loginemailtxt.text.toString()
        val password = loginpasswordtxt.text.toString()
        hidekeyboard()

        if (email.isNotEmpty() && password.isNotEmpty()){

            AuthService.loginUser(this, email, password){ loginSuccess->
                if (loginSuccess){
                    AuthService.findUserbyEmail(this){ findSuccess->
                        if (findSuccess){
                            enablespinner(false)
                            finish()
                        } else{
                            errorToast()
                        }

                    }

                } else{
                    errorToast()
                }
            }

        } else{
            Toast.makeText(this, "please fill both email and password", Toast.LENGTH_LONG).show()
        }

    }

    fun logincreateuserclicked(view: View){
        val createuserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createuserIntent)
        finish()

    }
    fun errorToast() {
        Toast.makeText(this, "something went wrong,please try again", Toast.LENGTH_LONG).show()
        enablespinner(false)
    }

    fun enablespinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE

        } else {
            loginSpinner.visibility = View.INVISIBLE
        }

        loginloginbtn.isEnabled = !enable
        logincreateuser.isEnabled = !enable
    }

    fun hidekeyboard() {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken , 0)
        }
    }
}
