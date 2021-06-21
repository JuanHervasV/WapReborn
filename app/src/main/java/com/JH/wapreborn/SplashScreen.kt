package com.JH.wapreborn

import android.Manifest.permission.ACCESS_NOTIFICATION_POLICY
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val tiempo: Long = 3000

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        //balloon.setAnimation(topAnimantion);
        //nombreapp.setAnimation(bottomAnimation);
        FirebaseMessaging.getInstance().subscribeToTopic("WAP")
        //Splash Screen Code to call new Activity after some time
         Handler(Looper.getMainLooper()).postDelayed({ //Calling new Activity
            val intent = Intent(this@SplashScreen, Login::class.java)
            startActivity(intent)
            finish()
        }, tiempo)

    }


}