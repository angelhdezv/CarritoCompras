/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.models;

import com.ipn.utils.ConectorDB;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class Ticket implements Serializable {

    private transient ConectorDB conexion;
    private final String table = "ticket";
    private final String table_items = "ticket_product";
    public UUID id = null;
    public UUID userId = null;
    public Float amount = null;
    public Timestamp transactionTime = null;
    private List<TicketProduct> products = null;

    public Ticket() {
        this.conexion = new ConectorDB("postgres", "postgres");
        this.products = new ArrayList<>();
    }
    
    public List<TicketProduct> GetProducts(){
        return this.products;
    }

    public boolean AddProduct(Product product, int count) throws SQLException {
        TicketProduct item = new TicketProduct();

        item.count = count;
        item.productId = product.id;

        boolean execute = product.UpdateCount(product.count - count);

        this.products.add(item);
        
        return execute;
    }

    public boolean DeleteProduct(TicketProduct item) throws SQLException {
        this.products.remove(item);

        Product product = new Product();
        product.Get(item.productId);

        boolean execute = product.UpdateCount(product.count + item.count);
        
        return execute;
    }

    public boolean Save(User user) throws SQLException {
        this.id = UUID.randomUUID();
        this.userId = user.id;
        this.transactionTime = new Timestamp(System.currentTimeMillis());
        this.amount = CalculateAmount();
        this.conexion = new ConectorDB("postgres", "postgres");
        this.conexion.Conectar();
        PreparedStatement stmt_ticket = this.conexion.connection
                .prepareStatement(
                        "INSERT INTO public.\"" + this.table + "\" (id,user_id,amount,transaction_time) VALUES (?,?,?,?)"
                );

        stmt_ticket.setObject(1, this.id);
        stmt_ticket.setObject(2, this.userId);
        stmt_ticket.setFloat(3, this.amount);
        stmt_ticket.setTimestamp(4, this.transactionTime);
        
        System.out.println(stmt_ticket.toString());

        int execute = stmt_ticket.executeUpdate();
        
        stmt_ticket.close();
        if (execute > 0) {
            PreparedStatement stmt_items = this.conexion.connection
                    .prepareStatement(
                            "INSERT INTO public.\"" + this.table_items + "\" (id,count,ticket,product) VALUES (?,?,?,?)"
                    );
            
            for (TicketProduct item : this.products) {
                item.id=UUID.randomUUID();
                item.ticketId = this.id;
                
                stmt_items.setObject(1, item.id);
                stmt_items.setInt(2, item.count);
                stmt_items.setObject(3, item.ticketId);
                stmt_items.setObject(4, item.productId);
                
                execute = stmt_items.executeUpdate();
            }
            
            stmt_items.close();
        }
        
        this.conexion.Desconectar();
        return execute > 0;
    }

    private Float CalculateAmount() throws SQLException {
        Float amount = new Float(0.0);
        for (TicketProduct item : this.products) {
            Product product = new Product();
            product.Get(item.productId);
            amount += product.price;
        }
        return amount;
    }
}
