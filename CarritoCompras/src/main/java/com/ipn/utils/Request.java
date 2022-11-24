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
public class Request implements Serializable{

    public String path;
    public String metodo;
    public Header[] headers;
    public Argumento[] args;

    public Request() {
    }
}
