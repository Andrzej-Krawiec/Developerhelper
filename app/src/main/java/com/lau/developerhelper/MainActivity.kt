package com.lau.developerhelper

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
import android.provider.Settings.ACTION_DEVICE_INFO_SETTINGS
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import androidx.core.app.NotificationManagerCompat

class MainActivity : Activity() {

  private val context = this

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    if (SDK_INT >= O) {
      createNotificationChannel()
    }
    showNotification()
    finish()
  }

  @RequiresApi(O) private fun createNotificationChannel() =
    getNotificationManager()
      .createChannel()

  @RequiresApi(O) private fun NotificationManager.createChannel() =
    createNotificationChannel(getChannel())

  @RequiresApi(O) private fun getChannel() =
    NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)

  private fun showNotification() =
    NotificationCompat.Builder(context, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setPriority(PRIORITY_DEFAULT)
      .addAboutPhoneAction()
      .addDeveloperOptionsAction()
      .build()
      .let { NotificationManagerCompat.from(context).notify(1, it) }

  private fun getNotificationManager() =
    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

  private fun NotificationCompat.Builder.addAboutPhoneAction() =
    createAction(ABOUT_PHONE, ACTION_DEVICE_INFO_SETTINGS)

  private fun NotificationCompat.Builder.addDeveloperOptionsAction() =
    createAction(DEVELOPER_OPTIONS, ACTION_APPLICATION_DEVELOPMENT_SETTINGS)

  private fun NotificationCompat.Builder.createAction(text: String, settingsName: String) =
    addAction(R.drawable.ic_launcher_background, text, createPendingIntent(
      Intent(settingsName).addFlags(FLAG_ACTIVITY_NEW_TASK)
    ))

  private fun createPendingIntent(intent: Intent) =
    PendingIntent.getActivity(context, 0, intent, 0)

  companion object {
    private const val CHANNEL_ID = "123"
    private const val CHANNEL_NAME = "DUPA"
    private const val ABOUT_PHONE = "ABOUT PHONE"
    private const val DEVELOPER_OPTIONS = "DEVELOPER OPTIONS"
  }
}