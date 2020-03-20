package com.jkr.albert.model;


import java.io.Serializable;

public class Projeto implements Serializable {
    private String url,nome,usuario,id,numagencia,nomeusuario;
    private double objetivo,arrecadado;

    public Projeto() {
    }

    public Projeto(String id,String url, String nome,String numagencia, double objetivo,String usuario,String nomeusuario) {
        this.url = url;
        this.nome = nome;
        this.usuario = usuario;
        this.id = id;
        this.numagencia = numagencia;
        this.objetivo = objetivo;
        this.nomeusuario = nomeusuario;
    }

    public String getNomeusuario() {
        return nomeusuario;
    }

    public String getNome() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public String getNumagencia() {
        return numagencia;
    }

    public String getUrl() {
        return url;
    }

    public double getObjetivo() {
        return objetivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public double getArrecadado() {
        return arrecadado;
    }

    public void setArrecadado(double arrecadado) {
        this.arrecadado = arrecadado;
    }
}
