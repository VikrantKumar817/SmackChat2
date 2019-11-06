package com.example.smackchat.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smackchat.R
import com.example.smackchat.model.Message
import com.example.smackchat.services.UserDataService
import kotlinx.android.synthetic.main.content_main.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class messageAdapter(val context: Context, val messages: ArrayList<Message>): RecyclerView.Adapter<messageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: messageAdapter.ViewHolder, position: Int) {
        holder?. bindMessage(context, messages[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView?.findViewById<ImageView>(R.id.messageuserimage)
        val timeStamp = itemView?.findViewById<TextView>(R.id.timestamplbl)
        val userName = itemView?.findViewById<TextView>(R.id.messageusernamelbl)
        val messageBody = itemView?.findViewById<TextView>(R.id.messagebodylbl)

        fun bindMessage(context: Context, message: Message){
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarcolor))
            userName?.text = message.userName
            timeStamp?.text = message.timeStamp
            messageBody?.text = message.message
        }


    }

    fun returnDateString(isoString: String): String {
        val isoFormatter = SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
        var convertedDate = Date()
        try {
            convertedDate = isoFormatter.parse(isoString)
        }
        catch (e:ParseException){
            Log.d("Parse", "cannot parse date")
        }

        val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
        return outDateString.format(convertedDate)
    }


}