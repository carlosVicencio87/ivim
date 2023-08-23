package com.ivim.ivim;

public class Sentencias {

    public static final String TABLA_CATALOGO = "CREATE TABLE catalogo (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, alcance_maximo TEXT, eod TEXT, tipo TEXT, alcance_minimo TEXT, alcance_medicion TEXT, clase_exactitud TEXT, marco_pesas TEXT, pesa_5kg TEXT, pesa_10kg TEXT, pesa_20kg TEXT, pesa_clase_exactitud TEXT, horario TEXT)";
    public static final String TABLA_BASCULAS = "CREATE TABLE basculas(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, marca TEXT, modelo TEXT, numero_aprobacion TEXT, codigo_marca TEXT, codigo_modelo TEXT, ano_aprobacion TEXT, alcance_minimo TEXT, alcance_maximo TEXT, eod TEXT)";
    public static final String DROP_CATALOGO = "DROP TABLE IF EXISTS catalogo";
    public static final String DROP_BASCULAS = "DROP TABLE IF EXISTS basculas";

}
