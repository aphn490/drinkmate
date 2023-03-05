package com.example.drinkmate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//MyFirebaseMessagingService Class to create an implementation of the FirebaseMessagingService to enable popup notifications for messages
//Set up a Firebase Cloud Messaging receiver to handle incoming messages
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //Get the message content from the data payload
        val messageContent = remoteMessage.data["message"]

        //Create a notification channel and set id and name
        val channel = NotificationChannel(
            "my_channel_id",
            "My Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        //Configure the channel's description
        channel.description = "My Channel Description"

        //Set the channel with the system
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        //Display the notification
        if (messageContent != null) {
            showNotification(messageContent)
        }
    }

    private fun showNotification(messageContent: String) {
        //Create a notification with the message
        val notification = NotificationCompat.Builder(this, "my_channel_id")
            .setContentTitle("New Message")
            .setContentText(messageContent)
            .setSmallIcon(R.drawable.ic_baseline_local_bar_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        //Display the notification to user device
        NotificationManagerCompat.from(this).notify(0, notification)
    }
}