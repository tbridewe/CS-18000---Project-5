# CS 18000 - Project 5 - Gadgets & Gizmos

## Running the Project
To run Project 5, navigate to the Server class and run Server.java. Before you can access the menus, you must create an account. The account will ask you to specify whether you are a buyer or a seller. After creating an account, it will take you back to the login menu. Input your credentials and begin!

## Submissions
- Brightspace - 
Liam McCormack Submitted Report
Liam McCormack Submitted Video
- Vocareum -
---
___
## User
Provides implementation of a User in the Marketplace, either of a Seller or a Customer.
### Constructor 
| Parameters | Description |
| ---------- | ----------- |
| String email, String password, int userType | Creates a new User object. |
### Methods
| Method | Parameters | Returns | Description |
| -------| ---------- | ------- | ----------- |
| getEmail | public none | String | Returns the email of the current User object. |
| getPassword | public none | String | Returns the password of the current User object. |
| isBuyer | public none | boolean | Returns a boolean whether the user is a buyer or not. |
| isSeller | public none | boolean | Returns a boolean whether the user is a seller or not. |
| setUserType | public int userType | void | Sets the userType of the current User Object. userType of 0 sets the User as a buyer. userType of 1 sets the User as a seller. |
| setEmail | public String email | void | Sets the email of the current User object. If the email is not valid, then the method will throw the InvalidUserInput exception. |
| setPassword | public String password | void | Sets the password of the current User object. If the password is not valid, then the method will throw the InvalidUserInput exception. |
| accountExists | public static String emailEntered | boolean | Checks if a user's information is currently in the userData.txt file. Reads the file, and if any of the lines have an indexOf the emailEntered that is greater than 1 then the method returns true. Otherwise, returns false. In the case of FileNotFoundException and IOException, also returns false and prints the error message. |
| readUserData | public static none | ArrayList<String> | Reads the userData.txt file and returns all of the results as an ArrayList<String>. The entries in the ArrayList are indexed in order that they were read. In the case of FileNotFoundException or IOException, returns null and print the error message.
| saveNewUser | public static String email, String password, String userType | void | Opens a FileWriter in append mode and writes the user's information to the userData.txt file in the following format: Email:%s, Password:%s, UserType: %s. In the case of an IOException, prints the error message and does not save the new user's information. |
| isCorrectLogin | public static String emailEntered, String passwordEntered | int | Verifies whether the user has a correct login by first calling readUserData(). After the data has been read, if the passwordEntered and emailEntered are equal to the email and password of any line, returns the userType of the user. Buyer = 0, Seller = 1. Returns -1 if the login is not correct (no matching password and email have been found in the userData.txt file). |
| isValidPassword | public static String password | boolean | Verifies if the password passed is valid or not. A valid password is not null and has a length > 0. If the password is valid, returns true. Otherwise, returns false. |
| isValidEmail | public static String email | boolean | Verifies if the email passed is valid or not. A valid email has an @ that is not at the first index of the string, a 3 letter dot ending (such as .com, .hub, etc.) and the space between the extension and @ are 3 characters apart (Ex. email@bro.com). If the email is valid, returns true. Otherwise, returns false. |
| editUser| public String newEmail, String newPassword, boolean deleted | void | Allows the user to edit their information. For newEmail and newPassword != null, verifies that email and/or password are valid. Calls readUserData() to receive userData in an ArrayList, and iterates through each of the lines in the userData.txt file. If the email and password of the User object are the same as the email and password on file, then edits the string to contain the new email and/or password. If the deleted boolean is true, then the string is removed entirely. Rewrites all of the new information to the userData.txt file afters with a PrintWriter. |
| getDisplayedItem | public int index | Item | Returns the listing at the index - 1 passed to the method in the sortedListings ArrayList. Throws an IndexOutOfBoundsException if the value at index - 1 in sortedListings is out of bounds.
| loadListings | protected String fileName| void | Calls the readFile() method with the passed fileName. Iterates through the read file and sets the listings to an Item ArrayList.
| printListings | public none | void | Prints the formatted listings in their current sorted or unsorted order.
| writeFile | protected static String filename, String[] lines | Writes to the provided file with the provided array of strings.
| readFile | protected static String filename | String[] | Reads from the file and returns an array of file lines.
| unsortListings | public none | void | Sets the sortedListings field to the initial listings field.
### Customer
Provides implementation of a Customer in the Marketplace
### Constructor 
| Parameters | Description |
| ----------- | ----------- |
| String email, String password, int userType | intializes a Customer object |
### Methods
| Method      | Parameters | Returns |  Description |
| ----------- | ----------- | ----------- | ----------- |
| printCart     | none | void | Prints the items in the cart with nice formatting. |
| addToCart     |  Item item, int quantity | void | Adds the item to the cart and adjusts the items remaining in the store accordingly. |
| saveCart | String fileName | void | Writes all the items in this.cart to the cart file. Puts them on the line of the corresponding user. |
| saveListings | String fileName | void | Writes all the items in this.cart to the cart file. Puts them on the line of the corresponding user. |
| loadCart | String fileName | void | Reads the cart file and puts the items for this user into this.cart. |
| removeFromCart | int index, int quanitity | void | Removes specified listing from the cart. |
| keywordSearch | String keyword | ArrayList \<Item> | Returns a String[] containing all lines containing the keyword. |
| checkout | none | double | Returns the total price of the cart. |
| readPurchaseLog | none | ArrayList \<Item> | Returns an ArrayList of the contents of the customer log file associated with a specific email. |
| viewPurchases | none | void | Prints out the contents of the customer log file associated with a specific email. |
| exportPurchases | String fileName | void | Writes the contents of the customer log file associated with a specific email to a file. |
| sortMarketplace | int sortType, int sortOrder | void | Sorts the marketplace listings based on user input. |
### Static Classes
| Class | Interfaces Implemented | Description |
| ----------- | ----------- | ----------- |
| PriceComparatorAscending | Comparator \<String> | Comparator for sorting by price in ascending order |
| PriceComparatorDescending | Comparator \<String> | Comparator for sorting by price in descending order |
| QuantityComparatorAscending | Comparator \<String> | Comparator for sorting by price in ascending order |
| QuantityComparatorDescending | Comparator \<String> | Comparator for sorting by price in descending order |
## Seller
Provides implementation of a Customer in the Marketplace
### Constructor 
| Parameters | Description |
| ---------- | ----------- |
| String email, String password, int userType| Creates a new Seller object which extends User. Seller's stores are also instantiated. |
### Methods
| Method | Parameters | Returns | Description |
| -------| ---------- | ------- | ----------- |
| getStores | none | ArrayList \<String\> | |
| saveStores | none | void | |
| displayItems | none | ArrayList \<String\> | Reads file with item listings and displays listings that the seller posted. |
| addFromCSV | String filename | void | Reads file with item listings and adds them to the exisitng item listings. |
| addNewItem | Item item | void | Allows a seller to add one item to item listings at a time. |
| removeItem | Item item | void | Allows a seller to remove one item from item listings at a time. |
| editItem | Item item, int changeType, String changeValue | void | Seller defines the item to change, what value type to change, and what value to change it to, and the item value is changed. |
| viewAllStats | none | void | Allows seller to see all the stats from their store. |
| sortStats | int sortType, int sortOrder | void | Allows a seller to see stats based on type and order. |
## Item
A listing in the Marketplace
### Constructors 
| Parameters | Description |
| ----------- | ----------- |
| String name, String store, String description, int quantity, double price | Creates a new Item object. |
| String itemFileLine | Creates a new Item object.|
| Item item | Makes a new copy of an Item. |
### Methods
| Method      | Parameters | Returns | Description |
| ----------- | ----------- | ----------- | ----------- |
| toLine     |   none          | String | Returns a description of an item in a specified format |
| equals     |   Item item          | none | Checks if items are equal but does not check quantity |
| findItem | ArrayList<Item> itemList | int | Returns index of item in array of items. Returns -1 if no item is found. |
| getName | none | String | Returns the name of the Item. |
| getStore | none | String | Returns the store of the Item. |
| getDescription | none | String | Returns the description of the Item. |
| getQuantity | none | int | Returns the quantity of the Item. |
| getPrice | none | double | Returns the price of the Item. |
| setName | String name | void | Sets the name of the Item. |
| setStore | String store | void | Sets the store of the Item. |
| setDescription | String description | void | Sets the description of the Item. |
| setQuantity | int quantity | void | Sets the quantity of the Item. |
| setPrice | double price | void | Sets the price of the Item. |
| changeQuantityBy | int number | void | Changes a quantity by a certain number. |
## Menu
Displays prompts for the user
## IncorrectLoginInformationException
Thrown when login information is incorrect
### Constructor 
| Parameters | Description |
| ----------- | ----------- |
| String message | An error that is thrown when login information is incorrect |
## InvalidLineException
Thrown when line is invalid
### Constructor 
| Parameters | Description |
| ----------- | ----------- |
| String message | An error that is thrown when the line is invalid |
## InvalidQuantityException
Thrown when quantity is invalid
### Constructor 
| Parameters | Description |
| ----------- | ----------- |
| String message | An error that is thrown when the quantity is invalid |
## InvalidUserInputException
Thrown when user input is invalid
### Constructor 
| Parameters | Description |
| ----------- | ----------- |
| String message | An error that is thrown when the input is invalid |

___
