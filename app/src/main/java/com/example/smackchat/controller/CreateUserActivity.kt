package com.example.smackchat.controller

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.smackchat.R
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var useravatar = "dafaultprofile"
    var avatarcolor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
    }

    fun generateuseravatar(view:View){
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(4)
        if (color==0){
            useravatar = "light$avatar"
        } else {
            useravatar = "dark$avatar"
        }

        val resourceId =  resources.getIdentifier(useravatar, "drawable", packageName)
        createavatarimageView.setImageResource(resourceId)
    }

    fun generatecolorclicked(view: View){
        val random = Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)
        createavatarimageView.setBackgroundColor(Color.rgb(r,g,b))
        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255
        avatarcolor = "[$savedR, $savedG, $savedB, 1]"
    }

    fun createuserbtnclicked(view: View){

    }
}
