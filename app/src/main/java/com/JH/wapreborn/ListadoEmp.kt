package com.JH.wapreborn

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.JH.wapreborn.Entidades.ListarAdapter
import com.JH.wapreborn.Entidades.Recojoinfo
import com.JH.wapreborn.Model.BuscarUsuario
import com.JH.wapreborn.Model.ListarRecojos
import com.JH.wapreborn.Model.RegistrarEstado
import com.JH.wapreborn.io.APIRetrofitInterface
import com.google.android.datatransport.runtime.dagger.multibindings.ElementsIntoSet
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class ListadoEmp : AppCompatActivity() {

    private var jsonPlaceHolderApi: APIRetrofitInterface? = null
    var listarAdapter: ListarAdapter? = null
    private var recyclerViewRecojos: RecyclerView? = null
    var listarrecoj: java.util.ArrayList<Recojoinfo>? = null

    private var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_emp)
        var retroceder: Button
        val retrofit = Retrofit.Builder()
            .baseUrl("http://200.37.50.53/ApiWap/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jsonPlaceHolderApi = retrofit.create(APIRetrofitInterface::class.java)
        recyclerViewRecojos = findViewById(R.id.recyclerViewRecojos);

        listarrecoj = java.util.ArrayList()

        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
        val checkListado = preferences.getInt("checkListado", 0)
        val NombreUsuario = preferences.getString("NombreUsuario", "NombreUsuario")
        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        val CodigoRecoj = preferences.getString("CodigoRecoj", "CodigoRecojo")
        val HoraInici = preferences.getString("HoraInici", "HoraInici")
        val HoraFi = preferences.getString("HoraFi", "HoraFi")
        val RazonSocia = preferences.getString("RazonSocia", "RazonSocia")
        val CodigoPosta = preferences.getString("CodigoPosta", "CodigoPosta")

        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE
        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }

        if (checkListado == 1 ) {
            val txtNombre = findViewById<TextView>(R.id.txtusuario);
            val txtPaquetes = findViewById<TextView>(R.id.txtpaquete);
            txtNombre.text = "BIENVENIDO " + NombreUsuario
            txtPaquetes.text = "Cargando.. "
        } else if(checkListado == 1 && HoraInici != "HoraInici"){
            val txtNombre = findViewById<TextView>(R.id.txtusuario);
            val txtPaquetes = findViewById<TextView>(R.id.txtpaquete);
            txtNombre.text = "BIENVENIDO " + NombreUsuario
            txtPaquetes.text = "Paquetes pendientes "+NumeroPaquetesRecojo
            //listarrecoj?.add(Recojoinfo("" + CodigoRecojo, "" + HoraInicio, "" + HoraFin, "" + RazonSocial, "" + CodigoPostal))
            recyclerViewRecojos?.setAdapter(listarAdapter)

        }
        Usuario()
        insertarofflinerecojos()
        cargarlistado()
        pasarActivityDetalles()
        onTouch()
    }

    fun onClick(view: View) {

        var retroceder: Button
        retroceder = findViewById(R.id.imgv)

        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()


        when (view.getId()) {
            R.id.imgv ->
                retroceder()
            R.id.btnrefresh ->
                refresh()
        }

    }

    fun Usuario(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
        val nombre = preferences.getString("dniusuario", "nombreusuario")
        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE

        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }
        val txtNombre = findViewById<TextView>(R.id.txtusuario);
        val txtPaquetes = findViewById<TextView>(R.id.txtpaquete);

        val buscarUsuario = BuscarUsuario(nombre)
        val callo: Call<BuscarUsuario> = jsonPlaceHolderApi!!.buscarUsuario(buscarUsuario)
        callo.enqueue(object : Callback<BuscarUsuario?> {
            override fun onResponse(
                call: Call<BuscarUsuario?>,
                response: Response<BuscarUsuario?>
            ) {
                if (!response.isSuccessful()) {
                    //mJsonTxtView.setText("Codigo:" + response.code());
                    Toast.makeText(
                        applicationContext,
                        "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val postsResponse: BuscarUsuario? = response.body()

                val CodigoUsuario: String = postsResponse?.CodigoUsuario() ?: "null"
                val NombreUsuario: String = postsResponse?.NombreUsuario() ?: "null"
                val NumeroPaquetesRecojo: String = postsResponse?.NumeroPaquetesRecojo() ?: "null"
                val EmpresaCod: String = postsResponse?.EmpresaCod() ?: "null"
                val SucursalCod: String = postsResponse?.SucursalCod() ?: "null"

                var checkListado = 1

                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
                val editor = preferences.edit()

                editor.putString("NombreUsuario", NombreUsuario);
                editor.putString("NumeroPaquetesRecojo", NumeroPaquetesRecojo)
                editor.putInt("checkListado", checkListado)
                editor.commit()

                txtNombre.text = "BIENVENIDO " + NombreUsuario
                txtPaquetes.text = "Paquetes pendientes " + NumeroPaquetesRecojo
                //Toast.makeText(Login.this, "Bienvenido "+nombre+" "+apellido, Toast.LENGTH_SHORT).show();
                //Guardar Login SharedPreferences
            }

            override fun onFailure(call: Call<BuscarUsuario?>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "No hay conexión a internet",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        })
    }

    fun onTouch() {
        val refresh = findViewById<Button>(R.id.btnrefresh)
        refresh.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsgray)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.refresh)
            }
            false
        })

        val retroceder = findViewById<Button>(R.id.imgv)
        retroceder.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                v.setBackgroundResource(R.drawable.buttonsgray)
            }
            if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                v.setBackgroundResource(R.drawable.atrasbtnimg)
            }
            false
        })
    }

    fun cargarlistado(){
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
        val dniusuario = preferences.getString("dniusuario", "nombreusuario")
        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE

        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }
        val listarRecojos = ListarRecojos(dniusuario)
        val call: Call<List<ListarRecojos>> = jsonPlaceHolderApi!!.listarRecojos(listarRecojos)
        call.enqueue(object : Callback<List<ListarRecojos>> {
            override fun onResponse(
                call: Call<List<ListarRecojos>>,
                response: Response<List<ListarRecojos>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    return
                }
                val rptas: List<ListarRecojos> = response.body()!!
                val tamanio = rptas.size
                for (i in 0 until tamanio) {

                    val abc: ListarRecojos = rptas[i]
                    val CodigoRecojo: String = abc.CodigoRecojo().toLowerCase() ?: "null"
                    val HoraInicio: String = abc.HoraInicio().toLowerCase() ?: "null"
                    val HoraFin: String = abc.HoraFin().toLowerCase() ?: "null"
                    val RazonSocial: String = abc.RazonSocial() ?: "null"
                    val CodigoPostal: String = abc.CodigoPostal() ?: "null"

                    listarrecoj?.add(
                        Recojoinfo(
                            "" + CodigoRecojo,
                            "" + HoraInicio,
                            "" + HoraFin,
                            "" + RazonSocial,
                            "" + CodigoPostal
                        )
                    )
                    recyclerViewRecojos?.setAdapter(listarAdapter)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(
                        applicationContext
                    )
                    val editor = preferences.edit()
                    val gson = Gson()
                    val json = gson.toJson(Recojoinfo(CodigoRecojo,HoraInicio,HoraFin,RazonSocial,CodigoPostal))
                    val contadortmn = i
                    editor.putInt("contadortmn", contadortmn)
                    editor.putString("Recojoinfo"+CodigoRecojo, json)
                    editor.commit()
                    editor.putString("dniusuario", dniusuario)
                    editor.putString("CodigoRecojo", CodigoRecojo)
                    editor.putString("HoraInici", HoraInicio + "" + CodigoRecojo)
                    editor.putString("HoraFi", HoraFin + "" + CodigoRecojo)
                    editor.putString("RazonSocia", RazonSocial + "" + CodigoRecojo)
                    editor.putString("CodigoPosta", CodigoPostal + "" + CodigoRecojo)
                    editor.commit()
                }
                return
            }

            override fun onFailure(call: Call<List<ListarRecojos>>, t: Throwable) {
                val txtNombre = findViewById<TextView>(R.id.txtusuario);
                val txtPaquetes = findViewById<TextView>(R.id.txtpaquete);
                val sharedPref = getPreferences(MODE_PRIVATE)
                val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
                val dniusuario = preferences.getString("dniusuario", "nombreusuario")
                val NombreUsuario = preferences.getString("NombreUsuario", "NombreUsuario")
                val NumeroPaquetesRecojo = preferences.getString(
                    "NumeroPaquetesRecojo",
                    "NumeroPaquetesRecojo"
                )
                val contadortmn = preferences.getInt("contadortmn", 0)

                for (i in 0 until contadortmn + 1) {
                    val gson = Gson()
                    val json: String? = preferences.getString("Recojoinfo"+"CodigoRecojo", "")
                    val Recojoinfo: Recojoinfo = gson.fromJson(json, Recojoinfo::class.java)

                    listarrecoj?.add(Recojoinfo)
                    recyclerViewRecojos?.setAdapter(listarAdapter)
                }
                txtNombre.text = "BIENVENIDO " + NombreUsuario
                txtPaquetes.text = "Paquetes pendientes " + NumeroPaquetesRecojo
                //Toast.makeText(applicationContext, "No hay acceso a internet", Toast.LENGTH_SHORT).show()
                return
            }
        })
    }

    fun pasarActivityDetalles(){
        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
        val nombre = preferences.getString("dniusuario", "nombreusuario")
        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE

        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }

        recyclerViewRecojos?.setLayoutManager(LinearLayoutManager(applicationContext))
        listarAdapter = ListarAdapter(applicationContext, listarrecoj)
        recyclerViewRecojos?.adapter = listarAdapter
        listarAdapter!!.setOnClickListener(View.OnClickListener { view ->
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return@OnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            //val nombrerecojo: String = listarrecoj!!.get(recyclerViewRecojos!!.getChildAdapterPosition(view)).horaFin
            //int tamanio = listaPersonas.size();
            //RetosInfo abc = listaPersonas.get(1);
            val listarRecojos = ListarRecojos(nombre)
            val call: Call<List<ListarRecojos>> = jsonPlaceHolderApi!!.listarRecojos(listarRecojos)
            call.enqueue(object : Callback<List<ListarRecojos>> {
                override fun onResponse(
                    call: Call<List<ListarRecojos>>,
                    response: Response<List<ListarRecojos>>
                ) {
                    if (!response.isSuccessful) {
                        val loadingThing = LoadingThing(this@ListadoEmp)
                        loadingThing.startLoadingAnimation()
                        Toast.makeText(applicationContext, "Error.", Toast.LENGTH_SHORT).show()
                        loadingThing.dismissDialog()
                        return
                    }
                    val loadingThing = LoadingThing(this@ListadoEmp)
                    loadingThing.startLoadingAnimation()
                    val rptas: List<ListarRecojos> = response.body()!!
                    val tamanio = rptas.size
                    for (i in 0 until tamanio) {
                        val abc: ListarRecojos = rptas[i]

                        var CodigoRecojo: String = abc.CodigoRecojo()
                        val HoraInicio: String = abc.HoraInicio()
                        val HoraFin: String = abc.HoraFin()
                        val RazonSocial: String = abc.RazonSocial()
                        val CodigoPostal: String = abc.CodigoPostal()

                        //Enviar datos----------------------------------------------------------

                        val codigoRecojo: String = listarrecoj!!.get(
                            recyclerViewRecojos!!.getChildAdapterPosition(
                                view
                            )
                        ).codigoRecojo

                        if (CodigoRecojo == codigoRecojo) {
                            val sharedPref = getPreferences(MODE_PRIVATE)
                            val preferences =
                                PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
                            val editor = preferences.edit()
                            editor.putString("CodigoRecojo", CodigoRecojo);
                            editor.putString("HoraInicio", HoraInicio)
                            editor.putString("HoraFin", HoraFin)
                            editor.putString("RazonSocial", RazonSocial)
                            editor.putString("CodigoPostal", CodigoPostal)
                            editor.commit()
                            loadingThing.dismissDialog()
                            val o = Intent(this@ListadoEmp, DetalleRecojo::class.java)
                            startActivity(o)
                            return
                        }

                    }
                    loadingThing.dismissDialog()
                    return
                }

                override fun onFailure(call: Call<List<ListarRecojos>>, t: Throwable) {

                    val loadingThing = LoadingThing(this@ListadoEmp)
                    loadingThing.startLoadingAnimation()
                    val sharedPref = getPreferences(MODE_PRIVATE)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
                    val CodigoRecojoDetalle = preferences.getString(
                        "CodigoRecojoDetalle",
                        "CodigoRecojoDetalle"
                    )
                    val codigoRecojo: String = listarrecoj!!.get(
                        recyclerViewRecojos!!.getChildAdapterPosition(
                            view
                        )
                    ).codigoRecojo


                    if (CodigoRecojoDetalle == "CodigoRecojoDetalle") {
                        Toast.makeText(
                            applicationContext,
                            "No hay conexión a internet",
                            Toast.LENGTH_SHORT
                        ).show()
                        loadingThing.dismissDialog()
                        return
                    } else {

                        val b = Bundle()
                        b.putString("recojoReco", codigoRecojo)
                        val o = Intent(this@ListadoEmp, DetalleRecojo::class.java)
                        o.putExtras(b)
                        startActivity(o)
                        loadingThing.dismissDialog()
                        return
                    }
                    loadingThing.dismissDialog()
                    return
                }
            })
        })
    }

    fun retroceder(){
        val i = Intent(this@ListadoEmp, popupatraslogin::class.java)
        startActivity(i)
    }

    fun refresh(){

        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)
        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE

        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }
        val nombre = preferences.getString("dniusuario", "nombreusuario")

        val loadingThing = LoadingThing(this@ListadoEmp)
        loadingThing.startLoadingAnimation()

        val listarRecojos = ListarRecojos(nombre)
        val call: Call<List<ListarRecojos>> = jsonPlaceHolderApi!!.listarRecojos(listarRecojos)
        call.enqueue(object : Callback<List<ListarRecojos>> {
            override fun onResponse(
                call: Call<List<ListarRecojos>>,
                response: Response<List<ListarRecojos>>
            ) {
                if (!response.isSuccessful) {
                    loadingThing.dismissDialog()
                    Toast.makeText(applicationContext, "Error.", Toast.LENGTH_SHORT).show()
                    return
                }
                val rptas: List<ListarRecojos> = response.body()!!
                val tamanio = rptas.size
                listarrecoj?.clear()
                for (i in 0 until tamanio) {
                    val s = i
                    val abc: ListarRecojos = rptas[i]
                    val CodigoRecojo: String = abc.CodigoRecojo().toLowerCase() ?: "null"
                    val HoraInicio: String = abc.HoraInicio().toLowerCase() ?: "null"
                    val HoraFin: String = abc.HoraFin().toLowerCase() ?: "null"
                    val RazonSocial: String = abc.RazonSocial() ?: "null"
                    val CodigoPostal: String = abc.CodigoPostal() ?: "null"


                    listarrecoj?.add(
                        Recojoinfo(
                            "" + CodigoRecojo,
                            "" + HoraInicio,
                            "" + HoraFin,
                            "" + RazonSocial,
                            "" + CodigoPostal
                        )
                    )
                    recyclerViewRecojos?.setAdapter(listarAdapter)
                    val preferences = PreferenceManager.getDefaultSharedPreferences(
                        applicationContext
                    )
                    val editor = preferences.edit()
                    editor.putString("dniusuario", nombre)
                    editor.commit()
                    loadingThing.dismissDialog()

                }

                Toast.makeText(applicationContext, "Lista actualizada", Toast.LENGTH_SHORT).show()
                return
            }

            override fun onFailure(call: Call<List<ListarRecojos>>, t: Throwable) {
                loadingThing.dismissDialog()
                Toast.makeText(applicationContext, "No hay acceso a internet", Toast.LENGTH_SHORT)
                    .show()
                return
            }
        })
        Usuario()
        insertarofflinerecojos()
        loadingThing.dismissDialog()
    }

    fun insertarofflinerecojos() {

        val sharedPref = getPreferences(MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)

        val NumeroPaquetesRecojo = preferences.getString(
            "NumeroPaquetesRecojo",
            "NumeroPaquetesRecojo"
        )
        if (NumeroPaquetesRecojo == "0"){
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.GONE

        }
        else{
            val recycler = findViewById<RecyclerView>(R.id.recyclerViewRecojos)
            recycler.visibility = View.VISIBLE
        }

        val dniusuario = preferences.getString("dniusuario", "nombreusuario")
        val listarRecojos = ListarRecojos(dniusuario)
        val call: Call<List<ListarRecojos>> = jsonPlaceHolderApi!!.listarRecojos(listarRecojos)
        call.enqueue(object : Callback<List<ListarRecojos>> {
            override fun onResponse(
                call: Call<List<ListarRecojos>>,
                response: Response<List<ListarRecojos>>
            ) {
                if (!response.isSuccessful) {
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    return
                }
                val rptas: List<ListarRecojos> = response.body()!!
                val tamanio = rptas.size
                for (i in 0 until tamanio) {

                    val abc: ListarRecojos = rptas[i]
                    val CodigoRecojo: String = abc.CodigoRecojo().toLowerCase() ?: "null"

                    val coderecoj = preferences.getString("coderecojo" + CodigoRecojo, "1")
                    val coderecojo = coderecoj!!.toInt()
                    val codusuario = preferences.getString(
                        "codeusuar" + CodigoRecojo,
                        "CodeUsuario"
                    )
                    val estadorecojo = preferences.getString(
                        "estarec" + CodigoRecojo,
                        "EstadoRecojo"
                    )
                    val fechaRecojo = preferences.getString(
                        "fechaRec" + CodigoRecojo,
                        "FechaRecojo"
                    )
                    val horaInicial = preferences.getString("horaI" + CodigoRecojo, "HoraInicial")
                    val horaFinal = preferences.getString("horaF" + CodigoRecojo, "HoraFinal")

                    if (CodigoRecojo == coderecoj) {
                        val registrarEstado = RegistrarEstado(
                            coderecojo,
                            "" + codusuario,
                            "" + estadorecojo,
                            "" + fechaRecojo,
                            "" + horaInicial,
                            "" + horaFinal
                        )
                        val callo: Call<RegistrarEstado> =
                            jsonPlaceHolderApi!!.registrarEstado(registrarEstado)
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
                                val preferences =
                                    PreferenceManager.getDefaultSharedPreferences(this@ListadoEmp)

                                preferences.edit().remove("coderecojo" + coderecoj).commit();
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


                                val dialog = Dialog(this@ListadoEmp)
                                dialog!!.setContentView(R.layout.popupsuccess)
                                dialog.show()
                                val handler = Handler()
                                handler.postDelayed({ dialog.dismiss() }, 4000)
                            }

                            override fun onFailure(call: Call<RegistrarEstado?>, t: Throwable) {

                                return
                            }
                        })
                    }

                }

                return
            }

            override fun onFailure(call: Call<List<ListarRecojos>>, t: Throwable) {

                return
            }
        })
    }
}
