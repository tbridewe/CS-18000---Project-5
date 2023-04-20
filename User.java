import java.io.*;
import java.util.ArrayList;
/**
 * User.java
 * 
 * User class. It contains basic account methods, 
 * along with item management methods used by both buyers and sellers. 
 * This class is extended by Customer and Seller. 
 *
 * @version 2023-4-10
 * @author 
 */

public class User extends FileFunctions implements Serializable{
    private String password; // password entered when a user creates their login information
    private String email; // email entered when a user creates their login information
    private boolean buyer;
    private boolean seller;
    protected final static String FILENAME = "userData.txt"; // name of file where userData is stored
    private final static String INVALID_EMAIL = "Please enter a valid email address!";
    private final static String INVALID_PASSWORD = "Please enter a valid password!";
    private final static String INVALID_BOTH = "Please enter a valid email address and password!";
    protected String cartFileName = "shoppingCarts.txt";
    protected static String itemListingsFileName = "itemListings.txt";
    protected String customerLogFileName  = "customerLog.txt";


    public User(String email, String password, String userType) throws InvalidUserInput {
        super(itemListingsFileName);
        if (!isValidPassword(password) || !isValidEmail(email)) {
            throw new InvalidUserInput(INVALID_BOTH);
        }

        this.buyer = (userType.equals("Buyer"));
        this.seller = !this.buyer;
        
        this.password = password;
        this.email = email;
    }
    
    public User(String email, String password, int userType) throws InvalidUserInput { // creates a new User object with email, password and userType; Buyer = 0, Seller = 1
        super(itemListingsFileName);
        
        if (!isValidPassword(password) || !isValidEmail(email)) {
            throw new InvalidUserInput(INVALID_BOTH);
        }

        this.buyer = (userType == 0);
        this.seller = !this.buyer;

        this.password = password;
        this.email = email;
    }

    ////////////////////////////////////////////////////////////////
    // Things moved from customer because seller can use them too
    ////////////////////////////////////////////////////////////////

    // protected ArrayList<Item> listings;
    protected ArrayList<Item> sortedListings;

    // /**
    //  * readFile(String filename)
    //  * @param filename: name of the file that needs to be read
    //  * @return String[] with all the lines of the file
    //  */
    // protected static String[] readFile(String filename) {
    //     String[] fileContents;
    //     ArrayList<String> contents = new ArrayList<>();

    //     File file = new File(filename);
    //     try {
    //         BufferedReader reader = new BufferedReader(new FileReader(file));
    //         String line;
    //         while ((line = reader.readLine()) != null) {
    //             contents.add(line);
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     fileContents = contents.toArray(new String[0]);
    //     return fileContents;
    // }

    // /**
    //  * writeFile(String filename)
    //  * @param filename: name of the file that needs to be read
    //  * @param lines: array of lines to be written
    //  * @return String[] with all the lines of the file
    //  */
    // protected static void writeFile(String filename, String[] lines) {
    //     File file = new File(filename);
    //     try {
    //         BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
    //         for (int i = 0; i < lines.length; i++) {
    //             bw.write(lines[i] + "\n");
    //         }
    //         bw.close();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * PrintListings()
     * just prints the items in the cart with nice formatting. 
     */
    public void printListings() {
        if (this.sortedListings.size() < 1) {
            this.sortedListings = readItems();
        }
        String itemFormat = "[%3d]: %-30s | %-24s | %-4s | $ %-6.2f\n";
        System.out.printf("[num]: %-30s | %-24s | %-4s | %-7s\n", "NAME", "STORE", "QNTY", "PRICE");
        for (int i = 0; i < this.sortedListings.size(); i++) {
            Item item = this.sortedListings.get(i);
            System.out.printf(itemFormat, i+1, item.getName(), item.getStore(), item.getQuantity(), item.getPrice());
        }
        System.out.println();
    }

    /*
     * sets sorted listings to all the listings in the file. This is useful for unsorting them or checking for updates. 
     * Note that sortedListings is always what gets printed, so call this beofre printing if ou want them unsorted. 
     */
    public void refreshListings() {
        this.sortedListings = readItems();
    }

    /**
     * getDisplayedItem()
     * @param index: The DISPLAYED index of the item (from print)
     */
    public Item getDisplayedItem(int index) throws IndexOutOfBoundsException {
        // index from the printed listing
        return this.sortedListings.get(index - 1);
    }

    //  /**
    //  * loadListings()
    //  * reads the item listings file file and puts loads items
    //  * @param fileName: name of the items cart file 
    //  */
    // protected void loadListings(String fileName) {
    //     String[] fileLines = readFile(fileName);
    //     ArrayList<Item> items = new ArrayList<>();
    //     for (int l = 0; l < fileLines.length; l++) {
    //         String line = fileLines[l];
    //         try {  
    //             items.add(new Item(line));
    //         } catch (InvalidLineException e) { // invalid line format exception
    //             e.printStackTrace();
    //         }
    //     }
    //     this.listings = items;
    // }

