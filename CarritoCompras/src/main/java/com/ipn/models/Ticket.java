/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.models;

import com.ipn.utils.ConectorDB;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class Ticket implements Serializable{
    transient private ConectorDB conexion = new ConectorDB("postgres","postgres");
    transient private String table = "product";
    public UUID id = null;
    public UUID userId = null;
    public Float amount = null;
    public Timestamp transactionTime = null;
    public List<TicketProduct> products = null;
    
    public Ticket(){
        
    }
}
