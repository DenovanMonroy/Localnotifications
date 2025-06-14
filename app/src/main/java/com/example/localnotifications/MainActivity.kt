package com.example.localnotifications

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import com.example.localnotifications.notification.NotificationHelper
import com.example.localnotifications.notification.NotificationScheduler
import com.example.localnotifications.ui.theme.LocalNotificationsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        // Manejar resultado del permiso
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNotificationsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotificationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationScreen() {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    val notificationScheduler = remember { NotificationScheduler(context) }

    // Estado para los campos de entrada
    var notificationTitle by remember { mutableStateOf("Título de prueba") }
    var notificationContent by remember { mutableStateOf("Este es el contenido de la notificación") }
    var delayMinutes by remember { mutableStateOf("5") }

    // Manejo de permisos
    val notificationPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Notificaciones Push Locales",
            style = MaterialTheme.typography.headlineMedium
        )

        // Verificación de permisos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionState?.let { permissionState ->
                when (permissionState.status) {
                    is PermissionStatus.Granted -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                text = "✓ Permisos de notificación concedidos",
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    is PermissionStatus.Denied -> {
                        val isDeniedPermanently = (permissionState.status as PermissionStatus.Denied).shouldShowRationale.not()

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDeniedPermanently)
                                    MaterialTheme.colorScheme.errorContainer
                                else
                                    MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isDeniedPermanently)
                                        "⚠️ Permisos denegados permanentemente"
                                    else
                                        "📱 Necesitamos permisos de notificación",
                                    color = if (isDeniedPermanently)
                                        MaterialTheme.colorScheme.onErrorContainer
                                    else
                                        MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                if (isDeniedPermanently) {
                                    Button(
                                        onClick = {
                                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            intent.data = Uri.parse("package:${context.packageName}")
                                            context.startActivity(intent)
                                        }
                                    ) {
                                        Text("Abrir configuración")
                                    }
                                } else {
                                    Button(
                                        onClick = { permissionState.launchPermissionRequest() }
                                    ) {
                                        Text("Conceder permisos")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Para versiones anteriores a Android 13, no se requiere permiso explícito
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "✓ Notificaciones disponibles (Android < 13)",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Verificar si las notificaciones están habilitadas en el sistema
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚠️ Las notificaciones están deshabilitadas en configuración",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            intent.data = Uri.parse("package:${context.packageName}")
                            context.startActivity(intent)
                        }
                    ) {
                        Text("Abrir configuración")
                    }
                }
            }
        }

        HorizontalDivider()

        // Campos de entrada
        OutlinedTextField(
            value = notificationTitle,
            onValueChange = { notificationTitle = it },
            label = { Text("Título de la notificación") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notificationContent,
            onValueChange = { notificationContent = it },
            label = { Text("Contenido de la notificación") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = delayMinutes,
            onValueChange = { delayMinutes = it },
            label = { Text("Retraso en minutos") },
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        // Función auxiliar para verificar si se pueden enviar notificaciones
        val canSendNotifications = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionState?.status is PermissionStatus.Granted
        } else {
            true
        } && NotificationManagerCompat.from(context).areNotificationsEnabled()

        // Botones de acción
        Button(
            onClick = {
                val id = Random.nextInt()
                notificationHelper.sendNotification(
                    id = id,
                    title = notificationTitle,
                    content = notificationContent
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canSendNotifications
        ) {
            Text("Enviar notificación inmediata")
        }

        Button(
            onClick = {
                val minutes = delayMinutes.toIntOrNull() ?: 5
                val id = Random.nextInt()
                notificationScheduler.scheduleNotificationInMinutes(
                    id = id,
                    title = notificationTitle,
                    content = notificationContent,
                    minutes = minutes
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canSendNotifications
        ) {
            Text("Programar notificación")
        }

        // Botones de ejemplo
        OutlinedButton(
            onClick = {
                val id = Random.nextInt()
                notificationHelper.sendNotification(
                    id = id,
                    title = "¡Hola!",
                    content = "Esta es una notificación de prueba con un mensaje más largo para ver cómo se comporta el sistema de notificaciones."
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canSendNotifications
        ) {
            Text("Notificación de ejemplo")
        }

        OutlinedButton(
            onClick = {
                val id = Random.nextInt()
                notificationScheduler.scheduleNotificationInMinutes(
                    id = id,
                    title = "Recordatorio",
                    content = "No olvides revisar la aplicación",
                    minutes = 1
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = canSendNotifications
        ) {
            Text("Recordatorio en 1 minuto")
        }

        // Información adicional
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💡 Información",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Las notificaciones inmediatas aparecen al instante\n" +
                            "• Las notificaciones programadas aparecerán después del tiempo especificado\n" +
                            "• Las notificaciones funcionan aunque la app esté cerrada\n" +
                            "• En Android 13+ necesitas conceder permisos de notificación",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}