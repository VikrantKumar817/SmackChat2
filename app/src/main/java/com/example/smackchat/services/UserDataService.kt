package com.example.smackchat.services

import android.graphics.Color
import com.example.smackchat.controller.App
import java.util.*

object UserDataService {

    var id = ""
    var avatarName = ""
    var avatarColor = ""
     var email = ""
    var name = ""

    fun logout(){
        id = ""
        avatarName = ""
        avatarColor = ""
        email = ""
        name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
        messageService.clearMessages()
        messageService.clearChannels()

    }

    fun returnAvatarColor(components: String) : Int {
        val strippedColor = components.replace("[", "").replace("]", "").replace(",", "")
        var r = 0
        var g = 0
        var b = 0
        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()) {
            r = (scanner.nextDouble() * 255).toInt()
            g = (scanner.nextDouble() * 255).toInt()
            b = (scanner.nextDouble() * 255).toInt()
        }
        return Color.rgb(r,g,b)
    }
}