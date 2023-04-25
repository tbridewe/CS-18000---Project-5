import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
/**
 * Customer.java
 * 
 * creates a customer. A customer can buy items and put them in a cart and check out and buy items.  
 *
 * @version 2023-4-10
 * @author Hannah, Tristen
 */
public class Customer extends User {
    private ArrayList<Item> cart = new ArrayList<>(); // stores the user's items. Everytime the cart is updtates the cart file will also be updated
    
    
    public Customer(String email, String password, int userType) throws InvalidUserInput {
        super(email, password, userType);
        // TODO: why is usertype an input here? The type is known to be customer

        loadCart(this.cartFileName); // loads items from the cart file into the cart
        this.sortedListings = readItems();
    }
    
    public ArrayList<Item> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Item> cart) {
        this.cart = cart;
    }
    
    /**
     * PrintCart()
     * just prints the items in the cart with nice formatting. 
     */
    public void printCart() {
        System.out.println("SHOPPING CART");
        String itemFormat = "[%3d]: %-30s | %-4d | %-24s | $ %-6.2f\n";
        System.out.printf("[num]: %-30s | %-4s | %-24s | %-7s\n", "NAME", "QNTY", "STORE", "PRICE");
        double price = 0;
        for (int i = 0; i < cart.size(); i++) {
            Item item = cart.get(i);
            System.out.printf(itemFormat, i+1, item.getName(), item.getQuantity(), item.getStore(), item.getPrice());
            price += item.getPrice() * item.getQuantity();
        }
        System.out.printf("TOTAL: $%74.2f\n", price);
    }


    /**
     * addToCart()
     * @param item: the item to be added to the cart
     * @param quantity: the quanitity to be added
     * @exception InvalidQuantityException: if the quantity is more than is in stock or <= 0. The quanity in the item object should be the amount in stock
     * This adds the item to the cart and adjusts the items remaining in the store accordingly
     */
    public void addToCart(Item item, int quantity) throws InvalidQuantityException {
        // check quantity
        if (quantity > item.getQuantity()) {
            throw new InvalidQuantityException(String.format("Invalid Quantity: There are only %d of this item in stock", item.getQuantity()));
        } else if (quantity <= 0) {
            throw new InvalidQuantityException(String.format("Invalid Quantity: Must be >= 0"));
        }
        
        // add item
        Item add = new Item(item);
        add.setQuantity(quantity);
        this.cart.add(add);

        // edit quanitiy in listings
        int i = item.findItem(readItems()); 
        item.changeQuanityBy(-1 * quantity); // update item quanitity
        replaceItem(i, item); // replaces item and saves changes
        
        // Write cart 
        saveCart(this.cartFileName);
    }

    /**
     * saveCart()
     * writes all the items in this.cart to the cart file. Puts them on the line of the corresponding user. 
     * Potential problem: it is assumed that there is only 1 line per user in cart file.
     * @param fileName: name of the shopping cart file 
     */
    public void saveCart(String fileName) {
        String[] fileLines = readFile(fileName);
        for (int l = 0; l < fileLines.length; l++) { // find the correct user line
            String user = this.getEmail(); 
            String line = fileLines[l];
            if (line.split(";")[0].equals(user)) { // found correct line
                line = user + ";";
                for (int i = 0; i < cart.size(); i++) {
                    line += cart.get(i).toLine() + ";";
                }
                fileLines[l] = line; // replace updated line
                break;
            }
            if (l == fileLines.length-1) { // user has no existing cart file
                ArrayList<String> newLines = new ArrayList<>(Arrays.asList(fileLines));
                line = user + ";";
                for (int i = 0; i < cart.size(); i++) {
                    line += cart.get(i).toLine() + ";";
                }
                newLines.add(line);
                fileLines = newLines.toArray(new String[0]);
            }
        }
        writeFile(fileName, fileLines);
    }


    /**
     * loadCart()
     * reads the cart file and puts the items for this user into this.cart
     * @param fileName: name of the shopping cart file 
     */
    public void loadCart(String fileName) {
        String[] fileLines = readFile(fileName);
        String user = this.getEmail(); 
        for (int l = 0; l < fileLines.length; l++) {
            String line = fileLines[l];
            if (line.split(";")[0].equals(user)) { // load these items
                String[] itemStrings = line.split(";");
                for (int i = 1; i < itemStrings.length; i++) {
                    try{    // catch invalid lines
                        this.cart.add(new Item(itemStrings[i]));
                    } catch (InvalidLineException e) {
                        System.out.printf("Invalid item format line while reading %s. \nLine: %s\n", 
                            this.cartFileName, itemStrings[i]);
                    }
                }
                break;
            }
        }
    }

   
    /**
     * removeFromCart(Item listing)
     * removes specified listing from the cart
     * @param item: the specified listing that the user wants to remove from cart
     * @param quantity: the amount of the item to be reomved
     */
    public void removeFromCart(int index, int quantity) {
        int i = index - 1;
        try {
            Item item = this.cart.get(i);
            if (quantity >= cart.get(i).getQuantity()) {
                if (quantity > cart.get(i).getQuantity()) {
                    // if quantity is greater than amount in cart just set it to amount in cart
                    quantity = cart.get(i).getQuantity(); 
                }
                this.cart.remove(this.cart.get(i));
            } else {
                Item updatedItem = cart.get(i);
                updatedItem.changeQuanityBy(-1 * quantity);
                this.cart.set(i, updatedItem);
            }
            
            // edit quanitiy in listings
            i = item.findItem(readItems()); 
            item.changeQuanityBy(quantity); // update item quanitity
            replaceItem(i, item); // replaces item and saves changes
            
            // save cart
            saveCart(this.cartFileName);

        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid item number selected!");
            e.printStackTrace(); // TODO: remove stack trace
        }
    }

    /**
     * keywordSearch(String keyword)
     * @param keyword: specified word that the user is looking for
     * @return String[] containing all lines containing the keyword 
     */
    public ArrayList<Item> keywordSearch(String keyword) {
        int j = 0;
        ArrayList<Item> listings = readItems();
        ArrayList<Item> listingsWKeyword = new ArrayList<>();
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getName().contains(keyword) || listings.get(i).getStore().contains(keyword) || 
                    listings.get(i).getDescription().contains(keyword)) {
                listingsWKeyword.add(listings.get(i));
                j++;
            }
        }
        this.sortedListings = listingsWKeyword; // update sorted listings
        return listingsWKeyword;
    }

    /**
     * checkout()
     * @return double the total price of the cart 
     */
    public void checkout() {

        String[] fileLines = readFile(this.customerLogFileName);
        // copied from saveCart()
        for (int l = 0; l < fileLines.length; l++) { // find the correct user line
            String user = this.getEmail(); 
            String line = fileLines[l];
            if (line.split(";")[0].equals(user)) { // found correct line
                line = user + ";";
                for (int i = 0; i < cart.size(); i++) {
                    line += cart.get(i).toLine() + ";";
                }
                fileLines[l] = line; // replace updated line
                break;
            }
            if (l == fileLines.length-1) { // user has no existing log file
                ArrayList<String> newLines = new ArrayList<>(Arrays.asList(fileLines));
                line = user + ";";
                for (int i = 0; i < cart.size(); i++) {
                    line += cart.get(i).toLine() + ";";
                }
                newLines.add(line);
                fileLines = newLines.toArray(new String[0]);
            }
        }
        writeFile(this.customerLogFileName, fileLines);

        printCart();
        System.out.println("Thank you for your purchase!");
        this.cart.clear(); // remove all items
        saveCart(cartFileName);
    }

    private ArrayList<Item> readPurchaseLog() { 
        String[] fileLines = readFile(this.customerLogFileName);
        String user = this.getEmail(); 
        ArrayList<Item> purchaseLog = new ArrayList<>();
        for (int l = 0; l < fileLines.length; l++) {
            String line = fileLines[l];
            if (line.split(";")[0].equals(user)) { // load these items
                String[] itemStrings = line.split(";");
                for (int i = 1; i < itemStrings.length; i++) {
                    try{    // catch invalid lines
                        purchaseLog.add(new Item(itemStrings[i]));
                    } catch (InvalidLineException e) {
                        System.out.printf("Invalid item format line while reading %s. \nLine: %s\n", 
                            this.customerLogFileName, itemStrings[i]);
                    }
                }
                break;
            }
        }
        return purchaseLog;
    }

    public void viewPurchases() {
        ArrayList<Item> purchaseLog = readPurchaseLog();
        System.out.println("PURCHASES:");
        String itemFormat = "[%3d]: %-30s | %-4d | %-24s | $ %-6.2f\n";
        System.out.printf("[num]: %-30s | %-4s | %-24s | %-7s\n\n", "NAME", "QNTY", "STORE", "PRICE");
        for (int i = 0; i < purchaseLog.size(); i++) {
            Item item = purchaseLog.get(i);
            System.out.printf(itemFormat, i+1, item.getName(), item.getQuantity(), item.getStore(), item.getPrice());
        }

    }
    

    public void exportPurchases(String fileName) {
        ArrayList<Item> purchaseLog = readPurchaseLog();
        String[] lines = new String[purchaseLog.size()];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = purchaseLog.toString();
        }
        writeFile(fileName, lines);
    }
  
    /**
     * sortMarketplace(int sortType, int sortOrder)
     * sorts the marketplace listings based on user input
     *
     * @param sortType:  either sort by 1price or by 2quantity
     * @param sortOrder: either sort in ascending or descending order
     */

    public void sortMarketplace(int sortType, int sortOrder) {
        String[] listings = readFile("itemListings.txt");
        ArrayList<String> itemListings = new ArrayList<>();
        Collections.addAll(itemListings, listings);
        ArrayList<Item> sorted = new ArrayList<>();

        switch (sortType) {
            case 1: // price
                switch (sortOrder) {
                    case 1: // ascending
                        itemListings.sort(new PriceComparatorAscending());
                        for (int i = 0; i < itemListings.size(); i++) {
                            // System.out.println(itemListings.get(i));
                            try {   // add to sortedItems arraylist
                                sorted.add(new Item(itemListings.get(i)));
                            } catch (InvalidLineException e) {
                            }
                        }
                        break;
                    case 2: // descending
                        itemListings.sort(new PriceComparatorDescending());
                        for (int i = 0; i < itemListings.size(); i++) {
                            // System.out.println(itemListings.get(i));
                            try {   // add to sortedItems arraylist
                                sorted.add(new Item(itemListings.get(i)));
                            } catch (InvalidLineException e) {
                            }
                        }
                        break;
                }
            case 2: // quantity
                switch (sortOrder) {
                    case 1: // ascending
                        itemListings.sort(new QuantityComparatorAscending());
                        for (int i = 0; i < itemListings.size(); i++) {
                            // System.out.println(itemListings.get(i));
                            try {   // add to sortedItems arraylist
                                sorted.add(new Item(itemListings.get(i)));
                            } catch (InvalidLineException e) {
                            }
                        }
                        break;
                    case 2: // descending
                        itemListings.sort(new QuantityComparatorDescending());
                        for (int i = 0; i < itemListings.size(); i++) {
                            // System.out.println(itemListings.get(i));
                            try {   // add to sortedItems arraylist
                                sorted.add(new Item(itemListings.get(i)));
                            } catch (InvalidLineException e) {
                            }
                        }
                        break;
                }
        }
        this.sortedListings = sorted; // update sorted for outputs
    }

    // Comparator for sorting by price in ascending order
    static class PriceComparatorAscending implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            double price1 = Double.parseDouble(o1.split(",")[4]);
            double price2 = Double.parseDouble(o2.split(",")[4]);
            return Double.compare(price1, price2);
        }
    }
    // Comparator for sorting by price in descending order
    static class PriceComparatorDescending implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            double price1 = Double.parseDouble(o1.split(",")[4]);
            double price2 = Double.parseDouble(o2.split(",")[4]);
            return Double.compare(price2, price1);
        }
    }

    // Comparator for sorting by price in ascending order
    static class QuantityComparatorAscending implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int quantity1 = Integer.parseInt(o1.split(",")[3]);
            int quantity2 = Integer.parseInt(o2.split(",")[3]);
            return Integer.compare(quantity1, quantity2);
        }
    }

    // Comparator for sorting by price in descending order
    static class QuantityComparatorDescending implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int quantity1 = Integer.parseInt(o1.split(",")[3]);
            int quantity2 = Integer.parseInt(o2.split(",")[3]);
            return Integer.compare(quantity2, quantity1);
        }
    }

    
    
}
