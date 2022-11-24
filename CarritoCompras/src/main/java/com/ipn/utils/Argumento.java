/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.utils;

import java.io.Serializable;

/**
 *
 * @author Angel
 */
public class Argumento implements Serializable{
    public String llave;
    public Object valor;

    public Argumento(String llave, Object valor) {
        this.llave = llave;
        this.valor = valor;
    }
}
