package com.example.newsapp.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color.WHITE
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.example.gaposanews.data.NewsApi
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.const.Constants
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class FcmService : FirebaseMessagingService() {

    val tag = "FcmService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("Send from:${remoteMessage.from}")
        if (remoteMessage.notification != null) {
            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }

    override fun onNewToken(token01: String) {
        val token = FirebaseInstanceId.getInstance().token
        Timber.d("This device Token: $token")
        sendRegistrationToServer(token!!)
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gplogo)
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.gnlogo)
            .setLargeIcon(bitmap)
            .setColor(WHITE)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun sendRegistrationToServer(token: String) {

        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun provideApi(): NewsApi {
            return provideRetrofit().create(NewsApi::class.java)
        }

        provideApi().sendTokenToUrl(token).enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Timber.d("Sent token to URL")
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Timber.e("Unable to send device token to URL")
            }
        })

    }
}
