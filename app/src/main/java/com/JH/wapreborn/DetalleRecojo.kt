package com.JH.wapreborn

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.JH.wapreborn.Model.DetalleRecojo
import com.JH.wapreborn.Model.RegistrarEstado
import com.JH.wapreborn.io.APIRetrofitInterface
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetalleRecojo : AppCompatActivity() {

    private var jsonPlaceHolderApi: APIRetrofitInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_recojo)
        FirebaseMessaging.getInstance().subscribeToTopic("WAP")
        val retrofit = Retrofit.Builder()
                .baseUrl("http://200.37.50.53/ApiWap/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        jsonPlaceHolderApi = retrofit.create(APIRetrofitInterface::class.java)

        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val CodigoRecojo = preferences.getString("CodigoRecojo", "CodigoRecojo")
        val CodigoRecojoDetalle = preferences.getString(
            "CodigoRecojoDetalle",
            "CodigoRecojoDetalle"
        )


        if(CodigoRecojo == "CodigoRecojo" && CodigoRecojoDetalle == "CodigoRecojoDetalle"){
            Toast.makeText(applicationContext, "No hay data", Toast.LENGTH_SHORT).show()
        }
        else if(CodigoRecojo !== "CodigoRecojo"){
            CargarInformacion()
        }
        else {

        }
    }

    fun onClick(view: View) {

        when (view.getId()) {
            R.id.btnRecogido ->
                funRecogido()
            R.id.btnPendiente ->
                funPendiente()
            R.id.btnFuturo ->
                funFuturo()
            R.id.btnAnulado ->
                funAnulado()
            R.id.imgv ->
                retroceder()
        }
    }

    fun onTouch() {
        val Recogido_b = findViewById<Button>(R.id.btnRecogido)
        Recogido_b.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttons)
                //v.setBackgroundColor(Color.parseColor("@drawable/rounded_corners"));
            }
            false
        })
        val Pendiente_b = findViewById<Button>(R.id.btnPendiente)
        Pendiente_b.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttonsceleste)
                //v.setBackgroundColor(Color.parseColor("@drawable/rounded_corners"));
            }
            false
        })
        val Futuro_b = findViewById<Button>(R.id.btnFuturo)
        Futuro_b.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttonspurple)
                //v.setBackgroundColor(Color.parseColor("@drawable/rounded_corners"));
            }
            false
        })
        val Anulado_b = findViewById<Button>(R.id.btnAnulado)
        Anulado_b.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsblack)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.buttonsgray)
                //v.setBackgroundColor(Color.parseColor("@drawable/rounded_corners"));
            }
            false
        })
    }

    fun CargarInformacion(){
        val titulodetalle = findViewById<TextView>(R.id.titulodetalle);
        val txtCodigoRecojo = findViewById<TextView>(R.id.CodigoRecojo)
        val txtRazonSocial = findViewById<TextView>(R.id.RazonSocial)
        val txtContacto = findViewById<TextView>(R.id.Contacto)
        val txtDireccion = findViewById<TextView>(R.id.Direccion)
        val txtCodigoPostal = findViewById<TextView>(R.id.CodigoPostal)
        val txtDescripcion = findViewById<TextView>(R.id.Descripcion)
        val txtEmbalaje = findViewById<TextView>(R.id.Embalaje)
        val txtPeso = findViewById<TextView>(R.id.Peso)
        val txtDestino = findViewById<TextView>(R.id.Destino)
        val txtPago1 = findViewById<TextView>(R.id.Pago1)
        val txtPago2 = findViewById<TextView>(R.id.Pago2)
        val txtObservacion = findViewById<TextView>(R.id.Observacion)

        //Llamar datos ----------------------------------------------------------
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val CodigoRecojo = preferences.getString("CodigoRecojo", "CodigoRecojo")
        //----------------------------------------------------------------------
        val loadingThing = LoadingThing(this@DetalleRecojo)
        loadingThing.startLoadingAnimation()

        val detalleRecojo = DetalleRecojo("" + CodigoRecojo)
        val callo: Call<DetalleRecojo> = jsonPlaceHolderApi!!.detalleRecojo(detalleRecojo)
        callo.enqueue(object : Callback<DetalleRecojo?> {
            override fun onResponse(
                call: Call<DetalleRecojo?>,
                response: Response<DetalleRecojo?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingThing.dismissDialog()
                    return
                }
                val postsResponse: DetalleRecojo? = response.body()

                val CodigoRecojoDetalle: String = postsResponse?.CodigoRecojo() ?: "null"
                val RazonSocial: String = postsResponse?.RazonSocial() ?: "null"
                val Contacto: String = postsResponse?.Contacto() ?: "null"
                val Direccion: String = postsResponse?.Direccion() ?: "null"
                val CodigoPostal: String = postsResponse?.CodigoPostal() ?: "null"
                val Embalaje: String = postsResponse?.Embalaje() ?: "null"
                val Peso: String = postsResponse?.Peso() ?: "null"
                val Destino: String = postsResponse?.Destino() ?: "null"
                val Descripcion: String = postsResponse?.Descripcion() ?: "null"
                val HoraIni: String = postsResponse?.HoraIni() ?: "null"
                val HoraFin: String = postsResponse?.CodigoPostal() ?: "null"
                val PagoRecojo1: String = postsResponse?.PagoRecojo1() ?: "null"
                val PagoRecojo2: String = postsResponse?.PagoRecojo2() ?: "null"
                val Observacion: String = postsResponse?.Observacion() ?: "null"

                txtCodigoRecojo.text = "" + CodigoRecojo
                txtRazonSocial.text = "" + RazonSocial
                txtContacto.text = "" + Contacto
                txtDireccion.text = "" + Direccion
                txtCodigoPostal.text = "" + CodigoPostal
                txtDescripcion.text = "" + Descripcion
                txtEmbalaje.text = "" + Embalaje
                txtPeso.text = "" + Peso
                txtDestino.text = "" + Destino
                txtPago1.text = "" + PagoRecojo1
                txtPago2.text = "" + PagoRecojo2
                txtObservacion.text = "" + Observacion

                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val editor = preferences.edit()
                editor.putString("CodigoRecojoDetalle", "" + CodigoRecojoDetalle);
                editor.putString("RazonSocial" + CodigoRecojoDetalle, "" + RazonSocial)
                editor.putString("Contacto" + CodigoRecojoDetalle, "" + Contacto)
                editor.putString("Direccion" + CodigoRecojoDetalle, "" + Direccion)
                editor.putString("CodigoPostal" + CodigoRecojoDetalle, "" + CodigoPostal)
                editor.putString("Peso" + CodigoRecojoDetalle, "" + Peso)
                editor.putString("Destino" + CodigoRecojoDetalle, "" + Destino)
                editor.putString("Descripcion" + CodigoRecojoDetalle, "" + Descripcion)
                editor.putString("HoraIni" + CodigoRecojoDetalle, "" + HoraIni)
                editor.putString("HoraFin" + CodigoRecojoDetalle, "" + HoraFin)
                editor.putString("PagoRecojo1" + CodigoRecojoDetalle, "" + PagoRecojo1)
                editor.putString("PagoRecojo2" + CodigoRecojoDetalle, "" + PagoRecojo2)
                editor.putString("Observacion" + CodigoRecojoDetalle, "" + Observacion)
                editor.commit()
                loadingThing.dismissDialog()
                return
            }

            override fun onFailure(call: Call<DetalleRecojo?>, t: Throwable) {

                val txtCodigoRecojo = findViewById<TextView>(R.id.CodigoRecojo)
                val txtRazonSocial = findViewById<TextView>(R.id.RazonSocial)
                val txtContacto = findViewById<TextView>(R.id.Contacto)
                val txtDireccion = findViewById<TextView>(R.id.Direccion)
                val txtCodigoPostal = findViewById<TextView>(R.id.CodigoPostal)
                val txtDescripcion = findViewById<TextView>(R.id.Descripcion)
                val txtEmbalaje = findViewById<TextView>(R.id.Embalaje)
                val txtPeso = findViewById<TextView>(R.id.Peso)
                val txtDestino = findViewById<TextView>(R.id.Destino)
                val txtPago1 = findViewById<TextView>(R.id.Pago1)
                val txtPago2 = findViewById<TextView>(R.id.Pago2)
                val txtObservacion = findViewById<TextView>(R.id.Observacion)

                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)

                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")

                val RazonSocial = preferences.getString("RazonSocial" + coderecoj, "RazonSocial")
                val Contacto = preferences.getString("Contacto" + coderecoj, "Contacto")
                val Direccion = preferences.getString("Direccion" + coderecoj, "Direccion")
                val CodigoPostal = preferences.getString("CodigoPostal" + coderecoj, "CodigoPostal")
                val Descripcion = preferences.getString("Descripcion" + coderecoj, "Descripcion")
                val Embalaje = preferences.getString("Embalaje" + coderecoj, "Embalaje")
                val Peso = preferences.getString("Peso" + coderecoj, "Peso")
                val Destino = preferences.getString("Destino" + coderecoj, "Destino")
                val PagoRecojo1 = preferences.getString("PagoRecojo1" + coderecoj, "PagoRecojo1")
                val PagoRecojo2 = preferences.getString("PagoRecojo2" + coderecoj, "PagoRecojo2")
                val Observacion = preferences.getString("Observacion" + coderecoj, "Observacion")

                txtCodigoRecojo.text = "" + coderecoj
                txtRazonSocial.text = "" + RazonSocial
                txtContacto.text = "" + Contacto
                txtDireccion.text = "" + Direccion
                txtCodigoPostal.text = "" + CodigoPostal
                txtDescripcion.text = "" + Descripcion
                txtEmbalaje.text = "" + Embalaje
                txtPeso.text = "" + Peso
                txtDestino.text = "" + Destino
                txtPago1.text = "" + PagoRecojo1
                txtPago2.text = "" + PagoRecojo2
                txtObservacion.text = "" + Observacion

                Toast.makeText(applicationContext, "No hay conexión a internet", Toast.LENGTH_SHORT)
                    .show()
                loadingThing.dismissDialog()
                return
            }
        })
    }

    fun funRecogido(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val idrecoj = preferences.getString("CodigoRecojo", "CodigoRecojo")
        val idrecojo = idrecoj!!.toInt()

        val codusuario = preferences.getString("dniusuario", "nombreusuario")
        val estadorecojo = "R"

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        val fechaRecojo = ""+dateFormat.format(date)
        val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
        val horaFinal = preferences.getString("HoraFin", "HoraFin")

        val registrarEstado = RegistrarEstado(
            idrecojo,
            "" + codusuario,
            "" + estadorecojo,
            "" + fechaRecojo,
            "" + horaInicial,
            "" + horaFinal
        )
        val callo: Call<RegistrarEstado> = jsonPlaceHolderApi!!.registrarEstado(registrarEstado)
        callo.enqueue(object : Callback<RegistrarEstado?> {
            override fun onResponse(
                call: Call<RegistrarEstado?>,
                response: Response<RegistrarEstado?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val postsResponse: RegistrarEstado? = response.body()

                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val contadortmn = preferences.getInt("contadortmn", 0)

                preferences.edit().remove("RazonSocial" + coderecoj).commit();
                preferences.edit().remove("Contacto" + coderecoj).commit();
                preferences.edit().remove("Direccion" + coderecoj).commit();
                preferences.edit().remove("CodigoPostal" + coderecoj).commit();
                preferences.edit().remove("Descripcion" + coderecoj).commit();
                preferences.edit().remove("Peso" + coderecoj).commit();
                preferences.edit().remove("Destino" + coderecoj).commit();
                preferences.edit().remove("PagoRecojo1" + coderecoj).commit();
                preferences.edit().remove("PagoRecojo2" + coderecoj).commit();
                preferences.edit().remove("Observacion" + coderecoj).commit();
                preferences.edit().remove("Recojoinfo"+""+coderecoj).commit()

                Toast.makeText(
                    applicationContext,
                    "Paquete recojido exitosamente",
                    Toast.LENGTH_SHORT
                ).show();
                finish()
            }

            override fun onFailure(call: Call<RegistrarEstado?>, t: Throwable) {

                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val editor = preferences.edit()

                val codusuario = preferences.getString("dniusuario", "nombreusuario")
                val estadorecojo = "R"
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = Date()
                val fechaRecojo = "" + dateFormat.format(date)
                val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
                val horaFinal = preferences.getString("HoraFin", "HoraFin")

                editor.putString("coderecojo" + coderecoj, "" + coderecoj);
                editor.putString("codeusuar" + coderecoj, "" + codusuario)
                editor.putString("estarec" + coderecoj, "" + estadorecojo)
                editor.putString("fechaRec" + coderecoj, "" + fechaRecojo)
                editor.putString("horaI" + coderecoj, "" + horaInicial)
                editor.putString("horaF" + coderecoj, "" + horaFinal)
                editor.commit()

                val dialog = Dialog(this@DetalleRecojo)
                dialog?.setContentView(R.layout.popup)
                dialog.show()
                val handler = Handler()
                handler.postDelayed({ dialog.dismiss() }, 4000)
                handler.postDelayed({finish()}, 4001)
                return
            }
        })
    }

    fun funPendiente(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val idrecoj = preferences.getString("CodigoRecojo", "CodigoRecojo")
        val idrecojo = idrecoj!!.toInt()

        val codusuario = preferences.getString("dniusuario", "nombreusuario")
        val estadorecojo = "P"

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        val fechaRecojo = ""+dateFormat.format(date)
        val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
        val horaFinal = preferences.getString("HoraFin", "HoraFin")

        val registrarEstado = RegistrarEstado(
            idrecojo,
            "" + codusuario,
            "" + estadorecojo,
            "" + fechaRecojo,
            "" + horaInicial,
            "" + horaFinal
        )
        val callo: Call<RegistrarEstado> = jsonPlaceHolderApi!!.registrarEstado(registrarEstado)
        callo.enqueue(object : Callback<RegistrarEstado?> {
            override fun onResponse(
                call: Call<RegistrarEstado?>,
                response: Response<RegistrarEstado?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val postsResponse: RegistrarEstado? = response.body()

                Toast.makeText(
                    applicationContext,
                    "Paquete recojido exitosamente",
                    Toast.LENGTH_SHORT
                ).show();
                finish()

            }

            override fun onFailure(call: Call<RegistrarEstado?>, t: Throwable) {
                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val editor = preferences.edit()

                val codusuario = preferences.getString("dniusuario", "nombreusuario")
                val estadorecojo = "P"
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = Date()
                val fechaRecojo = "" + dateFormat.format(date)
                val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
                val horaFinal = preferences.getString("HoraFin", "HoraFin")

                editor.putString("coderecojo" + coderecoj, "" + coderecoj);
                editor.putString("codeusuar" + coderecoj, "" + codusuario)
                editor.putString("estarec" + coderecoj, "" + estadorecojo)
                editor.putString("fechaRec" + coderecoj, "" + fechaRecojo)
                editor.putString("horaI" + coderecoj, "" + horaInicial)
                editor.putString("horaF" + coderecoj, "" + horaFinal)
                editor.commit()

                val dialog = Dialog(this@DetalleRecojo)
                dialog?.setContentView(R.layout.popup)
                dialog.show()
                val handler = Handler()
                handler.postDelayed({ dialog.dismiss() }, 4000)
                handler.postDelayed({finish()}, 4001)
                return
            }
        })
    }

    fun funFuturo(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val idrecoj = preferences.getString("CodigoRecojo", "CodigoRecojo")
        val idrecojo = idrecoj!!.toInt()

        val codusuario = preferences.getString("dniusuario", "nombreusuario")
        val estadorecojo = "F"

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        val fechaRecojo = ""+dateFormat.format(date)
        val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
        val horaFinal = preferences.getString("HoraFin", "HoraFin")

        val registrarEstado = RegistrarEstado(
            idrecojo,
            "" + codusuario,
            "" + estadorecojo,
            "" + fechaRecojo,
            "" + horaInicial,
            "" + horaFinal
        )
        val callo: Call<RegistrarEstado> = jsonPlaceHolderApi!!.registrarEstado(registrarEstado)
        callo.enqueue(object : Callback<RegistrarEstado?> {
            override fun onResponse(
                call: Call<RegistrarEstado?>,
                response: Response<RegistrarEstado?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val postsResponse: RegistrarEstado? = response.body()

                Toast.makeText(
                    applicationContext,
                    "Paquete recojido exitosamente",
                    Toast.LENGTH_SHORT
                ).show();
                finish()
            }

            override fun onFailure(call: Call<RegistrarEstado?>, t: Throwable) {
                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val editor = preferences.edit()

                val codusuario = preferences.getString("dniusuario", "nombreusuario")
                val estadorecojo = "F"
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = Date()
                val fechaRecojo = "" + dateFormat.format(date)
                val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
                val horaFinal = preferences.getString("HoraFin", "HoraFin")

                editor.putString("coderecojo" + coderecoj, "" + coderecoj);
                editor.putString("codeusuar" + coderecoj, "" + codusuario)
                editor.putString("estarec" + coderecoj, "" + estadorecojo)
                editor.putString("fechaRec" + coderecoj, "" + fechaRecojo)
                editor.putString("horaI" + coderecoj, "" + horaInicial)
                editor.putString("horaF" + coderecoj, "" + horaFinal)
                editor.commit()

                val dialog = Dialog(this@DetalleRecojo)
                dialog?.setContentView(R.layout.popup)
                dialog.show()
                val handler = Handler()
                handler.postDelayed({ dialog.dismiss() }, 4000)
                handler.postDelayed({finish()}, 4001)
                return
            }
        })
    }

    fun funAnulado(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
        val idrecoj = preferences.getString("CodigoRecojo", "CodigoRecojo")
        val idrecojo = idrecoj!!.toInt()

        val codusuario = preferences.getString("dniusuario", "nombreusuario")
        val estadorecojo = "A"

        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date()
        val fechaRecojo = ""+dateFormat.format(date)
        val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
        val horaFinal = preferences.getString("HoraFin", "HoraFin")

        val registrarEstado = RegistrarEstado(
            idrecojo,
            "" + codusuario,
            "" + estadorecojo,
            "" + fechaRecojo,
            "" + horaInicial,
            "" + horaFinal
        )
        val callo: Call<RegistrarEstado> = jsonPlaceHolderApi!!.registrarEstado(registrarEstado)
        callo.enqueue(object : Callback<RegistrarEstado?> {
            override fun onResponse(
                call: Call<RegistrarEstado?>,
                response: Response<RegistrarEstado?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val postsResponse: RegistrarEstado? = response.body()

                Toast.makeText(
                    applicationContext,
                    "Paquete recojido exitosamente",
                    Toast.LENGTH_SHORT
                ).show();
                finish()
            }

            override fun onFailure(call: Call<RegistrarEstado?>, t: Throwable) {
                val b = intent.extras
                val coderecoj = b?.getString("recojoReco", "recojoReco")
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@DetalleRecojo)
                val editor = preferences.edit()

                val codusuario = preferences.getString("dniusuario", "nombreusuario")
                val estadorecojo = "A"
                val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
                val date = Date()
                val fechaRecojo = "" + dateFormat.format(date)
                val horaInicial = preferences.getString("HoraInicio", "HoraInicio")
                val horaFinal = preferences.getString("HoraFin", "HoraFin")

                editor.putString("coderecojo" + coderecoj, "" + coderecoj);
                editor.putString("codeusuar" + coderecoj, "" + codusuario)
                editor.putString("estarec" + coderecoj, "" + estadorecojo)
                editor.putString("fechaRec" + coderecoj, "" + fechaRecojo)
                editor.putString("horaI" + coderecoj, "" + horaInicial)
                editor.putString("horaF" + coderecoj, "" + horaFinal)
                editor.commit()

                val dialog = Dialog(this@DetalleRecojo)
                dialog?.setContentView(R.layout.popup)
                dialog.show()
                val handler = Handler()
                handler.postDelayed({ dialog.dismiss() }, 4000)
                handler.postDelayed({finish()}, 4001)
                return
            }
        })
    }

    fun retroceder(){

        var i: Intent?= Intent(applicationContext, ListadoEmp::class.java)
        startActivity(i)
        finish()
    }

}