    // /**
    //  * saveListings()
    //  * writes all the items in this.cart to the cart file. Puts them on the line of the corresponding user. 
    //  * Potential problem: it is assumed that there is only 1 line per user in cart file.
    //  * @param fileName: name of the shopping cart file 
    //  */
    // protected void saveListings(String fileName) {
    //     String[] fileLines = new String[this.listings.size()];
    //     for (int l = 0; l < this.listings.size(); l++) {
    //         fileLines[l] = this.listings.get(l).toLine();
    //     }
    //     writeFile(fileName, fileLines);
    // }

    ///////////////////////////////////////////////////
    // Getters and setters and user stuff
    ///////////////////////////////////////////////////
    
    public String getEmail() { // gets the current user email
        return email;
    }

    public String getPassword() { // gets the current user password
        return password;
    }

    public boolean isBuyer() { // is the user a buyer
        return this.buyer;
    }

    public boolean isSeller() { // is the user a seller
        return this.seller;
    }

    public void setUserType(int userType) { // sets whether the user is a buyer or seller; 0 = buyer, 1 = seller
        this.buyer = (userType == 0);
        this.seller = !this.buyer;
    }

    public void setEmail(String email) throws InvalidUserInput { // sets a new email for the user
        if (!isValidEmail(email)) {
            throw new InvalidUserInput(INVALID_EMAIL);
        }

        this.email = email;
    }

    public void setPassword(String password) throws InvalidUserInput { // sets a new password for the user
        if (!isValidPassword(password)) {
            throw new InvalidUserInput(INVALID_PASSWORD);
        }

        this.password = password;
    }

    // User Data
    
    public static boolean accountExists(String emailEntered) { // checks if a user's information is saved to userData.txt
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.indexOf(emailEntered) != -1) {
                    return true;
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println("The userData.txt file does not exist!");

            return false;
        } catch(IOException e) {
            System.out.println("An error has occurred!");

            return false;
        }

        return false;
    }

    public static ArrayList<String> readUserData() { // reads the userData.txt file and returns an ArrayList of each line
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;

            ArrayList<String> list = new ArrayList<String>();

            while ((line = br.readLine()) != null) {
                list.add(line);
            }

            return list;
        } catch (FileNotFoundException e) {
            System.out.println("The userData.txt file does not exist!");
        } catch (IOException e) {
            System.out.println("There was an error processing the file!");
        }

        return null;
    }

    public static void saveNewUser(String email, String password, String userType) { // writes the new user information to userData.txt
        try {
            FileWriter fw = new FileWriter(FILENAME, true);

            fw.write(String.format("Email:%s, Password:%s, UserType:%s\n", email, password, userType));
            fw.close();

            // writes a new user to the userdata folder
        } catch (IOException e) {
            System.out.println("An error has occurred!");
        }
    }

    public static int isCorrectLogin(String emailEntered, String passwordEntered) { // verifies if the login entered matches the current User object fields
        ArrayList<String> list = readUserData();

        for (int i = 0; i < list.size(); i++) {
            String get = list.get(i);
            String[] split = get.split(", ");

            String email = split[0].replace("Email:", "");
            String password = split[1].replace("Password:", "");
            String userType = split[2].replace("UserType:", "");

            int toReturn;

            if (userType.equals("Buyer")) {
                toReturn = 0;
            } else if (userType.equals("Seller")) {
                toReturn = 1;
            } else {
                toReturn = -1;
            }

            if ((email.equals(emailEntered)) && (password.equals(passwordEntered))) {
                return toReturn;
            }
        }

        return -1;
    }

    public static boolean isValidPassword(String password) { // verifies that the password exists and is greater than 0 characters
        if (password == null || password.length() == 0) {
            return false;
        }

        return true;
    }

    public void editUser(String newEmail, String newPassword, boolean deleted) throws InvalidUserInput {
        if (newEmail != null) {
            if (!isValidEmail(email)) {
                throw new InvalidUserInput(INVALID_EMAIL);
            }
        }

        if (newPassword != null) {
            if (!isValidPassword(password)) {
                throw new InvalidUserInput(INVALID_PASSWORD);
            }
        }

        ArrayList<String> list = readUserData();

        try {
            PrintWriter pw = new PrintWriter(FILENAME);

            for (int i = 0; i < list.size(); i++) {
                String get = list.get(i);
                String[] split = get.split(", ");

                String fileEmail;
                String filePassword;
                String fileUserType;

                if (split.length > 2) {
                     fileEmail = split[0].replace("Email:", "");
                     filePassword = split[1].replace("Password:", "");
                     fileUserType = split[2].replace("UserType:", "");
                } else {
                    continue;
                }

                if (newEmail != (null) && email.equals(fileEmail)) {
                    fileEmail = newEmail;
                }

                if (newPassword != (null) && password.equals(filePassword)) {
                    filePassword = newPassword;
                }

                if ((fileEmail.equals(email) && !deleted) || !(fileEmail.equals(email))) {
                    pw.printf("Email:%s, Password:%s, UserType:%s\n", fileEmail, filePassword, fileUserType);
                }
            }

            pw.close();
        } catch(IOException e) {
            System.out.println("An error has occurred!");
        }
    }

    public static boolean isValidEmail(String email) { // verifies that the email is valid
       if (email == null || email.isEmpty()) {
           return false;
       }

       int at = email.indexOf('@');
       int dot = email.lastIndexOf('.');

       if (at <= 0 || dot < at + 2 || email.length() - 4 != dot) {
           return false;
       }

       return true;
    }
}
