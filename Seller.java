import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
/**
 * Seller.java
 * 
 * Creates a seller, who can create item listings 
 *
 * @version 2023-4-10
 * @author Hannah, Tristen, Amber 
 */
public class Seller extends User implements Serializable{
    private ArrayList<String> stores;
    

    public Seller (String email, String password, int userType) throws InvalidUserInput {
        super(email, password, userType);
        this.stores = new ArrayList<>();
        this.sortedListings = new ArrayList<>();

        //TODO: update store loading with new login stuff
        this.stores.add("store1");  // temporary
        // // load the stores
        // String[] fileLines = readFile(FILENAME);
        // for (int l = 0; l < fileLines.length; l++) { // find the correct user line
        //     String user = this.getEmail(); 
        //     String line = fileLines[l];
        //     String[] splitUserLine = line.split(", "); // get users as string
        //     if (splitUserLine[0].split(":")[1].equals(user)) { // found correct line
        //         if (splitUserLine.length < 4) { // no stores
        //             this.stores = new ArrayList<>();
        //         } else {
        //             String storesString = line.split(",")[3]; // get stores as string
        //             for (int i = 0; i < storesString.split(";").length; i++) {
        //                 String storeName = storesString.split(";")[i].trim();;
        //                 this.stores.add(storeName);
        //             }
                    
        //         }
        //         break;
        //     }
        // }

        // get only this seller's items
        findSellerItems(); // updates sorted listings


    }

    public ArrayList<String> getStores() {
        if (this.stores.size() > 0) {
            return this.stores;
        } else { 
            return null;
        }
    }

    public void setStores(ArrayList<String> stores) {
        this.stores = stores;
    }

    public void addStore(String storeName) {
        this.stores.add(storeName);
    }

    public void saveStores() {
        // TODO: new user stuff
        String fileName = this.FILENAME; // user data file
        String[] fileLines = readFile(fileName);
        for (int l = 0; l < fileLines.length; l++) { // find the correct user line
            String user = this.getEmail(); 
            String line = fileLines[l];
            String newStoresString = " ";
            String[] splitUserLine = line.split(", "); // get user line as string
            if (splitUserLine[0].split(":")[1].equals(user)) { // found correct line
                // String storesString = line.split(",")[3]; // get stores as string
                newStoresString = ", ";
                for (int i = 0; i < this.stores.size(); i++) {
                    newStoresString += this.stores.get(i) + ";";
                }
                if (splitUserLine.length > 3) { // some stores already saved
                    splitUserLine[3] = newStoresString;
                    // remake line
                    line = "";
                    for (int i = 0; i < splitUserLine.length; i++) {
                        line += splitUserLine[i];
                        if (i < splitUserLine.length -2) {
                            line += ", ";
                        }
                    }
                } else { // not stores saved yet
                    line += ", " + newStoresString.substring(1, newStoresString.length());
                }

                fileLines[l] = line; // replace updated line
                break;
            }
        }
        writeFile(fileName, fileLines);
    }



    /*
     * findSelleraItems
     * looks through listings and puts items mathching this seller into sorted itesm which is used for stuff
     */
    public void findSellerItems() {
        ArrayList<Item> listings = readItems();
        if (this.sortedListings.size() > 0) {
            this.sortedListings.clear();
            // TODO: check null/no store errors here
        }
        for (int i = 0; i < listings.size(); i++) {
            Item item = listings.get(i);
            for (int s = 0; s < this.stores.size(); s++) { // check each store
                if (item.getStore().equals(stores.get(s))) {
                    this.sortedListings.add(item);
                    break;
                }
            }
        }
    }

    public void addFromCSV(String filename) {
        String[] fileData = readFile(filename);
        int counter = 0;
        for (int i = 0; i < fileData.length; i++) {
            try {
                addNewItem(new Item(fileData[i]));
                counter += 1;
            } catch (InvalidLineException e) {
                System.out.printf("Invalid Item format in file %s line %d!\n", filename, i+1);
            }
        }
        System.out.printf("Added %d items.\n", counter);
    }

    public void addNewItem(Item item) {
        if (!this.stores.contains(item.getStore())) { // add store
            this.stores.add(item.getStore());
        }
        appendItem(item);
        // save files
        saveStores();
    }

    /*
     * removeItem()
     * removes the item with the displayed index from sorted items from the item listings
     */
    public void removeItem(int displayedIndex) {
        Item item = getDisplayedItem(displayedIndex);
        replaceItem(item.findItem(readItems()), null);
    }

    public void editItem(Item item, int changeType, String changeValue ) {
        // TODO: probably rewrite this
        ArrayList<Item> sellerItems = readItems();
        int index = item.findItem(readItems()); // get the index in the items file

        if (index > 0) {
            switch (changeType) { // choose what changeType
                case 1 -> // name
                    item.setName(changeValue);
                case 2 -> // store
                    item.setStore(changeValue);
                case 3 -> // description
                    item.setDescription(changeValue);
                case 4 -> // quantity
                    item.setQuantity(Integer.parseInt(changeValue));
                case 5 -> // price
                    item.setPrice(Integer.parseInt(changeValue));
            }   
            replaceItem(index, item);
        } else {
            System.out.println("Item does not exist!");
        }
    }

