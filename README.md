# ATM-Machine
An ATM Machine REST API

Application was written in JDK 15, but requires a minimum of Java 12.  
To make project run, please download intelliJ plugin called "Lombok", while also downloading JDK 12+.

The app was written having production in mind, so it has a fully logging functionality, that logs every action with the 
associated details, has a high code coverage testing, and was deployed and can be accessed in realtime. Application was 
deployed to Heroku server and can be found here: https://razvans-atm-simulator.herokuapp.com/


Postman Collection can be found here (Import - Raw): https://github.com/razvancornita/ATM-Machine/blob/main/ATM-Simulator.postman_collection.json

To connect to database GUI:
1. Go to https://razvans-atm-simulator.herokuapp.com/h2-console or http://localhost:8090/h2-console
2. Enter the value of the property spring.datasource.url from application.properties in the JDBC URL textbox
4. Enter the username & password defined in application.properties


High level design:
    The application is designed for RON, EUR or USD card holders. It includes the most used functionalities that can be 
done at an ATM with a card. The app is designed for users who have a bank account and at least one card for that bank
account. The functionalities that this app offers are:
* authenticating by entering the card and the PIN.
* changing PIN
* see bank account balance
* withdraw card
* deposit or withdraw money (in EUR, RON or USD).

The main restriction of this app was to have only 1 endpoint, so the app offers multiple HTTP methods, for the same endpoint ("/atmOperation"):
* GET - for getting the account balance
* PUT - for changing PIN
* DELETE - for deauthenticating
* POST - for authenticating, depositing and withdrawing money

Check the file "Business Analyisis.txt" for detailed descriptions of each method, along with their restrictions. The file can be found here: https://github.com/razvancornita/ATM-Machine/blob/main/Business%20Analysis.txt

Future improvements:
- add more currencies & call an API for conversions
- migrate from JdbcTemplate to JPA & Hibernate
- handle exceptions gracefully
- handle requests in parallel
