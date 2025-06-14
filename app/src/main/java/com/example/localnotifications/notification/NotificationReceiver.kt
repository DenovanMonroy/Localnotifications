package com.example.localnotifications.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_TITLE = "title"
        const val EXTRA_CONTENT = "content"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Notificación"
        val content = intent.getStringExtra(EXTRA_CONTENT) ?: "Tienes una nueva notificación"

        val notificationHelper = NotificationHelper(context)
        notificationHelper.sendNotification(notificationId, title, content)
    }
}