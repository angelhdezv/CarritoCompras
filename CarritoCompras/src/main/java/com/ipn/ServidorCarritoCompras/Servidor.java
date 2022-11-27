/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.ipn.ServidorCarritoCompras;

import com.ipn.models.Product;
import com.ipn.models.Ticket;
import com.ipn.models.TicketProduct;
import com.ipn.models.User;
import com.ipn.utils.Header;
import com.ipn.utils.Request;
import com.ipn.utils.Response;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Angel
 */
public class Servidor {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Ingrese el puerto del servidor");
        int puerto = Integer.parseInt(br.readLine());
        ServerSocket s = new ServerSocket(puerto);
        s.setReuseAddress(true);
        System.out.println("Servidor iniciado esperando por archivos..");
        for (;;) {
            Socket cl = s.accept();
            System.out.println("Cliente conectado desde " + cl.getInetAddress() + ":" + cl.getPort());
            ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
            DataInputStream dis = new DataInputStream(cl.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());

            Request request = (Request) ois.readObject();

            switch (request.path) {
                case "":
                    oos.writeObject(new Response<>(
                            null, "Servidor en Linea v0.1"
                    ));
                    break;
                case "Login":
                    String email = dis.readUTF();
                    String password = dis.readUTF();
                    User user = new User();
                    user.GetByEmail(email);
                    if (user.password.equals(password)) {
                        Header[] headers = {new Header("isValid", true)};
                        oos.writeObject(new Response<>(
                                headers, user
                        ));
                    } else {
                        Header[] headers = {new Header("isValid", false)};
                        oos.writeObject(new Response<User>(
                                headers, null
                        ));
                    }
                    break;
                case "ObtenerCatalogo": {
                    Product product = new Product();
                    oos.writeObject(new Response<>(
                            null, product.All()
                    ));
                    break;
                }
                case "ObtenerImagenDeProduct":
                    Product current_product = (Product) ois.readObject();
                    boolean sended = SendFile(cl, current_product);
                    if (sended) {
                        oos.writeObject(new Response<>(
                                null, "Ok"
                        ));
                    } else {
                        oos.writeObject(new Response<>(
                                null, "Error"
                        ));
                    }
                    break;
                case "GuardarUsuario": {
                    User new_user = (User) ois.readObject();
                    boolean created = new_user.Save();
                    if (created) {
                        oos.writeObject(new Response<>(
                                null, "Ok"
                        ));
                    } else {
                        oos.writeObject(new Response<>(
                                null, "Error"
                        ));
                    }
                    break;
                }
                case "GuardarUsuarioSuperAdmin": {
                    User new_user = (User) ois.readObject();
                    boolean created = new_user.SaveSuperAdmin();
                    if (created) {
                        oos.writeObject(new Response<>(
                                null, "Ok"
                        ));
                    } else {
                        oos.writeObject(new Response<>(
                                null, "Error"
                        ));
                    }
                    break;
                }
                case "GuardarTicket": {
                    User current_user = (User) ois.readObject();
                    Ticket new_ticket = (Ticket) ois.readObject();
                    boolean created = new_ticket.Save(current_user);
                    if (created) {
                        Header[] headers = {new Header("isValid", true)};
                        oos.writeObject(new Response<>(
                                headers, new_ticket
                        ));
                    } else {
                        Header[] headers = {new Header("isValid", false)};
                        oos.writeObject(new Response<Ticket>(
                                headers, null
                        ));
                    }
                    break;
                }
                case "AgregarProductoATicket": {
                    Ticket current_ticket = (Ticket) ois.readObject();
                    Product product = (Product) ois.readObject();
                    int count = ois.readInt();
                    boolean added = current_ticket.AddProduct(product, count);
                    if (added) {
                        oos.writeObject(new Response<>(
                                null, "Ok"
                        ));
                    } else {
                        oos.writeObject(new Response<>(
                                null, "Error"
                        ));
                    }
                    break;
                }
                case "QuitarProductoATicket": {
                    Ticket current_ticket = (Ticket) ois.readObject();
                    TicketProduct item = (TicketProduct) ois.readObject();
                    boolean deleted = current_ticket.DeleteProduct(item);
                    if (deleted) {
                        oos.writeObject(new Response<>(
                                null, "Ok"
                        ));
                    } else {
                        oos.writeObject(new Response<>(
                                null, "Error"
                        ));
                    }
                    break;
                }
                case "AgregarProductoACatalogo": {
                    User current_user = (User) ois.readObject();
                    Product product = (Product) ois.readObject();
                    if (current_user.isAdmin) {
                        boolean created = product.Save();
                        if (created) {
                            oos.writeObject(new Response<>(
                                    null, "Ok"
                            ));
                        } else {
                            oos.writeObject(new Response<>(
                                    null, "Error"
                            ));
                        }
                    }
                    oos.writeObject(new Response<>(
                            null, "Unauthorized"
                    ));
                    break;
                }
                case "EliminarProductoDelCatalogo": {
                    User current_user = (User) ois.readObject();
                    Product product = (Product) ois.readObject();
                    if (current_user.isAdmin) {
                        boolean deleted = product.Delete();
                        if (deleted) {
                            oos.writeObject(new Response<>(
                                    null, "Ok"
                            ));
                        } else {
                            oos.writeObject(new Response<>(
                                    null, "Error"
                            ));
                        }
                    }
                    oos.writeObject(new Response<>(
                            null, "Unauthorized"
                    ));
                    break;
                }
                default:
                    System.err.println("InvalidPath");
                    break;
            }

            cl.close();
        }
    }

    private static boolean SendFile(Socket cl, Product product) {
        try {
            File f = new File(product.imagePath);
            String nombre = f.getName();
            String path = f.getAbsolutePath();
            long tam = f.length();
            //Entrada a la carpeta de donde enviaremos los datos
            System.out.println("Preparandose pare enviar archivo " + path + " de " + tam + " bytes\n\n");
            DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
            DataInputStream dis = new DataInputStream(new FileInputStream(path));
            //Limpieza y Escritura de los Datos.
            dos.writeUTF(nombre);
            dos.flush();
            dos.writeLong(tam);
            dos.flush();
            //Tamaño de los datos a enviar.
            long enviados = 0;
            int l = 0, porcentaje = 0;
            //Enviado de nuestros datos.
            while (enviados < tam) {
                byte[] b = new byte[1500];
                l = dis.read(b);
                System.out.println("enviados: " + l);
                //Es
                dos.write(b, 0, l);
                dos.flush();
                enviados = enviados + l;
                porcentaje = (int) ((enviados * 100) / tam);
                System.out.print("\rEnviado el " + porcentaje + " % del archivo");
            }//while
            //Limpieza de los procesos.
            System.out.println("\nArchivo enviado..");
            dis.close();
            dos.close();

            return true;
        } catch (IOException ex) {
            System.out.println(ex.toString());
            return false;
        }
    }
}
