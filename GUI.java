//These are all of the complex GUIs; I will add actions to the Action Listeners

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class GUI {
    static class ShowWelcome implements Runnable {
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "Welcome",
                    "Welcome", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class WelcomeMenuGUI implements ActionListener, Runnable {
        private static JButton login;
        private static JButton createAccount;
        private static JButton quit;
        private static JLabel welcomeOptions;
        public void run() {
            JFrame frameOne = new JFrame();
            JPanel panelOne = new JPanel();
            Container welcomeContent = frameOne.getContentPane();
            frameOne.setSize(350, 200);
            frameOne.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameOne.add(panelOne);
            welcomeOptions = new JLabel("Please select an option:");
            welcomeOptions.setBounds(10,20, 80, 25);
            panelOne.add(welcomeOptions);

            login = new JButton("Login");
            login.setBounds(10, 80, 80, 25);
            panelOne.add(login);
            login.addActionListener(new WelcomeMenuGUI());

            createAccount = new JButton("Create an account");
            createAccount.setBounds(10, 80, 80, 25);
            panelOne.add(createAccount);
            createAccount.addActionListener(new WelcomeMenuGUI());

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
    static class LoginGui implements ActionListener, Runnable {
        private static JLabel userLabel;
        private static JTextField userText;
        private static JLabel passwordLabel;
        private static JPasswordField passwordText;
        private static JButton loginButton;
        private static JLabel success;
        @Override
        public void run() {
            JFrame loginFrame = new JFrame();
            JPanel loginPanel = new JPanel();
            loginFrame.setSize(350, 200);
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.add(loginPanel);

            userLabel = new JLabel("User");
            userLabel.setBounds(10,20, 80, 25);
            loginPanel.add(userLabel);

            userText = new JTextField(20);
            userText.setBounds(100, 20, 165, 25);
            loginPanel.add(userText);

            passwordLabel = new JLabel("Password");
            passwordLabel.setBounds(10, 50, 80, 25);
            loginPanel.add(passwordLabel);

            passwordText = new JPasswordField(20);
            passwordText.setBounds(100, 50, 165, 25);
            loginPanel.add(passwordText);

            loginButton = new JButton("Login");
            loginButton.setBounds(10, 80, 80, 25);
            loginPanel.add(loginButton);
            loginButton.addActionListener(new LoginGui());

            success = new JLabel("");
            success.setBounds(10, 110, 300, 25);
            loginPanel.add(success);
            loginFrame.setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
            String user = userText.getText();
            String password = new String(passwordText.getPassword());
            if (e.getSource() == loginButton)  {
                new GUI.LoginSuccessful();
            }
        }
    }
    static class NewAccountGUI implements ActionListener, Runnable {
        private static JLabel userLabel;
        private static JTextField userText;
        private static JLabel passwordLabel;
        private static JPasswordField passwordText;
        private static JButton makeNewAccount;
        private static JLabel success;
        @Override
        public void run() {
            JFrame newAccountFrame = new JFrame();
            JPanel newAccountPanel = new JPanel();
            newAccountFrame.setSize(350, 200);
            newAccountFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newAccountFrame.add(newAccountPanel);
            userLabel = new JLabel("User");
            userLabel.setBounds(10,20, 80, 25);
            newAccountPanel.add(userLabel);
            userText = new JTextField(20);
            userText.setBounds(100, 20, 165, 25);
            newAccountPanel.add(userText);
            passwordLabel = new JLabel("Password");
            passwordLabel.setBounds(10, 50, 80, 25);
            newAccountPanel.add(passwordLabel);
            passwordText = new JPasswordField(20);
            passwordText.setBounds(100, 50, 165, 25);
            newAccountPanel.add(passwordText);
            makeNewAccount = new JButton("Create new account");
            makeNewAccount.setBounds(10, 80, 80, 25);
            newAccountPanel.add(makeNewAccount);
            makeNewAccount.addActionListener(new NewAccountGUI());
            success = new JLabel("");
            success.setBounds(10, 110, 300, 25);
            newAccountPanel.add(success);
            newAccountFrame.setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
            String user = userText.getText();
            String password = new String(passwordText.getPassword());
            if (e.getSource() == makeNewAccount)  {
                new GUI.LoginSuccessful();
            }
        }
    }
    //shown if the email submitted in the login menu is invalid
    static class enterValidEmailAddress implements Runnable { //enter a valid email address simple GUI
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address!",
                    "Please enter a valid email address!", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class IncorrectCredentials implements Runnable { //incorrect credentials simple GUI
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null,
                    "Incorrect login credentials or account does not exist, please try again.",
                    "Incorrect credentials", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class CreateOptions implements ActionListener, Runnable {
        private JLabel noAccountFound;
        private JLabel selectOption;
        private JButton createNewAccount;
        private JButton reAttemptLogin;
        private JButton back;
        @Override
        public void run() {
            JFrame createFrame = new JFrame();
            JPanel createPanel = new JPanel();

            noAccountFound = new JLabel("No account was found with that email!");
            noAccountFound.setBounds(10, 110, 300, 25);
            createPanel.add(noAccountFound);

            selectOption = new JLabel("Select an option:");
            selectOption.setBounds(10, 110, 300, 25);
            createPanel.add(selectOption);

            createNewAccount = new JButton("Create new account");
            createNewAccount.setBounds(10, 80, 80, 25);
            createPanel.add(createNewAccount);
            createNewAccount.addActionListener(new LoginGui());

            reAttemptLogin = new JButton("Re-attempt login");
            reAttemptLogin.setBounds(10, 80, 80, 25);
            createPanel.add(reAttemptLogin);
            reAttemptLogin.addActionListener(new LoginGui());

            back = new JButton("Back");
            back.setBounds(10, 80, 80, 25);
            createPanel.add(back);
            back.addActionListener(new LoginGui());

            createFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class LoginSuccessful implements Runnable { //login successful simple GUI
        public void run() { //shown if the login is successful
            JOptionPane.showMessageDialog(null, "Login Successful", "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class BuyerMenuGui implements ActionListener, Runnable { //buyer menu complex GUI
        private static JButton chooseItem;
        private static JButton search;
        private static JButton sort;
        private static JButton viewCart;
        private static JButton editAccount;
        private static JButton logOut;
        private static JLabel buyerOptions;
        @Override
        public void run() {
            JFrame buyerMenuFrame = new JFrame();
            JPanel buyerMenuPanel = new JPanel();
            buyerMenuFrame.setSize(350, 200);
            buyerMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            buyerMenuFrame.add(buyerMenuPanel);
            buyerOptions = new JLabel("Please select an option:");
            buyerOptions.setBounds(10,20, 80, 25);
            buyerMenuPanel.add(buyerOptions);

            chooseItem = new JButton("Choose an item");
            chooseItem.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(chooseItem);
            chooseItem.addActionListener(new BuyerMenuGui());

            search = new JButton("Search");
            search.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(search);
            search.addActionListener(new BuyerMenuGui());

            sort = new JButton("Sort");
            sort.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(sort);
            sort.addActionListener(new BuyerMenuGui());

            viewCart = new JButton("View cart");
            viewCart.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(viewCart);
            viewCart.addActionListener(new BuyerMenuGui());

            editAccount = new JButton("Edit account");
            editAccount.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(editAccount);
            editAccount.addActionListener(new BuyerMenuGui());

            logOut = new JButton("Log out");
            logOut.setBounds(10, 80, 80, 25);
            buyerMenuPanel.add(logOut);
            logOut.addActionListener(new BuyerMenuGui());

            buyerMenuFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class SearchByKeyword implements ActionListener, Runnable {
        private JTextField searchBar;
        private JLabel searchBarLabel;
        @Override
        public void run() {
            JFrame keywordFrame = new JFrame();
            JPanel keywordPanel = new JPanel();
            keywordFrame.setSize(350, 200);
            keywordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            keywordFrame.add(keywordPanel);
            searchBarLabel = new JLabel("Enter a keyword:");
            searchBarLabel.setBounds(10,20, 80, 25);
            keywordPanel.add(searchBarLabel);

            searchBar = new JTextField(20);
            searchBar.setBounds(100, 20, 165, 25);
            keywordPanel.add(searchBar);

            keywordFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class SearchBar implements ActionListener, Runnable { //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel search;
        private static JTextField searchBar;
        @Override
        public void run() {
            JFrame enterFrame = new JFrame();
            JPanel enterPanel = new JPanel();
            enterFrame.setSize(350, 200);
            enterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            enterFrame.add(enterPanel);

            search = new JLabel("Search:");
            search.setBounds(10,20, 80, 25);
            enterPanel.add(search);

            searchBar = new JTextField(20);
            searchBar.setBounds(100, 20, 165, 25);
            enterPanel.add(searchBar);
            
            enterFrame.setVisible(true);
        }
    }
    static class SelectItem implements ActionListener, Runnable { 
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel select;
        private static JTextField item;
        private static JLabel number;
        private static JTextField quantity;
        @Override
        public void run() {
            JFrame enterFrame = new JFrame();
            JPanel enterPanel = new JPanel();
            enterFrame.setSize(350, 200);
            enterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            enterFrame.add(enterPanel);

            select = new JLabel("Please select the item you wish to purchase:");
            select.setBounds(10,20, 80, 25);
            enterPanel.add(select);

            item = new JTextField(20);
            item.setBounds(100, 20, 165, 25);
            enterPanel.add(item);

            number = new JLabel("Please enter how many you would like to buy:");
            number.setBounds(10,20, 80, 25);
            enterPanel.add(number);

            quantity = new JTextField(20);
            quantity.setBounds(100, 20, 165, 25);
            enterPanel.add(quantity);

            enterFrame.setVisible(true);
        }
    }
    static class cancelPurchase implements ActionListener, Runnable {

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
        private static JLabel cancelQuestion;
        private static JButton cancel;
        private static JButton approve;
        @Override
        public void run() {
            JFrame sortFrame = new JFrame();
            JPanel sortPanel = new JPanel();
            sortFrame.setSize(350, 200);
            sortFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sortFrame.add(sortPanel);

            cancelQuestion = new JLabel("Do you want to cancel the purchase?");
            cancelQuestion.setBounds(10,20, 80, 25);
            sortPanel.add(cancelQuestion);

            cancel = new JButton("Cancel");
            cancel.setBounds(10, 80, 80, 25);
            sortPanel.add(cancel);
            cancel.addActionListener(new cancelPurchase());

            approve = new JButton("Don't cancel");
            approve.setBounds(10, 80, 80, 25);
            sortPanel.add(approve);
            approve.addActionListener(new cancelPurchase());
            
            sortFrame.setVisible(true);
        }
    }
    static class PurchaseCancelled implements Runnable { //purchase cancelled simple GUI
        public void run() {
            JOptionPane.showMessageDialog(null, "Purchase Cancelled",
                    "The purchase was cancelled.", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class AddedToCart implements Runnable { //added to cart simple GUI
        public void run() { //%dx %s!\n", quantity, selectedItem.getName() //add info about quantity and selected item

            JOptionPane.showMessageDialog(null, "Added to cart");
        }
    }
    static class InvalidQuantityException implements Runnable { //invalid quantity exception simple GUI
        public void run() { //if int addingChoices is not equal to 1 or 2
            JOptionPane.showMessageDialog(null, "Invalid quantity!",
                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
        }
    }
    static class SortingItems implements ActionListener, Runnable {
        private JLabel priceOrQuantity;
        private JButton price;
        private JButton quantity;
        private JLabel ascendingOrDescending;
        private JButton ascending;
        private JButton descending;
        @Override
        public void run() {
            JFrame sortFrame = new JFrame();
            JPanel sortPanel = new JPanel();
            sortFrame.setSize(350, 200);
            sortFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sortFrame.add(sortPanel);

            priceOrQuantity = new JLabel("Do you want to sort by price or quantity?");
            priceOrQuantity.setBounds(10,20, 80, 25);
            sortPanel.add(priceOrQuantity);

            price = new JButton("Price");
            price.setBounds(10, 80, 80, 25);
            sortPanel.add(price);
            price.addActionListener(new SortingItems());

            quantity = new JButton("Quantity");
            quantity.setBounds(10, 80, 80, 25);
            sortPanel.add(quantity);
            quantity.addActionListener(new SortingItems());

            ascendingOrDescending = new JLabel("Do you want to sort ascending or descending?");
            ascendingOrDescending.setBounds(10,20, 80, 25);
            sortPanel.add(ascendingOrDescending);

            ascending = new JButton("Ascending");
            ascending.setBounds(10, 80, 80, 25);
            sortPanel.add(ascending);
            ascending.addActionListener(new SortingItems());

            descending = new JButton("Descending");
            descending.setBounds(10, 80, 80, 25);
            sortPanel.add(descending);
            descending.addActionListener(new SortingItems());

            sortFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class ViewCartOptions implements ActionListener, Runnable { //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
        private static JLabel choose;
        private static JButton checkout;
        private static JButton view;
        private static JButton remove;
        private static JButton back;
        @Override
        public void run() {
            JFrame cartFrame = new JFrame();
            JPanel cartPanel = new JPanel();
            cartFrame.setSize(350, 200);
            cartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            cartFrame.add(cartPanel);

            choose = new JLabel("Choose an option:");
            choose.setBounds(10,20, 80, 25);
            cartPanel.add(checkout);

            checkout = new JButton("Checkout");
            checkout.setBounds(10, 80, 80, 25);
            cartFrame.add(checkout);
            checkout.addActionListener(new ViewCartOptions());

            view = new JButton("View Purchase History");
            view.setBounds(10, 80, 80, 25);
            cartFrame.add(view);
            view.addActionListener(new ViewCartOptions());

            remove = new JButton("Remove Item");
            remove.setBounds(10, 80, 80, 25);
            cartFrame.add(remove);
            remove.addActionListener(new ViewCartOptions());

            back = new JButton("Back");
            back.setBounds(10, 80, 80, 25);
            cartFrame.add(back);
            back.addActionListener(new ViewCartOptions());
            
            cartFrame.setVisible(true);
        }
    }
    static class CheckoutComplete implements Runnable { //checkout complete simple GUI
        //if the buyer chooses to checkout and checks out successfully
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "Checkout Complete!",
                    "Checkout complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class ExportPurchaseHistory implements ActionListener, Runnable { 
        private static JLabel export;
        private static JButton yes;
        private static JButton no;
        @Override
        public void actionPerformed(ActionEvent e) {
            
        }

        @Override
        public void run() {
            JFrame exportFrame = new JFrame();
            JPanel exportPanel = new JPanel();
            exportFrame.setSize(350, 200);
            exportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            exportFrame.add(exportPanel);

            export = new JLabel("Would you like to export purchase history?");
            export.setBounds(10,20, 80, 25);
            exportPanel.add(export);

            yes = new JButton("Yes");
            yes.setBounds(10, 80, 80, 25);
            exportFrame.add(yes);
            yes.addActionListener(new ExportPurchaseHistory());

            no = new JButton("No");
            no.setBounds(10, 80, 80, 25);
            exportFrame.add(no);
            no.addActionListener(new ExportPurchaseHistory());
            
            exportFrame.setVisible(true);
        }
    }
    static class EnterNameOfFile implements Runnable { //enter name of file simple GUI
        private static JLabel nameFile;
        private static JTextField file;
        @Override
        public void run() {
            JFrame enterFrame = new JFrame();
            JPanel enterPanel = new JPanel();
            enterFrame.setSize(350, 200);
            enterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            enterFrame.add(enterPanel);

            nameFile = new JLabel("Enter the name of the file to save the purchase history to:");
            nameFile.setBounds(10,20, 80, 25);
            enterPanel.add(nameFile);

            file = new JTextField(20);
            file.setBounds(100, 20, 165, 25);
            enterPanel.add(file);
            
            enterFrame.setVisible(true);
        }
    }
    static class PurchaseHistoryExported implements Runnable { //purchase history successfully exported simple GUI
        //message shown if the buyer successfully saves purchase history to file

        @Override
        public void run() {
            JOptionPane.showInputDialog(null,
                    "Purchase history successfully exported to file!",
                    "Purchase history exported", JOptionPane.PLAIN_MESSAGE);
        }
    }
    static class RemoveItem implements ActionListener, Runnable { 
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel select;
        private static JTextField item;
        private static JLabel number;
        private static JTextField quantity;
        @Override
        public void run() {
            JFrame removeFrame = new JFrame();
            JPanel removePanel = new JPanel();
            removeFrame.setSize(350, 200);
            removeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            removeFrame.add(removePanel);

            select = new JLabel("Enter an item to remove from cart:");
            select.setBounds(10,20, 80, 25);
            removePanel.add(select);

            item = new JTextField(20);
            item.setBounds(100, 20, 165, 25);
            removePanel.add(item);

            number = new JLabel("Please enter how many you would like to remove:");
            number.setBounds(10,20, 80, 25);
            removePanel.add(number);

            quantity = new JTextField(20);
            quantity.setBounds(100, 20, 165, 25);
            removePanel.add(quantity);

            removeFrame.setVisible(true);
        }
    }
    static class EditUserInfo implements ActionListener, Runnable { 
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel choose;
        private static JButton editEmail;
        private static JButton editPassword;
        private static JButton delete;
        private static JButton back;
        @Override
        public void run() {
            JFrame editUserFrame = new JFrame();
            JPanel editUserPanel = new JPanel();
            editUserFrame.setSize(350, 200);
            editUserFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            editUserFrame.add(editUserPanel);

            choose = new JLabel("Choose an option:");
            choose.setBounds(10,20, 80, 25);
            editUserPanel.add(choose);

            editEmail = new JButton("Edit Account Email");
            editEmail.setBounds(10, 80, 80, 25);
            editUserFrame.add(editEmail);
            editEmail.addActionListener(new EditUserInfo());

            editPassword = new JButton("Edit Account Password");
            editPassword.setBounds(10, 80, 80, 25);
            editUserFrame.add(editPassword);
            editPassword.addActionListener(new EditUserInfo());

            delete = new JButton("Delete Account");
            delete.setBounds(10, 80, 80, 25);
            editUserFrame.add(delete);
            delete.addActionListener(new EditUserInfo());

            back = new JButton("Back");
            back.setBounds(10, 80, 80, 25);
            editUserFrame.add(back);
            back.addActionListener(new EditUserInfo());
            
            editUserFrame.setVisible(true);
        }
    }
    static class NewEmail implements ActionListener, Runnable { //new email simple GUI
        private static JLabel enterEmail;
        private static JTextField emailText;

        @Override
        public void run() {
            JFrame newCustomerEmailFrame = new JFrame();
            JPanel newCustomerEmailPanel = new JPanel();
            newCustomerEmailFrame.setSize(350, 200);
            newCustomerEmailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newCustomerEmailFrame.add(newCustomerEmailPanel);

            enterEmail = new JLabel("Please enter a new email address for your account:");
            enterEmail.setBounds(10,20, 80, 25);
            newCustomerEmailPanel.add(enterEmail);

            emailText = new JTextField(20);
            emailText.setBounds(100, 20, 165, 25);
            newCustomerEmailPanel.add(emailText);

            newCustomerEmailFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    static class ConfirmEmail implements ActionListener, Runnable { //confirm simple GUI
        private static JLabel editEmail;
        private static JButton yes;
        private static JButton no;
        @Override
        public void run() {
            JFrame confirmEmailFrame = new JFrame();
            JPanel confirmEmailPanel = new JPanel();
            confirmEmailFrame.setSize(350, 200);
            confirmEmailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            confirmEmailFrame.add(confirmEmailPanel);

            editEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
            editEmail.setBounds(10,20, 80, 25);
            confirmEmailPanel.add(editEmail);

            yes = new JButton("Yes");
            yes.setBounds(10, 80, 80, 25);
            confirmEmailFrame.add(yes);
            yes.addActionListener(new ConfirmEmail());

            no = new JButton("No");
            no.setBounds(10, 80, 80, 25);
            confirmEmailPanel.add(no);
            no.addActionListener(new ConfirmEmail());

            confirmEmailFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class NewPassword implements ActionListener, Runnable { 
        private static JLabel enterPassword;
        private static JTextField passwordText;

        @Override
        public void run() {
            JFrame newPasswordFrame = new JFrame();
            JPanel newPasswordPanel = new JPanel();
            newPasswordFrame.setSize(350, 200);
            newPasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newPasswordFrame.add(newPasswordPanel);

            enterPassword = new JLabel("Please enter a new password for your account:");
            enterPassword.setBounds(10,20, 80, 25);
            newPasswordPanel.add(enterPassword);

            passwordText = new JTextField(20);
            passwordText.setBounds(100, 20, 165, 25);
            newPasswordPanel.add(passwordText);

            newPasswordFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class ConfirmPassword implements ActionListener, Runnable { //confirm simple GUI //***make complex GUI
        private static JLabel areYouSure;
        private static JButton yes;
        private static JButton no;
        @Override
        public void run() {
            JFrame confirmCustomerPasswordFrame = new JFrame();
            JPanel confirmPasswordPanel = new JPanel();
            confirmCustomerPasswordFrame.setSize(350, 200);
            confirmCustomerPasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            confirmCustomerPasswordFrame.add(confirmPasswordPanel);

            areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
            areYouSure.setBounds(10,20, 80, 25);
            confirmPasswordPanel.add(areYouSure);

            yes = new JButton("Yes");
            yes.setBounds(10, 80, 80, 25);
            confirmPasswordPanel.add(yes);
            yes.addActionListener(new ConfirmPassword());

            no = new JButton("No");
            no.setBounds(10, 80, 80, 25);
            confirmPasswordPanel.add(no);
            no.addActionListener(new ConfirmPassword());

            confirmCustomerPasswordFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class Logout implements Runnable { //logout simple GUI
        //if the buyer chooses the log out option
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "Goodbye!",
                    "Farewell", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class SellerMenu implements ActionListener, Runnable {  //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel choose;
        private static JButton viewListings;
        private static JButton viewStatistics;
        private static JButton editAccount;
        private static JButton logOut;
        @Override
        public void run() {
            JFrame sellerFrame = new JFrame();
            JPanel sellerPanel = new JPanel();
            sellerFrame.setSize(350, 200);
            sellerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sellerFrame.add(sellerPanel);

            choose = new JLabel("Chose an option:");
            choose.setBounds(10,20, 80, 25);
            sellerPanel.add(choose);

            viewListings = new JButton("View listings");
            viewListings.setBounds(10, 80, 80, 25);
            sellerPanel.add(viewListings);
            viewListings.addActionListener(new SellerMenu());

            viewStatistics = new JButton("View statistics");
            viewStatistics.setBounds(10, 80, 80, 25);
            sellerPanel.add(viewStatistics);
            viewStatistics.addActionListener(new SellerMenu());

            editAccount = new JButton("Edit account");
            editAccount.setBounds(10, 80, 80, 25);
            sellerPanel.add(editAccount);
            editAccount.addActionListener(new SellerMenu());

            logOut = new JButton("Log out");
            logOut.setBounds(10, 80, 80, 25);
            sellerPanel.add(logOut);
            logOut.addActionListener(new SellerMenu());

            sellerFrame.setVisible(true);
        }
    }
    static class ListingsMenu implements ActionListener, Runnable {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel choose;
        private static JButton add;
        private static JButton edit;
        private static JButton delete;
        private static JButton back;
        @Override
        public void run() {
            JFrame listingsFrame = new JFrame();
            JPanel listingsPanel = new JPanel();
            listingsFrame.setSize(350, 200);
            listingsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            listingsFrame.add(listingsPanel);

            choose = new JLabel("Chose an option:");
            choose.setBounds(10,20, 80, 25);
            listingsPanel.add(choose);

            add = new JButton("Add");
            add.setBounds(10, 80, 80, 25);
            listingsPanel.add(add);
            add.addActionListener(new ListingsMenu());

            edit = new JButton("Edit");
            edit.setBounds(10, 80, 80, 25);
            listingsPanel.add(edit);
            edit.addActionListener(new ListingsMenu());

            delete = new JButton("Delete");
            delete.setBounds(10, 80, 80, 25);
            listingsPanel.add(delete);
            delete.addActionListener(new ListingsMenu());

            back = new JButton("Back");
            back.setBounds(10, 80, 80, 25);
            listingsPanel.add(back);
            back.addActionListener(new ListingsMenu());

            listingsFrame.setVisible(true);
        }
    }
    static class AddOptions implements ActionListener, Runnable { //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JButton add;
        private static JButton csv;
        @Override
        public void run() {
            JFrame addOptionsFrame = new JFrame();
            JPanel addOptionsPanel = new JPanel();
            addOptionsFrame.setSize(350, 200);
            addOptionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addOptionsFrame.add(addOptionsPanel);

            add = new JButton("Add item");
            add.setBounds(10, 80, 80, 25);
            addOptionsPanel.add(add);
            add.addActionListener(new AddOptions());

            csv = new JButton("Add from CSV");
            csv.setBounds(10, 80, 80, 25);
            addOptionsPanel.add(csv);
            csv.addActionListener(new AddOptions());

            addOptionsFrame.setVisible(true);
        }
    }
    static class AddItem implements ActionListener, Runnable { //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel enterName;
        private static JTextField name;
        private static JLabel enterStore;
        private static JTextField store;
        private static JLabel enterDescription;
        private static JTextField description;
        private static JLabel enterQuantity;
        private static JTextField quantity;
        private static JLabel enterPrice;
        private static JTextField price;
        @Override
        public void run() {
            JFrame addFrame = new JFrame();
            JPanel addPanel = new JPanel();
            addFrame.setSize(350, 200);
            addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            addFrame.add(addPanel);

            enterName = new JLabel("Enter name:");
            enterName.setBounds(10,20, 80, 25);
            addPanel.add(enterName);

            name = new JTextField(20);
            name.setBounds(100, 20, 165, 25);
            addPanel.add(name);

            enterStore = new JLabel("Enter store:");
            enterStore.setBounds(10,20, 80, 25);
            addPanel.add(enterStore);

            store = new JTextField(20);
            store.setBounds(100, 20, 165, 25);
            addPanel.add(store);

            enterDescription = new JLabel("Enter description:");
            enterDescription.setBounds(10,20, 80, 25);
            addPanel.add(enterDescription);

            description = new JTextField(20);
            description.setBounds(100, 20, 165, 25);
            addPanel.add(description);

            enterQuantity = new JLabel("Enter quantity:");
            enterQuantity.setBounds(10,20, 80, 25);
            addPanel.add(enterQuantity);

            quantity = new JTextField(20);
            quantity.setBounds(100, 20, 165, 25);
            addPanel.add(quantity);

            enterPrice = new JLabel("Enter price:");
            enterPrice.setBounds(10,20, 80, 25);
            addPanel.add(enterPrice);

            price = new JTextField(20);
            price.setBounds(100, 20, 165, 25);
            addPanel.add(price);

            addFrame.setVisible(true);
        }
    }
    static class AddFromCSV implements ActionListener, Runnable { //***make complex GUI
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel enterFileName;
        private static JTextField fileName;
        @Override
        public void run() {
            JFrame csvFrame = new JFrame();
            JPanel csvPanel = new JPanel();
            csvFrame.setSize(350, 200);
            csvFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            csvFrame.add(csvPanel);

            enterFileName = new JLabel("Enter file name:");
            enterFileName.setBounds(10,20, 80, 25);
            csvPanel.add(enterFileName);

            fileName = new JTextField(20);
            fileName.setBounds(100, 20, 165, 25);
            csvPanel.add(fileName);

            csvFrame.setVisible(true);
        }
    }
    static class ChooseItemToEdit implements ActionListener, Runnable {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel chooseItem;
        private static JTextField item;
        private static JLabel options;
        private static JButton name;
        private static JButton store;
        private static JButton description;
        private static JButton quantity;
        private static JButton price;
        private static JLabel change;
        private static JTextField theChange;
        @Override
        public void run() {
            JFrame editFrame = new JFrame();
            JPanel editPanel = new JPanel();
            editFrame.setSize(350, 200);
            editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            editFrame.add(editPanel);

            chooseItem = new JLabel("Choose an item to edit:");
            chooseItem.setBounds(10,20, 80, 25);
            editPanel.add(chooseItem);

            item = new JTextField(20);
            item.setBounds(100, 20, 165, 25);
            editPanel.add(item);

            options = new JLabel("What would you like to change?");
            options.setBounds(10, 20, 80, 25);
            editPanel.add(options);

            name = new JButton("Name");
            name.setBounds(10, 80, 80, 25);
            editPanel.add(name);
            name.addActionListener(new ChooseItemToEdit());

            store = new JButton("Store");
            store.setBounds(10, 80, 80, 25);
            editPanel.add(store);
            store.addActionListener(new ChooseItemToEdit());

            description = new JButton("Description");
            description.setBounds(10, 80, 80, 25);
            editPanel.add(description);
            description.addActionListener(new ChooseItemToEdit());

            quantity = new JButton("Quantity");
            quantity.setBounds(10, 80, 80, 25);
            editPanel.add(quantity);
            quantity.addActionListener(new ChooseItemToEdit());

            price = new JButton("Price");
            price.setBounds(10, 80, 80, 25);
            editPanel.add(price);
            price.addActionListener(new ChooseItemToEdit());

            change = new JLabel("What would you like to change it to?");
            change.setBounds(10,20, 80, 25);
            editPanel.add(change);

            theChange = new JTextField(20);
            theChange.setBounds(100, 20, 165, 25);
            editPanel.add(theChange);

            editFrame.setVisible(true);
        }
    }
    static class Remove implements ActionListener, Runnable { //(also need to print the cart)
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel chooseItem;
        private static JTextField item;
        @Override
        public void run() {
            JFrame removeFrame = new JFrame();
            JPanel removePanel = new JPanel();
            removeFrame.setSize(350, 200);
            removeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            removeFrame.add(removePanel);

            chooseItem = new JLabel("Choose an item to remove:");
            chooseItem.setBounds(10,20, 80, 25);
            removePanel.add(chooseItem);

            item = new JTextField(20);
            item.setBounds(100, 20, 165, 25);
            removePanel.add(item);

            removeFrame.setVisible(true);
        }
    }
    static class ViewStatistics implements ActionListener, Runnable {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel choose;
        private static JButton allStats;
        private static JButton specificStats;
        private static JButton back;
        @Override
        public void run() {
            JFrame statisticsFrame = new JFrame();
            JPanel statisticsPanel = new JPanel();
            statisticsFrame.setSize(350, 200);
            statisticsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            statisticsFrame.add(statisticsPanel);

            choose = new JLabel("Choose an option:");
            choose.setBounds(10,20, 80, 25);
            statisticsPanel.add(choose);

            allStats = new JButton("View all statistics");
            allStats.setBounds(10, 80, 80, 25);
            statisticsPanel.add(allStats);
            allStats.addActionListener(new ViewStatistics());

            specificStats = new JButton("View specific statistics");
            specificStats.setBounds(10, 80, 80, 25);
            statisticsPanel.add(specificStats);
            specificStats.addActionListener(new ViewStatistics());

            back = new JButton("Back");
            back.setBounds(10, 80, 80, 25);
            statisticsPanel.add(back);
            back.addActionListener(new ViewStatistics());

            statisticsFrame.setVisible(true);
        }
    }
    static class SpecificStats implements ActionListener, Runnable {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel byPriceOrQuantity;
        private static JButton price;
        private JButton quantity;
        private static JLabel ascendingOrDescending;
        private static JButton ascending;
        private static JButton descending;
        @Override
        public void run() {
            JFrame specificFrame = new JFrame();
            JPanel specificPanel = new JPanel();
            specificFrame.setSize(350, 200);
            specificFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            specificFrame.add(specificPanel);

            byPriceOrQuantity = new JLabel("Sort specific statistics by price or quantity:");
            byPriceOrQuantity.setBounds(10,20, 80, 25);
            specificPanel.add(byPriceOrQuantity);

            price = new JButton("Price");
            price.setBounds(10, 80, 80, 25);
            specificPanel.add(price);
            price.addActionListener(new SpecificStats());

            quantity = new JButton("Price");
            quantity.setBounds(10, 80, 80, 25);
            specificPanel.add(quantity);
            quantity.addActionListener(new SpecificStats());

            ascendingOrDescending = new JLabel("Sort specific statistics ascending or descending:");
            ascendingOrDescending.setBounds(10,20, 80, 25);
            specificPanel.add(ascendingOrDescending);

            ascending = new JButton("Ascending");
            ascending.setBounds(10, 80, 80, 25);
            specificPanel.add(ascending);
            ascending.addActionListener(new SpecificStats());

            descending = new JButton("Descending");
            descending.setBounds(10, 80, 80, 25);
            specificPanel.add(descending);
            descending.addActionListener(new SpecificStats());

            specificFrame.setVisible(true);
        }
    }
    static class HaveNoStores implements Runnable { //have no stores simple GUI
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "You have no stores!", "No stores",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    static class EditOptions implements ActionListener, Runnable {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        private static JLabel choose;
        private static JButton editEmail;
        private static JButton editPassword;
        private static JButton deleteAccount;
        private static JButton back;
        @Override
        public void run() {
            JFrame editFrame = new JFrame();
            JPanel editPanel = new JPanel();
            editFrame.setSize(350, 200);
            editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            editFrame.add(editPanel);

            choose = new JLabel("Choose an option:");
            choose.setBounds(10,20, 80, 25);
            editPanel.add(choose);

            editEmail = new JButton("Edit account email");
            editEmail.setBounds(10, 80, 80, 25);
            editPanel.add(editEmail);
            editEmail.addActionListener(new EditOptions());

            editPassword = new JButton("Edit account password");
            editPassword.setBounds(10, 80, 80, 25);
            editPanel.add(editPassword);
            editPassword.addActionListener(new EditOptions());

            deleteAccount = new JButton("Delete account");
            deleteAccount.setBounds(10, 80, 80, 25);
            editPanel.add(deleteAccount);
            deleteAccount.addActionListener(new EditOptions());

            back = new JButton("Delete account");
            back.setBounds(10, 80, 80, 25);
            editPanel.add(back);
            back.addActionListener(new EditOptions());

            editFrame.setVisible(true);
        }
    }
    static class SellerNewEmail implements ActionListener, Runnable { //new email simple GUI
        private static JLabel enterEmail;
        private static JTextField emailText;

        @Override
        public void run() {
            JFrame newSellerEmailFrame = new JFrame();
            JPanel newSellerEmailPanel = new JPanel();
            newSellerEmailFrame.setSize(350, 200);
            newSellerEmailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newSellerEmailFrame.add(newSellerEmailPanel);

            enterEmail = new JLabel("Please enter a new email address for your account:");
            enterEmail.setBounds(10,20, 80, 25);
            newSellerEmailPanel.add(enterEmail);

            emailText = new JTextField(20);
            emailText.setBounds(100, 20, 165, 25);
            newSellerEmailPanel.add(emailText);

            newSellerEmailFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class SellerConfirmEmail implements ActionListener, Runnable { //confirm simple GUI
        private static JLabel editSellerEmail;
        private static JButton yes;
        private static JButton no;
        @Override
        public void run() {
            JFrame confirmSellerEmailFrame = new JFrame();
            JPanel confirmSellerEmailPanel = new JPanel();
            confirmSellerEmailFrame.setSize(350, 200);
            confirmSellerEmailFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            confirmSellerEmailFrame.add(confirmSellerEmailPanel);

            editSellerEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
            editSellerEmail.setBounds(10,20, 80, 25);
            confirmSellerEmailPanel.add(editSellerEmail);

            yes = new JButton("Yes");
            yes.setBounds(10, 80, 80, 25);
            confirmSellerEmailFrame.add(yes);
            yes.addActionListener(new SellerConfirmEmail());

            no = new JButton("No");
            no.setBounds(10, 80, 80, 25);
            confirmSellerEmailPanel.add(no);
            no.addActionListener(new SellerConfirmEmail());

            confirmSellerEmailFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class SellerNewPassword implements ActionListener, Runnable { //new password simple GUI
        private static JLabel enterPassword;
        private static JTextField passwordText;

        @Override
        public void run() {
            JFrame newSellerPasswordFrame = new JFrame();
            JPanel newSellerPasswordPanel = new JPanel();
            newSellerPasswordFrame.setSize(350, 200);
            newSellerPasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newSellerPasswordFrame.add(newSellerPasswordPanel);

            enterPassword = new JLabel("Please enter a new password for your account:");
            enterPassword.setBounds(10,20, 80, 25);
            newSellerPasswordPanel.add(enterPassword);

            passwordText = new JTextField(20);
            passwordText.setBounds(100, 20, 165, 25);
            newSellerPasswordPanel.add(passwordText);

            newSellerPasswordFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class SellerConfirmPassword implements ActionListener, Runnable { //confirm simple GUI
        private static JLabel areYouSure;
        private static JButton yes;
        private static JButton no;
        @Override
        public void run() {
            JFrame confirmSellerPasswordFrame = new JFrame();
            JPanel confirmSellerPasswordPanel = new JPanel();
            confirmSellerPasswordFrame.setSize(350, 200);
            confirmSellerPasswordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            confirmSellerPasswordFrame.add(confirmSellerPasswordPanel);

            areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
            areYouSure.setBounds(10,20, 80, 25);
            confirmSellerPasswordPanel.add(areYouSure);

            yes = new JButton("Yes");
            yes.setBounds(10, 80, 80, 25);
            confirmSellerPasswordPanel.add(yes);
            yes.addActionListener(new SellerConfirmPassword());

            no = new JButton("No");
            no.setBounds(10, 80, 80, 25);
            confirmSellerPasswordPanel.add(no);
            no.addActionListener(new SellerConfirmPassword());

            confirmSellerPasswordFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
    static class LogoutSeller implements Runnable { //logout simple GUI
        //if the buyer chooses the log out option
        @Override
        public void run() {
            JOptionPane.showMessageDialog(null, "Goodbye!",
                    "Farewell", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
