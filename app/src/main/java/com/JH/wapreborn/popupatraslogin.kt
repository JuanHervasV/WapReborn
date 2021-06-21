package com.JH.wapreborn

import android.app.Activity
import android.app.ActivityManager
import android.app.Instrumentation
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import androidx.annotation.Nullable

open class popupatraslogin : Activity() {

    @Override
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popupatraslogin)
        val popUp: popupatraslogin = this

        val dm = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout(950, 610)
        onTouch()
    }


    fun onTouch() {
        val ButtonSi = findViewById<Button>(R.id.btnsi)
        val ButtonNo = findViewById<Button>(R.id.btnno)
        ButtonSi.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
                //v.setBackgroundColor(Color.parseColor("#9C9C9C"));
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttons)
                //v.setBackgroundColor(Color.parseColor("#FF7177"));
            }
            false
        })
        ButtonNo.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
                //v.setBackgroundColor(Color.parseColor("#9C9C9C"));
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttons)
                //v.setBackgroundColor(Color.parseColor("#FF7177"));
            }
            false
        })
    }

    open fun getInstance(): popupatraslogin {
        val popUp: popupatraslogin = this
        return popUp
    }

    fun onClick(view: View) {
        when (view.getId()) {
            R.id.btnsi ->
                retroceder()
            R.id.btnno ->
                finish()
        }
    }
    fun retroceder(){

        var preferences: SharedPreferences?= PreferenceManager.getDefaultSharedPreferences(
            applicationContext
        )
        preferences!!.edit().clear().commit()
        var i: Intent?= Intent(applicationContext, Login::class.java)
        startActivity(i)
        finish()
    }
}