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
            String input; // store the input read from the client. This should start with a 2 cahracter action number code, any additional information needed fro that action should follow
            int action; // the number of the action the server should do
            Object output = null; // the object to be sent to the client
            Seller seller = null;
            Customer customer = null;
            String info = null;

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
                l.add("hi");

                output = l;

                // Big switch statement for all the actions the server needs to perfrom

                switch (action) {
                    // 1-9: User / generic options
                    case 1 -> { // login / check login credentials
                        // buyer OR seller should be not null

                        String[] limitedInput = input.split(",");

                        String userEmail = "";
                        String userPassword = "";

                        for (int i = 0; i < limitedInput.length; i++) {
                            if (limitedInput[i].lastIndexOf("Username:") > -1) {
                                userEmail = limitedInput[i].substring(limitedInput[i].lastIndexOf("Username:"));
                            } else if (limitedInput[i].lastIndexOf("Password:") > -1)  {
                                userPassword = limitedInput[i].substring(limitedInput[i].lastIndexOf("Password:"));
                            }
                        }

                        ObjectInputStream ois = null;

                        try {
                            ois = new ObjectInputStream(new FileInputStream("file.txt"));
                            Object obj;
                            // String userEmail = /GUI textfield that has email/
                            // String userPassword = /GUI textfield that has password/

                            while ((obj = ois.readObject()) != null) {
                                if (obj instanceof Customer && ((Customer) obj).getEmail().equals(userEmail) && (((Customer) obj).getPassword().equals(userPassword))) {
                                    customer = customer;

                                    break;
                                }
                                if (obj instanceof Seller && ((Seller) obj).getEmail().equals(userEmail) && ((Seller) obj).getPassword().equals(userPassword)) {
                                    seller = seller;

                                    break;
                                }
                            }
                            ois.close();
                        } catch (EOFException eofException) {
                            // display GUI that says user not found, create an account
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    case 2 -> { // create new account

                    }
                    case 3 -> { // change username

                    }
                    case 4 -> { // change password

                    }
                    case 5 -> { // check valid email
                        //TODO: fix these so they actually check
                        output = true;
                    }
                    case 6 -> { // check if account exists
                        output = true;
                    }
                }
                if (customer != null) { // all the customer actions here
                    // 20-39: Buyer functions
                    switch (action) {
                        case 20 -> { // view all listings
                            customer.refreshListings(); // loads all listings
output = customer.getSortedItems(); // gets all the listings
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
                            seller.addFromCSV(info); // info assumed to be filename
                            output = true;
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
