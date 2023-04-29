//I added enter buttons for the text fields
//comment
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.List;
import java.io.*;
import java.util.ArrayList;


public class GUI {
    private PrintWriter writer;
    private ObjectInputStream reader;
    public static Object input;
    public static String Output;

    private int test = 1;
    public GUI(PrintWriter write, ObjectInputStream read) {
        // sets up reader and write for talking to server
        this.writer = write;
        this.reader = read;
    }

    /**
     * sendToServer(String message)
     * sends a message to the werver using the printwriter passed through the constructor
     * @param message: The message (String) to send to the server. The first 2 characters should be an integer corresponding to the desired action (use leading 0!). Then add any other information the action needs
     */
    private void sendToServer(String message) {
        this.writer.write(message);
        this.writer.println();
        this.writer.flush();
    }

    /**
     * readFromServer()
     * reads an object from the server socket using the ObjectInputStream passed through the constructor
     * returns null if there is an exception
     * You will probably want to cast the object into the correct type before you use it
     */
    private Object readFromServer() {
        Object o;
        try {
            o = reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        }
        return o;
    }

    /**
     * serverAction()
     * A combination of send and receive from server just to make the code cleaner
     * @param actionNumber int number for desired action
     * @param info String of any other info needed for action, use null for none
     * @return Object from the server
     */
    private Object serverAction(int actionNumber, String info) {
        String message = String.format("%02d", actionNumber);
        if (info != null) {
            message += info;
        }
        sendToServer(message);
        Object out = readFromServer();
        return out;
    }

    Container content;
    static JFrame frame = new JFrame();
    public void ShowWelcome() {
        //this GUI is a welcome message before the welcome menu and is shown when a user logs out

        sendToServer("09"); // Tell the server user has logged out
        readFromServer(); // just read and ignore the return message

        JLabel welcome;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        welcome = new JLabel("Welcome");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton welcomMenu = new JButton("Menu");
        welcomMenu.setBounds(10, 80, 80, 25);
        panel.add(welcomMenu);
        welcomMenu.addActionListener(e -> WelcomeMenuGUI());

        frame.setVisible(true);
    }
    public void WelcomeMenuGUI() { //this GUI gives a user an option to login, create an account, or log out
        JButton login;
        JButton createAccount;
        JButton quit;
        JLabel welcomeOptions;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        JPanel panelOne = new JPanel();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panelOne);
        welcomeOptions = new JLabel("Please select an option:");
        welcomeOptions.setBounds(10, 20, 80, 25);
        panelOne.add(welcomeOptions);

        login = new JButton("Login");
        login.setBounds(10, 80, 80, 25);
        panelOne.add(login);

        createAccount = new JButton("Create an account");
        createAccount.setBounds(10, 80, 80, 25);
        panelOne.add(createAccount);

