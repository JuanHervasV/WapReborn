package com.JH.wapreborn.Model;

public class DetalleRecojo {

    private String iDRecojo;

    private String CodigoRecojo;
    private String RazonSocial;
    private String Contacto;
    private String Direccion;
    private String CodigoPostal;
    private String Embalaje;
    private String Peso;
    private String Destino;
    private String Descripcion;
    private String HoraIni;
    private String HoraFin;
    private String PagoRecojo1;
    private String PagoRecojo2;
    private String Observacion;

    public String CodigoRecojo() {
        return CodigoRecojo;
    }
    public String RazonSocial() {
        return RazonSocial;
    }
    public String Contacto() {
        return Contacto;
    }
    public String Direccion() {
        return Direccion;
    }
    public String CodigoPostal() {
        return CodigoPostal;
    }
    public String Embalaje() {
        return Embalaje;
    }
    public String Peso() {
        return Peso;
    }
    public String Destino() {
        return Destino;
    }
    public String Descripcion() {
        return Descripcion;
    }
    public String HoraIni() {
        return HoraIni;
    }
    public String HoraFin() {
        return HoraFin;
    }
    public String PagoRecojo1() {
        return PagoRecojo1;
    }
    public String PagoRecojo2() {
        return PagoRecojo2;
    }
    public String Observacion() {
        return Observacion;
    }

    public DetalleRecojo(String IDRecojo) {
        iDRecojo= IDRecojo;
    }

}
