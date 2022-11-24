/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ipn.ServidorCarritoCompras;

import com.ipn.utils.Request;
import io.ebean.DB;
import io.ebean.Database;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Angel
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        CrearDb(); 
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el puerto del servidor");
        int puerto = Integer.parseInt(br.readLine());
        ServerSocket s = new ServerSocket(puerto);
        s.setReuseAddress(true);
        System.out.println("Servidor iniciado esperando por archivos..");
        for(;;){
            Socket cl = s.accept();
            System.out.println("Cliente conectado desde " + cl.getInetAddress() + ":" + cl.getPort());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            
            Request request=(Request) ois.readObject();
            
            if(request.path.equals("")){
                
            }else if (request.path.equals("ObtenerCatalogo")){
                
            }else{
                System.err.println("InvalidPath");
            }
            
            dis.close();
            cl.close();
        }
    }

    private static void CrearDb() {
        Database db = DB.getDefault();
    }
    
}
