package com.google.developers.lettervault.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.preference.PreferenceManager
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.developers.lettervault.R
import com.google.developers.lettervault.ui.detail.LetterDetailActivity
import com.google.developers.lettervault.util.LETTER_ID
import com.google.developers.lettervault.util.NOTIFICATION_CHANNEL_ID
import com.google.developers.lettervault.util.NOTIFICATION_ID
import com.google.developers.lettervault.util.NOTIFICATION_NAME

/**
 * Run a work to show a notification on a background thread by the {@link WorkManger}.
 */
class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val letterId: Long = inputData.getLong(LETTER_ID, 0)

    /**
     * Create an intent with extended data to the letter.
     */
    private fun getContentIntent(): PendingIntent? {
        val intent = Intent(applicationContext, LetterDetailActivity::class.java).apply {
            putExtra(LETTER_ID, letterId)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            return@run getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }

    override fun doWork(): Result {
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val shouldNotify = prefManager.getBoolean(
            applicationContext.getString(R.string.pref_key_notify),
            false
        )

        if (shouldNotify) showNotify()

        return Result.success()
    }

    private fun showNotify() {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mail)
            .setContentTitle("Open a Letter")
            .setContentText("Vault is ready to open secret letter")
            .setColor(ContextCompat.getColor(applicationContext, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(getContentIntent())
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
