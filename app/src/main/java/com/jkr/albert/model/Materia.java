package com.jkr.albert.model;

import java.util.ArrayList;

public class Materia {
    private ArrayList<String> topicos;

    public Materia() {
    }

    public Materia(ArrayList<String> topicos) {
        this.topicos = topicos;
    }

    public ArrayList<String> getTopicos() {
        return topicos;
    }

    public void setTopicos(ArrayList<String> topicos) {
        this.topicos = topicos;
    }
}
