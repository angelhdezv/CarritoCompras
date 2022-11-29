/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ipn.carritocompras;

import com.ipn.models.Product;
import com.ipn.models.Ticket;
import com.ipn.models.TicketProduct;
import com.ipn.models.User;
import com.ipn.utils.Request;
import com.ipn.utils.Response;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

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
                        Request request = new Request();
                        request.path = "";

                        Socket server = new Socket(host, puerto);
                        ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                        ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());
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
                    System.out.println("8.- Ver carrito de compras");
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

                            catalogo = response.body;
                            System.out.println("Catalogo cargado");

                            ois.close();
                            oos.close();
                            server.close();
                            break;
                        }
                        case 2: {
                            Product product = SeleccionarProducto(catalogo);

                            if (product != null) {
                                Request request = new Request();
                                request.path = "ObtenerImagenDeProduct";

                                Socket server = new Socket(host, puerto);
                                ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                                oos.writeObject(request);
                                oos.writeObject(product);

                                boolean received = ReceiveFile(server, "Catalogo");

                                if (received) {
                                    System.out.println("Ticket recibido en carpeta Catalogo");
                                } else {
                                    System.out.println("Algo salio mal, revisar logs");
                                }

                                server.close();
                            }

                            break;
                        }
                        case 3: {
                            ticket = new Ticket();
                            break;
                        }
                        case 4: {
                            if (ticket == null) {
                                System.out.println("Ticket vacio, inicialice en opcion 3");
                                break;
                            }

                            Request request = new Request();
                            request.path = "GuardarTicket";

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            oos.writeObject(user);
                            oos.writeObject(ticket);

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
                        case 5: {
                            if (ticket == null) {
                                System.out.println("Ticket vacio, inicialice en opcion 3");
                                break;
                            }

                            Product product = SeleccionarProducto(catalogo);

                            if (product != null) {
                                System.out.println("Cuantos productos: ");

                                int count = Integer.parseInt(br.readLine());
                                Request request = new Request();
                                request.path = "AgregarProductoATicket";

                                Socket server = new Socket(host, puerto);
                                ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                                ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                                oos.writeObject(request);
                                oos.writeObject(ticket);
                                oos.writeObject(product);
                                oos.writeObject(count);

                                System.out.println("Objetos enviados");

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

                                request = new Request();
                                request.path = "ObtenerCatalogo";

                                server = new Socket(host, puerto);
                                ois = new ObjectInputStream(server.getInputStream());
                                oos = new ObjectOutputStream(server.getOutputStream());

                                oos.writeObject(request);
                                Response<List<Product>> response_catalogo = (Response<List<Product>>) ois.readObject();

                                catalogo = response_catalogo.body;
                                System.out.println("Catalogo cargado");

                            }

                            break;
                        }

                        case 6: {
                            if (ticket == null) {
                                System.out.println("Ticket vacio, inicialice en opcion 3");
                                break;
                            }

                            TicketProduct product = SeleccionarProductoDelTicket(ticket);

                            Request request = new Request();
                            request.path = "QuitarProductoATicket";

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

                            request = new Request();
                            request.path = "ObtenerCatalogo";

                            server = new Socket(host, puerto);
                            ois = new ObjectInputStream(server.getInputStream());
                            oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            Response<List<Product>> response_catalogo = (Response<List<Product>>) ois.readObject();

                            catalogo = response_catalogo.body;
                            System.out.println("Catalogo cargado");

                            ois.close();
                            oos.close();
                            server.close();

                            break;
                        }

                        case 7: {
                            if (ticket == null) {
                                System.out.println("Ticket vacio, inicialice en opcion 3");
                                break;
                            }

                            Request request = new Request();
                            request.path = "GenerarTicket";

                            Socket server = new Socket(host, puerto);
                            ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
                            ObjectOutputStream oos = new ObjectOutputStream(server.getOutputStream());

                            oos.writeObject(request);
                            oos.writeObject(ticket);

                            boolean received = ReceiveFile(server, "TicketCliente");

                            if (received) {
                                System.out.println("Ticket recibido en carpeta clientes");
                            } else {
                                System.out.println("Algo salio mal, revisar logs");
                            }

                            server.close();
                            break;
                        }

                        case 8: {
                            if (ticket == null) {
                                System.out.println("Ticket vacio, inicialice en opcion 3");
                                break;
                            } else {
                                if (ticket.GetProducts().isEmpty()) {
                                    System.out.println("sin productos");
                                } else {

                                    for (TicketProduct item : ticket.GetProducts()) {
                                        Product producto = new Product();
                                        producto.Get(item.productId);
                                        System.out.println((ticket.GetProducts().indexOf(item) + 1) + "------" + producto.name);
                                        System.out.println("cantidad: " + item.count);
                                        System.out.println("");
                                    }
                                }
                            }

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

    private static TicketProduct SeleccionarProductoDelTicket(Ticket ticket) throws SQLException, IOException {
        if (ticket == null) {
            System.out.println("ticket vacio");
            return null;
        }

        System.out.println("Seleccione el producto");
        for (TicketProduct item : ticket.GetProducts()) {
            Product producto = new Product();
            producto.Get(item.productId);
            System.out.println((ticket.GetProducts().indexOf(item) + 1) + "------" + producto.name);
            System.out.println("cantidad: " + item.count);
            System.out.println("");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int seleccion = Integer.parseInt(br.readLine()) - 1;

        return ticket.GetProducts().get(seleccion);
    }

    private static boolean ReceiveFile(Socket server, String carpeta) {
        try {
            File f = new File(""); //en raiz de proyecto
            String ruta = f.getAbsolutePath(); // la ruta a donde va a ir todo
            String ruta_archivos = ruta + "\\" + carpeta + "\\";
            System.out.println("ruta:" + ruta_archivos);
            File f2 = new File(ruta_archivos);
            f2.mkdirs();
            f2.setWritable(true);
            DataInputStream dis = new DataInputStream(server.getInputStream());
            String nombre = dis.readUTF();
            long tam = dis.readLong();
            System.out.println("Comienza descarga del archivo " + nombre + " de " + tam + " bytes\n\n");
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(ruta_archivos + nombre));
            long recibidos = 0;
            int l = 0, porcentaje = 0;
            while (recibidos < tam) {
                byte[] b = new byte[1500];
                l = dis.read(b);
                System.out.println("leidos: " + l);
                dos.write(b, 0, l);
                dos.flush();
                recibidos = recibidos + l;
                porcentaje = (int) ((recibidos * 100) / tam);
                System.out.print("\rRecibido el " + porcentaje + " % del archivo");
            }//while
            System.out.println("Archivo recibido..");

            return true;
        } catch (IOException ex) {
            Logger.getLogger(CarritoCompras.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
}
