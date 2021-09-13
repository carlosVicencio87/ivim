package com.ivim.ivim;

public class Servidor {
    public static String  servidor= "http://vdi.netsec.com.mx:80/controlador/usuario/";
    public  String local = "http://192.168.0.3:80/basculas/controlador/";
    public String getLocalHost(){
        return this.local;    }
    public String getServer (){
        return this.servidor;
    }
}
