package com.example.dowriq.almoxarife;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Estoque implements Serializable {
    private String id;
    private String nome;
    private String img;
    private int quantidade;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return this.nome;
    }

}
