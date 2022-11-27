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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class User implements Serializable {

    transient private ConectorDB conexion = new ConectorDB("postgres", "postgres");
    transient private String table = "user";
    public UUID id = null;
    public String name = null;
    public String lastName = null;
    public String email = null;
    public String password = null;
    public Timestamp createdAt = null;
    public Boolean isAdmin = null;

    public User() {

    }

    public User(
            String name,
            String lastName,
            String email,
            String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public List<User> All() throws SQLException {
        List<User> all = new ArrayList<>();
        this.conexion.Conectar();
        Statement stmt = this.conexion.connection.createStatement();
        ResultSet result = stmt.executeQuery("SELECT * FROM public.\"" + this.table + "\"");
        while (result.next()) {
            User user = new User();
            user.id = result.getObject("id", UUID.class);
            user.name = result.getString("name");
            user.lastName = result.getString("last_name");
            user.email = result.getString("email");
            user.password = result.getString("password");
            user.createdAt = result.getObject("created_at", Timestamp.class);
            all.add(user);
        }
        result.close();
        this.conexion.Desconectar();
        return all;
    }

    public boolean Save() throws SQLException {
        this.conexion.Conectar();
        PreparedStatement stmt = this.conexion.connection
                .prepareStatement(
                        "INSERT INTO public.\"" + this.table + "\" (id,name,last_name,email,password,created_at,is_admin) VALUES (?,?,?,?,?,?,?)"
                );
        this.id = UUID.randomUUID();
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.isAdmin = false;

        stmt.setObject(0, this.id);
        stmt.setString(1, this.name);
        stmt.setString(2, this.lastName);
        stmt.setString(3, this.email);
        stmt.setString(4, this.password);
        stmt.setObject(5, this.createdAt);
        stmt.setBoolean(6, this.isAdmin);

        int execute = stmt.executeUpdate();
        this.conexion.Desconectar();

        return execute > 0;
    }
    
    public boolean SaveSuperAdmin() throws SQLException {
        this.conexion.Conectar();
        PreparedStatement stmt = this.conexion.connection
                .prepareStatement(
                        "INSERT INTO public.\"" + this.table + "\" (id,name,last_name,email,password,created_at, is_admin) VALUES (?,?,?,?,?,?,?)"
                );
        this.id = UUID.randomUUID();
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.isAdmin = true;

        stmt.setObject(0, this.id);
        stmt.setString(1, this.name);
        stmt.setString(2, this.lastName);
        stmt.setString(3, this.email);
        stmt.setString(4, this.password);
        stmt.setObject(5, this.createdAt);
        stmt.setBoolean(6, this.isAdmin);

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
            this.lastName = result.getString("last_name");
            this.email = result.getString("email");
            this.password = result.getString("password");
            this.createdAt = result.getObject("created_at", Timestamp.class);
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
            this.lastName = result.getString("last_name");
            this.email = result.getString("email");
            this.password = result.getString("password");
            this.createdAt = result.getObject("created_at", Timestamp.class);
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
}
