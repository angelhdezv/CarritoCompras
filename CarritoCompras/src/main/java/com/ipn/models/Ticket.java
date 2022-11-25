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

    transient private ConectorDB conexion = new ConectorDB("postgres", "postgres");
    transient private String table = "ticket";
    transient private String table_items = "ticket_product";
    public UUID id = null;
    public UUID userId = null;
    public Float amount = null;
    public Timestamp transactionTime = null;
    private List<TicketProduct> products = null;

    public Ticket() {
        this.products = new ArrayList<>();
    }

    public void AddProduct(Product product, int count) throws SQLException {
        TicketProduct item = new TicketProduct();

        item.count = count;
        item.productId = product.id;

        product.UpdateCount(product.count - count);

        this.products.add(item);
    }

    public void DeleteProduct(TicketProduct item) throws SQLException {
        this.products.remove(item);

        Product product = new Product();
        product.Get(item.productId);

        product.UpdateCount(product.count + item.count);
    }

    public boolean Save(User user) throws SQLException {
        this.id = UUID.randomUUID();
        this.userId = user.id;
        this.transactionTime = new Timestamp(System.currentTimeMillis());
        this.amount = CalculateAmount();

        this.conexion.Conectar();
        PreparedStatement stmt_ticket = this.conexion.connection
                .prepareStatement(
                        "INSERT INTO public.\"" + this.table + "\" (id,user,amount,transaction_time) VALUES (?,?,?,?)"
                );

        stmt_ticket.setObject(0, this.id);
        stmt_ticket.setObject(1, this.userId);
        stmt_ticket.setFloat(2, this.amount);
        stmt_ticket.setTimestamp(3, this.transactionTime);

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
                
                stmt_items.setObject(0, item.id);
                stmt_items.setInt(1, item.count);
                stmt_items.setObject(2, item.ticketId);
                stmt_items.setObject(3, item.productId);
                
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
