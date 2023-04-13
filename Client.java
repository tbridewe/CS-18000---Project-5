import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        try {
            String host = (String) JOptionPane.showInputDialog("Welcome\nPlease enter a host name");
            int port = Integer.parseInt(JOptionPane.showInputDialog("Please enter a port number"));

            Socket socket = new Socket(host, port);

            JOptionPane.showMessageDialog(null, "Connection Successful!", "Success", JOptionPane.PLAIN_MESSAGE);

        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "Connection Failed", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }
}
