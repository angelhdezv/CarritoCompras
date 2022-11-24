/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Angel
 */
public class ConectorDB {
    
    public Connection connection = null;
    private String user = null;
    private String password = null;
    
    public ConectorDB(String user, String psw) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }
        
        this.user = user; 
        this.password = psw; 
    }
    
    public void Conectar(){
         try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/shop",
                    this.user, this.password);
            
        } catch (java.sql.SQLException sqle) {
            System.out.println("Error: " + sqle);
        }
    }
    
    public void Desconectar() throws SQLException{
        this.connection.close();
    }
}
