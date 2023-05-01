import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.List;
import java.io.*;
import java.util.ArrayList;
////

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
     * sends a message to the server using the printwriter passed through the constructor
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
        // Object out2 = readFromServer();
        return out;
    }

    private ArrayList<Item> parseItemList(String items) {
        String[] s = items.split(";");
        ArrayList<Item> list = new ArrayList<>();
        if (s.length > 0) {
            for (int i = 0; i < s.length; i++) {
                try {
                    list.add(new Item(s[i]));
                } catch (InvalidLineException e) {       
                }
            }
        }
        return list;
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

        JButton welcomeMenu = new JButton("Menu");
        welcomeMenu.setBounds(10, 80, 80, 25);
        panel.add(welcomeMenu);
        welcomeMenu.addActionListener(e -> WelcomeMenuGUI());

        frame.setVisible(true);
    }
    public void WelcomeMenuGUI() { //this GUI gives a user an option to log in, create an account, or log out
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
        //if the user chooses to log in in the welcome menu, this GUI allows them to log in
        //by selecting a user type and password
        //and then attempting to log in by using the login button
        JLabel userLabel;
        JTextField userText;
        JLabel passwordLabel;
        JPasswordField passwordText;
        JButton loginButton;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        content = frame.getContentPane();

        Container welcomeContent = frame.getContentPane();
        frame.setSize(650, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel emailPanel = new JPanel();
        frame.add(emailPanel);
        content.add(emailPanel);

        JPanel passwordPanel = new JPanel();
        frame.add(passwordPanel);
        content.add(passwordPanel);

        JPanel backPanel = new JPanel();
        frame.add(backPanel);
        content.add(backPanel);

        userLabel = new JLabel("User");
        userLabel.setBounds(10, 20, 80, 25);
        emailPanel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        emailPanel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        passwordPanel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        passwordPanel.add(passwordText);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        backPanel.add(loginButton);
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
                sendToServer(String.format("01,%s,%s", user, password));
                int out = (int) readFromServer(); // get feedback on whether login is invalid, customer, or seller
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
        backPanel.add(back);
        back.addActionListener(e -> WelcomeMenuGUI());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, emailPanel, passwordPanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, backPanel);
        frame.add(sp2);
        content.add(sp2);
    }
    int userNumber = 99;
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
        JPanel userTypePanel = new JPanel();
        JPanel textFieldPanel = new JPanel();
        JPanel createPanel = new JPanel();


        Container content = frame.getContentPane();
        frame.setSize(700, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(userTypePanel);
        content.add(userTypePanel);
        frame.add(textFieldPanel);
        content.add(textFieldPanel);
        frame.add(createPanel);
        content.add(createPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userTypeLabel = new JLabel("User type");
        userTypeLabel.setBounds(10,20, 80, 25);
        userTypePanel.add(userTypeLabel);

        customer = new JButton("Customer");
        customer.setBounds(10, 80, 80, 25);
        userTypePanel.add(customer);
        customer.addActionListener(e -> userNumber = 0);

        seller = new JButton("Seller");
        seller.setBounds(10, 80, 80, 25);
        userTypePanel.add(seller);
        seller.addActionListener(e -> userNumber = 1);

        userLabel = new JLabel("Email");
        userLabel.setBounds(10,20, 80, 25);
        textFieldPanel.add(userLabel);
        userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        textFieldPanel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        textFieldPanel.add(passwordLabel);
        passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        textFieldPanel.add(passwordText);

        makeNewAccount = new JButton("Create new account");
        makeNewAccount.setBounds(10, 80, 80, 25);
        createPanel.add(makeNewAccount);
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
            if (userNumber == 99) {
                NewAccountError();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        createPanel.add(back);
        back.addActionListener(e -> WelcomeMenuGUI());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, userTypePanel, textFieldPanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, createPanel);
        frame.add(sp2);
        content.add(sp2);

        frame.setVisible(true);
    }
    public void NewAccountError() {
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

        enterValidEmail = new JLabel("Select whether you are a customer or seller!");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(e -> NewAccountGUI());
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
    }
    public void IncorrectCredentials() {
        JLabel noAccountFound;
        JLabel selectOption;
        JButton createNewAccount;
        JButton reAttemptLogin;
        JButton back;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel incorrectPanel = new JPanel();
        JPanel selectPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(incorrectPanel);
        content.add(incorrectPanel);
        frame.add(selectPanel);
        content.add(selectPanel);
        frame.add(buttonPanel);
        content.add(buttonPanel);

        noAccountFound = new JLabel("Incorrect login credentials or account does not exist, please try again.  ");
        noAccountFound.setBounds(10, 20, 80, 25);
        incorrectPanel.add(noAccountFound);

        selectOption = new JLabel("Select an option:");
        selectOption.setBounds(10, 20, 80, 25);
        selectPanel.add(selectOption);

        createNewAccount = new JButton("Create new account");
        createNewAccount.setBounds(10, 80, 80, 25);
        buttonPanel.add(createNewAccount);
        createNewAccount.addActionListener(e -> NewAccountGUI());

        reAttemptLogin = new JButton("Re-attempt login");
        reAttemptLogin.setBounds(10, 80, 80, 25);
        buttonPanel.add(reAttemptLogin);
        reAttemptLogin.addActionListener(e -> LoginGui());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, incorrectPanel, selectPanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, buttonPanel);
        frame.add(sp2);
        content.add(sp2);

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
        chooseItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                serverAction(20, null);
                SelectItem(); // go to selection gui
            }
        });

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
        JTextField showMessage;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel searchPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(searchPanel);
        frame.add(backPanel);
        content.add(searchPanel);
        content.add(backPanel);

        search = new JLabel("Search:");
        search.setBounds(10,20, 80, 25);
        searchPanel.add(search);

        searchBar = new JTextField(20);
        searchBar.setBounds(100, 20, 165, 25);
        searchPanel.add(searchBar);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        searchPanel.add(enter);
        enter.addActionListener(e -> {
            String keyword = new String(searchBar.getText());
            ArrayList<Item> searchResults = (ArrayList<Item>) serverAction(22, keyword); // tell server to sort search
            if (searchResults.size() < 1) {
                // TODO: no results message
            } else {
                SelectItem(); // go to item selection gui
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> SearchByKeywordOrNot());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, searchPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }

    /**
     * Makes the item selection GUI for customer. Can be used for any selection of items
     */
    public void SelectItem() {
        //should print out all the available items                                                                   ******
        //and make them easier to select
        JLabel select;
        // JTextField item;
        JLabel number;
        JTextField quantity;

        // Get items form server
        // ArrayList<Item> itemListings = (ArrayList<Item>) serverAction(30, null); // display these somehow and select one
        ArrayList<Item> itemListings = parseItemList((String) serverAction(30, null)); // display these somehow and select one

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        //JPanel panel = new JPanel();
        JPanel selectPanel = new JPanel();
        JPanel selectPanelTwo = new JPanel();
        JPanel enterPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(selectPanel);
        content.add(selectPanel);
        frame.add(enterPanel);
        content.add(enterPanel);

        select = new JLabel("Please select the item you wish to purchase:");
        select.setBounds(10,20, 80, 25);
        selectPanel.add(select);

        // item = new JTextField(20);
        // item.setBounds(100, 20, 165, 25);
        // panel.add(item);

        
        // String[] choices = new String[itemListings.size()];
        // for (int i = 0; i < itemListings.size(); i++) {
        //     Item item = itemListings.get(i);
        //     String itemInfo = String.format("%20s | $%-6.2f", item.getName(), item.getPrice());
        //     choices[i] = itemInfo;
        // }

        JComboBox<String> dropdown = createItemDropdown(itemListings, true, true, true);
        selectPanelTwo.add(dropdown);

        number = new JLabel("Please enter how many you would like to buy:");
        number.setBounds(10,20, 80, 25);
        enterPanel.add(number);

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        enterPanel.add(quantity);
        // if (Integer.valueOf(quantity.getName()) >= 0) {
        //     int theQuantity = Integer.valueOf(quantity.getName());
        // } else {
        //     InvalidQuantityException();
        // }

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 20, 80, 25);
        enterPanel.add(enter);
        enter.addActionListener(e -> {
            int selection = dropdown.getSelectedIndex();
            int amount = Integer.valueOf(quantity.getText());
            serverAction(24, String.format("%d,%d", selection, amount)); // add to cart
            BuyerMenuGui();; // go back to buyer menu
        });

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectPanel, selectPanelTwo);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, enterPanel);
        frame.add(sp2);
        content.add(sp2);

        frame.setVisible(true);
    }

    /**
     * @param items Arraylist of items to show
     * @param showStore Boolean to show store
     * @param showQnty Boolean to show quantity of item
     * @param showPrice Boolean to show price
     * @return a dropdown menu that can be added to a panel
     */
    private JComboBox<String> createItemDropdown(ArrayList<Item> items, boolean showStore, boolean showQnty, boolean showPrice) {
        String[] choices = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            String itemInfo = String.format("%-40s |", item.getName());
            if (showStore) {
                itemInfo += String.format(" %-30s |", item.getStore());
            }
            if (showQnty) {
                itemInfo += String.format(" x%-4d |", item.getQuantity());
            }
            if (showPrice) {
                itemInfo += String.format(" $%-6.2f ", item.getPrice());
            }
            choices[i] = itemInfo;
        }
        JComboBox<String> dropdown = new JComboBox<>(choices);
        return dropdown;
    }

    private JComboBox<String> createCustomerDropdown(ArrayList<Customer> customers) {
        String[] choices = new String[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            String info = String.format("%-18s | %d items purchased", c.getEmail(), c.getPurchases().size());
            choices[i] = info;
        }
        JComboBox<String> dropdown = new JComboBox<>(choices);
        return dropdown;
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
    String priceOrQuantityString;
    public void SortingItems() {
        JLabel priceOrQuantity;
        JButton price;
        JButton quantity;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel priceOrQuantityPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(priceOrQuantityPanel);
        content.add(priceOrQuantityPanel);
        frame.add(backPanel);
        content.add(backPanel);

        priceOrQuantity = new JLabel("Do you want to sort by price or quantity?");
        priceOrQuantity.setBounds(10,20, 80, 25);
        priceOrQuantityPanel.add(priceOrQuantity);

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        priceOrQuantityPanel.add(price);
        price.addActionListener(e -> {
            //should allow the buyer to sort items by price                                                             ******
            serverAction(31, "1"); // set price
            priceOrQuantityString = "You selected to sort by price.";
            SortingItemsTwo();
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        priceOrQuantityPanel.add(quantity);
        quantity.addActionListener(e -> {
            //should allow the buyer to sort items by quantity
            serverAction(31, "2"); // set qnty
            priceOrQuantityString = "You selected to sort by quantity.";
            SortingItemsTwo();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, priceOrQuantityPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }
    public void SortingItemsTwo() {
        JLabel priceOrQuantity;
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel choicePanel = new JPanel();
        JPanel ascendingOrDescendingPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(choicePanel);
        content.add(choicePanel);
        frame.add(ascendingOrDescendingPanel);
        content.add(ascendingOrDescendingPanel);

        priceOrQuantity = new JLabel(priceOrQuantityString);
        priceOrQuantity.setBounds(10, 20, 80, 25);
        choicePanel.add(priceOrQuantity);

        ascendingOrDescending = new JLabel("Do you want to sort ascending or descending?");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        ascendingOrDescendingPanel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        ascendingOrDescendingPanel.add(ascending);
        ascending.addActionListener(e -> {
            //should allow the buyer to sort items ascending
            serverAction(32, "1"); // set ascending
            serverAction(21, null); // perform the sort
            SelectItem();
        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        ascendingOrDescendingPanel.add(descending);
        descending.addActionListener(e -> {
            //should allow the buyer to sort items descending
            serverAction(32, "2"); // set descending
            serverAction(21, null); // perform the sort
            SelectItem();
        });

        JButton enter = new JButton("Sort");
        enter.setBounds(10, 80, 80, 25);
        ascendingOrDescendingPanel.add(enter);
        enter.addActionListener(e -> {
            serverAction(21, null); // perform the sort
            SelectItem(); // go to selection menu
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, choicePanel, ascendingOrDescendingPanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, backPanel);
        frame.add(sp2);
        content.add(sp2);

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
        frame.setSize(650, 350);
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
            ViewPurchaseHistory();
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

        // ArrayList<Item> cartItems = (ArrayList<Item>) serverAction(23, null);
        ArrayList<Item> cartItems = parseItemList((String) serverAction(23, null));
        JComboBox<String> dropdown = createItemDropdown(cartItems, false, true, true);
        panel.add(dropdown);

        frame.setVisible(true);
    }
    public void ViewPurchaseHistory() {
        //this GUI is a welcome message before the welcome menu and is shown when a user logs out

        // sendToServer("09"); // Tell the server user has logged out
        // readFromServer(); // just read and ignore the return message

        JLabel welcome;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        ArrayList<Item> cartItems = parseItemList((String) serverAction(26, null));
        JComboBox<String> dropdown = createItemDropdown(cartItems, false, true, true);
        panel.add(dropdown);

        welcome = new JLabel("Purchase history");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton viewPurchaseHistory = new JButton("View purchase history");
        viewPurchaseHistory.setBounds(10, 80, 80, 25);
        panel.add(viewPurchaseHistory);
        viewPurchaseHistory.addActionListener(e -> ViewPurchaseHistory());

        JButton exportPurchaseHistory = new JButton("Export purchase history");
        exportPurchaseHistory.setBounds(10, 80, 80, 25);
        panel.add(exportPurchaseHistory);
        exportPurchaseHistory.addActionListener(e -> EnterNameOfFile());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ViewCartOptions());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        frame.setVisible(true);
    }

    public void CheckoutComplete() { //if the buyer chooses to check out and checks out successfully
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

        JButton back = new JButton("Return to menu");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> BuyerMenuGui());

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
        yes.addActionListener(e -> {
            //TODO: export purchase history to file
        });

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
        JPanel enterPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(enterPanel);
        content.add(backPanel);
        frame.add(backPanel);
        content.add(backPanel);

        nameFile = new JLabel("Enter the name of the file to save the purchase history to:");
        nameFile.setBounds(10,20, 80, 25);
        enterPanel.add(nameFile);

        file = new JTextField(20);
        file.setBounds(100, 20, 165, 25);
        enterPanel.add(file);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enterPanel.add(enter);
        enter.addActionListener(e -> {
            String filename = file.getText();
            serverAction(27, filename);
            PurchaseHistoryExported();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> ExportPurchaseHistory());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, enterPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }
    public void PurchaseHistoryExported() {
        //TODO: access this method
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
        // JTextField item;
        JLabel number;
        JTextField quantity;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel selectPanel = new JPanel();
        JPanel enterPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(selectPanel);
        content.add(selectPanel);
        frame.add(enterPanel);
        content.add(enterPanel);
        frame.add(backPanel);
        content.add(backPanel);

        select = new JLabel("Select an item to remove from cart:");
        select.setBounds(10,20, 80, 25);
        selectPanel.add(select);

        // ArrayList<Item> cartItems = (ArrayList<Item>) serverAction(23, null);
        ArrayList<Item> cartItems = parseItemList((String) serverAction(23, null));
        JComboBox<String> dropdown = createItemDropdown(cartItems, false, true, true);
        selectPanel.add(dropdown);

        // item = new JTextField(20);
        // item.setBounds(100, 20, 165, 25);
        // panel.add(item);
        number = new JLabel("Please enter how many you would like to remove:");
        number.setBounds(10,20, 80, 25);
        enterPanel.add(number);
        //should save the number as a variable

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        enterPanel.add(quantity);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enterPanel.add(enter);
        enter.addActionListener(e -> {
            int itemToRemove = dropdown.getSelectedIndex();
            int numToRemove = Integer.valueOf(quantity.getText());
            serverAction(25, String.format("%d,%d", itemToRemove, numToRemove));
            ViewCartOptions(); // go back to cart
        });

        

        // JButton enterTwo = new JButton("Enter");
        // enterTwo.setBounds(10, 80, 80, 25);
        // panel.add(enterTwo);
        // enterTwo.addActionListener(e -> {
        //     int numberOfItems = Integer.parseInt(quantity.getText());
        // });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> ViewCartOptions());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, selectPanel, enterPanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, backPanel);
        frame.add(sp2);
        content.add(sp2);

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
            DeleteAccountQuestion();
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
        JPanel emailPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(emailPanel);
        content.add(emailPanel);
        frame.add(backPanel);
        content.add(backPanel);

        enterEmail = new JLabel("Please enter a new email address for your account:");
        enterEmail.setBounds(10,20, 80, 25);
        emailPanel.add(enterEmail);

        emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        emailPanel.add(emailText);
        emailTextString = emailText.getText();

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enter.addActionListener(e -> {
            emailTextString = emailText.getText();
            ConfirmEmail();
        });
        emailPanel.add(enter);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> EditUserInfo());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, emailPanel, backPanel);
        frame.add(sp);
        content.add(sp);

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
                serverAction(3, user);
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

        enterValidEmail = new JLabel("Please enter a valid email address!");
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
        JPanel passwordPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(passwordPanel);
        content.add(passwordPanel);
        frame.add(backPanel);
        content.add(backPanel);

        enterPassword = new JLabel("Please enter a new password for your account:");
        enterPassword.setBounds(10,20, 80, 25);
        passwordPanel.add(enterPassword);

        passwordText = new JTextField(20);
        passwordText.setBounds(100, 20, 165, 25);
        passwordPanel.add(passwordText);
        passwordTextString = passwordText.getText();

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enter.addActionListener(e -> {
            passwordTextString = passwordText.getText();
            ConfirmPassword();
        });
        passwordPanel.add(enter);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> EditUserInfo());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, passwordPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }
    public void ConfirmPassword() {
        JLabel areYouSure;
        JButton yes;
        JButton no;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel editPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(editPanel);
        content.add(editPanel);
        frame.add(backPanel);
        content.add(backPanel);

        areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
        areYouSure.setBounds(10,20, 80, 25);
        editPanel.add(areYouSure);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        editPanel.add(yes);
        yes.addActionListener(e -> {
            String password = passwordTextString;
            serverAction(4, password);
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        editPanel.add(no);
        no.addActionListener(e -> NewPassword());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> NewPassword());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }
    public void SellerMenu() {
        JLabel choose;
        JButton viewListings;
        JButton viewStatistics;
        JButton editAccount;

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

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> ListingsMenu());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

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

        // show items
        serverAction(50, null); // FIXME: mkflsdk
        // ArrayList<Item> sellerItems = (ArrayList<Item>) serverAction(40, null); // wtf
        ArrayList<Item> sellerItems = parseItemList((String) serverAction(40, null));
        JComboBox<String> dropdown = createItemDropdown(sellerItems, true, true, true);
        panel.add(dropdown);
        // FIXME: wtf is happening here? Server sends arraylist of 2 items, client receives 3 (old arraylist?)
        // Going back and forth = sometimes server update isn't called? but even when it is it reads the wrong thing

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
        //JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.add(panel);

        JPanel namePanel = new JPanel();
        frame.add(namePanel);
        content.add(namePanel);
        enterName = new JLabel("Enter name:");
        enterName.setBounds(10,20, 80, 25);
        namePanel.add(enterName);

        nameField = new JTextField(20);
        nameField.setBounds(100, 20, 165, 25);
        namePanel.add(nameField);

        JPanel storePanel = new JPanel();
        frame.add(namePanel);
        content.add(namePanel);
        enterStore = new JLabel("Enter store:");
        enterStore.setBounds(10,20, 80, 25);
        storePanel.add(enterStore);

        storeField = new JTextField(20);
        storeField.setBounds(100, 20, 165, 25);
        storePanel.add(storeField);

        JPanel descriptionPanel = new JPanel();
        frame.add(descriptionPanel);
        content.add(descriptionPanel);
        enterDescription = new JLabel("Enter description:");
        enterDescription.setBounds(10,20, 80, 25);
        descriptionPanel.add(enterDescription);

        descriptionField = new JTextField(20);
        descriptionField.setBounds(100, 20, 165, 25);
        descriptionPanel.add(descriptionField);

        JPanel quantityPanel = new JPanel();
        frame.add(quantityPanel);
        content.add(quantityPanel);
        enterQuantity = new JLabel("Enter quantity:");
        enterQuantity.setBounds(10,20, 80, 25);
        quantityPanel.add(enterQuantity);

        quantityField = new JTextField(20);
        quantityField.setBounds(100, 20, 165, 25);
        quantityPanel.add(quantityField);

        JPanel pricePanel = new JPanel();
        frame.add(pricePanel);
        content.add(pricePanel);
        enterPrice = new JLabel("Enter price:");
        enterPrice.setBounds(10,20, 80, 25);
        pricePanel.add(enterPrice);

        priceField = new JTextField(20);
        priceField.setBounds(100, 20, 165, 25);
        pricePanel.add(priceField);

        JPanel enterPanel = new JPanel();
        frame.add(enterPanel);
        content.add(enterPanel);
        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enterPanel.add(enter);
        enter.addActionListener(e -> {
            String name = nameField.getText();
            String store = storeField.getText();
            String description1 = descriptionField.getText();
            int quantity = Integer.valueOf(quantityField.getText());
            double price = Double.valueOf(priceField.getText());
            Item newItem = new Item(name, store, description1, quantity, price);
            serverAction(42, newItem.toLine());
            SellerMenu();
        });

        JPanel backPanel = new JPanel();
        frame.add(backPanel);
        content.add(backPanel);
        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> AddOptions());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, namePanel, storePanel);
        JSplitPane sp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, descriptionPanel, quantityPanel);
        JSplitPane sp3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, sp2);
        JSplitPane sp4 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pricePanel, enterPanel);
        JSplitPane sp5 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp4, backPanel);
        JSplitPane sp6 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp3, sp5);
        frame.add(sp6);
        content.add(sp6);

        frame.setVisible(true);
    }
    public void AddFromCSV() {
        JLabel enterFileName;
        JTextField fileNameField;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel enterPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(enterPanel);
        content.add(enterPanel);
        frame.add(backPanel);
        content.add(backPanel);

        enterFileName = new JLabel("Enter file name:");
        enterFileName.setBounds(10,20, 80, 25);
        enterPanel.add(enterFileName);

        fileNameField = new JTextField(20);
        fileNameField.setBounds(100, 20, 165, 25);
        enterPanel.add(fileNameField);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        enterPanel.add(enter);
        enter.addActionListener(e -> {
            String fileName = fileNameField.getText();
            Object o = serverAction(41, fileName); // send to server
            if (o instanceof Exception) {
                String errorMessage = ((Exception) o).getMessage();
                CSVError();
            } else {
                CSVSuccess();
                int numAdded = (int) o;
                System.out.printf("%d Items added!", numAdded);
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> AddOptions());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, enterPanel, backPanel);
        frame.add(sp);
        content.add(sp);

        frame.setVisible(true);
    }
    public void CSVSuccess() {
        //this GUI is a success message when items are added from CSV
        // TODO: show how many items were successfully added from CSV
        JLabel welcome;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        welcome = new JLabel("Items were successfully added from CSV");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton logOut = new JButton("Log out");
        logOut.setBounds(10, 80, 80, 25);
        panel.add(logOut);
        logOut.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> AddFromCSV());

        frame.setVisible(true);
    }
    public void CSVError() {
        //this GUI is an error message for when items are not added from CSV

        JLabel welcome;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        welcome = new JLabel("Error: Items were not successfully added from CSV");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton logOut = new JButton("Log out");
        logOut.setBounds(10, 80, 80, 25);
        panel.add(logOut);
        logOut.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> AddFromCSV());

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

        // show items
        ArrayList<Item> sellerItems = parseItemList((String) serverAction(40, null));
        // ArrayList<Item> sellerItems = (ArrayList<Item>) serverAction(40, null);
        JComboBox<String> dropdown = createItemDropdown(sellerItems, true, true, true);
        panel.add(dropdown);

        // JButton enter = new JButton();
        // enter.setBounds(10, 80, 80, 25);
        // panel.add(enter);
        // enter.addActionListener(e -> {
        //     int itemToEdit = dropdown.getSelectedIndex();

        // });

        options = new JLabel("Type in the new name of a characteristic of the item.");
        options.setBounds(10, 20, 80, 25);
        panel.add(options);

        theChange = new JTextField(20);
        theChange.setBounds(100, 20, 165, 25);

        change = new JLabel("Click the category button to make the change.");
        change.setBounds(10,20, 80, 25);
        panel.add(change);
        panel.add(theChange);

        name = new JButton("Name");
        name.setBounds(10, 80, 80, 25);
        panel.add(name);
        name.addActionListener(e -> {
            int itemToEdit = dropdown.getSelectedIndex();
            serverAction(43, String.format("%d,%d,%s", itemToEdit, 1, theChange.getText()));
        });

        store = new JButton("Store");
        store.setBounds(10, 80, 80, 25);
        panel.add(store);
        store.addActionListener(e -> {
            int itemToEdit = dropdown.getSelectedIndex();
            serverAction(43, String.format("%d,%d,%s", itemToEdit, 2, theChange.getText()));
        });

        description = new JButton("Description");
        description.setBounds(10, 80, 80, 25);
        panel.add(description);
        description.addActionListener(e -> {
            int itemToEdit = dropdown.getSelectedIndex();
            serverAction(43, String.format("%d,%d,%s", itemToEdit, 3, theChange.getText()));
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(e -> {
            int itemToEdit = dropdown.getSelectedIndex();
            serverAction(43, String.format("%d,%d,%s", itemToEdit, 4, theChange.getText()));
        });

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(e -> {
            int itemToEdit = dropdown.getSelectedIndex();
            serverAction(43, String.format("%d,%d,%s", itemToEdit, 5, theChange.getText()));
        });

        // JButton enterTwo = new JButton("Enter");
        // enterTwo.setBounds(10, 80, 80, 25);
        // panel.add(enterTwo);
        // enterTwo.addActionListener(e -> {
        //     String newName = theChange.getText();
        // });

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

    public void Remove() {
        JLabel chooseItem;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel choosePanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(choosePanel);
        content.add(choosePanel);
        frame.add(backPanel);
        content.add(backPanel);

        chooseItem = new JLabel("Choose an item to remove:");
        chooseItem.setBounds(10,20, 80, 25);
        choosePanel.add(chooseItem);

        // show items
        ArrayList<Item> sellerItems = parseItemList((String) serverAction(40, null));
        // ArrayList<Item> sellerItems = (ArrayList<Item>) serverAction(40, null);
        JComboBox<String> dropdown = createItemDropdown(sellerItems, true, true, true);
        choosePanel.add(dropdown);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        choosePanel.add(enter);
        enter.addActionListener(e -> {
            int itemToRemove = dropdown.getSelectedIndex();
            serverAction(44, String.format("%d", itemToRemove));
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> SellerMenu());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, choosePanel, backPanel);
        frame.add(sp);
        content.add(sp);

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

        // allStats = new JButton("View all statistics");
        // allStats.setBounds(10, 80, 80, 25);
        // panel.add(allStats);
        // allStats.addActionListener(e -> {

        // });
        ArrayList<Item> purchasedItems = parseItemList((String) serverAction(45, null));
        JComboBox dropdown = createItemDropdown(purchasedItems, true, true, true);
        panel.add(dropdown);

        specificStats = new JButton("View Sorted statistics");
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
    String priceOrQuantityChoice;
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
            serverAction(47, "1"); // set price
            SpecificStatsTwo();
            priceOrQuantityChoice = "You selected to sort the statistics by price.";
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(e -> {
            serverAction(47, "2"); // set quantity
            priceOrQuantityChoice = "You selected to sort the statistics by quantity.";
            SpecificStatsTwo();
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
    public void SpecificStatsTwo() {
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;
        JButton enter;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel priceOrQuantity = new JLabel(priceOrQuantityChoice);
        priceOrQuantity.setBounds(10,20, 80, 25);
        panel.add(priceOrQuantity);

        ascendingOrDescending = new JLabel("Sort specific statistics ascending or descending:");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        panel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        panel.add(ascending);
        ascending.addActionListener(e -> {
            serverAction(48, "1"); // set ascending
        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        panel.add(descending);
        descending.addActionListener(e -> {
            serverAction(48, "2"); // set descending
        });

        ArrayList<Item> purchasedItems = parseItemList((String) serverAction(46, null));
        JComboBox dropdown = createItemDropdown(purchasedItems, true, true, true);
        panel.add(dropdown);



        enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        panel.add(enter);
        enter.addActionListener(e -> {
            SpecificStatsTwo(); // reload;
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
    public void HaveNoStores() { // TODO: put this somewhere
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
        deleteAccount.addActionListener(e -> DeleteAccountQuestion());

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
    public void DeleteAccountQuestion() {
        //this GUI is an error message for when items are not added from CSV

        JLabel welcome;
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        welcome = new JLabel("Are you sure you want to delete your account?");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton deleteAccount = new JButton("Delete account");
        deleteAccount.setBounds(10, 80, 80, 25);
        panel.add(deleteAccount);
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                serverAction(7, null);
                WelcomeMenuGUI();
            }
        });

        JButton logOut = new JButton("Log out");
        logOut.setBounds(10, 80, 80, 25);
        panel.add(logOut);
        logOut.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(e -> AddFromCSV());

        frame.setVisible(true);
    }
    String potentialNewEmail;
    Container emailContent;
    public void SellerNewEmail() {
        JLabel enterEmail;
        JTextField emailText;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel emailPanel = new JPanel();
        JPanel enterPanel = new JPanel();
        JPanel backPanel = new JPanel();

        //content = frame.getContentPane();
        emailContent = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(emailPanel);
        emailContent.add(backPanel);
        frame.add(backPanel);
        emailContent.add(backPanel);
        frame.add(enterPanel);
        emailContent.add(enterPanel);

        enterEmail = new JLabel("Enter new email:");
        enterEmail.setBounds(10,20, 80, 25);
        emailPanel.add(enterEmail);
        potentialNewEmail = enterEmail.getText();

        emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        emailPanel.add(emailText);

        JButton enter = new JButton("Enter");
        enter.setBounds(10, 80, 80, 25);
        backPanel.add(enter);
        enter.addActionListener(e -> potentialNewEmail = emailText.getText());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> EditOptions());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, emailPanel, backPanel);
        frame.add(sp);
        content.add(sp);

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
    String potentialNewPassword;
    public void SellerNewPassword() {
        JLabel enterPassword;
        JTextField passwordText;
        JButton confirm;

        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel enterPanel = new JPanel();
        JPanel backPanel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(enterPanel);
        content.add(enterPanel);
        frame.add(backPanel);
        content.add(backPanel);

        enterPassword = new JLabel("Please enter a new password for your account:");
        enterPassword.setBounds(10,20, 80, 25);
        enterPanel.add(enterPassword);
        potentialNewPassword = enterPassword.getText();

        passwordText = new JTextField(20);
        passwordText.setBounds(100, 20, 165, 25);
        enterPanel.add(passwordText);

        confirm = new JButton("Enter");
        confirm.setBounds(10, 80, 80, 25);
        enterPanel.add(confirm);
        confirm.addActionListener(e -> {
            potentialNewPassword = passwordText.getText();
            SellerConfirmPassword();
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        backPanel.add(logout);
        logout.addActionListener(e -> ShowWelcome());

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        backPanel.add(back);
        back.addActionListener(e -> EditOptions());

        content.setLayout(new GridLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, enterPanel, backPanel);
        frame.add(sp);
        content.add(sp);

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
