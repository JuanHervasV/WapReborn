package com.JH.wapreborn.io;



import com.JH.wapreborn.Model.BuscarUsuario;
import com.JH.wapreborn.Model.DetalleRecojo;
import com.JH.wapreborn.Model.InsertarToken;
import com.JH.wapreborn.Model.ListarRecojos;
import com.JH.wapreborn.Model.RegistrarEstado;
import com.JH.wapreborn.Model.Vars;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIRetrofitInterface {

    @POST("ListarRecojos")
    Call<List<ListarRecojos>> listarRecojos(@Body ListarRecojos listarRecojos);

    @POST("BuscarUsuario")
    Call<BuscarUsuario> buscarUsuario(@Body BuscarUsuario buscarUsuario);

    @POST("DetalleRecojo")
    Call<DetalleRecojo> detalleRecojo(@Body DetalleRecojo detalleRecojo);

    @POST("RegistrarEstado")
    Call<RegistrarEstado> registrarEstado(@Body RegistrarEstado registrarEstado);

    @POST("Login")
    Call<Vars> login(@Body Vars login);

    @POST("InsertarToken")
    Call<InsertarToken> insertarToken(@Body InsertarToken insertarToken);

}
