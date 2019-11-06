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
import androidx.core.view.GravityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smackchat.Adapter.messageAdapter
import com.example.smackchat.R
import com.example.smackchat.Utilities.BROADCAST_USER_DATA_CHANGE
import com.example.smackchat.Utilities.SOCKET_URL
import com.example.smackchat.model.Message
import com.example.smackchat.model.channel
import com.example.smackchat.services.AuthService
import com.example.smackchat.services.UserDataService
import com.example.smackchat.services.messageService
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.nio.channels.Channel

class MainActivity : AppCompatActivity() {

    val socket = IO.socket(SOCKET_URL)
    lateinit var channelAdapter: ArrayAdapter<channel>
    lateinit var messageAdapter: messageAdapter
    var selectedChannel: channel? = null

    private fun setupAdapters(){
        channelAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, messageService.channels)

            channel_list.adapter = channelAdapter
        messageAdapter = messageAdapter(this, messageService.messages)
        messagelistview.adapter = messageAdapter
        val layoutManager = LinearLayoutManager(this)
        messagelistview.layoutManager = layoutManager
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        socket.connect()
        socket.on("channel created", onNewChannel)
        socket.on("message created", onnewMessage )

        LocalBroadcastManager.getInstance(this).registerReceiver(
            UserDataChangeReciever,
            IntentFilter(BROADCAST_USER_DATA_CHANGE))

        channel_list.setOnItemClickListener { _, _, i, _ ->
            selectedChannel = messageService.channels[i]
            drawer_layout.closeDrawer(GravityCompat.START)



        }

        if (App.prefs.isLoggedIn) {
            AuthService.findUserbyEmail(this) {

            }
        }
    }



    override fun onDestroy() {
        socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(UserDataChangeReciever)

        super.onDestroy()
    }


    private val UserDataChangeReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (App.prefs.isLoggedIn) {
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

                messageService.getchannels{complete->
                    if (complete){
                        if (messageService.channels.count()>0){
                            selectedChannel = messageService.channels[0]
                            channelAdapter.notifyDataSetChanged()
                            updatewithchannel()
                        }
                    }
                }

            }
        }

        fun updatewithchannel() {
            mainchannelname.text = "#${selectedChannel?.name}"

            if (selectedChannel!=null){
                messageService.getMessages(selectedChannel!!.id){complete->
                    if (complete){
                     messageAdapter.notifyDataSetChanged()
                        if (messageAdapter.itemCount>0){
                            messagelistview.smoothScrollToPosition(messageAdapter.itemCount - 1)

                        }
                    }
                }

            }

        }    }

    fun loginbtnNavClicked(view: View) {
        if (App.prefs.isLoggedIn) {
            UserDataService.logout()
            channelAdapter.notifyDataSetChanged()
            messageAdapter.notifyDataSetChanged()
            usernameNavHeader.text = ""
            userEmailNavHeader.text = ""
            userimageNavHeader.setImageResource(R.drawable.defaultprofile)
            userimageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginbtnNavHeader.text = "Login"
            mainchannelname.text = "Please Log In"

        } else {

            val loginIntent = Intent(this, loginactivity::class.java)
            startActivity(loginIntent)


        }


    }

    fun addchannelclicked(view: View) {

        if (App.prefs.isLoggedIn) {

            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)


            builder.setView(dialogView).setPositiveButton("add") { _, _ ->

                val nametextfield = dialogView.findViewById<EditText>(R.id.addchannelnametxt)
                val desctextfield = dialogView.findViewById<EditText>(R.id.addchanneldesctxt)
                val channelName = nametextfield.text.toString()
                val channelDesc = desctextfield.text.toString()

                socket.emit("newChannel", channelName, channelDesc)


            }.setNegativeButton("cancel") { _, _ ->


            }.show()


        }

    }

      private val onNewChannel = Emitter.Listener { args ->
          if (App.prefs.isLoggedIn){
              runOnUiThread {
                  val channelName = args[0] as String
                  val channelDescription = args[1] as String
                  val channeId = args[2] as String

                  val newChannel = channel(channelName, channelDescription, channeId)
                  messageService.channels.add(newChannel)
                  channelAdapter.notifyDataSetChanged()
          }

          }

      }

    private val onnewMessage = Emitter.Listener {args ->
        if (App.prefs.isLoggedIn){

            runOnUiThread {

                val channelId = args[2] as String
                if (channelId==selectedChannel?.id){
                    val msgBody = args[0] as String

                    val userName = args[3] as String
                    val userAvatar = args[4] as String
                    val userAvatarColor = args[5] as String
                    val id = args[6] as String
                    val timeStamp = args[7] as String

                    val newMessage = Message(msgBody, channelId, userName, userAvatar, userAvatarColor, id, timeStamp)
                    messageService.messages.add(newMessage)
                    messageAdapter.notifyDataSetChanged()
                    messagelistview.smoothScrollToPosition(messageAdapter.itemCount - 1)

                }


            }
        }

    }

    fun sendmsgbtnclicked(view: View) {

        if (App.prefs.isLoggedIn && messagetext.text.isNotEmpty() && selectedChannel != null){
            val userId = UserDataService.id
            val channelId = selectedChannel!!.id
            socket.emit("newMessage", messagetext.text.toString(), userId, channelId, UserDataService.name,
                UserDataService.avatarName, UserDataService.avatarColor)
            messagetext.text.clear()
        }
        hidekeyboard()

    }

    fun hidekeyboard() {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }
}

