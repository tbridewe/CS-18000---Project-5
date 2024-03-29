/**
 * Client.java
 * <p>
 * Takes user input from the customer and sends it to a server to be used
 * for data management operations.
 *
 * @version 2023-4-19
 * @author Liam & Sam
 */

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.*;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {

        try {
            // server connection setup
            String host = /*(String) JOptionPane.showInputDialog("Welcome\nPlease enter a host name")*/ "localhost";
            int port = /*Integer.parseInt(JOptionPane.showInputDialog("Please enter a port number"))*/1800;
            // Socket s = new Socket("localhost", 1800);

            Socket socket = new Socket(host, port); // make socket

            PrintWriter writer = new PrintWriter(socket.getOutputStream()); // for sending instructions to server as strings
            ObjectInputStream oi = new ObjectInputStream(socket.getInputStream()); // for receiving information from the server as objects

            // Setup the GUI
            GUI gui = new GUI(writer, oi);
            gui.ShowWelcome();

            JOptionPane.showMessageDialog(null, "Connection Successful!", "Success", JOptionPane.PLAIN_MESSAGE);

            // // loop for debugging
            // Scanner sc = new Scanner(System.in);
            // String send;
            // Object o;
            // while (true) {
            //     send = sc.nextLine();
            //     writer.write(send);
            //     writer.println();
            //     writer.flush();
            //     o = readObject(oi);
            //     System.out.println(o.toString());
            // }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }


    }

    private static Object readObject(ObjectInputStream oi) {
        Object o = null;
        try {
            o = oi.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        }
        return o;
    }
}
