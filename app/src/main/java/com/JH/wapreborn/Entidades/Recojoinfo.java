package com.JH.wapreborn.Entidades;

public class Recojoinfo {
    private String CodigoRecojo;
    private String HoraInicio;
    private String HoraFin;
    private String RazonSocial;
    private String CodigoPostal;

    public Recojoinfo(String CodigoRecojo, String HoraInicio, String HoraFin, String RazonSocial, String CodigoPostal){
        this.CodigoRecojo = CodigoRecojo;
        this.HoraInicio = HoraInicio;
        this.HoraFin = HoraFin;
        this.RazonSocial = RazonSocial;
        this.CodigoPostal = CodigoPostal;
    }

    public String getCodigoRecojo(){ return CodigoRecojo;}
    public String getHoraInicio(){return HoraInicio;}
    public String getHoraFin(){ return HoraFin;}
    public String getRazonSocial(){return RazonSocial;}
    public String getCodigoPostal(){return CodigoPostal;}
}
