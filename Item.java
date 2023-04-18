import java.io.Serializable;
import java.util.ArrayList;
import java.io.Serializable;
/**
 * Item.java
 * 
 * Creates an item object that contains all the properties of an item that can be bought or sold
 * The constructor accepts a string which is read out of the items file. 
 * It is serializabelt so it can be written to a file as an object
 * 
 * @version 2023-4-10
 * @author Hannah Kadlec
 */
public class Item implements Serializable {
    private String name;
    private String store;
    private String description;
    private int quantity; // maybe move
    private double price;
    private final String itemsFile = "itemListings.txt";
    // private String sellerName;

    public Item(String name, String store, String description, int quantity, double price) {
        this.name = name;
        this.store = store;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public Item(String itemFileLine) throws InvalidLineException {
        try {

            String[] splitString = itemFileLine.split(",");
            this.name = splitString[0];
            this.store = splitString[1];
            this.description = splitString[2];
            this.quantity = Integer.valueOf(splitString[3]);
            this.price = Double.valueOf(splitString[4]);
        } catch (Exception e) {// TODO: specific exceptions
            throw new InvalidLineException("This line in invalid! Can't create item.");
        }
    }

    public Item(Item item) {
        // just to make a new copy of an item
        this.name = item.name;
        this.store = item.store;
        this.description = item.description;
        this.quantity = item.quantity;
        this.price = item.price;
    }

    // FILE FORMAT: name,store,description,quantity,price
    public String toLine() {
        String line = String.format("%s,%s,%s,%d,%.2f", 
            this.name, this.store, this.description, this.quantity, this.price);
        return line;
    }

     /**
     * equals()
     * checks if the items are equal. Does NOT check quantity
     */
    public boolean equals(Item item) {
        if ( this.name.equals(item.getName())
            && this.store.equals(item.getStore())
            && this.description.equals(item.getDescription())
            && this.price == item.getPrice()) {
                return true;
        } else{
            return false;
        }
    }

     /**
     * isContainedIn()
     * @param itemList: an arraylist of items
     * returns index of item in array of items. returns -1 of no item is found
     * Does NOT require equal quantity
     */
    public int findItem(ArrayList<Item> itemList) {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            if ( this.name.equals(item.getName()) // same conditions as equals method but this doesn't require self-referencing
            && this.store.equals(item.getStore())
            && this.description.equals(item.getDescription())
            && this.price == item.getPrice()) {
                return i;
            }
        }
        return -1;
    }
    
    // get and set
    public String getName() {
        return this.name;
    }

    public String getStore() {
        return this.store;
    }

    public String getDescription() {
        return this.description;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void changeQuanityBy(int number) {
        this.quantity += number;
    }
    
    // FILE IO STUFF
    
}