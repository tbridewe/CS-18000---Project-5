import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

// port 1800

public class Server implements Runnable {
    private Socket socket;
    private String userEmail;
    private String userPassword;
    static ArrayList<Object> usersList; // stores list of users from usersData so they can be edited and rewritten to the file
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


                // sort type vars
                int sortType = 0;
                int sortOrder = 0;
                // Big switch statement for all the actions the server needs to perfrom

                switch (action) {
                    // 1-9: User / generic options
                    case 1 -> { // login / check login credentials
                        // buyer OR seller should be not null

                        String[] s = info.split(",");
                        String email = s[1];
                        String password = s[2];

                        System.out.printf("Email logging in: %s, Password logging in: %s\n", email, password);

                        try {
                            // ois = new ObjectInputStream(new FileInputStream("userData.txt"));
                            // Object obj;

                            // String userEmail = /GUI textfield that has email/
                            // String userPassword = /GUI textfield that has password/

                            Object f = User.passwordIsCorrect(email, password, Server.usersList);

                            //System.out.println(f.getClass());

                            if (f instanceof Customer) {
                                customer = (Customer) f;
                            } else if (f instanceof Seller) {
                                seller = (Seller) f;
                            }

                            // ois.close();
                            // TODO: display GUI that says user not found, create an account
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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
                                customer = new Customer(email, password, userType);
                                Server.usersList.add(customer);
                            } else if (userType == 1) {
                                seller = new Seller(email, password, userType);
                                Server.usersList.add(seller);
                            }

                            FileFunctions.writeUsersToFile(userData);
                        } catch (InvalidUserInput e) {
                        }

                        // TODO: save user data
                        // User.saveNewUser(email, password, userType);
                        output = "New account created";
                    }
                    // case 3 -> { // change username
                    //     String[] limitedInput = input.split(",");

                    //     String oldEmail = "";
                    //     String userPassword = "";
                    //     String newEmail = "";

                    //     for (int i = 0; i < limitedInput.length; i++) {
                    //         if (limitedInput[i].lastIndexOf("OldEmail:") > -1) {
                    //             oldEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("OldEmail:"));
                    //         } else if (limitedInput[i].lastIndexOf("Password:") > -1)  {
                    //             userPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("Password:"));
                    //         }  else if (limitedInput[i].lastIndexOf("NewEmail:") > -1) {
                    //             newEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("NewEmail:"));
                    //         }
                    //     }

                    //     if (User.isCorrectLogin(oldEmail, userPassword) > -1)  {
                    //         // account attempted to change exists
                    //         // assuming that they are already logged in

                    //         try {
                    //             if (customer != null) {
                    //                 // edit customer

                    //                 customer.setEmail(newEmail);
                    //             } else if (seller != null) {
                    //                 // edit seller

                    //                 seller.setEmail(newEmail);

                    //             }

