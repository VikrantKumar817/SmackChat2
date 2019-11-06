package com.example.smackchat.controller

import android.app.Application
import com.example.smackchat.Utilities.SharedPref

class App: Application() {

    companion object {
         lateinit var prefs =  SharedPref


    }

    override fun onCreate() {
        prefs = SharedPref(applicationContext)
        super.onCreate()
    }
}