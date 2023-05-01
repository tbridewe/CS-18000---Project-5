# CS 18000 Gadgets and Gizmos Project 5 TESTING

## Test 1: User creates account
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Create Account button on the second page.
4. User selects the Username and Password textboxes.
5. User enters username and password into the textbox with their keyboard.
6. User selects the Seller or Customer button to identify which type of account they are trying to make
7. User selects the Create New Account button after entering their information.

Expected Result: Application verifies that the username and password are correct and creates an account. The account is saved to the userData.txt file and the user is automatically logged into the program.

Test Status: Passed

## Test 2: User logs in
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Log In button on the second page.
4. User selects the Username and Password textboxes.
5. User enters username and password into the textbox with their keyboard.
6. User selects the Log In button.

Expected Result: Application verifies that the username and password exist in the userData.txt file and that the user being entered exists. User is logged in and able to utilize the program.

Test Status: Passed

## Test 3 Quit
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Quit button on the second page.

Expected Result: Application returns to the welcome page.

Test Status: Passed

## Test 4 Seller creates single listing
1. User launches application.
2. User logs in (see instructions above).
3. User selects View Listings button on the second page.
4. User selects Add Item button on the listings page.
5. User selects the Name, Description, Price, Quantity, and Store textboxes.
6. User enters information into the afforementioned textboxes.
7. User selects the Enter button.

Expected Result: Application verifies that the information entered is valid and then creates a new listing based on the information entered.

Test Status: Passed

## Test 5 Seller creates listings from .csv
1. User launches application.
2. User logs in (see instructions above).
3. User selects View Listings button on the second page.
4. User selects the Add from CSV button on the listings page.
5. User selects the Enter File Name text box.
6. User enters a .csv file name into the text box.
7. User presses the Enter button.

Expected Result: The application verifies that the file entered exists, and then reads information from the file. Items read are created and listed as new items in the marketplace.

Test Status: Passed

## Test 6 User logs out
1. User launches application.
2. User logs in (see above).
3. User selects the Log Out button on the second page.

Expected Result: The application logs the user out and returns them to the welcome page

Test Status: Passed

## Test 7 Customer sorts items by price (ascending)
1. User launches application.
2. User logs in (see above).
3. User selects the sort button on the second page.
4. User presses the Price button and then the Ascending button on the sort page.
5. User selects the Back button on the sort page.
7. User selects the Choose an Item button on the second page.
8. User views the drop down menu.

Expected Result: The application displays the items listed by price in ascending order in the items dropdown to the user.

Test Status: Passed.

## Test 8 User edits account information
1. User launches application.
2. User logs in (see above).
3. User selects the Edit Account button on the second page.
4. User selects the Edit Account Email button on the Edit Account page.
5. User selects the text box for entering a new email.
6. User enters the log out button after entering a new email.
7. User selects the Menu button from the welcome page.
8. User logs in with their new email (see above).

Expected Result: Application allows user to login with the new email that they entered for their account in the edit account section.

Test Status: Passed

## Test 9 Delete Account
1. User launches application.
2. User logs in (see above).
3. User clicks the Edit Account button on the second page.
4. User clicks the Delete Account button on the Edit Account Page.

Expected Result: The application successfully deletes the user's account.

Test Status: Passed

## Test 10 Customer Checks out items
1. User launches application
2. User logs in (see above).
3. User presses the View Cart button on the second page.
4. User presses the Checkout button on the View Cart page.

Expected Result: The user is able to checkout with their items (assuming the items being checked out still exist)

Test Status: Passed

## Test 11 Customer adds an item to their cart
1. User launches application.
2. Users logs in (see above).
3. User presses the Choose an Item button on the second page.
4. User selects an available item from the dropdown menu.
5. User selects the quantity of items to purchase textbox.
6. User enters how many items they want to buy in the quantity of items to purchase textbox.
7. User presses the Enter button on the View Listings page.

Expected Result: The item and quantity that was entered of that item are added to the user's cart

Test Status: Passed

## Test 12 Customer views their purchase history
1. User launches application.
2. User logs in (see above).
3. User presses the View Cart button on the second page.
4. User presses the View Purchase History on the View Cart Page.

Expected Result: The application returns a dropdown list of the items that the user has bought from the store in the past.

Test Status: Passed

## Test 13 Seller sorts statistics (by price)
1. User launches application.
2. User logs in (see above).
3. User selects the View statistics button on the second page.
4. User selects View Sorted Statistics on the View statistics page.
5. User selects the Price button on the Sorted Statistics page.
6. User selects Ascending or Descending on the Price statistics page.

Expected Result: Seller statistics listed by price are returned to the seller and viewable on their screen.

Test Status: Failed.

## Test 14 Customer searches for a specific item
1. User launches application.
2. User logs in (see above). 
3. User selects the Search button on the second page.
4. User enters a keyword to search from.
5. If the item exists, in this case, a TV, return a dropdown for the user that displays items with the searched for keyword

Expected Result: The application displays a dropdown with items that were searched for, if they exist.

Test Status: Passed