    public void viewAllStats() {
        String[] stats = readFile(this.customerLogFileName);
        ArrayList<String> statsList = new ArrayList<>();
        ArrayList<String> customerData = new ArrayList<>();
        Collections.addAll(statsList, stats);

        // filters item listings to see if the listing is from one of the seller's stores
        for (int i = 0; i < statsList.size(); i++) {
            String[] customerHistory = statsList.get(i).split(";");
            ArrayList<String> sellerSales = new ArrayList<>();
            for (int j = 1; j < customerHistory.length; j++) {
                if (stores.contains(customerHistory[j])) {
                    sellerSales.add(customerHistory[j] + ";");
                }
            }
            if (sellerSales.size() > 0) {
                String format = customerHistory[0] + ";" + sellerSales;
                customerData.add(format);
            }
        }

        // displays all stats
        for (int i = 0; i < customerData.size(); i++) {
            System.out.println(customerData.get(i));
        }
    }
    
    public ArrayList<Customer> updatedViewAllStats() {
        Object[] users = readObjectsFromFile("userData.txt");
        ArrayList<Customer> customersOfSeller = new ArrayList<>();
        
        for (int i = 0; i < users.length; i++) {
            if (users[i] instanceof Customer) {
                ArrayList<Item> cart = ((Customer) users[i]).getCart();
                for (int j = 0; j < cart.size(); j++) {
                    if (stores.contains(cart.get(j).getStore())) {
                        customersOfSeller.add((Customer) users[i]);
                    }
                }
            }
        }
        
        // TODO: determine the way that the stats get displayed
        return(customersOfSeller); // done I think
    }
    public ArrayList<Customer> updatedSortStats(int sortType, int sortOrder) {
        ArrayList<Customer> customersOfSeller = updatedViewAllStats();
        ArrayList<Item> sellerTransactions = new ArrayList<>();
        
        for (int i = 0; i < customersOfSeller.size(); i++) {
            ArrayList<Item> customerCart = customersOfSeller.get(i).getCart();
            sellerTransactions.addAll(customerCart);
        }
        ArrayList<Customer> sortedCustomersOfSeller = new ArrayList<>();

        if (sortType == 1 && sortOrder == 1) {
            sellerTransactions.sort(new newPriceComparatorAscending());
        }

        if (sortType == 1 && sortOrder == 2) {
            sellerTransactions.sort(new newPriceComparatorDescending());
        }

        if (sortType == 2 && sortOrder == 1) {
            sellerTransactions.sort(new newQuantityComparatorAscending());
        }

        if (sortType == 2 && sortOrder == 2) {
            sellerTransactions.sort(new newQuantityComparatorDescending());
        }
        return sortedCustomersOfSeller;
    }

    // Comparator for sorting by price in ascending order
    static class newPriceComparatorAscending implements Comparator<Item> {
        public int compare(Item o1, Item o2) {
            double price1 = o1.getPrice();
            double price2 = o2.getPrice();
            return Double.compare(price1, price2);
        }
    }

    // Comparator for sorting by price in descending order
    static class newPriceComparatorDescending implements Comparator<Item> {
        public int compare(Item o1, Item o2) {
            double price1 = o1.getPrice();
            double price2 = o2.getPrice();
            return Double.compare(price2, price1);
        }
    }

    // Comparator for sorting by price in ascending order
    static class newQuantityComparatorAscending implements Comparator<Item> {
        public int compare(Item o1, Item o2) {
            int quantity1 = o1.getQuantity();
            int quantity2 = o2.getQuantity();
            return Integer.compare(quantity1, quantity2);
        }
    }

    // Comparator for sorting by price in descending order
    static class newQuantityComparatorDescending implements Comparator<Item> {
        public int compare(Item o1, Item o2) {
            int quantity1 = o1.getQuantity();
            int quantity2 = o2.getQuantity();
            return Integer.compare(quantity2, quantity1);
        }
    }
    
    public void sortStats(int sortType, int sortOrder) {
        String[] stats = readFile(this.customerLogFileName);
        ArrayList<String> statsList = new ArrayList<>();
        ArrayList<String> customerData = new ArrayList<>();
        Collections.addAll(statsList, stats);

        for (int i = 0; i < statsList.size(); i++) {
            String[] customerHistory = statsList.get(i).split(";");
            ArrayList<String> sellerSales = new ArrayList<>();
            for (int j = 1; j < customerHistory.length; j++) {
                if (stores.contains(customerHistory[j])) {
                    sellerSales.add(customerHistory[j] + ";");
                }
            }
            if (sellerSales.size() > 0) {
                String format = customerHistory[0] + ";" + sellerSales;
                customerData.add(format);
            }
        }

        switch (sortType) {
            case 1 -> {
                switch (sortOrder) {
                    case 1 -> {
                        customerData.sort(new PriceComparatorAscending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    }
                    case 2 -> {
                        customerData.sort(new PriceComparatorDescending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    }
                }
            }
            case 2 -> {
                switch (sortOrder) {
                    case 1 -> {
                        customerData.sort(new QuantityComparatorAscending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    }
                    case 2 -> {
                        customerData.sort(new QuantityComparatorDescending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    }
                }
            }
        }
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
