import java.net.*;
import java.io.*;

// port 1800

public class Server implements Runnable {
    private Socket socket;
    public Server(Socket socket) {
        this.socket = socket;
    }
    
    public void run() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            
        }
    }
        
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1800);
            
            while(true) {
                Socket socket = ss.accept();
                
                Thread toRun = new Thread(new Server(socket));
                
                toRun.start();
            }
        } catch (IOException e) {
            
        }
    }
}
