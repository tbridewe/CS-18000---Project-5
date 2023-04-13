import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1800);
        System.out.println("Waiting for the client to connect...");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected!");

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
    }
}
