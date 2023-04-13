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
 * @author Hannah, Tristan, Amber 
 */
public class Seller extends User {
    private ArrayList<String> stores;
    

    public Seller (String email, String password, int userType) throws InvalidUserInput {
        super(email, password, userType);
        this.stores = new ArrayList<>();
        loadListings(this.itemListingsFileName);
        this.sortedListings = new ArrayList<>();

        // load the stores
        String[] fileLines = readFile(FILENAME);
        for (int l = 0; l < fileLines.length; l++) { // find the correct user line
            String user = this.getEmail(); 
            String line = fileLines[l];
            String[] splitUserLine = line.split(", "); // get users as string
            if (splitUserLine[0].split(":")[1].equals(user)) { // found correct line
                if (splitUserLine.length < 4) { // no stores
                    this.stores = new ArrayList<>();
                } else {
                    String storesString = line.split(",")[3]; // get stores as string
                    for (int i = 0; i < storesString.split(";").length; i++) {
                        String storeName = storesString.split(";")[i].trim();;
                        this.stores.add(storeName);
                    }
                    
                }
                break;
            }
        }

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

    public void saveStores() {
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


    // old code but It's needed id what it does gknsdsindsgkj
    /*
     * doesn't actually display it's just for sorting but whatever it works
     */
    protected ArrayList<String> displayItems() {
        String[] itemsList = readFile(this.itemListingsFileName);
        ArrayList<String> sellerItems = new ArrayList<>();
        this.sortedListings.clear();

        for (int i = 0; i < itemsList.length; i++) {
            if (stores.contains(itemsList[i].split(",")[1])) {
                sellerItems.add(itemsList[i]);
                // System.out.println(itemsList[i]);
                try {
                    sortedListings.add(new Item(itemsList[i]));
                } catch (InvalidLineException e) {
                }
            }
        }
        return sellerItems;
    }

    /*
     * findSelleraItems
     * looks through listings and puts items mathching this seller into sorted itesm which is used for stuff
     */
    public void findSellerItems() {
        if (this.sortedListings.size() > 0) {
            this.sortedListings.clear();
        }
        for (int i = 0; i < this.listings.size(); i++) {
            Item item = this.listings.get(i);
            for (int s = 0; s < this.stores.size(); s++) { // check each store
                if (item.getStore().equals(stores.get(s))) {
                    this.sortedListings.add(item);
                    break;
                }
            }
        }
    }

    public void addFromCSV(String filename) {
        String[] csvData = readFile(filename);
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(this.itemListingsFileName, true));

            for (int i = 0; i < csvData.length; i++) {
                writer.println(csvData[i]);
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace(); // implement different exception catch?
        }
    }

    public void addNewItem(Item item) {
        // adds to both because we have both and idk what usese which one
        this.listings.add(item);
        this.sortedListings.add(item);
        if (!this.stores.contains(item.getStore())) {
            this.stores.add(item.getStore());
        }
        
        // save files
        saveStores();
        saveListings(this.itemListingsFileName);
    }

    public void removeItem(Item item) {
        ArrayList<String> sellerItems = displayItems(); // seller items

        for (int i = 0; i < sellerItems.size(); i++) { // loop through seller items
            if (sellerItems.contains(String.valueOf(item))) { // if the item is in seller items, remove it
                sellerItems.remove(String.valueOf(item));
            } else {
                System.out.println("Item does not exist!"); // display error message if not in the seller items
            }
        }
    }

    public void editItem(Item item, int changeType, String changeValue ) {
        ArrayList<String> sellerItems = displayItems();

        if (sellerItems.contains(String.valueOf(item))) {
            for (int i = 0; i < sellerItems.size(); i++) { // loop through the seller items
                if (sellerItems.get(i).equals(String.valueOf(item))) { // find the item
                    switch (changeType) { // choose what changeType
                        case 1: // name
                            item.setName(changeValue);
                        case 2: // store
                            item.setStore(changeValue);
                        case 3: // description
                            item.setDescription(changeValue);
                        case 4: // quantity
                            item.setQuantity(Integer.parseInt(changeValue));
                        case 5: // price
                            item.setPrice(Integer.parseInt(changeValue));
                    }
                }
            }
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
            case 1:
                switch (sortOrder) {
                    case 1:
                        customerData.sort(new PriceComparatorAscending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    case 2:
                        customerData.sort(new PriceComparatorDescending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                }
            case 2:
                switch (sortOrder) {
                    case 1:
                        customerData.sort(new QuantityComparatorAscending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
                        }
                    case 2:
                        customerData.sort(new QuantityComparatorDescending());
                        for (int i = 0; i < statsList.size(); i++) {
                            System.out.println(statsList.get(i));
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
