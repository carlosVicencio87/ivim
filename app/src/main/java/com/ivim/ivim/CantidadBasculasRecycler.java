package com.ivim.ivim;

import androidx.appcompat.app.AppCompatActivity;

public class CantidadBasculasRecycler extends AppCompatActivity {
    private String cantidad_bascula;
    private String cantidad_tipo_instrumento;
    private String cantidad_modelo;
    private String cantidad_serie;
    private String cantidad_AlcanceMax;
    private String cantidad_Eod;
    private String cantidad_AlcanceMin;
    private String cantidad_exactitud;
    private String cantidad_checkbox;
    private String cantidad_costo;

    public CantidadBasculasRecycler(String cant_bascula,String cant_tipo_instrumento,String cant__modelo,String cant_serie,String cant_AlcanceMax,
                                    String cant_Eod,String cant_AlcanceMin,String cant_exactitud, String cant_checkbox, String cant_costo){
        this.cantidad_bascula =cant_bascula;
        this.cantidad_tipo_instrumento =cant_tipo_instrumento;
        this.cantidad_modelo =cant__modelo;
        this.cantidad_serie =cant_serie;
        this.cantidad_AlcanceMax =cant_AlcanceMax;
        this.cantidad_Eod =cant_Eod;
        this.cantidad_AlcanceMin =cant_AlcanceMin;
        this.cantidad_exactitud =cant_exactitud;
        this.cantidad_checkbox =cant_checkbox;
        this.cantidad_costo =cant_costo;
    }

    public String getCantidad_bascula() { return cantidad_bascula; }
   public String getCantidad_tipo_instrumento() { return cantidad_tipo_instrumento; }
    public String getCantidad_modelo() { return cantidad_modelo; }
    public String getCantidad_serie() { return cantidad_serie; }
    public String getCantidad_AlcanceMax() { return cantidad_AlcanceMax; }
    public String getCantidad_Eod() { return cantidad_Eod; }
    public String getCantidad_AlcanceMin() { return cantidad_AlcanceMin; }
    public String getCantidad_exactitud() { return cantidad_exactitud; }

    public String getCantidad_checkbox() { return cantidad_checkbox; }

    public String getCantidad_costo() { return cantidad_costo; }

    public void setCantidad_bascula(String cantidad_bascula) {
        this.cantidad_bascula = cantidad_bascula; }
    public void setCantidad_tipo_instrumento(String cantidad_tipo_instrumento) {
        this.cantidad_tipo_instrumento = cantidad_tipo_instrumento; }
    public void setCantidad_modelo(String cantidad_modelo) {
        this.cantidad_modelo = cantidad_modelo; }
    public void setCantidad_serie(String cantidad_serie) {
        this.cantidad_serie = cantidad_serie; }
    public void setCantidad_AlcanceMax(String cantidad_AlcanceMax) {
        this.cantidad_AlcanceMax = cantidad_AlcanceMax; }
    public void setCantidad_Eod(String cantidad_eod) {
        this.cantidad_Eod = cantidad_eod; }
    public void setCantidad_AlcanceMin(String cantidad_AlcanceMin) {
        this.cantidad_AlcanceMin = cantidad_AlcanceMin; }
    public void setCantidad_exactitud(String cantidad_exactitud) {
        this.cantidad_exactitud = cantidad_exactitud; }
    public void setCantidad_checkbox(String cantidad_checkbox) {
        this.cantidad_bascula = cantidad_bascula; }
    public void setCantidad_costo(String cantidad_costo) {
        this.cantidad_costo = cantidad_costo; }
}