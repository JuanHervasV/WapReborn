package com.JH.wapreborn.Model;

public class InsertarToken {

    private int usuid;
    private String token;
    private boolean estado;
    private String mensaje;

    public boolean Rpta() {
        return estado;
    }
    public String Mensaje() {
        return mensaje;
    }

    public InsertarToken(int Usuid, String Token)

    {
        usuid= Usuid;
        token = Token;
    }

}