        quit = new JButton("Quit");
        quit.setBounds(10, 80, 80, 25);
        panelOne.add(quit);
        frame.setVisible(true);
        login.addActionListener(e -> LoginGui());
        createAccount.addActionListener(e -> NewAccountGUI());
        quit.addActionListener(e -> ShowWelcome());
    }
    public void LoginGui() { //requires isValidEmail() and accountExists() methods in User class
        //if the user chooses to login in the welcome menu, this GUI allows them to log in
        //by selecting a user type and password
        //and then attempting to log in by using the log in button
        JLabel userLabel;
        JTextField userText;
        JLabel passwordLabel;
        JPasswordField passwordText;
        JButton loginButton;


        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JPanel loginPanel = new JPanel();
        Container welcomeContent = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(loginPanel);

        userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
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

        //TODO: Login HERE
        loginButton.addActionListener(e -> {
            String userTextContents = userText.getText();
            String passwordTextContents = passwordText.getText();
            // first make sure we are logged out
            serverAction(9, null);

            // get info from server
            sendToServer("05" + userTextContents);
            boolean emailIsValid = (boolean) readFromServer();
            sendToServer("06" + userTextContents);
            boolean accountExists = (boolean) readFromServer();

            if (emailIsValid && accountExists) {
                String user = userTextContents;
                String password = new String(passwordTextContents);
                sendToServer(String.format("01,Username: %s, Password: %s", user, password));
                int out = (Integer) readFromServer(); // get feedback on wether login is invalid, customer, or seller
                switch (out) {
                    case -1 -> ShowWelcome(); // TODO: Invalid password here
                    case 0 -> BuyerMenuGui();
                    case 1 -> SellerMenu();
                }
            } else if ((!User.isValidEmail(userTextContents))) {
                EnterValidEmailAddress();
            } else if (!User.accountExists(userTextContents)) {
                IncorrectCredentials();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        loginPanel.add(back);
        back.addActionListener(e -> WelcomeMenuGUI());
    }
    int userNumber;
    String emailContents;
    String newPasswordContents;
    public void NewAccountGUI() { //this GUI allows the user to create a new account
        // after selecting that option in the welcome menu
        JLabel userLabel;
        JTextField userText;
        JLabel userTypeLabel;
        JButton customer;
        JButton seller;
        JLabel passwordLabel;
        JPasswordField passwordText;
        JButton makeNewAccount;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JPanel newAccountPanel = new JPanel();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(newAccountPanel);

        userTypeLabel = new JLabel("User type");
        userTypeLabel.setBounds(10,20, 80, 25);
        newAccountPanel.add(userTypeLabel);

        customer = new JButton("Customer");
        customer.setBounds(10, 80, 80, 25);
        newAccountPanel.add(customer);
        customer.addActionListener(e -> userNumber = 0);

        seller = new JButton("Seller");
        seller.setBounds(10, 80, 80, 25);
        newAccountPanel.add(seller);
        seller.addActionListener(e -> userNumber = 1);

        userLabel = new JLabel("Email");
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
        makeNewAccount.addActionListener(e -> {
            // TODO: New account stuff HERE (done I just want the marker)
            emailContents = userText.getText();
            newPasswordContents = passwordText.getText();
            sendToServer("05" + emailContents);
            boolean emailIsValid = (boolean) readFromServer();
            sendToServer("06" + emailContents);
            boolean accountExists = (boolean) readFromServer();
            if (emailIsValid && !accountExists) {  // create new accounts                                                                         //create an error message for if the email is already associated with an account
                String user = emailContents;
                String password = new String(newPasswordContents);                                                       //does not create new account
                String serverMessage = "02" + user + "," + password + "," + userNumber + "";
                sendToServer(serverMessage);
                readFromServer(); // just read and ignore the return message
                LoginGui();
            } else if (emailIsValid && accountExists) { // account exists, go back to login
                EmailAlreadyExists();
            } else {      // invalid email                                                                                                      //doesn't work
                EnterValidEmailAddress();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        newAccountPanel.add(back);
        back.addActionListener(e -> WelcomeMenuGUI());
        frame.setVisible(true);
    }
    public void EmailAlreadyExists() { //shown if the email submitted in the new account GUI is associated with an existing account
        JLabel enterValidEmail;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("This email is associated with an existing account.  Please log in.");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        frame.setVisible(true);
    }
    public void EnterValidEmailAddress() { //shown if the email submitted in the login menu is invalid
        JLabel enterValidEmail;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        frame.setVisible(true);
    }
    public void IncorrectCredentials() {
        //shown if there is nt an account associated with the email and password submitted in the login menu
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel incorrectLoginCredentials = new JLabel
                ("Incorrect login credentials or account does not exist, please try again.");
        incorrectLoginCredentials.setBounds(10, 20, 80, 25);
        panel.add(incorrectLoginCredentials);

        JButton tryAgain = new JButton("Try again");
        tryAgain.setBounds(10, 20, 80, 25);
        panel.add(tryAgain);
        tryAgain.addActionListener(e -> CreateOptions());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());
        frame.setVisible(true);
    }
    public void CreateOptions() {
        JLabel noAccountFound;
        JLabel selectOption;
        JButton createNewAccount;
        JButton reAttemptLogin;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
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
        createNewAccount.addActionListener(e -> NewAccountGUI());

        reAttemptLogin = new JButton("Re-attempt login");
        reAttemptLogin.setBounds(10, 80, 80, 25);
        createPanel.add(reAttemptLogin);
        reAttemptLogin.addActionListener(e -> LoginGui());

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        createPanel.add(back);
        back.addActionListener(e -> LoginGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void LoginSuccessful() {
        //shown if the login is successful
        JLabel loginSuccessful;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        loginSuccessful = new JLabel("Login successful");
        frame.setBounds(10, 20, 80, 25);
        panel.add(loginSuccessful);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());
        frame.setVisible(true);
    }
    public void BuyerMenuGui() {
        JButton chooseItem;
        JButton search;
        JButton sort;
        JButton viewCart;
        JButton editAccount;
        JButton logOut;
        JLabel buyerOptions;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        buyerOptions = new JLabel("Please select an option:");
        buyerOptions.setBounds(10,20, 80, 25);
        panel.add(buyerOptions);

        chooseItem = new JButton("Choose an item");
        chooseItem.setBounds(10, 80, 80, 25);
        panel.add(chooseItem);
        chooseItem.addActionListener(e -> SelectItem());

        search = new JButton("Search");
        search.setBounds(10, 80, 80, 25);
        panel.add(search);
        search.addActionListener(e -> SearchByKeywordOrNot());

        sort = new JButton("Sort");
        sort.setBounds(10, 80, 80, 25);
        panel.add(sort);
        sort.addActionListener(e -> SortingItems());

        viewCart = new JButton("View cart");
        viewCart.setBounds(10, 80, 80, 25);
        panel.add(viewCart);
        viewCart.addActionListener(e -> ViewCartOptions());

        editAccount = new JButton("Edit account");
        editAccount.setBounds(10, 80, 80, 25);
        panel.add(editAccount);
        editAccount.addActionListener(e -> EditUserInfo());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());
        frame.setVisible(true);
    }
    public void SearchByKeywordOrNot() {
        JButton searchByKeyword;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        searchByKeyword = new JButton("Search by Keyword");
        searchByKeyword.setBounds(10, 80, 80, 25);
        panel.add(searchByKeyword);
        searchByKeyword.addActionListener(e -> SearchBar());

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void SearchBar() {
        JLabel search;
        JTextField searchBar;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        search = new JLabel("Search:");
        search.setBounds(10,20, 80, 25);
        panel.add(search);

        searchBar = new JTextField(20);
        searchBar.setBounds(100, 20, 165, 25);
        panel.add(searchBar);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String keyword = new String(searchBar.getText());
            // get search results from server
            ArrayList<Item> searchResults = (ArrayList<Item>) serverAction(22, keyword);
            // TODO: Display the search results
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SearchByKeywordOrNot());

        frame.setVisible(true);
    }
    public void SelectItem() {
        //should print out all of the available items                                                                   ******
        //and make them easier to select
        JLabel select;
        // JTextField item;
        JLabel number;
        JTextField quantity;

        // Get items form server
        // TODO: Amber I got the items, you figure out how you want to display them
        sendToServer("20"); // case to get all listings
        ArrayList<Item> itemListings = (ArrayList<Item>) readFromServer(); // display these somehow and select one

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        select = new JLabel("Please select the item you wish to purchase:");
        select.setBounds(10,20, 80, 25);
        panel.add(select);

        // item = new JTextField(20);
        // item.setBounds(100, 20, 165, 25);
        // panel.add(item);

        // TODO: Dropdown menu here
        String[] choices = new String[itemListings.size()];
        for (int i = 0; i < itemListings.size(); i++) {
            Item item = itemListings.get(i);
            String itemInfo = String.format("%20s | $%-6.2f", item.getName(), item.getPrice());
            choices[i] = itemInfo;
        }
        JComboBox<String> dropdown = new JComboBox<>(choices);
        panel.add(dropdown);

        number = new JLabel("Please enter how many you would like to buy:");
        number.setBounds(10,20, 80, 25);
        panel.add(number);

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        panel.add(quantity);
        if (Integer.valueOf(quantity.getName()) >= 0) {
            int theQuantity = Integer.valueOf(quantity.getName());
        } else {
            InvalidQuantityException();
        }

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            int selection = dropdown.getSelectedIndex();
            int amount = Integer.valueOf(quantity.getText());
            serverAction(24, String.format("%d,%d", selection, amount)); // add to cart
            
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        panel.setVisible(true);
        frame.setVisible(true);
    }
    public void cancelPurchase() {
        JLabel cancelQuestion;
        JButton cancel;
        JButton approve;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        cancelQuestion = new JLabel("Do you want to cancel the purchase?");
        cancelQuestion.setBounds(10,20, 80, 25);
        panel.add(cancelQuestion);

        cancel = new JButton("Cancel");
        cancel.setBounds(10, 80, 80, 25);
        panel.add(cancel);
        cancel.addActionListener(e -> PurchaseCancelled());

        approve = new JButton("Don't cancel");
        approve.setBounds(10, 80, 80, 25);
        panel.add(approve);
        approve.addActionListener(e -> AddedToCart());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SelectItem());

        frame.setVisible(true);
    }
    public void PurchaseCancelled() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();

        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel purchaseCancelled = new JLabel("Purchase cancelled");
        purchaseCancelled.setBounds(10, 20, 80, 25);
        panel.add(purchaseCancelled);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> cancelPurchase());

        frame.setVisible(true);
    }
    public void AddedToCart() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        //%dx %s!\n", quantity, selectedItem.getName() //add info about quantity and selected item
        JLabel addedToCart = new JLabel("Added to cart");
        addedToCart.setBounds(10, 20, 80, 25);
        panel.add(addedToCart);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SelectItem());

        frame.setVisible(true);
    }
    public void InvalidQuantityException() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel invalidQuantity = new JLabel("Invalid Quantity");
        invalidQuantity.setBounds(10, 20, 80, 25);
        panel.add(invalidQuantity);

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SelectItem());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void SortingItems() {
        JLabel priceOrQuantity;
        JButton price;
        JButton quantity;
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        priceOrQuantity = new JLabel("Do you want to sort by price or quantity?");
        priceOrQuantity.setBounds(10,20, 80, 25);
        panel.add(priceOrQuantity);

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(e -> {
            //should allow the buyer to sort items by price                                                             ******
            serverAction(31, "1"); // set price
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(e -> {
            //should allow the buyer to sort items by quantity
            serverAction(31, "2"); // set qnty
        });

        ascendingOrDescending = new JLabel("Do you want to sort ascending or descending?");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        panel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        panel.add(ascending);
        ascending.addActionListener(e -> {
            //should allow the buyer to sort items ascending
            serverAction(32, "1"); // set ascending
        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        panel.add(descending);
        descending.addActionListener(e -> {
            //should allow the buyer to sort items descending
            serverAction(32, "2"); // set desceding
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        frame.setVisible(true);
    }
    public void ViewCartOptions() {
        JLabel choose;
        JButton checkout;
        JButton view;
        JButton remove;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        checkout = new JButton("Checkout");
        checkout.setBounds(10, 80, 80, 25);
        panel.add(checkout);
        checkout.addActionListener(e -> CheckoutComplete());

        view = new JButton("View Purchase History");
        view.setBounds(10, 80, 80, 25);
        panel.add(view);
        view.addActionListener(e -> {
            //view purchase history                                                                                     ***
        });

        remove = new JButton("Remove Item");
        remove.setBounds(10, 80, 80, 25);
        panel.add(remove);
        remove.addActionListener(e -> RemoveItem());

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void CheckoutComplete() { //if the buyer chooses to checkout and checks out successfully
        double price = (double) serverAction(28, null); // tell server checkout has happened
        
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel checkoutComplete = new JLabel(String.format("Checkout complete!\nYour total was $%.2f\n", price));
        checkoutComplete.setBounds(10, 20, 80, 25);
        panel.add(checkoutComplete);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ViewCartOptions());

        frame.setVisible(true);
    }
    public void ExportPurchaseHistory() {
        JLabel export;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        export = new JLabel("Would you like to export purchase history?");
        export.setBounds(10,20, 80, 25);
        panel.add(export);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(e -> EnterNameOfFile());

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(e -> ViewCartOptions());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> {

        });                                                                                                                     //back***

        frame.setVisible(true);
    }
    public void EnterNameOfFile() {
        JLabel nameFile;
        JTextField file;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        nameFile = new JLabel("Enter the name of the file to save the purchase history to:");
        nameFile.setBounds(10,20, 80, 25);
        panel.add(nameFile);

        file = new JTextField(20);
        file.setBounds(100, 20, 165, 25);
        panel.add(file);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String filename = file.getText();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EnterNameOfFile());

        frame.setVisible(true);
    }
    public void PurchaseHistoryExported() {
        //message shown if the buyer successfully saves purchase history to file
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel exportSuccess = new JLabel("Purchase history successfully exported to file!");
        exportSuccess.setBounds(10, 20, 80, 25);
        panel.add(exportSuccess);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EnterNameOfFile());

        frame.setVisible(true);
    }
    public void RemoveItem() {
        //should print out all items in the cart so the buyer can select one                                                ***
        JLabel select;
        JTextField item;
        JLabel number;
        JTextField quantity;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        select = new JLabel("Enter an item to remove from cart:");
        select.setBounds(10,20, 80, 25);
        panel.add(select);
        //should save the item as a variable                                                                                ***

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String itemToRemove = item.getText();
        });

        number = new JLabel("Please enter how many you would like to remove:");
        number.setBounds(10,20, 80, 25);
        panel.add(number);
        //should save the number as a variable

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        panel.add(quantity);

        JButton enterTwo = new JButton("Enter");
        enterTwo.setBounds(10, 80, 80, 25);
        panel.add(enterTwo);
        enterTwo.addActionListener(e -> {
            int numberOfItems = Integer.parseInt(quantity.getText());
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ViewCartOptions());

        frame.setVisible(true);
    }
    public void EditUserInfo() {
        JLabel choose;
        JButton editEmail;
        JButton editPassword;
        JButton delete;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        editEmail = new JButton("Edit Account Email");
        editEmail.setBounds(10, 80, 80, 25);
        panel.add(editEmail);
        editEmail.addActionListener(e -> NewEmail());

        editPassword = new JButton("Edit Account Password");
        editPassword.setBounds(10, 80, 80, 25);
        panel.add(editPassword);
        editPassword.addActionListener(e -> NewPassword());

        delete = new JButton("Delete Account");
        delete.setBounds(10, 80, 80, 25);
        panel.add(delete);
        delete.addActionListener(e -> {

        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    static String emailTextString;
    public void NewEmail() {
        JLabel enterEmail;
        JTextField emailText;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterEmail = new JLabel("Please enter a new email address for your account:");
        enterEmail.setBounds(10,20, 80, 25);
        panel.add(enterEmail);

        emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);
        emailTextString = emailText.getText();

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enter.addActionListener(e -> {
            emailTextString = emailText.getText();
            ConfirmEmail();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EditUserInfo());

        frame.setVisible(true);
    }
    public void ConfirmEmail() {
        JLabel editEmail;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        editEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
        editEmail.setBounds(10,20, 80, 25);
        panel.add(editEmail);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(e -> {
            sendToServer("05" + emailTextString);
            boolean emailIsValid = (boolean) readFromServer();
            if (emailIsValid) {
                String user = emailTextString;
            } else if (!emailIsValid) {
                EnterValidEmailAddressTwo();
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(e -> NewEmail());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EditUserInfo());

        frame.setVisible(true);
    }
    public void EnterValidEmailAddressTwo() { //shown if the email submitted in the customer edit user info is invalid
        JLabel enterValidEmail;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ConfirmEmail());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        frame.setVisible(true);
    }
    static String passwordTextString;
    public void NewPassword() {
        JLabel enterPassword;
        JTextField passwordText;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterPassword = new JLabel("Please enter a new password for your account:");
        enterPassword.setBounds(10,20, 80, 25);
        panel.add(enterPassword);

        passwordText = new JTextField(20);
        passwordText.setBounds(100, 20, 165, 25);
        panel.add(passwordText);
        passwordTextString = passwordText.getText();

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enter.addActionListener(e -> {
            passwordTextString = passwordText.getText();
            ConfirmPassword();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EditUserInfo());

        frame.setVisible(true);
    }
    public void ConfirmPassword() {
        JLabel areYouSure;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
        areYouSure.setBounds(10,20, 80, 25);
        panel.add(areYouSure);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(e -> {
            String password = passwordTextString;
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(e -> NewPassword());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> NewPassword());

        frame.setVisible(true);
    }
    public void SellerMenu() {
        JLabel choose;
        JButton viewListings;
        JButton viewStatistics;
        JButton editAccount;
        JButton logOut;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Chose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        viewListings = new JButton("View listings");
        viewListings.setBounds(10, 80, 80, 25);
        panel.add(viewListings);
        viewListings.addActionListener(e -> ListingsMenu());

        viewStatistics = new JButton("View statistics");
        viewStatistics.setBounds(10, 80, 80, 25);
        panel.add(viewStatistics);
        viewStatistics.addActionListener(e -> ViewStatistics());

        editAccount = new JButton("Edit account");
        editAccount.setBounds(10, 80, 80, 25);
        panel.add(editAccount);
        editAccount.addActionListener(e -> EditOptions());

        logOut = new JButton("Log out");
        logOut.setBounds(10, 80, 80, 25);
        panel.add(logOut);
        logOut.addActionListener(e -> {

        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> LoginGui());

        frame.setVisible(true);
    }
    public void ListingsMenu() {
        JLabel choose;
        JButton add;
        JButton edit;
        JButton delete;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Chose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        add = new JButton("Add");
        add.setBounds(10, 80, 80, 25);
        panel.add(add);
        add.addActionListener(e -> AddOptions());

        edit = new JButton("Edit");
        edit.setBounds(10, 80, 80, 25);
        panel.add(edit);
        edit.addActionListener(e -> ChooseItemToEdit());

        delete = new JButton("Delete");
        delete.setBounds(10, 80, 80, 25);
        panel.add(delete);
        delete.addActionListener(e -> Remove());

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerMenu());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void AddOptions() {
        JButton add;
        JButton csv;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        add = new JButton("Add item");
        add.setBounds(10, 80, 80, 25);
        panel.add(add);
        add.addActionListener(e -> AddItem());

        csv = new JButton("Add from CSV");
        csv.setBounds(10, 80, 80, 25);
        panel.add(csv);
        csv.addActionListener(e -> AddFromCSV());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ListingsMenu());

        frame.setVisible(true);
    }
    public void AddItem() {
        JLabel enterName;
        JTextField nameField;
        JLabel enterStore;
        JTextField storeField;
        JLabel enterDescription;
        JTextField descriptionField;
        JLabel enterQuantity;
        JTextField quantityField;
        JLabel enterPrice;
        JTextField priceField;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterName = new JLabel("Enter name:");
        enterName.setBounds(10,20, 80, 25);
        panel.add(enterName);

        nameField = new JTextField(20);
        nameField.setBounds(100, 20, 165, 25);
        panel.add(nameField);

        enterStore = new JLabel("Enter store:");
        enterStore.setBounds(10,20, 80, 25);
        panel.add(enterStore);

        storeField = new JTextField(20);
        storeField.setBounds(100, 20, 165, 25);
        panel.add(storeField);

        enterDescription = new JLabel("Enter description:");
        enterDescription.setBounds(10,20, 80, 25);
        panel.add(enterDescription);

        descriptionField = new JTextField(20);
        descriptionField.setBounds(100, 20, 165, 25);
        panel.add(descriptionField);


        enterQuantity = new JLabel("Enter quantity:");
        enterQuantity.setBounds(10,20, 80, 25);
        panel.add(enterQuantity);

        quantityField = new JTextField(20);
        quantityField.setBounds(100, 20, 165, 25);
        panel.add(quantityField);

        enterPrice = new JLabel("Enter price:");
        enterPrice.setBounds(10,20, 80, 25);
        panel.add(enterPrice);

        priceField = new JTextField(20);
        priceField.setBounds(100, 20, 165, 25);
        panel.add(priceField);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String name = enterName.getText();
            String store = storeField.getText();
            String description1 = descriptionField.getText();
            String quantity = quantityField.getText();
            String price = priceField.getText();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> AddOptions());

        frame.setVisible(true);
    }
    public void AddFromCSV() {
        JLabel enterFileName;
        JTextField fileNameField;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterFileName = new JLabel("Enter file name:");
        enterFileName.setBounds(10,20, 80, 25);
        panel.add(enterFileName);

        fileNameField = new JTextField(20);
        fileNameField.setBounds(100, 20, 165, 25);
        panel.add(fileNameField);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String fileName = fileNameField.getText();
            Object o = serverAction(41, fileName); // send to server
            if (o instanceof Exception) {
                String errorMessage = ((Exception) o).getMessage();
                // TODO: error message here that shows error message
            } else {
                // TODO: sucess message and or go back to menu
                int numAdded = (int) o;
                System.out.printf("%d Items added!", numAdded);
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> AddOptions());

        frame.setVisible(true);
    }
    public void ChooseItemToEdit() {
        JLabel chooseItem;
        JTextField item;
        JLabel options;
        JButton name;
        JButton store;
        JButton description;
        JButton quantity;
        JButton price;
        JLabel change;
        JTextField theChange;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        chooseItem = new JLabel("Choose an item to edit:");
        chooseItem.setBounds(10,20, 80, 25);
        panel.add(chooseItem);

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        JButton enter = new JButton();
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String itemToEdit = item.getText();
        });

        options = new JLabel("What would you like to change?");
        options.setBounds(10, 20, 80, 25);
        panel.add(options);

        name = new JButton("Name");
        name.setBounds(10, 80, 80, 25);
        panel.add(name);
        name.addActionListener(e -> {

        });

        store = new JButton("Store");
        store.setBounds(10, 80, 80, 25);
        panel.add(store);
        store.addActionListener(e -> {

        });

        description = new JButton("Description");
        description.setBounds(10, 80, 80, 25);
        panel.add(description);
        description.addActionListener(e -> {

        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(e -> {

        });

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(e -> {

        });

        change = new JLabel("What would you like to change it to?");
        change.setBounds(10,20, 80, 25);
        panel.add(change);

        theChange = new JTextField(20);
        theChange.setBounds(100, 20, 165, 25);
        panel.add(theChange);

        JButton enterTwo = new JButton("Enter");
        enterTwo.setBounds(10, 80, 80, 25);
        panel.add(enterTwo);
        enterTwo.addActionListener(e -> {
            String newName = theChange.getText();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ListingsMenu());

        frame.setVisible(true);
    }
    public void Remove() {                                                               //(also need to print the cart) ****
        JLabel chooseItem;
        JTextField item;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        chooseItem = new JLabel("Choose an item to remove:");
        chooseItem.setBounds(10,20, 80, 25);
        panel.add(chooseItem);

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            String itemToRemoveFrom = item.getText();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerMenu());

        frame.setVisible(true);
    }
    public void ViewStatistics() {
        JLabel choose;
        JButton allStats;
        JButton specificStats;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        allStats = new JButton("View all statistics");
        allStats.setBounds(10, 80, 80, 25);
        panel.add(allStats);
        allStats.addActionListener(e -> {

        });

        specificStats = new JButton("View specific statistics");
        specificStats.setBounds(10, 80, 80, 25);
        panel.add(specificStats);
        specificStats.addActionListener(e -> SpecificStats());

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerMenu());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    public void SpecificStats() {
        JLabel byPriceOrQuantity;
        JButton price;
        JButton quantity;
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        byPriceOrQuantity = new JLabel("Sort specific statistics by price or quantity:");
        byPriceOrQuantity.setBounds(10,20, 80, 25);
        panel.add(byPriceOrQuantity);

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(e -> {

        });

        quantity = new JButton("Price");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(e -> {

        });

        ascendingOrDescending = new JLabel("Sort specific statistics ascending or descending:");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        panel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        panel.add(ascending);
        ascending.addActionListener(e -> {

        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        panel.add(descending);
        descending.addActionListener(e -> {

        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ViewStatistics());

        frame.setVisible(true);
    }
    public void HaveNoStores() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel noStores = new JLabel("You have no stores!");
        noStores.setBounds(10, 20, 80, 25);
        panel.add(noStores);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ViewStatistics());

        frame.setVisible(true);
    }
    public void EditOptions() {
        JLabel choose;
        JButton editEmail;
        JButton editPassword;
        JButton deleteAccount;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        editEmail = new JButton("Edit account email");
        editEmail.setBounds(10, 80, 80, 25);
        panel.add(editEmail);
        editEmail.addActionListener(e -> NewEmail());

        editPassword = new JButton("Edit account password");
        editPassword.setBounds(10, 80, 80, 25);
        panel.add(editPassword);
        editPassword.addActionListener(e -> SellerNewPassword());

        deleteAccount = new JButton("Delete account");
        deleteAccount.setBounds(10, 80, 80, 25);
        panel.add(deleteAccount);
        deleteAccount.addActionListener(e -> {

        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerMenu());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        frame.setVisible(true);
    }
    String potentialNewEmail;
    public void SellerNewEmail() {
        JLabel enterEmail;
        JTextField emailText;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterEmail = new JLabel("Please enter a new email address for your account:");
        enterEmail.setBounds(10,20, 80, 25);
        panel.add(enterEmail);
        potentialNewEmail = enterEmail.getText();

        emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> potentialNewEmail = emailText.getText());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EditOptions());

        frame.setVisible(true);
    }
    public void SellerConfirmEmail() {
        JLabel editSellerEmail;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        editSellerEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
        editSellerEmail.setBounds(10,20, 80, 25);
        panel.add(editSellerEmail);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(e -> yes.addActionListener(e1 -> {
            sendToServer("05" + potentialNewEmail);
            boolean emailIsValid = (boolean) readFromServer();
            if (emailIsValid) {
                String email = potentialNewEmail;
            } else if (!emailIsValid) {
                EnterValidEmailAddressThree();
            }
        }));

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(e -> SellerNewEmail());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerNewEmail());

        frame.setVisible(true);
    }
    public void EnterValidEmailAddressThree() { //shown if the email submitted in the seller edit user info is invalid
        JLabel enterValidEmail;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerConfirmEmail());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());
        frame.setVisible(true);
    }

    public void SellerEnterValidEmailAddress() {
        //shown if the email submitted in SellerConfirmEmail() is invalid
        JLabel enterValidEmail;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerNewEmail());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }
    String potentialNewPassword;
    public void SellerNewPassword() {
        JLabel enterPassword;
        JTextField passwordText;
        JButton confirm;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterPassword = new JLabel("Please enter a new password for your account:");
        enterPassword.setBounds(10,20, 80, 25);
        panel.add(enterPassword);
        potentialNewPassword = enterPassword.getText();

        confirm = new JButton("Confirm");
        confirm.setBounds(10, 80, 80, 25);
        panel.add(confirm);
        confirm.addActionListener(e -> SellerConfirmPassword());

        passwordText = new JTextField(20);
        passwordText.setBounds(100, 20, 165, 25);
        panel.add(passwordText);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> potentialNewPassword = passwordText.getText());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> EditOptions());

        frame.setVisible(true);
    }
    public void SellerConfirmPassword() {
        JLabel areYouSure;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
        areYouSure.setBounds(10,20, 80, 25);
        panel.add(areYouSure);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(e -> {
            String password = potentialNewPassword;
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(e -> SellerNewPassword());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> SellerNewPassword());

        frame.setVisible(true);
    }
}
