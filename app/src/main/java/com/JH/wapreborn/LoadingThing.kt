package com.JH.wapreborn

import android.app.Activity
import android.app.AlertDialog


class LoadingThing(private val activity: Activity) {

    var dialog: AlertDialog? = null
    fun startLoadingAnimation() {

        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_thing, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }
}