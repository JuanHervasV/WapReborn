package com.JH.wapreborn.Model;

public class BuscarUsuario {


    private String codigoUsuario;

    private String CodigoUsuario;
    private String NombreUsuario;
    private String NumeroPaquetesRecojo;
    private String EmpresaCod;
    private String SucursalCod;

    public String CodigoUsuario() {
        return CodigoUsuario;
    }
    public String NombreUsuario() {
        return NombreUsuario;
    }
    public String NumeroPaquetesRecojo() {
        return NumeroPaquetesRecojo;
    }
    public String EmpresaCod() {
        return EmpresaCod;
    }
    public String SucursalCod() {
        return SucursalCod;
    }

    public BuscarUsuario(String CodigoUsuario) {
        codigoUsuario= CodigoUsuario;
    }
}
