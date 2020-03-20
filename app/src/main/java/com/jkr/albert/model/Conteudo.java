package com.jkr.albert.model;

import java.io.Serializable;

public class Conteudo implements Serializable {
    private String titulo,texto;

    public Conteudo() {

    }

    public Conteudo(String titulo, String texto) {
        this.titulo = titulo;
        this.texto = texto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
