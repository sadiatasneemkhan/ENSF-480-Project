# Description

This is a **Property Rental Management System (PRMS)** that has multiple functions and caters to four types of users (landlords, regular renters, registered renters and managers). 

## Functionalities 
- Landlords can login and have the following options:
  - Register their properties
  - Pay a certain fee to publish their property for renters to view it
  - Change property listing status to active, rented, cancelled or suspended

- Renters can search for properties based on one or more of these criteria:
  - House type (apartment, townhouse, etc)
  - Number of bedrooms
  - Number of bathrooms
  - City quadrant
  
- Regular renters do not need to login but are still able to search for properties
- Renters who want to be notified of new listings that match their criteria can register for an account. The list of notifications can be viewed whenever they login.

- Renters can send an email to the landlord if they are interested in a property or would like further details

- Managers can login and have the following options:
  - Full access to renter, landlord and property information through the company's database
  - Change listing status to active, rented, cancelled or suspended
  - Change fee amount and period
  - Create a periodical summary report that shows:
    - Number of properties listed within a period 
    - Number of properties rented within a period
    - Number of active listings 
    - List of properties rented, displaying landlord's email, property ID and address


# Video Demonstration

https://youtu.be/MZ6zLIa2UrY <br/>
Please watch the video demonstration to see the functionalities of our Property Rental Management System. 
The demonstration begins at 3:41.

# Setup

1. make sure MySQL is running. enter interactive mysql (`mysql -uroot -p` in terminal), then run `source setup.sql`.
2. install maven https://maven.apache.org/install.html
3. run `mvn package` to install dependencies and compile
4. run `mvn exec:java` to run everything in App.main
