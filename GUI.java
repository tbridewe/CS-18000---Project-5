//I added most of the ActionListeners and added logout and back buttons to every GUI
// I am working on the rest of the ActionListeners
//I put comments beyond 120 characters for all of the unfinished ActionListeners
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.List;
import java.io.*;


public class GUI {
    static JFrame frame;
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
        Object o = null;
        try {
            o = reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            o = null;
        }
        return o;
    }

    public void ShowWelcome() {
        //this GUI is a welcome message before the welcome menu and is shown when a user logs out
        JLabel welcome;
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container welcomeContent = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        welcome = new JLabel("Welcome");
        welcome.setBounds(10, 20, 80, 25);
        panel.add(welcome);

        JButton welcomMenu = new JButton("Menu");
        welcomMenu.setBounds(10, 80, 80, 25);
        panel.add(welcomMenu);
        welcomMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeMenuGUI();
            }
        });

        frame.setVisible(true);
    }
    public void WelcomeMenuGUI() { //this GUI gives a user an option to login, create an account, or log out
        JButton login;
        JButton createAccount;
        JButton quit;
        JLabel welcomeOptions;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        JPanel panelOne = new JPanel();
        frame.setSize(350, 200);
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
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });
        createAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAccountGUI();
            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
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
        JLabel success;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JPanel loginPanel = new JPanel();
        Container welcomeContent = frame.getContentPane();
        frame.setSize(350, 200);
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

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        loginPanel.add(success);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            //TODO: HERE
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((User.isValidEmail(userText.getText())) && (User.accountExists(userText.getText()))) {
                    String user = userText.getText();
                    String password = new String(passwordText.getPassword());
                } else if ((!User.isValidEmail(userText.getText()))) {
                    EnterValidEmailAddress();
                } else if (!User.accountExists(userText.getText())) {
                    IncorrectCredentials();
                }
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeMenuGUI();
            }
        });
    }
    public void NewAccountGUI() { //this GUI allows the user to create a new account
        // after selecting that option in the welcome menu
        JLabel userLabel;
        JTextField userText;
        JLabel passwordLabel;
        JPasswordField passwordText;
        JButton makeNewAccount;
        JLabel success;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JPanel newAccountPanel = new JPanel();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(newAccountPanel);
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
        makeNewAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendToServer("05" + userText.getText());
                boolean emailIsValid = (boolean) readFromServer();
                sendToServer("06" + userText.getText());
                boolean accountExists = (boolean) readFromServer();
                if (emailIsValid && accountExists) {
                    String user = userText.getText();
                    String password = new String(passwordText.getPassword());
                    LoginSuccessful();
                } else if (!emailIsValid) {
                    EnterValidEmailAddress();
                }
            }
        });

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        newAccountPanel.add(success);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WelcomeMenuGUI();
            }
        });
        frame.setVisible(true);
    }
    public void EnterValidEmailAddress() { //shown if the email submitted in the login menu is invalid
        JLabel enterValidEmail;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        frame.setVisible(true);
    }
    public void IncorrectCredentials() {
        //shown if there is nt an account associated with the email and password submitted in the login menu
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel incorrectLoginCredentials = new JLabel
                ("Incorrect login credentials or account does not exist, please try again.");
        incorrectLoginCredentials.setBounds(10, 20, 80, 25);
        panel.add(incorrectLoginCredentials);

        JButton tryAgain = new JButton("Try again");
        tryAgain.setBounds(10, 20, 80, 25);
        panel.add(tryAgain);
        tryAgain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateOptions();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });
        frame.setVisible(true);
    }
    public void CreateOptions() {
        JLabel noAccountFound;
        JLabel selectOption;
        JButton createNewAccount;
        JButton reAttemptLogin;
        JButton back;

        frame = new JFrame();
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
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
        createNewAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAccountGUI();
            }
        });

        reAttemptLogin = new JButton("Re-attempt login");
        reAttemptLogin.setBounds(10, 80, 80, 25);
        createPanel.add(reAttemptLogin);
        reAttemptLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        createPanel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void LoginSuccessful() {
        //shown if the login is successful
        JLabel loginSuccessful;
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        loginSuccessful = new JLabel("Login successful");
        frame.setBounds(10, 20, 80, 25);
        panel.add(loginSuccessful);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });
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

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        buyerOptions = new JLabel("Please select an option:");
        buyerOptions.setBounds(10,20, 80, 25);
        panel.add(buyerOptions);

        chooseItem = new JButton("Choose an item");
        chooseItem.setBounds(10, 80, 80, 25);
        panel.add(chooseItem);
        chooseItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectItem();
            }
        });

        search = new JButton("Search");
        search.setBounds(10, 80, 80, 25);
        panel.add(search);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchByKeywordOrNot();
            }
        });

        sort = new JButton("Sort");
        sort.setBounds(10, 80, 80, 25);
        panel.add(sort);
        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SortingItems();
            }
        });

        viewCart = new JButton("View cart");
        viewCart.setBounds(10, 80, 80, 25);
        panel.add(viewCart);
        viewCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCartOptions();
            }
        });

        editAccount = new JButton("Edit account");
        editAccount.setBounds(10, 80, 80, 25);
        panel.add(editAccount);
        editAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUserInfo();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });
        frame.setVisible(true);
    }
    public void SearchByKeywordOrNot() {
        JButton searchByKeyword;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        searchByKeyword = new JButton("Search by Keyword");
        searchByKeyword.setBounds(10, 80, 80, 25);
        panel.add(searchByKeyword);
        searchByKeyword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchBar();
            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyerMenuGui();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void SearchBar() {
        JLabel search;
        JTextField searchBar;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        search = new JLabel("Search:");
        search.setBounds(10,20, 80, 25);
        panel.add(search);

        searchBar = new JTextField(20);
        searchBar.setBounds(100, 20, 165, 25);
        panel.add(searchBar);

        String keyword = new String(searchBar.getText());

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchByKeywordOrNot();
            }
        });

        frame.setVisible(true);
    }
    public void SelectItem() {
        //should print out all of the available items                                                                   ******
        //and make them easier to select
        JLabel select;
        JTextField item;
        JLabel number;
        JTextField quantity;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        select = new JLabel("Please select the item you wish to purchase:");
        select.setBounds(10,20, 80, 25);
        panel.add(select);

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

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

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyerMenuGui();
            }
        });

        frame.setVisible(true);
    }
    public void cancelPurchase() {
        JLabel cancelQuestion;
        JButton cancel;
        JButton approve;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        cancelQuestion = new JLabel("Do you want to cancel the purchase?");
        cancelQuestion.setBounds(10,20, 80, 25);
        panel.add(cancelQuestion);

        cancel = new JButton("Cancel");
        cancel.setBounds(10, 80, 80, 25);
        panel.add(cancel);
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PurchaseCancelled();
            }
        });

        approve = new JButton("Don't cancel");
        approve.setBounds(10, 80, 80, 25);
        panel.add(approve);
        approve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddedToCart();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectItem();
            }
        });

        frame.setVisible(true);
    }
    public void PurchaseCancelled() {
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();

        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel purchaseCancelled = new JLabel("Purchase cancelled");
        purchaseCancelled.setBounds(10, 20, 80, 25);
        panel.add(purchaseCancelled);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelPurchase();
            }
        });

        frame.setVisible(true);
    }
    public void AddedToCart() {
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        //%dx %s!\n", quantity, selectedItem.getName() //add info about quantity and selected item
        JLabel addedToCart = new JLabel("Added to cart");
        addedToCart.setBounds(10, 20, 80, 25);
        panel.add(addedToCart);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectItem();
            }
        });

        frame.setVisible(true);
    }
    public void InvalidQuantityException() {
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel invalidQuantity = new JLabel("Invalid Quantity");
        invalidQuantity.setBounds(10, 20, 80, 25);
        panel.add(invalidQuantity);

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectItem();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void SortingItems() {
        JLabel priceOrQuantity;
        JButton price;
        JButton quantity;
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        priceOrQuantity = new JLabel("Do you want to sort by price or quantity?");
        priceOrQuantity.setBounds(10,20, 80, 25);
        panel.add(priceOrQuantity);

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //should allow the buyer to sort items by price                                                             ******
            }
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //should allow the buyer to sort items by quantity                                                             ******
            }
        });

        ascendingOrDescending = new JLabel("Do you want to sort ascending or descending?");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        panel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        panel.add(ascending);
        ascending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //should allow the buyer to sort items ascending                                                             ******
            }
        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        panel.add(descending);
        descending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //should allow the buyer to sort items descending                                                             ******
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyerMenuGui();
            }
        });

        frame.setVisible(true);
    }
    public void ViewCartOptions() {
        JLabel choose;
        JButton checkout;
        JButton view;
        JButton remove;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        checkout = new JButton("Checkout");
        checkout.setBounds(10, 80, 80, 25);
        panel.add(checkout);
        checkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckoutComplete();
            }
        });

        view = new JButton("View Purchase History");
        view.setBounds(10, 80, 80, 25);
        panel.add(view);
        view.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //view purchase history                                                                                     ***
            }
        });

        remove = new JButton("Remove Item");
        remove.setBounds(10, 80, 80, 25);
        panel.add(remove);
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RemoveItem();
            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyerMenuGui();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void CheckoutComplete() { //if the buyer chooses to checkout and checks out successfully
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel checkoutComplete = new JLabel("Checkout complete!");
        checkoutComplete.setBounds(10, 20, 80, 25);
        panel.add(checkoutComplete);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCartOptions();
            }
        });

        frame.setVisible(true);
    }
    public void ExportPurchaseHistory() {
        JLabel export;
        JButton yes;
        JButton no;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        export = new JLabel("Would you like to export purchase history?");
        export.setBounds(10,20, 80, 25);
        panel.add(export);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnterNameOfFile();
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCartOptions();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });                                                                                                                     //back***

        frame.setVisible(true);
    }
    public void EnterNameOfFile() {
        JLabel nameFile;
        JTextField file;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        nameFile = new JLabel("Enter the name of the file to save the purchase history to:");
        nameFile.setBounds(10,20, 80, 25);
        panel.add(nameFile);

        file = new JTextField(20);
        file.setBounds(100, 20, 165, 25);
        panel.add(file);
        String filename = file.getText();

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnterNameOfFile();
            }
        });

        frame.setVisible(true);
    }
    public void PurchaseHistoryExported() {
        //message shown if the buyer successfully saves purchase history to file
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel exportSuccess = new JLabel("Purchase history successfully exported to file!");
        exportSuccess.setBounds(10, 20, 80, 25);
        panel.add(exportSuccess);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnterNameOfFile();
            }
        });

        frame.setVisible(true);
    }
    public void RemoveItem() {
        //should print out all items in the cart so the buyer can select one                                                ***
        JLabel select;
        JTextField item;
        JLabel number;
        JTextField quantity;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        select = new JLabel("Enter an item to remove from cart:");
        select.setBounds(10,20, 80, 25);
        panel.add(select);
        //should save the item as a variable                                                                                ***

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        number = new JLabel("Please enter how many you would like to remove:");
        number.setBounds(10,20, 80, 25);
        panel.add(number);
        //should save the number as a variable                                                                              ***

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        panel.add(quantity);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCartOptions();
            }
        });

        frame.setVisible(true);
    }
    public void EditUserInfo() {
        JLabel choose;
        JButton editEmail;
        JButton editPassword;
        JButton delete;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        editEmail = new JButton("Edit Account Email");
        editEmail.setBounds(10, 80, 80, 25);
        panel.add(editEmail);
        editEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewEmail();
            }
        });

        editPassword = new JButton("Edit Account Password");                                                            //what is a valid password?
        editPassword.setBounds(10, 80, 80, 25);
        panel.add(editPassword);
        editPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(editPassword.getText());
            }
        });

        delete = new JButton("Delete Account");
        delete.setBounds(10, 80, 80, 25);
        panel.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BuyerMenuGui();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    static String emailTextString;
    public void NewEmail() {
        JLabel enterEmail;
        JTextField emailText;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
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
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfirmEmail();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUserInfo();
            }
        });

        frame.setVisible(true);
    }
    public void ConfirmEmail() {
        JLabel editEmail;
        JButton yes;
        JButton no;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        editEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
        editEmail.setBounds(10,20, 80, 25);
        panel.add(editEmail);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((User.isValidEmail(emailTextString) &&
                        (User.accountExists(emailTextString)))) {
                    String user = emailTextString;
                    LoginSuccessful();
                } else if ((!User.isValidEmail(emailTextString))) {
                    EnterValidEmailAddress();
                }
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewEmail();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUserInfo();
            }
        });

        frame.setVisible(true);
    }
    static String passwordTextString;
    public void NewPassword() {
        JLabel enterPassword;
        JTextField passwordText;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
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
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfirmPassword();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditUserInfo();
            }
        });

        frame.setVisible(true);
    }
    public void ConfirmPassword() {
        JLabel areYouSure;
        JButton yes;
        JButton no;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
        areYouSure.setBounds(10,20, 80, 25);
        panel.add(areYouSure);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = passwordTextString;
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewPassword();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewPassword();
            }
        });

        frame.setVisible(true);
    }
    public void SellerMenu() {
        JLabel choose;
        JButton viewListings;
        JButton viewStatistics;
        JButton editAccount;
        JButton logOut;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Chose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        viewListings = new JButton("View listings");
        viewListings.setBounds(10, 80, 80, 25);
        panel.add(viewListings);
        viewListings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListingsMenu();
            }
        });

        viewStatistics = new JButton("View statistics");
        viewStatistics.setBounds(10, 80, 80, 25);
        panel.add(viewStatistics);
        viewStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewStatistics();
            }
        });

        editAccount = new JButton("Edit account");
        editAccount.setBounds(10, 80, 80, 25);
        panel.add(editAccount);
        editAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditOptions();
            }
        });

        logOut = new JButton("Log out");
        logOut.setBounds(10, 80, 80, 25);
        panel.add(logOut);
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGui();
            }
        });

        frame.setVisible(true);
    }
    public void ListingsMenu() {
        JLabel choose;
        JButton add;
        JButton edit;
        JButton delete;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Chose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        add = new JButton("Add");
        add.setBounds(10, 80, 80, 25);
        panel.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddOptions();
            }
        });

        edit = new JButton("Edit");
        edit.setBounds(10, 80, 80, 25);
        panel.add(edit);
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChooseItemToEdit();
            }
        });

        delete = new JButton("Delete");
        delete.setBounds(10, 80, 80, 25);
        panel.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Remove();
            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerMenu();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void AddOptions() {
        JButton add;
        JButton csv;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        add = new JButton("Add item");
        add.setBounds(10, 80, 80, 25);
        panel.add(add);
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddItem();
            }
        });

        csv = new JButton("Add from CSV");
        csv.setBounds(10, 80, 80, 25);
        panel.add(csv);
        csv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddFromCSV();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListingsMenu();
            }
        });

        frame.setVisible(true);
    }
    public void AddItem() {
        JLabel enterName;
        JTextField name;
        JLabel enterStore;
        JTextField store;
        JLabel enterDescription;
        JTextField description;
        JLabel enterQuantity;
        JTextField quantity;
        JLabel enterPrice;
        JTextField price;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterName = new JLabel("Enter name:");
        enterName.setBounds(10,20, 80, 25);
        panel.add(enterName);

        name = new JTextField(20);
        name.setBounds(100, 20, 165, 25);
        panel.add(name);

        enterStore = new JLabel("Enter store:");
        enterStore.setBounds(10,20, 80, 25);
        panel.add(enterStore);

        store = new JTextField(20);
        store.setBounds(100, 20, 165, 25);
        panel.add(store);

        enterDescription = new JLabel("Enter description:");
        enterDescription.setBounds(10,20, 80, 25);
        panel.add(enterDescription);

        description = new JTextField(20);
        description.setBounds(100, 20, 165, 25);
        panel.add(description);

        enterQuantity = new JLabel("Enter quantity:");
        enterQuantity.setBounds(10,20, 80, 25);
        panel.add(enterQuantity);

        quantity = new JTextField(20);
        quantity.setBounds(100, 20, 165, 25);
        panel.add(quantity);

        enterPrice = new JLabel("Enter price:");
        enterPrice.setBounds(10,20, 80, 25);
        panel.add(enterPrice);

        price = new JTextField(20);
        price.setBounds(100, 20, 165, 25);
        panel.add(price);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddOptions();
            }
        });

        frame.setVisible(true);
    }
    public void AddFromCSV() {
        JLabel enterFileName;
        JTextField fileName;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterFileName = new JLabel("Enter file name:");
        enterFileName.setBounds(10,20, 80, 25);
        panel.add(enterFileName);

        fileName = new JTextField(20);
        fileName.setBounds(100, 20, 165, 25);
        panel.add(fileName);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddOptions();
            }
        });

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

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        chooseItem = new JLabel("Choose an item to edit:");
        chooseItem.setBounds(10,20, 80, 25);
        panel.add(chooseItem);

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        options = new JLabel("What would you like to change?");
        options.setBounds(10, 20, 80, 25);
        panel.add(options);

        name = new JButton("Name");
        name.setBounds(10, 80, 80, 25);
        panel.add(name);
        name.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        store = new JButton("Store");
        store.setBounds(10, 80, 80, 25);
        panel.add(store);
        store.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        description = new JButton("Description");
        description.setBounds(10, 80, 80, 25);
        panel.add(description);
        description.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        quantity = new JButton("Quantity");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        change = new JLabel("What would you like to change it to?");
        change.setBounds(10,20, 80, 25);
        panel.add(change);

        theChange = new JTextField(20);
        theChange.setBounds(100, 20, 165, 25);
        panel.add(theChange);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListingsMenu();
            }
        });

        frame.setVisible(true);
    }
    public void Remove() {                                                               //(also need to print the cart) ****
        JLabel chooseItem;
        JTextField item;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        chooseItem = new JLabel("Choose an item to remove:");
        chooseItem.setBounds(10,20, 80, 25);
        panel.add(chooseItem);

        item = new JTextField(20);
        item.setBounds(100, 20, 165, 25);
        panel.add(item);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerMenu();
            }
        });

        frame.setVisible(true);
    }
    public void ViewStatistics() {
        JLabel choose;
        JButton allStats;
        JButton specificStats;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        allStats = new JButton("View all statistics");
        allStats.setBounds(10, 80, 80, 25);
        panel.add(allStats);
        allStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        specificStats = new JButton("View specific statistics");
        specificStats.setBounds(10, 80, 80, 25);
        panel.add(specificStats);
        specificStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SpecificStats();
            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerMenu();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    public void SpecificStats() {
        JLabel byPriceOrQuantity;
        JButton price;
        JButton quantity;
        JLabel ascendingOrDescending;
        JButton ascending;
        JButton descending;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        byPriceOrQuantity = new JLabel("Sort specific statistics by price or quantity:");
        byPriceOrQuantity.setBounds(10,20, 80, 25);
        panel.add(byPriceOrQuantity);

        price = new JButton("Price");
        price.setBounds(10, 80, 80, 25);
        panel.add(price);
        price.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        quantity = new JButton("Price");
        quantity.setBounds(10, 80, 80, 25);
        panel.add(quantity);
        quantity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        ascendingOrDescending = new JLabel("Sort specific statistics ascending or descending:");
        ascendingOrDescending.setBounds(10,20, 80, 25);
        panel.add(ascendingOrDescending);

        ascending = new JButton("Ascending");
        ascending.setBounds(10, 80, 80, 25);
        panel.add(ascending);
        ascending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        descending = new JButton("Descending");
        descending.setBounds(10, 80, 80, 25);
        panel.add(descending);
        descending.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewStatistics();
            }
        });

        frame.setVisible(true);
    }
    public void HaveNoStores() {
        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        JLabel noStores = new JLabel("You have no stores!");
        noStores.setBounds(10, 20, 80, 25);
        panel.add(noStores);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });                                                                                                                     //back***

        frame.setVisible(true);
    }
    public void EditOptions() {
        JLabel choose;
        JButton editEmail;
        JButton editPassword;
        JButton deleteAccount;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        choose = new JLabel("Choose an option:");
        choose.setBounds(10,20, 80, 25);
        panel.add(choose);

        editEmail = new JButton("Edit account email");
        editEmail.setBounds(10, 80, 80, 25);
        panel.add(editEmail);
        editEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewEmail();
            }
        });

        editPassword = new JButton("Edit account password");
        editPassword.setBounds(10, 80, 80, 25);
        panel.add(editPassword);
        editPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewPassword();
            }
        });

        deleteAccount = new JButton("Delete account");
        deleteAccount.setBounds(10, 80, 80, 25);
        panel.add(deleteAccount);
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerMenu();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });
        frame.setVisible(true);
    }
    static String enterEmailString;
    public void SellerNewEmail() {
        JLabel enterEmail;
        JTextField emailText;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterEmail = new JLabel("Please enter a new email address for your account:");
        enterEmail.setBounds(10,20, 80, 25);
        panel.add(enterEmail);
        enterEmailString = enterEmail.getText();

        emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditOptions();
            }
        });

        frame.setVisible(true);
    }
    public void SellerConfirmEmail() {
        JLabel editSellerEmail;
        JButton yes;
        JButton no;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        editSellerEmail = new JLabel("Are you sure you would like to edit your email?  Choose yes or no:");
        editSellerEmail.setBounds(10,20, 80, 25);
        panel.add(editSellerEmail);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yes.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if ((User.isValidEmail(enterEmailString) &&
                                (User.accountExists(enterEmailString)))) {
                            String email = enterEmailString;
                            LoginSuccessful();
                        } else if ((!User.isValidEmail(enterEmailString))) {
                            SellerEnterValidEmailAddress();
                        }
                    }
                });
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewEmail();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewEmail();
            }
        });

        frame.setVisible(true);
    }

    public void SellerEnterValidEmailAddress() {
        //shown if the email submitted in SellerConfirmEmail() is invalid
        JLabel enterValidEmail;
        JButton back;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterValidEmail = new JLabel("Please enter a valid email address");
        enterValidEmail.setBounds(10, 20, 80, 25);
        panel.add(enterValidEmail);

        back = new JButton("Back");
        back.setBounds(10, 20, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewEmail();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        frame.setVisible(true);
    }
    static String enterPasswordString;
    public void SellerNewPassword() {
        JLabel enterPassword;
        JTextField passwordText;
        JButton confirm;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        enterPassword = new JLabel("Please enter a new password for your account:");
        enterPassword.setBounds(10,20, 80, 25);
        panel.add(enterPassword);
        enterPasswordString = enterPassword.getText();

        confirm = new JButton("Confirm");
        confirm.setBounds(10, 80, 80, 25);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerConfirmPassword();
            }
        });

        passwordText = new JTextField(20);
        passwordText.setBounds(100, 20, 165, 25);
        panel.add(passwordText);

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditOptions();
            }
        });

        frame.setVisible(true);
    }
    public void SellerConfirmPassword() {
        JLabel areYouSure;
        JButton yes;
        JButton no;

        frame = new JFrame();
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        JPanel panel = new JPanel();
        Container content = frame.getContentPane();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        areYouSure = new JLabel("Are you sure you would like to edit your password?  Choose yes or no:");
        areYouSure.setBounds(10,20, 80, 25);
        panel.add(areYouSure);

        yes = new JButton("Yes");
        yes.setBounds(10, 80, 80, 25);
        panel.add(yes);
        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = enterPasswordString;
            }
        });

        no = new JButton("No");
        no.setBounds(10, 80, 80, 25);
        panel.add(no);
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewPassword();
            }
        });

        JButton logout = new JButton("Log out");
        logout.setBounds(10, 80, 80, 25);
        panel.add(logout);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowWelcome();
            }
        });

        JButton back = new JButton("Back");
        back.setBounds(10, 80, 80, 25);
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SellerNewPassword();
            }
        });

        frame.setVisible(true);
    }
}
