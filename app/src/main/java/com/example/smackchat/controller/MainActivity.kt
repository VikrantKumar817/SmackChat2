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
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.smackchat.R
import com.example.smackchat.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smackchat.Utilities.SOCKET_URL
import com.example.smackchat.model.channel
import com.example.smackchat.services.AuthService
import com.example.smackchat.services.UserDataService
import com.example.smackchat.services.messageService
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.nio.channels.Channel

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<channel>

    private fun setupAdapters(){
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageService.channels)

            channel_list.adapter = channelAdapter
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        socket.connect()
        socket.on("channel created", onNewChannel )
    }
        override fun onResume() {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                UserDataChangeReciever,
                IntentFilter(BROADCAST_USER_DATA_CHANGE))

            super.onResume()
        }

    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UserDataChangeReciever)

        super.onDestroy()
    }


    private val UserDataChangeReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AuthService.isLoggedIn) {
                usernameNavHeader.text = UserDataService.name
                userEmailNavHeader.text = UserDataService.email
                val resourceId =
                    resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                userimageNavHeader.setImageResource(resourceId)
                userimageNavHeader.setBackgroundColor(
                    UserDataService.returnAvatarColor(
                        UserDataService.avatarColor
                    )
                )
                loginbtnNavHeader.text = "Logout"

                messageService.getchannels(context){complete->
                    if (complete){
                        channelAdapter.notifyDataSetChanged()
                    }

                }



            }

        }


    }

    fun loginbtnNavClicked(view: View) {
        if (AuthService.isLoggedIn) {
            UserDataService.logout()
            usernameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userimageNavHeader.setImageResource(R.drawable.defaultprofile)
            userimageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginbtnNavHeader.text = "Login"

        } else {

            val loginIntent = Intent(this, loginactivity::class.java)
            startActivity(loginIntent)


        }


    }

    fun addchannelclicked(view: View) {

        if (AuthService.isLoggedIn) {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)


            builder.setView(dialogView).setPositiveButton("add") { dialogInterface, i ->

                val nametextfield = dialogView.findViewById<EditText>(R.id.addchannelnametxt)
                val desctextfield = dialogView.findViewById<EditText>(R.id.addchanneldesctxt)
                val channelName = nametextfield.text.toString()
                val channelDesc = desctextfield.text.toString()

                socket.emit("newChannel", channelName, channelDesc)


            }.setNegativeButton("cancel") { dialogInterface, i ->



            }.show()


        }

    }

      private val onNewChannel = Emitter.Listener { args ->
          runOnUiThread {
              val channelName = args[0] as String
              val channelDescription = args[1] as String
              val channeId = args[2] as String

              val newChannel = channel(channelName, channelDescription, channeId)
              messageService.channels.add(newChannel)
              channelAdapter.notifyDataSetChanged()
          }

      }

    fun sendmsgbtnclicked(view: View) {
        hidekeyboard()

    }

    fun hidekeyboard() {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}