                    //             // save new account information inside of the try bracket
                    //         } catch (InvalidUserInput e) {
                    //             // display to the user that they entered an invalid email
                    //         }
                    //     } else {
                    //         // account attempted to change does not exist
                    //     }
                    // }
                    case 3 -> { // change username
                        String password = info;

                        boolean success = true;

                        if (customer != null) {
                            try {
                                customer.setPassword(password);
                            } catch (InvalidUserInput e)  {
                                success = false;
                                // display error message for wrong email?
                            }
                        } else if (seller != null) {
                            try {
                                seller.setPassword(password);
                            } catch (InvalidUserInput e) {
                                success = false;
                                // display error message for wrong email?
                            }
                        }

                        if (success) {
                            FileFunctions.writeUsersToFile(userData);
                        }
                    }
                    case 4 -> { // change password
                        String email = info;

                        boolean success = true;

                        if (customer != null) {
                            try {
                                customer.setEmail(email);
                            } catch (InvalidUserInput e)  {
                                success = false;
                                // display error message for wrong email?
                            }
                        } else if (seller != null) {
                            try {
                                seller.setEmail(email);
                            } catch (InvalidUserInput e) {
                                success = false;
                                // display error message for wrong email?
                            }
                        }

                        if (success) {
                            FileFunctions.writeUsersToFile(userData);
                        }
                    }
                    case 5 -> { // check valid email
                        //info = username string
                        output = User.isValidEmail(info);
                    }
                    case 6 -> { // check if account exists
                        // info = username strings

                        boolean result = User.accountObjectExists(info, Server.usersList);
                        output = result;

                        System.out.printf("Result: %b\n", result);
                        System.out.printf("Output is: %b\n", output);
                    }
                    case 7 -> { // delete account
                        if (customer != null || seller != null) {
                            for (int i = 0; i < Server.usersList.size(); i++) {
                                Object obj = Server.usersList.get(i);

                                if (obj instanceof Customer && customer != null) {
                                    Customer temp = (Customer) obj;

                                    if (temp.getEmail().equals(seller.getEmail())) {
                                        Server.usersList.remove(obj);
                                        
                                        customer = null;
                                        
                                        break;
                                    }
                                } else if (obj instanceof Seller && seller != null) {
                                    Seller temp = (Seller) obj;

                                    if (temp.getEmail().equals(seller.getEmail())) {
                                        Server.usersList.remove(obj);
                                        
                                        seller = null;

                                        break;
                                    }
                                }
                            }
                        }

                        FileFunctions.writeUsersToFile(userData);
                    }
                    case 9 -> { // log out
                        //TODO: save the data
                        seller = null;
                        customer  = null;
                    }
                    case 50 -> { // test
                        output = "test message";
                    }
                }
                if (customer != null) { // all the customer actions here
                    // cusomter specific vars

                    // 20-39: Buyer functions
                    switch (action) {
                        case 20 -> { // view all listings
                            customer.refreshListings(); // loads all listings
                            output = customer.getSortedItems(); // gets all the listings
                        }
                        case 21 -> { // sort listings by previously sent order and type
                            customer.updatedSortMarketplace(sortType, sortOrder);
                            output = ItemListToString(customer.getSortedItems());

                        }
                        case 22 -> { // keyword search
                            customer.keywordSearch(info);
                            output = customer.getSortedItems();
                        }
                        case 23 -> { // view cart
                            output = ItemListToString(customer.getCart());
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
                            String[] s = info.split(",");
                            int i = Integer.valueOf(s[0]);
                            int q = Integer.valueOf(s[1]); 
                            customer.removeFromCart(i, q);
                        }
                        case 26 -> { // view purchase log
                            output = ItemListToString(customer.getPurchases());
                        }
                        case 27 -> { // export purchase log
                            customer.exportPurchases(info);

                        }
                        case 28 -> { // checkout
                            output = customer.getCartPrice(); // get the total price
                            customer.checkout(); // moves cart items to purcahse history. 
                        }
                        case 30 -> { // get sorted listings
                            output = ItemListToString(customer.getSortedItems());
                            // output = customer.getSortedItems();
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
                            // output = seller.getSortedItems(); // gets the filtered items
                            output = ItemListToString(seller.getSortedItems());
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
                        case 43 -> { // edit item
                            try {
                                String[] s = info.split(",");
                                int i = Integer.valueOf(s[0]);
                                Item item = seller.getDisplayedItem(i);
                                int changetype = Integer.valueOf(s[1]);
                                seller.editItem(item, changetype, s[2]);
                                seller.replaceItem(i, item);
                            } catch(Exception e) {
                                output = e;
                            }
                        }
                        case 44 -> { // remove item
                            int i = Integer.valueOf(info); // info is the displayed index of the item to be deleted
                            seller.removeItem(i);
                            output = "removed item " + i + "";
                        }
                        case 45 -> { // view all stats
                            output = seller.updatedViewAllStats(Server.usersList);

                        }
                        case 46 -> { // view sorted stats
                            output = seller.updatedSortStats(sortType, sortOrder, Server.usersList);
                            // output = ItemListToString(seller.getSortedItems());
                        }
                        case 47 -> { // set sort type
                            sortType = Integer.valueOf(info);
                        }
                        case 48 -> { // set sort order
                            sortOrder = Integer.valueOf(info);
                        }
                        
                    }

                }
                
                objectOut.writeObject(output);
                objectOut.flush();
                // Print statements for debugging
                System.out.printf("Server Sent: %s\n", output.toString());
                FileFunctions.writeUsersToFile(userData); // save user data

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
                FileFunctions.writeUsersToFile(userData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(1800);
            usersList = new ArrayList<>();

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userData));

                while (true) {
                    Object obj = ois.readObject();

                    if (obj != null) {
                        boolean valid = false;

                        if (obj instanceof Customer) {
                            valid = true;

                            Customer temp = (Customer) obj;

                            //System.out.printf("Account with username: %s and password: %s is a customer!\n", temp.getEmail(), temp.getPassword());
                        } else if (obj instanceof Seller) {
                            valid = true;

                            Seller temp = (Seller) obj;

                            //System.out.printf("Account with username: %s and password: %s is a customer!\n", temp.getEmail(), temp.getPassword());
                        }

                        if (valid) {
                            usersList.add(obj);
                        }
                    }

                    usersList.add(obj);
                }
            } catch (EOFException e) {
                // file ended?
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Thread shutdown = new Thread(() -> {
                System.out.println("Saving users to file after server close!");

                FileFunctions.writeUsersToFile(userData);
            });

            Runtime.getRuntime().addShutdownHook(shutdown);

            while(true) {
                Socket socket = ss.accept();

                Thread toRun = new Thread(new Server(socket));

                toRun.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String ItemListToString(ArrayList<Item> items) {
        String s = ";";
        if (items.size() > 0) {
            s += items.get(0).toLine();
            if (items.size() > 1) {
                for (int i = 1; i < items.size(); i++) {
                    s += ";" + items.get(i).toLine();
                } 
            }
        }
        System.out.printf("%d Items\n", items.size());
        return s;
    }

    // public ArrayList<Item> getAllPurchases() {
    //     ArrayList<Item> purchases = new ArrayList<>();
    //     for (int i = 0; i < this.usersList.size(); i++) { // for each user
    //         if(this.usersList.get(i) instanceof Customer) { // check if customer
    //             Customer c = (Customer) this.usersList.get(i); // add each purchase
    //             ArrayList<Item> p = c.getPurchases();
    //             for (int j = 0; j < p.size(); j++) {
    //                 purchases.add(p.get(j));
    //             }
    //         }
    //     }
    //     return purchases;
    // }
}
