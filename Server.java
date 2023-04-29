import java.net.*;
import java.io.*;
import java.util.ArrayList;

// port 1800

public class Server implements Runnable {
    private Socket socket;
    private String userEmail;
    private String userPassword;
    private final static String userData = "userData.txt";
    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        String input; // store the input read from the client. This should start with a 2 cahracter action number code, any additional information needed fro that action should follow
        int action; // the number of the action the server should do
        Object output = null; // the object to be sent to the client
        Seller seller = null;
        Customer customer = null;
        String info = null;

        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());

            /*
             * HOW THE SERVER CLIENT MESSAGES WORK
             * The client will send a string to the server.
             * The first two characters will be an integer corresponding to an action. This must be 2 characters (use leading 0)
             * Any additional information needed is added to the string immediately behind these characters.
             *
             * OUTPUT:
             * output is an object that is passed back to the client
             * for actions that don't need an output the ouput will be true if the action happens sucessfully and ?something? if there is an error
             */
            

            while ((input = bfr.readLine()) != null) { // reads the next line
                action = Integer.valueOf(input.substring(0, 2)); // TODO: exception catching here
                if (input.length() > 2) { // checks for any other info in the client's message
                    info = input.substring(2); // the remainder of the input string
                } else {
                    info = null; // no more info
                }

                // Print statements for debugging
                System.out.printf("Server Received: %s\n", input);
                // output = new Item("banana", "store1", "fruit", 7, 1.25);
                ArrayList<String> l= new ArrayList<>();
                l.add("hi. Nothing happening here");

                output = l;

                // Big switch statement for all the actions the server needs to perfrom

                switch (action) {
                    // 1-9: User / generic options
                    case 1 -> { // login / check login credentials
                        // buyer OR seller should be not null

                        String[] limitedInput = input.split(",");

                        String userEmail = ""; //USERNAME: email
                        String userPassword = ""; //PASSWORD: password

                        for (int i = 0; i < limitedInput.length; i++) {
                            if (limitedInput[i].lastIndexOf("Username:") > -1) {
                                userEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("Username:"));
                            } else if (limitedInput[i].lastIndexOf("Password:") > -1)  {
                                userPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("Password:"));
                            }
                        }

                        // fix the weird formatting (separate Username: username)
                        userEmail = userEmail.split(": ")[1];
                        userPassword = userPassword.split(": ")[1];

                        // ObjectInputStream ois = null;
                        System.out.printf("Username:%s\nPassword:%s.\n", userEmail, userPassword);

                        try {
                            // ois = new ObjectInputStream(new FileInputStream("userData.txt"));
                            // Object obj;

                            // String userEmail = /GUI textfield that has email/
                            // String userPassword = /GUI textfield that has password/

                            Object[] users =  FileFunctions.readObjectsFromFile(userData); // use the nice functions we made!
                            // TODO: the nice function^ is broken :(
                            for (int i = 0; i < users.length; i++) { 
                                Object obj = users[i];
                                if (obj instanceof Customer && ((Customer) obj).getEmail().equals(userEmail) && (((Customer) obj).getPassword().equals(userPassword))) {
                                    customer = (Customer) obj;

                                    break;
                                }
                                if (obj instanceof Seller && ((Seller) obj).getEmail().equals(userEmail) && ((Seller) obj).getPassword().equals(userPassword)) {
                                    seller = (Seller) obj;

                                    break;
                                }
                            }

                            

                            // ois.close();
                            // TODO: display GUI that says user not found, create an account
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //////// DEBUG LOGIN OVERRIDE ///////
                        try {

                            if (userEmail.equals("seller@gmail.com") && userPassword.equals("test")) {
                                seller = new Seller("seller@gmail.com", "test", 1);
                                seller.addStore("store1");
                                System.out.println("Login Override as seller");
                            }
                            if (userEmail.equals("customer@gmail.com") && userPassword.equals("test")) {
                                customer = new Customer("customer@gmail.com", "test", 0);
                                System.out.println("Login Override as customer");
                            }
                        } catch (InvalidUserInput e) {
                            System.out.println("How did you break the debug override case???");
                            e.printStackTrace();
                        }
                        //////// DEBUG LOGIN OVERRIDE ///////

                        // response to client
                        if (seller != null) {
                            output = 1;
                        } else if (customer != null) {
                            output = 0;
                        } else {
                            output = -1;
                        }

                    }
                    case 2 -> { // create new account
                        // 0 = customer
                        // 1 = seller
                        //info = `username,password,usertype`
                        String[] s = info.split(",");
                        String email = s[0];
                        String password = s[1];
                        int userType = Integer.valueOf(s[2]);
                        try {
                        if (userType == 0) {
                            User.updatedSaveNewCustomer(email, password);
                            customer = new Customer(email, password, userType);
                        } else if (userType == 1) {
                            User.updatedSaveNewSeller(email, password);
                            seller = new Seller(email, password, userType);
                        }
                        } catch (InvalidUserInput e) {
                        }
                        // TODO: save user data
                        // User.saveNewUser(email, password, userType);
                        output = "New account created";
                    }
                    case 3 -> { // change username
                        String[] limitedInput = input.split(",");

                        String oldEmail = "";
                        String userPassword = "";
                        String newEmail = "";

                        for (int i = 0; i < limitedInput.length; i++) {
                            if (limitedInput[i].lastIndexOf("OldEmail:") > -1) {
                                oldEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("OldEmail:"));
                            } else if (limitedInput[i].lastIndexOf("Password:") > -1)  {
                                userPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("Password:"));
                            }  else if (limitedInput[i].lastIndexOf("NewEmail:") > -1) {
                                newEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("NewEmail:"));
                            }
                        }
                        
                        if (User.isCorrectLogin(oldEmail, userPassword) > -1)  {
                            // account attempted to change exists
                            // assuming that they are already logged in
                            
                            try {
                                if (customer != null) {
                                    // edit customer
                                    
                                    customer.setEmail(newEmail);
                                } else if (seller != null) {
                                    // edit seller
                                    
                                    seller.setEmail(newEmail);
                                    
                                }
                                
                                // save new account information inside of the try bracket
                            } catch (InvalidUserInput e) {
                                // display to the user that they entered an invalid email
                            }
                        } else {
                            // account attempted to change does not exist
                        }
                    }
                    case 4 -> { // change password
                        String[] limitedInput = input.split(",");

                        String userEmail = "";
                        String oldPassword = "";
                        String newPassword = "";

                        for (int i = 0; i < limitedInput.length; i++) {
                            if (limitedInput[i].lastIndexOf("Username:") > -1) {
                                userEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("Username:"));
                            } else if (limitedInput[i].lastIndexOf("NewPassword:") > -1)  {
                                newPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("NewPassword:"));
                            }  else if (limitedInput[i].lastIndexOf("OldPassword:") > -1) {
                                oldPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("OldPassword:"));
                            }
                        }

                        if (User.isCorrectLogin(userEmail, oldPassword) > -1)  {
                            // account attempted to change exists
                            // assuming that they are already logged in

                            try {
                                if (customer != null) {
                                    // edit customer

                                    customer.setPassword(newPassword);
                                } else if (seller != null) {
                                    // edit seller

                                    seller.setPassword(newPassword);
                                }

                                // save new account information inside of the try bracket... is there a method that exists for this?
                            } catch (InvalidUserInput e) {
                                // display to the user that they entered an invalid email
                            }
                        } else {
                            // account attempted to change does not exist
                        }
                    }
                    case 5 -> { // check valid email
                        //info = username string
                        output = User.isValidEmail(info);
                    }
                    case 6 -> { // check if account exists
                        // info = username strings
                        output = User.accountExists(info);

                        // DEBUG LOGIN OVERRIDE
                        if (info == "seller@gmail.com" || info == "customer@gmail.com"); {
                            output = true;
                        }
                    }
                    case 7 -> { // delete account

                    }
                    case 9 -> { // log out
                        //TODO: save the data
                        seller = null;
                        customer  = null;
                    }
                }
                if (customer != null) { // all the customer actions here
                    // cusomter specific vars
                    int sortType;
                    int sortOrder;
                    // 20-39: Buyer functions
                    switch (action) {
                        case 20 -> { // view all listings
                            customer.refreshListings(); // loads all listings
                            output = customer.getSortedItems(); // gets all the listings
                        }
                        case 21 -> { // sort listings by `info`
                            customer.sortMarketplace(action, action);
                            output = customer.getSortedItems();

                        }
                        case 22 -> { // keyword search
                            customer.keywordSearch(info);
                            output = customer.getSortedItems();
                        }
                        case 23 -> { // view cart

                        }
                        case 24 -> { // add to cart
                            // info = displayedIndex,quanitity
                            String[] s = info.split(",");
                            int i = Integer.valueOf(s[0]);
                            int q = Integer.valueOf(s[1]);
                            Item item = customer.getDisplayedItem(i);
                            try {
                                customer.addToCart(item, q);
                                output = true;
                            } catch (InvalidQuantityException e) {
                                output = e;
                            }
                        }
                        case 25 -> { // remove from cart

                        }
                        case 26 -> { // view purchase log

                        }
                        case 27 -> { // export purchase log

                        }
                        case 28 -> { // checkout
                            output = customer.getCartPrice(); // get the total price
                            customer.checkout(); // moves cart items to purcahse history. 
                        }
                        case 31 -> { // set sort type
                            sortType = Integer.valueOf(info);
                        }
                        case 32 -> { // set sort order
                            sortOrder = Integer.valueOf(info);
                        }


                    }
                } else if (seller != null) {
                    switch (action) {
                        // 40-59: Seller functions
                        case 40 -> { // view the seller's listings
                            seller.findSellerItems(); // filters all the items to only the seller
                            output = seller.getSortedItems(); // gets the filtered items
                        }
                        case 41 -> { // add items from csv
                            try {
                                output = seller.addFromCSV(info); // info assumed to be filename, output is number of items added;
                            } catch (FileNotFoundException e) {
                                output = e;
                            }
                        }
                        case 42 -> { //add new individual item
                            try {
                                Item item = new Item(info); // info assumed to be Item.toLine() of new item
                                seller.addNewItem(item);
                            } catch(InvalidLineException e) {
                                output = e;
                            }
                        }
                        case 43 -> {
                            //TODO: check with GUI methods for this
                            // use editItem or replaceitem
                            try {
                                Item item = new Item(info); // info assumed to be Item.toLine() of new item
                                seller.replaceItem(action, item);
                            } catch(InvalidLineException e) {
                                output = e;
                            }
                        }
                        case 44 -> { // remove item
                            int i = Integer.valueOf(info); // info is the displayed index of the item to be deleted
                            seller.removeItem(i);
                        }
                        case 45 -> { // TODO: stats stuff

                        }

                    }

                }
                objectOut.writeObject(output);
                // Print statements for debugging
                System.out.printf("Server Sent: %s\n", output.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (e instanceof SocketException) { // user disconnects/closes gui
                // TODO: save the data
                System.out.println("User disconnected");
                seller = null;
                customer  = null;
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            e.printStackTrace();
        }
    }
}
