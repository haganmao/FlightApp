# FlightMate
This is a simple android flight app, I give the name of app is Flight Mate, the aim is to help users buy the tickets through android mobile devices. The original reason of creating this flight booking app is mainly because I often find that I could not get one ticket at the right price when I want to buy a flight ticket to go somewhere. If I go to a certain airline webpage or app to book a ticket at certain time, such as holidays, the airline companies may raise the price of the ticket, which will cause some users to buy the ticket with extra price. The purpose of my app is to provide users with the ability to compare the prices of the same route of each airline and give them a wide range of choice to buy a cheaper ticket.

![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504554.png)
<br>
<br>
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504585.png)
<br>
<br>
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504597.png)
<br>
<br>
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504924.png)
<br>
<br>
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504931.png)
<br>
<br>
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/Screenshot_1598504951.png)
<br>
<br>
<br>
<br>

# Deliverables  
*	Enter the Main Screen, there are two choice for users, user can enter either Sign Page or Sign Up.

*	Enter the phone number and password, user can click sign in button into the homepage immediately.

*	Enter the phone number and name, and Password, user able to click sign up button to register account.


*	User can get proper message if they registered successfully.
*	User can get warning message once type wrong password or phone number.

*	Enter the homepage, user can check current bookings.

*	Enter the Airline list pages, user can click one of airlines to load
Airline details.

*	Enter the Airline list pages, user can click one of airlines to load
Airline details.

*	Enter the Airline detail pages, user can check airline ticket price.

*	Enter the Airline detail pages, user can check read airline ticket descriptions.

*	Enter the Airline detail pages, user can add or count number of airline ticket. 

*	 Enter the Airline detail pages, user can add airline ticket to My bookings.

*	User can click menu bar to load side app drawer.

*	User can check current booking and booking history through the side app drawer.

*	User able to sign out from homepage.

*	User able to Delete item of current bookings

*	User can refresh the destination page

*	User can delete booking history after confirmed order

*	User can get all information about booking history

*	User can use Remember me feature to be able to login without typing phone number and password

*	User can retrieve password if they forget password

*	User can not use the app without internet

*	User able to search the airline ticket by providing airline name

*	User able to add the airline to favourite

*	User able to remove favourite airline

*	User able to rate the airline service

*	User able to comment airline service

*	User able to reset password after login 

*	User able to update Payment status 

<br>
<br>

# Requirements specification
*	To running this app, the minimum requirements is that API of android device should be above 23.
*	To running this app, you may need use android studio emulator.
*	This app may have different layout in different device, currently working properly in Nexus 4 API 26.
*	Test case phone number:0224004614, password:123456
*	In order to booking a flight ticket, this app ask user to register with phone number and password (Reason for requiring phone number see Constrains encountered and strategies part)
*	Users using the App must enter a valid mobile phone number and password meanwhile pass the verification and authentication by firebase cloud database.
*	User only can book up to 10 tickets at one time.
*	User sign up the account with secure code
* User find password by providing secure code
<br>
<br>

# Conceptual Framework
![svg](https://github.com/haganmao/FlightApp/blob/master/app/src/image/dbdemo.png)
<br>
