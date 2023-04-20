import java.net.*;
import java.io.*;
import java.util.ArrayList;

// port 1800

public class Server implements Runnable {
    private Socket socket;
    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());


            String input; // store the input read from the client. This should start with a 2 cahracter action number code, any additional information needed fro that action should follow
            int action; // the number of the action the server should do
            Object output = null; // the object to be sent to the client
            Seller seller = null;
            Customer customer = null;
            while ((input = bfr.readLine()) != null) { // reads teh next line
                action = Integer.valueOf(input.substring(0, 2)); // TODO: exception catching here

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
                        // isCustomer or isSeller should now equal true
                        
                    }
                    case 2 -> { // create new account

                    }
                    case 3 -> { // change username

                    }
                    case 4 -> { // change password

                    }
                    // 10-19: Buyer functions
                    case 10 -> { // view all listings

                    }

                    // 20-29: Seller functions
                    case 20 -> { // view the seller's listings

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
