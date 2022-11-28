/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ipn.carritocompras;

import com.ipn.models.Product;
import com.ipn.models.Ticket;
import com.ipn.models.User;
import com.ipn.utils.Request;
import com.ipn.utils.Response;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Angel
 */
public class CarritoCompras {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        User user = null;
        Ticket ticket = null;
        List<Product> catalogo = null;
        int option;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Escriba la direccion del servidor");
        String host = br.readLine();
        System.out.println("Escriba el puerto");
        int puerto = Integer.parseInt(br.readLine());

        for (;;) {
            if (user == null) {
                System.out.println("Seleccione una opcion");
                System.out.println("1.- Registrar usuario");
                System.out.println("2.- Registrar administrador");
                System.out.println("3.- Iniciar Sesion");
                System.out.println("4.- Health Check");
                System.out.println("0.- Terminar programa");

                option = Integer.parseInt(br.readLine());

                switch (option) {
                    case 1: {
                        User new_user = new User();

                        System.out.println("Ingrese el nombre del usuario");
                        new_user.name = br.readLine();

                        System.out.println("Ingrese el apellido del usuario");
                        new_user.lastName = br.readLine();

                        System.out.println("Ingrese el email del usuario");
                        new_user.email = br.readLine();

                        System.out.println("Ingrese el password del usuario");
                        new_user.password = br.readLine();

                        Request request = new Request();
                        request.path = "GuardarUsuario";

                        Socket server = new Socket(host, puerto);
                        ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                        oos.writeObject(request);
                        oos.writeObject(new_user);

                        Response<String> response = (Response<String>) ois.readObject();
                        System.out.println(response.body);
                        
                        ois.close();
                        oos.close();
                        server.close();
                        break;
                    }
                    case 2: {
                        User new_user = new User();

                        System.out.println("Ingrese el nombre del usuario");
                        new_user.name = br.readLine();

                        System.out.println("Ingrese el apellido del usuario");
                        new_user.lastName = br.readLine();

                        System.out.println("Ingrese el email del usuario");
                        new_user.email = br.readLine();

                        System.out.println("Ingrese el password del usuario");
                        new_user.password = br.readLine();

                        Request request = new Request();
                        request.path = "GuardarUsuarioSuperAdmin";

                        Socket server = new Socket(host, puerto);
                        ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                        oos.writeObject(request);
                        oos.writeObject(new_user);

                        Response<String> response = (Response<String>) ois.readObject();
                        System.out.println(response.body);
                        
                        ois.close();
                        oos.close();
                        server.close();
                        
                        break;
                    }

                    case 3: {
                        System.out.println("Ingrese el email del usuario");
                        String email = br.readLine();

                        System.out.println("Ingrese el password del usuario");
                        String password = br.readLine();

                        Socket server = new Socket(host, puerto);
                        ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                        DataOutputStream dos = new DataOutputStream(server.getOutputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                        Request request = new Request();
                        request.path = "Login";

                        oos.writeObject(request);
                        dos.writeUTF(email);
                        dos.writeUTF(password);

                        Response<User> response = (Response<User>) ois.readObject();

                        if ("isValid".equals(response.headers[0].llave)
                                && (boolean) response.headers[0].valor == true) {
                            user = response.body;
                            System.out.println("Bienvenido " + user.name + " " + user.lastName);
                        } else if ("isValid".equals(response.headers[0].llave)
                                && (boolean) response.headers[0].valor == false) {
                            System.out.println("Error en email o password");
                        } else {
                            System.out.println("Algo salio mal, revisar logs");
                        }
                        
                        ois.close();
                        dos.close();
                        oos.close();
                        server.close();
                        break;
                    }

                    case 4: {
                        Socket server = new Socket(host, puerto);
                        ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                        Request request = new Request();
                        request.path = "";

                        oos.writeObject(request);

                        Response<String> response = (Response<String>) ois.readObject();
                        System.out.println(response.body);
                        
                        ois.close();
                        oos.close();
                        server.close();
                        
                        break;
                    }

                    default: {
                        return;
                    }
                }
            } else {
                if (user.isAdmin == false) {
                    System.out.println("Seleccione una opcion");
                    System.out.println("1.- Obtener Catalogo");
                    System.out.println("2.- Obtener imagen del producto");
                    System.out.println("3.- Nueva compra");
                    System.out.println("4.- Terminar compra");
                    System.out.println("5.- Agregar producto");
                    System.out.println("6.- Quitar producto");
                    System.out.println("7.- Obtener ticket para imprimir");
                    System.out.println("0.- Terminar programa");

                    option = Integer.parseInt(br.readLine());

                    switch (option) {
                        case 1: {
                            Request request = new Request();
                            request.path = "ObtenerCatalogo";

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            Response<List<Product>> response = (Response<List<Product>>) ois.readObject();

                            if ("isValid".equals(response.headers[0].llave)
                                    && (boolean) response.headers[0].valor == true) {
                                catalogo = response.body;
                                System.out.println("Catalogo cargado");
                            } else {
                                System.out.println("Algo salio mal, revisar logs");
                            }
                            
                            request = new Request();
                            request.path = "ObtenerCatalogo";

                            server = new Socket(host, puerto);
                            ois = new ObjectInputStream(server.getInputStream());
                            oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            Response<List<Product>> response_catalogo = (Response<List<Product>>) ois.readObject();

                            if ("isValid".equals(response_catalogo.headers[0].llave)
                                    && (boolean) response_catalogo.headers[0].valor == true) {
                                catalogo = response_catalogo.body;
                                System.out.println("Catalogo cargado");
                            } else {
                                System.out.println("Algo salio mal, revisar logs");
                            }
                            
                            ois.close();
                            oos.close();
                            server.close();
                            break;
                        }
                        case 2: {
                            Product product = SeleccionarProducto(catalogo);
                            break;
                        }
                        case 3: {
                            ticket = new Ticket();
                            break;
                        }
                        case 4: {
                            break;
                        }
                        case 5: {
                            Product product = SeleccionarProducto(catalogo);

                            Request request = new Request();
                            request.path = "AgregarProductoATicket";

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            oos.writeObject(ticket);
                            oos.writeObject(product);

                            Response<Ticket> response = (Response<Ticket>) ois.readObject();

                            if ("isValid".equals(response.headers[0].llave)
                                    && (boolean) response.headers[0].valor == true) {
                                ticket = response.body;
                                System.out.println("producto agregado correctamente");
                            } else if ("isValid".equals(response.headers[0].llave)
                                    && (boolean) response.headers[0].valor == false) {
                                System.out.println("Error al agregar producto");
                            } else {
                                System.out.println("Algo salio mal, revisar logs");
                            }
                            
                            ois.close();
                            oos.close();
                            server.close();

                            break;
                        }
                        default: {
                            return;
                        }
                    }

                } else if (user.isAdmin == true) {
                    System.out.println("Seleccione una opcion");
                    System.out.println("1.- Agregar Producto al Catalogo");
                    System.out.println("2.- Eliminar producto del Catalogo");
                    System.out.println("0.- Terminar programa");

                    option = Integer.parseInt(br.readLine());

                    switch (option) {
                        case 1: {
                            Product product = new Product();

                            System.out.println("Ingrese nombre del producto");
                            product.name = br.readLine();

                            System.out.println("Ingrese precio del producto");
                            product.price = Float.valueOf(br.readLine());

                            System.out.println("Ingrese numero de existencias del producto");
                            product.count = Integer.valueOf(br.readLine());

                            System.out.println("Ingrese el direccion de la imagen del producto");
                            System.out.println("(en el servidor)");
                            product.imagePath = br.readLine();

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            Request request = new Request();
                            request.path = "AgregarProductoACatalogo";

                            oos.writeObject(request);
                            oos.writeObject(user);
                            oos.writeObject(product);

                            Response<String> response = (Response<String>) ois.readObject();
                            System.out.println(response.body);
                            
                            ois.close();
                            oos.close();
                            server.close();
                            
                            break;
                        }
                        case 2: {
                            Product product = new Product();

                            System.out.println("Ingrese el ID del producto");
                            product.id = UUID.fromString(br.readLine());

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            Request request = new Request();
                            request.path = "EliminarProductoDelCatalogo";

                            oos.writeObject(request);
                            oos.writeObject(user);
                            oos.writeObject(product);

                            Response<String> response = (Response<String>) ois.readObject();
                            System.out.println(response.body);
                            
                            ois.close();
                            oos.close();
                            server.close();
                            
                            break;
                        }

                        default: {
                            return;
                        }
                    }
                } else {
                    System.out.println("Algo salio mal, revisar logs");
                }
            }
        }
    }

    private static Product SeleccionarProducto(List<Product> catalogo) throws IOException {
        if (catalogo == null) {
            System.out.println("Catalogo vacio");
            return null;
        }

        System.out.println("Seleccione el producto");
        for (Product item : catalogo) {
            System.out.println((catalogo.indexOf(item) + 1) + "------" + item.name);
            System.out.println("en existencia: " + item.count);
            System.out.println("");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int seleccion = Integer.parseInt(br.readLine()) - 1;

        return catalogo.get(seleccion);
    }
}
