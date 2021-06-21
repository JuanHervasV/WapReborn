package com.JH.wapreborn

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.JH.wapreborn.Model.InsertarToken
import com.JH.wapreborn.Model.Vars
import com.JH.wapreborn.io.APIRetrofitInterface
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    lateinit var recuerdame: CheckBox

    private var jsonPlaceHolderApi: APIRetrofitInterface? = null

    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Log.i(TAG,"Mi token es: " + refreshedToken);
        ///
        val LoginText = findViewById<EditText>(R.id.nombreusuario)
        val PasswordText = findViewById<EditText>(R.id.claveusuario)
        //TestApi = findViewById(R.id.TestApi);
        val retrofit = Retrofit.Builder()
            .baseUrl("http://200.37.50.53/ApiRuteando/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jsonPlaceHolderApi = retrofit.create(APIRetrofitInterface::class.java)

        FirebaseMessaging.getInstance().subscribeToTopic("WAP")
        //--
        //--
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.iconwap)
                .setContentTitle("Title")
                .setContentText("Body")
                .setAutoCancel(true)
                .setOnlyAlertOnce(true) as NotificationCompat.Builder

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationBuilder.priority = Notification.PRIORITY_HIGH
        notificationBuilder.setVibrate(LongArray(0))
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())

        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
        val check = preferences.getInt("check", 0)

        if (check == 1 ) {
            val i = Intent(this@Login, ListadoEmp::class.java)
            startActivity(i)
            finish()
        } else {
//H
        }

        onTouch()

    }

    fun onClick(view: View) {
        val loadingThing = LoadingThing(this@Login)

        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (view.getId()) {
            R.id.btnlogin ->                 //loadingThing.startLoadingAnimation();
                createPost()
        }
    }

    fun onTouch() {
        val Login_b = findViewById<Button>(R.id.btnlogin)
        Login_b.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttons)
                //v.setBackgroundColor(Color.parseColor("@drawable/rounded_corners"));
            }
            false
        })
    }

    private fun createPost() {

        val LoginText = findViewById<EditText>(R.id.nombreusuario)

        val PasswordText = findViewById<EditText>(R.id.claveusuario)
        val loadingThing = LoadingThing(this@Login)
        loadingThing.startLoadingAnimation()
        val usuario: String = LoginText.text.toString()
        val password: String = PasswordText.text.toString()
        //Aqui enviar los datos
        //String resul = mTvResult.getText().toString();
        val vars = Vars(usuario, password)
        val call: Call<Vars> = jsonPlaceHolderApi!!.login(vars)
        call.enqueue(object : Callback<Vars?> {
            override fun onResponse(call: Call<Vars?>, response: Response<Vars?>) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                            applicationContext,
                            "Usuario/Contraseña incorrecta.",
                            Toast.LENGTH_SHORT
                    ).show()
                    loadingThing.dismissDialog()
                    return
                }
                val postsResponse: Vars? = response.body()
                val idusu: Int = postsResponse?.UsuId() ?: 0
                val nombre: String = postsResponse?.Nombre() ?: "null"
                val apellido: String = postsResponse?.Apep() ?: "null"

                // Get updated InstanceID token.
                val refreshedToken: String? = FirebaseInstanceId.getInstance().getToken()
                val insertarToken = InsertarToken(idusu, refreshedToken)
                val callo: Call<InsertarToken> = jsonPlaceHolderApi!!.insertarToken(insertarToken)
                callo.enqueue(object : Callback<InsertarToken?> {
                    override fun onResponse(
                            call: Call<InsertarToken?>,
                            response: Response<InsertarToken?>
                    ) {
                        if (!response.isSuccessful()) {
                            //mJsonTxtView.setText("Codigo:" + response.code());
                            Toast.makeText(
                                    applicationContext,
                                    "Usuario/Contraseña incorrecta.",
                                    Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        val postsResponse: InsertarToken? = response.body()

                        //Toast.makeText(Login.this, "Bienvenido "+nombre+" "+apellido, Toast.LENGTH_SHORT).show();
                        //Guardar Login SharedPreferences
                    }

                    override fun onFailure(call: Call<InsertarToken?>, t: Throwable) {
                        Toast.makeText(
                                applicationContext,
                                "No tiene conexión a internet.",
                                Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                })

                //Toast.makeText(Login.this, "Bienvenido "+nombre+" "+apellido, Toast.LENGTH_SHORT).show();
                //Guardar Login SharedPreferences
                recuerdame = findViewById(R.id.recuerdame)
                if (recuerdame.isChecked) {
                    var check = 1

                    val sharedPref = getPreferences(MODE_PRIVATE)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
                    val editor = preferences.edit()
                    editor.putString("dniusuario", usuario);
                    editor.putInt("id", idusu)
                    editor.putInt("check", check)
                    editor.putString("nombreusuario", nombre)
                    editor.putString("apellidousuario", apellido)
                    editor.commit()
                    loadingThing.dismissDialog()
                    val i = Intent(this@Login, ListadoEmp::class.java)
                    startActivity(i)
                    finish()

                } else {
                    val sharedPref = getPreferences(MODE_PRIVATE)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@Login)
                    val editor = preferences.edit()
                    editor.putString("dniusuario", usuario);
                    editor.putInt("id", idusu)
                    editor.putString("nombreusuario", nombre)
                    editor.putString("apellidousuario", apellido)
                    editor.commit()
                    loadingThing.dismissDialog()
                    val i = Intent(this@Login, ListadoEmp::class.java)
                    startActivity(i)
                    finish()
                }

            }

            override fun onFailure(call: Call<Vars?>, t: Throwable) {
                Toast.makeText(
                        applicationContext,
                        "No tiene conexión a internet.",
                        Toast.LENGTH_SHORT
                ).show()
                loadingThing.dismissDialog()
                //mJsonTxtView.setText(t.getMessage());
                return
            }
        })
    }

}