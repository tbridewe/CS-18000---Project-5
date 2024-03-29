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
    
    public Seller() {
        // implemented so that the classes in Server main will deserialize
    }
    
    public Seller (String email, String password, int userType) throws InvalidUserInput {
        super(email, password, userType);
        this.stores = new ArrayList<>();
        this.sortedListings = new ArrayList<>();
        findSellerItems(); // updates sorted listings


    }

    // public saveData() {
    //     writeItems(this.items);
    // }

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
        System.out.println("The save stores function needs updated!");
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

    public int addFromCSV(String filename) throws FileNotFoundException{
        String[] fileData = readFile(filename);
        int counter = 0;
        if (fileData.length < 1) {
            throw new FileNotFoundException("File doesn't exist or is empty.");
        }
        for (int i = 0; i < fileData.length; i++) {
            try {
                Item item = new Item(fileData[i]);
                addNewItem(item);
                if (!this.stores.contains(item.getStore())) { // add store
                    this.stores.add(item.getStore());
                }
                counter += 1;
            } catch (InvalidLineException e) {
                System.out.println("Invalid line.");
            }
        }
        System.out.printf("Added %d items.\n", counter);
        return counter;
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

    /**
     * 
     * @param item item to be edited
     * @param changeType [1:5] -> name, store, desc, qnty, price
     * @param changeValue
     */
    public void editItem(Item item, int changeType, String changeValue ) {
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
    
    public ArrayList<Customer> customersOfSeller() {
        Object[] users = readObjectsFromFile("userData.txt");
        ArrayList<Customer> customersOfSeller = new ArrayList<>();
        
        for (int i = 0; i < users.length; i++) {
            if (users[i] instanceof Customer) {
                ArrayList<Item> purchases = ((Customer) users[i]).getPurchases();
                for (int j = 0; j < purchases.size(); j++) {
                    if (stores.contains(purchases.get(j).getStore())) {
                        customersOfSeller.add((Customer) users[i]);
                    }
                }
            }
        }
        
        return(customersOfSeller); // done I think
    }
    
    public ArrayList<Item> updatedViewAllStats() {
        ArrayList<Customer> customersOfSeller = customersOfSeller();
        ArrayList<Item> sellerTransactions = new ArrayList<>();
        
        for (int i = 0; i < customersOfSeller.size(); i++) {
            ArrayList<Item> purchases = customersOfSeller.get(i).getPurchases();
            sellerTransactions.addAll(purchases);
        }
        return sellerTransactions;
    }
    
   public ArrayList<Item> updatedSortStats(int sortType, int sortOrder) {
        ArrayList<Customer> customersOfSeller = customersOfSeller();
        ArrayList<Item> sellerTransactions = new ArrayList<>();
        ArrayList<Item> sortedItems = new ArrayList<>();

        for (int i = 0; i < customersOfSeller.size(); i++) {
            ArrayList<Item> customerCart = customersOfSeller.get(i).getPurchases();
            sellerTransactions.addAll(customerCart);
        }

        if (sortType == 1 && sortOrder == 1) {
            sellerTransactions.sort(new newPriceComparatorAscending());
            sortedItems.addAll(sellerTransactions);
        } else if (sortType == 1 && sortOrder == 2) {
            sellerTransactions.sort(new newPriceComparatorDescending());
            sortedItems.addAll(sellerTransactions);
        } else if (sortType == 2 && sortOrder == 1) {
            sellerTransactions.sort(new newQuantityComparatorAscending());
            sortedItems.addAll(sellerTransactions);
        } else if (sortType == 2 && sortOrder == 2) {
            sellerTransactions.sort(new newQuantityComparatorDescending());
            sortedItems.addAll(sellerTransactions);
        }

        return sortedItems;
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
