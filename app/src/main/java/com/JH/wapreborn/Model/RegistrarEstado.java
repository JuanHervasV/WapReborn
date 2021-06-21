package com.JH.wapreborn.Model;

public class RegistrarEstado {

    private int iDRecojo;
    private String codigoUsuario;
    private String estadoRecojo;
    private String fechaRecojo;
    private String horainicial;
    private String horafinal;

    private boolean estado;
    private String mensaje;

    public boolean estado() {
        return estado;
    }
    public String mensaje() {
        return mensaje;
    }

    public RegistrarEstado(int IDRecojo, String CodigoUsuario, String EstadoRecojo, String FechaRecojo, String Horainicial, String Horafinal)
    {
        iDRecojo= IDRecojo;
        codigoUsuario = CodigoUsuario;
        estadoRecojo= EstadoRecojo;
        fechaRecojo = FechaRecojo;
        horainicial= Horainicial;
        horafinal = Horafinal;
    }



}
