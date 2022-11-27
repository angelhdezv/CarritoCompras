/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.utils;

import java.io.Serializable;

/**
 *
 * @author Angel
 * @param <T>
 */
public class Response<T> implements Serializable{
    public Header[] headers;
    public T body;
    
    public Response(Header[] headers, T body){
        this.headers = headers;
        this.body = body;
    }
}
