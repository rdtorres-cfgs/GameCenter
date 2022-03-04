package com.example.gamecenter;

public class Usuario {
    int id;
    String username, password, nombre, apellido1, apellido2 ;
    int puntuacion2048, puntuacionpeg;

    //Constructor Completo
    public Usuario(int id, String username, String password, String nombre, String apellido1, String apellido2, int puntuacion2048, int puntuacionpeg) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.puntuacion2048 = puntuacion2048;
        this.puntuacionpeg = puntuacionpeg;
    }

    //Constructor sin ID


    public Usuario(String username, String password, String nombre, String apellido1, String apellido2, int puntuacion2048, int puntuacionpeg) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.puntuacion2048 = puntuacion2048;
        this.puntuacionpeg = puntuacionpeg;
    }

    //Getters && Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPuntuacion2048() {
        return puntuacion2048;
    }

    public void setPuntuacion2048(int puntuacion2048) {
        this.puntuacion2048 = puntuacion2048;
    }

    public int getPuntuacionpeg() {
        return puntuacionpeg;
    }

    public void setPuntuacionpeg(int puntuacionpeg) {
        this.puntuacionpeg = puntuacionpeg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }
}
