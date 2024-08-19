package mael.hoon1222.alarm_receiver

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannel()

        //메세지로 넘어온 type을 eum class인 NotificationType에 매칭되는 값을 찾아서 변수 type에 할당한다.
        //let을 써서 null safty하게 선언해준다.
        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]

        type ?: return

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            channel.description = CHANNEL_DESCRIPTION

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, type.id, intent, FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_circle_notifications_24)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
                notificationBuilder.setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(
                            "\uD83D\uDE01\uD83D\uDE01\uD83D\uDE01\uD83D\uDE01\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE0A\uD83D\uDE0A\uD83E\uDD23\uD83E\uDD23\uD83D\uDE18\uD83D\uDE18\uD83D\uDE18\uD83D\uDC4C\uD83D\uDC4C\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6D\uD83C\uDF6D\uD83C\uDF7E\uD83C\uDF7E\uD83E\uDDC9\uD83E\uDDD1\uD83E\uDDD1\uD83D\uDE91\uD83D\uDE91\uD83D\uDE91\uD83D\uDE91\uD83D\uDE8E\uD83D\uDE90\uD83D\uDE90\uD83E\uDDE1\uD83E\uDDE1\uD83E\uDD0E\uD83D\uDEB4\u200D♂️\uD83D\uDEB4\u200D♂️\uD83D\uDEB4\u200D♀️\uD83E\uDD38\u200D♂️\uD83E\uDDB6\uD83D\uDEB4\u200D♀️⛹️\u200D♂️\uD83D\uDE01\uD83D\uDE01\uD83D\uDE01\uD83D\uDE01\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE0A\uD83D\uDE0A\uD83E\uDD23\uD83E\uDD23\uD83D\uDE18\uD83D\uDE18\uD83D\uDE18\uD83D\uDC4C\uD83D\uDC4C\uD83C\uDF6A\uD83C\uDF6A\uD83C\uDF6D\uD83C\uDF6D\uD83C\uDF7E\uD83C\uDF7E\uD83E\uDDC9\uD83E\uDDD1\uD83E\uDDD1\uD83D\uDE91\uD83D\uDE91\uD83D\uDE91\uD83D\uDE91\uD83D\uDE8E\uD83D\uDE90\uD83D\uDE90\uD83E\uDDE1\uD83E\uDDE1\uD83E\uDD0E\uD83D\uDEB4\u200D♂️\uD83D\uDEB4\u200D♂️\uD83D\uDEB4\u200D♀️\uD83E\uDD38\u200D♂️\uD83E\uDDB6\uD83D\uDEB4\u200D♀️⛹️\u200D♂️"
                        )
                )
            }

            NotificationType.CUSTOM -> {
                notificationBuilder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(
                            packageName,
                            R.layout.view_custom_notification
                        ).apply {
                            setTextViewText(R.id.title, title)
                            setTextViewText(R.id.message, message)
                        }
                    )
            }
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Emoji Party"
        private const val CHANNEL_DESCRIPTION = "Emoji Party를 위한 채널"
        private const val CHANNEL_ID = "Channel ID"
    }
}