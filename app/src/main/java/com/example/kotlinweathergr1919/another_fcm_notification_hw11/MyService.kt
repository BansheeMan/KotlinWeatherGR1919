package com.example.kotlinweathergr1919.another_fcm_notification_hw11

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.view_viewmodel.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

//Server key:
//AAAAMbPEPIY:APA91bH0o7_CIXoB7rcZJmTOC2Mn0wJBwlFSDrSet6JJel6g5iO_FGOPrG-iABfYfCWAWW5ip6zVk86KunOQIcxkFC04G_P3Sm1B5zcK8hLTDKcZplK5n4zD1-sK2Yd3a7t1hY-2QBJO

class MyService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {     //рассылка всем сразу
        super.onMessageReceived(message)
        if (!message.data.isNullOrEmpty()) {
            val title = message.data[KEY_TITLE]
            val message = message.data[KEY_MESSAGE]
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                push(title, message)
            }
        }
    }

    private fun push(title: String, message: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java)

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationBuilderHigh = NotificationCompat.Builder(this, CHANNEL_ID_LOW).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle(title)
            setContentText(message)
            setContentIntent(contentIntent)
            priority = NotificationManager.IMPORTANCE_HIGH
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelNameLow = "Name $CHANNEL_ID_LOW"
            val channelDescriptionLow = "Description $CHANNEL_ID_LOW"
            val channelPriorityLow = NotificationManager.IMPORTANCE_LOW
            val channelLow =
                NotificationChannel(CHANNEL_ID_LOW, channelNameLow, channelPriorityLow).apply {
                    description = channelDescriptionLow
                }
            notificationManager.createNotificationChannel(channelLow)
        }

        notificationManager.notify(NOTIFICATION_ID_LOW, notificationBuilderHigh.build())
    }

    companion object {
        private const val NOTIFICATION_ID_LOW = 1
        private const val CHANNEL_ID_LOW = "channel_id_1"

        private const val KEY_TITLE = "myTitle"
        private const val KEY_MESSAGE = "myMessage"
    }
}