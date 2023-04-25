import java.util.ArrayList;
import java.io.*;
/**
 * FileFunctions
 * 
 * Methods for reading/writing things to files including items. This class should be extended by customer and seller. 
 * @version 2023-4-10
 * @author Hannah Kadlec
 */

public class FileFunctions {
    private static String itemFileName = "itemsTest.txt";

    public FileFunctions(String itemFile) {
        this.itemFileName = "itemsTest.txt";
    }
protected static synchronized Object[] readObjectsFromFile(String filename) {
        Object[] objectContents;
        ArrayList<Object> objectArrayList = new ArrayList<>();

        File file = new File(filename);
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object obj;

            while ((obj = ois.readObject()) != null) {
                objectArrayList.add(obj);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        objectContents = objectArrayList.toArray();
        return objectContents;
    }

    protected static synchronized String[] readFile(String filename) {
        String[] fileContents;
        ArrayList<String> contents = new ArrayList<>();

        File file = new File(filename);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                contents.add(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        fileContents = contents.toArray(new String[0]);
        return fileContents;
    }

    /**
     * writeFile(String filename)
     * @param filename: name of the file that needs to be read
     * @param lines: array of lines to be written
     * @return String[] with all the lines of the file
     */
    protected static synchronized void writeFile(String filename, String[] lines) {
        File file = new File(filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            for (int i = 0; i < lines.length; i++) {
                bw.write(lines[i] + "\n");
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public void writeItem(Object item) {
    //     try {
 
    //         FileOutputStream fileOut = new FileOutputStream(new File(itemFileName));
    //         ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
    //         objectOut.writeObject(item);
    //         objectOut.close();
    //         System.out.println("The Object  was succesfully written to a file");
 
    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //     }
    // }

    // item stuff

    /**
     * readItems()
     * Reads all the items saved in the file. This is syncronized to avoid problems. Use it to get up to date item information. 
     * @return ArrayList<Item> of all the items saved in the store
     */
    public static synchronized ArrayList<Item> readItems() {
        ArrayList<Item> items = new ArrayList<>();
        try {
            FileInputStream fi = new FileInputStream(new File(itemFileName));
			ObjectInputStream oi = new ObjectInputStream(fi);
            
			// Read objects
            Item item = (Item) oi.readObject();
            do {
                items.add(item);
                try {
                    item = (Item) oi.readObject();
                } catch(EOFException e) { // end of file
                    item = null;
                }
            } while (item != null);
                        
			oi.close();
			fi.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * writeItems()
     * Write all the items in the arraylist to the item file. Static syncronozed to avoid problems
     */
    protected static synchronized void writeItems(ArrayList<Item> itemList) {
        try {
            FileOutputStream fileOut = new FileOutputStream(new File(itemFileName));
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            for (int i = 0; i < itemList.size(); i++) {
                objectOut.writeObject(itemList.get(i));
                // System.out.println("The Object  was succesfully written to a file");
            }
            objectOut.close();
        
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void appendItem(Item item) {
        ArrayList<Item> fileItems = readItems();
        fileItems.add(item);
        writeItems(fileItems);
    }

    /**
     * replaceItem()
     * Replaces the item at the desired line in the file with a new or modified item. Can also be used to delete items
     * @param index: int of index (line) in file to be replaced
     * @param newitem: replace with this item. Use null to delete. 
     * 
     */
    public void replaceItem(int index, Item newItem) {
        ArrayList<Item> items = readItems();
        if (newItem == null) {
            items.remove(index);
        } else {
            items.set(index, newItem);
        }
        writeItems(items);
    }

    public static void saveUser(User user, String filename) {
        
    }
}

