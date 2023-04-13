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
        String[] options = {"(1) Login", "(2) Create an account", "(3) Quit"};
        int welcomeOption = (int) JOptionPane.showInputDialog(null,
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
        String[] createAccountOptions = {"(1) Create New Account", "(2) Re-attempt Login", "(3) Back"};
        int welcomeOption = (int) JOptionPane.showInputDialog(null,
                "Please select an option:", "No account found with that email!", JOptionPane.PLAIN_MESSAGE,
                null,
                createAccountOptions, null);
    }
    public void loginSuccess() { //shown if the login is successful
        JOptionPane.showMessageDialog(null, "Login Successful", "Login Successful",
                JOptionPane.INFORMATION_MESSAGE);
    }
    //if the user is a buyer
    public void buyerMenu() { //the options that are shown to a buyer
        String[] buyerOptions = {"(1) Choose Item", "(2) Search", "(3) Sort", "(4) View Cart", "(5) Edit Account", "(6) Log Out"};
        String buyerOption = (String) JOptionPane.showInputDialog(null,
                "Please select an option:", "Buyer options", JOptionPane.PLAIN_MESSAGE,
                null, buyerOptions, null);
    }
}

/*
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    static class LoginGui implements ActionListener {
        private static JLabel userLabel;
        private static JTextField userText;
        private static JLabel passwordLabel;
        private static JPasswordField passwordText;
        private static JButton loginButton;
        private static JLabel success;

        public void displayLoginGui() {
            JFrame frame = new JFrame();
            JPanel panel = new JPanel();
            frame.setSize(350, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.add(panel);

            userLabel = new JLabel("User");
            userLabel.setBounds(10,20, 80, 25);
            panel.add(userLabel);

            userText = new JTextField(20);
            userText.setBounds(100, 20, 165, 25);
            panel.add(userText);

            passwordLabel = new JLabel("Password");
            passwordLabel.setBounds(10, 50, 80, 25);
            panel.add(passwordLabel);

            passwordText = new JPasswordField(20);
            passwordText.setBounds(100, 50, 165, 25);
            panel.add(passwordText);

            loginButton = new JButton("Login");
            loginButton.setBounds(10, 80, 80, 25);
            panel.add(loginButton);

            success = new JLabel("");
            success.setBounds(10, 110, 300, 25);
            panel.add(success);
            loginButton.addActionListener(new LoginGui());

            frame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String user = userText.getText();
            String password = new String(passwordText.getPassword());
            System.out.println(user + "," + password);

        }
    }
}
*/
