package com.example.localnotifications.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Aquí puedes reprogramar las notificaciones que tenías pendientes
            // Por ejemplo, leer de SharedPreferences las notificaciones programadas
            // y volver a programarlas usando NotificationScheduler
        }
    }
}