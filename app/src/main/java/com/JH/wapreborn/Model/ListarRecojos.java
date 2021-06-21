package com.JH.wapreborn.Model;

public class ListarRecojos {

    private String codigoUsuario;

    private String CodigoRecojo;
    private String HoraInicio;
    private String HoraFin;
    private String RazonSocial;
    private String CodigoPostal;

    public String CodigoRecojo() {
        return CodigoRecojo;
    }
    public String HoraInicio(){return HoraInicio; }
    public String HoraFin() {
        return HoraFin;
    }
    public String RazonSocial() {
        return RazonSocial;
    }
    public String CodigoPostal() {
        return CodigoPostal;
    }

    public ListarRecojos(String CodigoUsuario) {
        codigoUsuario= CodigoUsuario;
    }
}
