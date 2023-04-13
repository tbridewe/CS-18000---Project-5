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
    public void enterEmail() { //shown in the login menu
        String email = JOptionPane.showInputDialog(null, "Enter email:",
                "Email", JOptionPane.QUESTION_MESSAGE);
    }
    public void enterPassword() { //shown in the login menu
        String password = JOptionPane.showInputDialog(null, "Enter password:",
                "Password", JOptionPane.QUESTION_MESSAGE);
    }
    public void invalidEmail() { //shown if the email submitted in the login menu is invalid
        JOptionPane.showMessageDialog(null, "Please enter a valid email address!",
                "Please enter a valid email address!", JOptionPane.INFORMATION_MESSAGE);
    }
    public void incorrectCredentials() { //shown in the login menu if the account does not exist
        JOptionPane.showMessageDialog(null, 
                "Incorrect login credentials or account does not exist, please try again.", 
                "Incorrect credentials", JOptionPane.INFORMATION_MESSAGE);
    }
    public void createAccount() { //shown in the login menu if the email is an email but is not associated with an account
        JOptionPane.showMessageDialog(null,
                "No account found with that email! Would you like to make an account, or continue trying to log in?",
                "No account found with that email!", JOptionPane.INFORMATION_MESSAGE);
    }
    public void createAccountOptions() { //the options the user is shown after being shown the createAccount GUI
        //"(1) Create New Account\n(2) Re-attempt Login\n(3) Back"
        String[] createAccountOptions = {"Create New Account", "Re-attempt Login", "Back"};
        String welcomeOption = (String) JOptionPane.showInputDialog(null,
                "Please select an option:", "No account found with that email!", JOptionPane.PLAIN_MESSAGE,
                null,
                createAccountOptions, null);
    }
    public void loginSuccess() { //shown if the login is successful
        JOptionPane.showMessageDialog(null, "Login Successful", "Login Successful", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    //if the user is a buyer
}
