/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ipn.models;

import com.ipn.utils.ConectorDB;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class Product implements Serializable {

    transient private ConectorDB conexion = new ConectorDB("postgres", "postgres");
    transient private String table = "product";
    public UUID id = null;
    public String name = null;
    public Integer count = null;
    public Float price = null;
    public String imagePath = null;

    public Product() {

    }

    public Product(
            String name,
            Integer count,
            Float price,
            String imagePath) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.imagePath = imagePath;
    }

    public List<Product> All() throws SQLException {
        List<Product> all = new ArrayList<>();
        this.conexion.Conectar();
        Statement stmt = this.conexion.connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM public.\"" + this.table + "\"");
        while (result.next()) {
            Product Product = new Product();
            Product.id = result.getObject("id", UUID.class);
            Product.name = result.getString("name");
            Product.imagePath = result.getString("image_path");
            Product.count = result.getInt("count");
            Product.price = result.getFloat("price");
            all.add(Product);
        }
        result.close();
        this.conexion.Desconectar();
        return all;
    }

    public boolean Save() throws SQLException {
        this.conexion.Conectar();
        PreparedStatement stmt = this.conexion.connection
                .prepareStatement(
                        "INSERT INTO public.\"" + this.table + "\" (id,name,image_path,count,price) VALUES (?,?,?,?,?,?)"
                );
        this.id = UUID.randomUUID();

        stmt.setObject(0, this.id);
        stmt.setString(1, this.name);
        stmt.setString(2, this.imagePath);
        stmt.setInt(3, this.count);
        stmt.setFloat(4, this.price);

        int execute = stmt.executeUpdate();
        this.conexion.Desconectar();

        return execute > 0;
    }

    public void Get(UUID id) throws SQLException {
        this.conexion.Conectar();
        Statement stmt = this.conexion.connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM public.\"" + this.table + "\" where id = '" + id.toString() + "'");
        while (result.next()) {
            this.id = result.getObject("id", UUID.class);
            this.name = result.getString("name");
            this.imagePath = result.getString("image_path");
            this.count = result.getInt("count");
            this.price = result.getFloat("price");
            break;
        }
        result.close();
        this.conexion.Desconectar();
    }

    public void GetByEmail(String email) throws SQLException {
        this.conexion.Conectar();
        Statement stmt = this.conexion.connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM public.\"" + this.table + "\" where email = '" + email + "'");
        while (result.next()) {
            this.id = result.getObject("id", UUID.class);
            this.name = result.getString("name");
            this.imagePath = result.getString("image_path");
            this.count = result.getInt("count");
            this.price = result.getFloat("price");
            break;
        }
        result.close();
        this.conexion.Desconectar();
    }

    public boolean Delete() throws SQLException {
        this.conexion.Conectar();
        PreparedStatement stmt = this.conexion.connection
                .prepareStatement(
                        "DELETE FROM public.\"" + this.table + "\" WHERE id = ? "
                );

        stmt.setObject(0, this.id);

        int execute = stmt.executeUpdate();
        this.conexion.Desconectar();

        return execute > 0;
    }

    public boolean UpdateCount(int newCount) throws SQLException {
        this.conexion.Conectar();
        PreparedStatement stmt = this.conexion.connection
                .prepareStatement(
                        "UPDATE public.\"" + this.table + "\" SET count = ? WHERE id = ? "
                );

        stmt.setInt(0, newCount);
        stmt.setObject(1, this.id);

        int execute = stmt.executeUpdate();
        this.conexion.Desconectar();

        this.count = newCount;

        return execute > 0;
    }
}
