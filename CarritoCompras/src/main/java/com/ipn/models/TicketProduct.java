/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.models;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class TicketProduct implements Serializable{
    public UUID id = null;
    public UUID ticketId = null;
    public UUID productId = null;
    public Integer count = null;
    
    public TicketProduct(){
        
    }
}
