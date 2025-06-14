
# 📱 Notificaciones Push Locales - Android

Una aplicación móvil desarrollada en **Android Studio** con **Kotlin** y **Jetpack Compose** que permite enviar y programar notificaciones push locales de manera sencilla y eficiente.

## 🚀 Características

- ✅ **Notificaciones inmediatas**: Envía notificaciones al instante
- ⏰ **Notificaciones programadas**: Programa notificaciones para un momento específico
- 🎨 **Interfaz moderna**: Desarrollada con Jetpack Compose y Material Design 3
- 🔒 **Manejo de permisos**: Gestión automática de permisos para Android 13+
- 📱 **Compatibilidad**: Compatible con Android API 24+ (Android 7.0)
- 🔄 **Persistencia**: Las notificaciones programadas sobreviven al reinicio del dispositivo
- 🎯 **Personalización**: Títulos y contenidos personalizables

## 📋 Requisitos

- **Android Studio**: Arctic Fox o superior
- **Kotlin**: 1.8+
- **Gradle**: 8.0+
- **Android API**: Mínimo 24 (Android 7.0)
- **Target API**: 34 (Android 14)

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **UI Framework**: Jetpack Compose
- **Arquitectura**: MVVM
- **Gestión de permisos**: Accompanist Permissions
- **Notificaciones**: NotificationManagerCompat
- **Programación de tareas**: AlarmManager
- **Material Design**: Material 3

## Capturas
### Aplicacion
![image](https://github.com/user-attachments/assets/89332ce6-1cd5-4691-93b0-28b096cbb017)
### Validacion 
![image](https://github.com/user-attachments/assets/c5ae607c-dc1a-403b-953f-bd41dd329385)

![image](https://github.com/user-attachments/assets/5c934722-21a2-4c9f-895b-0919259cc6d4)



## 📦 Dependencias Principales

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:2024.06.00"))

// Core Android
implementation("androidx.core:core-ktx:1.13.1")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
implementation("androidx.activity:activity-compose:1.9.0")

// Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")

// Permissions
implementation("com.google.accompanist:accompanist-permissions:0.34.0")

// Work Manager
implementation("androidx.work:work-runtime-ktx:2.9.0")
```

## 🚀 Instalación

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/DenovanMonroy/notificaciones-push-locales.git
   cd notificaciones-push-locales
   ```

2. **Abre el proyecto en Android Studio**:
   - Abre Android Studio
   - Selecciona "Open an existing project"
   - Navega hasta la carpeta del proyecto y ábrela

3. **Sincroniza las dependencias**:
   - Android Studio automáticamente sincronizará las dependencias
   - Si no, haz clic en "Sync Now" cuando aparezca la notificación

4. **Ejecuta la aplicación**:
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en el botón "Run" o presiona `Shift + F10`

## 📱 Uso de la Aplicación

### Permisos
La aplicación solicitará automáticamente los permisos necesarios:
- **Android 13+**: Permiso de notificaciones (`POST_NOTIFICATIONS`)
- **Todas las versiones**: Verificación de notificaciones habilitadas en configuración del sistema

### Funcionalidades

1. **Notificación Inmediata**:
   - Escribe el título y contenido
   - Presiona "Enviar notificación inmediata"
   - La notificación aparecerá al instante

2. **Notificación Programada**:
   - Configura el título, contenido y tiempo de retraso (en minutos)
   - Presiona "Programar notificación"
   - La notificación aparecerá después del tiempo especificado

3. **Ejemplos Predefinidos**:
   - "Notificación de ejemplo": Envía una notificación de prueba
   - "Recordatorio en 1 minuto": Programa un recordatorio para dentro de 1 minuto

## 🏗️ Estructura del Proyecto

```
app/src/main/java/com/example/localnotifications/
├── MainActivity.kt                 # Actividad principal con UI
├── notification/
│   ├── NotificationHelper.kt       # Gestión de notificaciones
│   ├── NotificationScheduler.kt    # Programación de notificaciones
│   ├── NotificationReceiver.kt     # Receptor de notificaciones programadas
│   └── BootReceiver.kt            # Receptor para reinicio del sistema
└── ui/theme/
    ├── Theme.kt                   # Tema de la aplicación
    └── Color.kt                   # Colores personalizados
```

## 🔧 Configuración

### AndroidManifest.xml
Los siguientes permisos están configurados automáticamente:

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM" />
```

### Canales de Notificación
La aplicación crea automáticamente un canal de notificación con:
- **ID**: `local_notifications_channel`
- **Nombre**: "Notificaciones Locales"
- **Importancia**: Normal
- **Características**: Vibración y luces habilitadas

## 🎨 Personalización

### Modificar el Estilo de Notificaciones
En `NotificationHelper.kt`, puedes personalizar:

```kotlin
val notification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_notification)  // Cambiar icono
    .setContentTitle(title)
    .setContentText(content)
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)  // Cambiar prioridad
    .setVibrate(longArrayOf(0, 500, 200, 500))        // Cambiar patrón de vibración
    .build()
```

### Modificar el Tema
En `ui/theme/Color.kt`, puedes cambiar los colores:

```kotlin
val Purple80 = Color(0xFFD0BCFF)  // Color primario oscuro
val Purple40 = Color(0xFF6650a4)  // Color primario claro
```

## 🧪 Testing

### Pruebas Manuales
1. **Notificaciones Inmediatas**:
   - Verifica que aparezcan al instante
   - Confirma que el contenido sea correcto
   - Prueba con diferentes longitudes de texto

2. **Notificaciones Programadas**:
   - Programa una notificación para 1 minuto
   - Cierra la aplicación
   - Verifica que la notificación aparezca

3. **Permisos**:
   - Prueba en Android 13+ para verificar solicitud de permisos
   - Prueba denegando permisos y verificando el manejo de errores

## 🚀 Próximas Características

- [ ] Notificaciones recurrentes (diarias, semanales)
- [ ] Sonidos personalizados para notificaciones
- [ ] Imágenes en notificaciones
- [ ] Acciones rápidas en notificaciones
- [ ] Base de datos para historial de notificaciones
- [ ] Widgets para programar notificaciones rápidas
- [ ] Temas personalizables
- [ ] Exportar/importar configuraciones

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Haz fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Denovan Monroy**
- GitHub: [@DenovanMonroy](https://github.com/DenovanMonroy)



