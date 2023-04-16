import javax.swing.*;
import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GUI {
    static class ShowWelcome {
        public void showWelcome() {
            JOptionPane.showMessageDialog(null, "Welcome",
                    "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class WelcomeMenuGUI implements ActionListener {
        private static JButton login;
        private static JButton createAccount;
        private static JButton quit;
        private static JLabel welcomeOptions;
        public void displayWelcomeMenu() {
            JFrame frameOne = new JFrame();
            JPanel panelOne = new JPanel();
            frameOne.setSize(350, 200);
            frameOne.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameOne.add(panelOne);
            welcomeOptions = new JLabel("Please select an option:");
            welcomeOptions.setBounds(10,20, 80, 25);
            panelOne.add(welcomeOptions);

            login = new JButton("Login");
            login.setBounds(10, 80, 80, 25);
            panelOne.add(login);
            login.addActionListener(new LoginGui());

            createAccount = new JButton("Create an account");
            createAccount.setBounds(10, 80, 80, 25);
            panelOne.add(createAccount);
            createAccount.addActionListener(new LoginGui());

            quit = new JButton("Quit");
            quit.setBounds(10, 80, 80, 25);
            panelOne.add(quit);
            quit.addActionListener(new WelcomeMenuGUI());
            frameOne.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {
                new GUI.LoginGui();
            }
            if (e.getSource() == createAccount) {
                new GUI.NewAccountGUI();
            }
            if (e.getSource() == quit) {

            }
        }
    }
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
        public void actionPerformed(ActionEvent e) {
            String user = userText.getText();
            String password = new String(passwordText.getPassword());
            if (e.getSource() == loginButton)  {
                new GUI.LoginSuccessful();
            }
        }
    }
    static class NewAccountGUI implements ActionListener {
        private static JLabel userLabel;
        private static JTextField userText;
        private static JLabel passwordLabel;
        private static JPasswordField passwordText;
        private static JButton makeNewAccount;
        private static JLabel success;
        public void newAccount() {
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
            makeNewAccount = new JButton("Create new account");
            makeNewAccount.setBounds(10, 80, 80, 25);
            panel.add(makeNewAccount);
            success = new JLabel("");
            success.setBounds(10, 110, 300, 25);
            panel.add(success);
            makeNewAccount.addActionListener(new LoginGui());
            frame.setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
            String user = userText.getText();
            String password = new String(passwordText.getPassword());
            if (e.getSource() == makeNewAccount)  {
                new GUI.LoginSuccessful();
            }
        }
    }
    static class enterValidEmailAddress { //enter a valid email address simple GUI
        public void invalidEmail() { //shown if the email submitted in the login menu is invalid
            JOptionPane.showMessageDialog(null, "Please enter a valid email address!",
                    "Please enter a valid email address!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class IncorrectCredentials { //incorrect credentials simple GUI
        public void incorrectCredentials() { //shown in the login menu if the account does not exist
            JOptionPane.showMessageDialog(null,
                    "Incorrect login credentials or account does not exist, please try again.",
                    "Incorrect credentials", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class CreateOptions { //create_options complex GUI ***should be a complex GUI
        public void createAccountOptions() { //the options the user is shown after being shown the createAccount GUI
            String[] createAccountOptions = {"(1) Create New Account", "(2) Re-attempt Login", "(3) Back"};
            int welcomeOption = (int) JOptionPane.showInputDialog(null,
                    "Please select an option:", "No account found with that email!", JOptionPane.PLAIN_MESSAGE,
                    null,
                    createAccountOptions, null);
        }
    }
    static class LoginSuccessful { //login successful simple GUI
        public void loginSuccess() { //shown if the login is successful
            JOptionPane.showMessageDialog(null, "Login Successful", "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class BuyerMenu { //buyer menu complex GUI //********
        private static JButton chooseItem;
        private static JButton search;
        private static JButton sort;
        private static JButton viewCart;
        private static JButton editAccount;
        private static JButton logOut;
    }
    //search by keyword complex GUI
    //search bar complex GUI
    //select item complex GUI
    static class PurchaseCancelled { //purchase cancelled simple GUI
        public void purchaseWasCancelled() {
            JOptionPane.showMessageDialog(null, "Purchase Cancelled",
                    "The purchase was cancelled.", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class AddedToCart { //added to cart simple GUI
        public void addedToCart() { //%dx %s!\n", quantity, selectedItem.getName() //add info about quantity and selected item

            JOptionPane.showMessageDialog(null, "Added to cart:");
        }
    }
    static class InvalidQuantityException { //invalid quantity exception simple GUI
        public void invalidQuantity() { //if int addingChoices is not equal to 1 or 2
            JOptionPane.showMessageDialog(null, "Invalid quantity!",
                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
        }
    }
    //sorting items complex GUI
    //view cart options complex GUI
    static class CheckoutComplete { //checkout complete simple GUI
        public void checkoutComplete() { //if the buyer chooses to checkout and checks out successfully
            JOptionPane.showMessageDialog(null, "Checkout Complete!",
                    "Checkout complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //export purchase history complex GUI
    static class EnterNameOfFile { //enter name of file simple GUI
        public void fileName() {
            String email = JOptionPane.showInputDialog(null,
                    "Enter the name of the file to save the purchase history to:",
                    "File name", JOptionPane.QUESTION_MESSAGE);
        }
    }
    static class PurchaseHistoryExported { //purchase history successfully exported simple GUI
        public void purchaseHistoryExported() { //message shown if the buyer successfully saves purchase history to file
            JOptionPane.showInputDialog(null,
                    "Purchase history successfully exported to file!",
                    "Purchase history exported", JOptionPane.PLAIN_MESSAGE);
        }
    }
    //remove item complex GUI
    //edit user info complex GUI
    static class NewEmail { //new email simple GUI
        public void newEmail() {
            String newEmail = JOptionPane.showInputDialog(null,
                    "Please enter an new email address for your account",
                    "New Email", JOptionPane.QUESTION_MESSAGE);
        }
    }
    static class ConfirmEmail { //confirm simple GUI
        public void confirm() {
            String[] yesOrNo = {"(1) Yes", "(2) No"};
            int confirmOption = (int) JOptionPane.showInputDialog(null,
                    "Are you sure you would like to edit your email?  Choose yes or no:", "Confirmation",
                    JOptionPane.PLAIN_MESSAGE, null, yesOrNo, null);
        }
    }
    static class NewPassword { //new password simple GUI
        public void newPassword() {
            String newPassword = JOptionPane.showInputDialog(null,
                    "Please enter an new password for your account",
                    "New Password", JOptionPane.QUESTION_MESSAGE);
        }
    }
    static class ConfirmPassword { //confirm simple GUI
        public void confirmPassword() {
            String[] yesOrNo = {"(1) Yes", "(2) No"};
            int confirmPasswordOption = (int) JOptionPane.showInputDialog(null,
                    "Are you sure you would like to edit your password?  Choose yes or no:", "Confirmation",
                    JOptionPane.PLAIN_MESSAGE, null, yesOrNo, null);
        }
    }
    static class Logout { //logout simple GUI
        public void goodbye() { //if the buyer chooses the log out option
            JOptionPane.showMessageDialog(null, "Goodbye!",
                    "Farewell", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //seller menu complex GUI
    //listings menu complex GUI
    //add options complex GUI
    //add item complex GUI
    //add from CSV complex GUI
    //choose item to edit complex GUI
    //remove complex GUI and print the cart
    //view statistics complex GUI
    //specific stats complex GUI
    static class HaveNoStores { //have no stores simple GUI
        public void haveNoStores() {
            JOptionPane.showMessageDialog(null, "You have no stores!", "No stores",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    //edit options complex GUI
    static class SellerNewEmail { //new email simple GUI
        public void newEmail() {
            String newEmail = JOptionPane.showInputDialog(null,
                    "Please enter an new email address for your account",
                    "New Email", JOptionPane.QUESTION_MESSAGE);
        }
    }
    static class SellerConfirmEmail { //confirm simple GUI
        public void confirm() {
            String[] yesOrNo = {"(1) Yes", "(2) No"};
            int confirmOption = (int) JOptionPane.showInputDialog(null,
                    "Are you sure you would like to edit your email?  Choose yes or no:", "Confirmation",
                    JOptionPane.PLAIN_MESSAGE, null, yesOrNo, null);
        }
    }
    static class SellerNewPassword { //new password simple GUI
        public void newPassword() {
            String newPassword = JOptionPane.showInputDialog(null,
                    "Please enter an new password for your account",
                    "New Password", JOptionPane.QUESTION_MESSAGE);
        }
    }
    static class SellerConfirmPassword { //confirm simple GUI
        public void confirmPassword() {
            String[] yesOrNo = {"(1) Yes", "(2) No"};
            int confirmPasswordOption = (int) JOptionPane.showInputDialog(null,
                    "Are you sure you would like to edit your password?  Choose yes or no:", "Confirmation",
                    JOptionPane.PLAIN_MESSAGE, null, yesOrNo, null);
        }
    }
    static class LogoutSeller { //logout simple GUI
        public void goodbye() { //if the buyer chooses the log out option
            JOptionPane.showMessageDialog(null, "Goodbye!",
                    "Farewell", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
