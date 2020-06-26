package com.a.marvel_infiniteapp.model;

public class modelo {


    private int ID;
    private String Titulo;
    private String Genero;
    private String Ano;
    private String Poster;
    private String Nota;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getAno() {
        return Ano;
    }

    public void setAno(String ano) {
        Ano = ano;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }


    public modelo( ) {

    }


    public modelo( int id, String titulo, String genero, String ano, String poster, String nota) {
        this.ID = id;
        this.Titulo = titulo;
        this.Genero = genero;
        this.Ano = ano;
        this.Poster = poster;
        this.Nota = nota;
    }




}
