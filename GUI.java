import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class GUI {
    public void showWelcome() {
        JOptionPane.showMessageDialog(null, "Welcome",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
    }
    public void connectToServer() throws IOException {
        String hostName = JOptionPane.showInputDialog(null, "Enter a host name",
                "Enter a host name", JOptionPane.QUESTION_MESSAGE);
        //port number is 1234
        int portNumber = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter port number",
                "Enter port number", JOptionPane.QUESTION_MESSAGE));
        Socket socket = new Socket(hostName, portNumber);
        JOptionPane.showMessageDialog(null, "Connection established successfully",
                "Connection established successfully", JOptionPane.INFORMATION_MESSAGE);
    }
    public void welcomeMenu() { //after the showWelcome GUI
        String[] options = {"Login", "Create an account", "Quit"};
        String welcomeOption = (String) JOptionPane.showInputDialog(null,
                "Please select an option:", "Welcome Menu", JOptionPane.PLAIN_MESSAGE, null,
                options, null);
    }
    public void loginMenu() { //if login is selected in the welcome menu
        JOptionPane.showMessageDialog(null, "Login Menu",
                "Login Menu", JOptionPane.INFORMATION_MESSAGE);
    }
    public void newAccountMenu() { //if create new account is selected in the welcome menu
        JOptionPane.showMessageDialog(null, "Create New Account",
                "Create New Account", JOptionPane.INFORMATION_MESSAGE);
    }
    public void quit() { //if quit is selected in the welcome menu
        JOptionPane.showMessageDialog(null, "Bye!",
                "Bye!", JOptionPane.INFORMATION_MESSAGE);
    }
    public void invalidWelcomeOption() { //if any other thing is selected in the welcome menu
        JOptionPane.showMessageDialog(null, "Invalid Option!",
                "Invalid Option!", JOptionPane.INFORMATION_MESSAGE);
    }
}
