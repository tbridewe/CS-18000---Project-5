# CS 18000 Gadgets and Gizmos Project 5 TESTING

## Test 1: User creates account
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Create Account button on the second page.
4. User selects the Username and Password textboxes.
5. User enters username and password into the textbox with their keyboard.
6. User selects the Create New Account button after entering their information.

Expected Result: Application verifies that the username and password are correct and creates an account. The account is saved to the userData.txt file and the user is automatically logged into the program.

Test Status: Error

## Test 2: User logs in
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Log In button on the second page.
4. User selects the Username and Password textboxes.
5. User enters username and password into the textbox with their keyboard.
6. User selects the Log In button.

Expected Result: Application verifies that the username and password exist in the userData.txt file and that the user being entered exists. User is logged in and able to utilize the program.

Test Status: Error

## Test 3 Quit
1. User launches application.
2. User selects the Menu button on the welcome page.
3. User selects the Quit button on the second page.

Expected Result: Application returns to the welcome page.

Test Status: Passed
