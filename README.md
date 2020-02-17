# BookingGo Technical Test

## Usage

### Setup
  The application was packaged using the `spring-boot-maven-plugin` into a jar file containing all of the dependencies. The project uses Java 8.

### Part 1: command line app

The command line arguments needed are: pickup, dropoff and number of passengers.

```bash
git clone https://github.com/mihneasavu/bookinggo.git
cd bookinggo/target/
java -jar bookinggo-0.0.1-SNAPSHOT.jar 51.470020,-0.454295 51.470020,-0.454295 3
```
The available options for the number of passengers specified will be displayed in descending price order. If multiple providers offer the same vehicle type, the cheapest one is selected.  

### Part 2: REST API

To start the endpoint, run the jar file with no arguments.
```bash
git clone https://github.com/mihneasavu/bookinggo.git
cd bookinggo/target/
java -jar bookinggo-0.0.1-SNAPSHOT.jar
```


- To query all providers:  `http://localhost:8080/rideways`. Example:

   `http://localhost:8080/rideways?pickup=51.470020,-0.454295&dropoff=53.481134,-2.240262&passengers=1`

   Json result:
   
   ```json
   [
  {
    "car_type": "MINIBUS",
    "price": 285943,
    "provider": "JEFF"
  },
  {
    "car_type": "LUXURY",
    "price": 264515,
    "provider": "ERIC"
  },
  {
    "car_type": "STANDARD",
    "price": 220912,
    "provider": "ERIC"
  },
  {
    "car_type": "LUXURY_PEOPLE_CARRIER",
    "price": 116380,
    "provider": "ERIC"
  },
  {
    "car_type": "PEOPLE_CARRIER",
    "price": 86436,
    "provider": "JEFF"
  }
  ]
   ```
- To query for a specific provider, the path is  http://localhost:8080/{providerName}. Example:
  
  `http://localhost:8080/rideways/dave?pickup=51.470020,-0.454295&dropoff=53.481134,-2.240262&passengers=1`
  
  Result:
  
  ```json
  {
  "supplier_id": "DAVE",
  "pickup": "51.470020,-0.454295",
  "dropoff": "3.410632,-2.157533",
  "options": [
    {
      "car_type": "STANDARD",
      "price": 591832
    },
    {
      "car_type": "EXECUTIVE",
      "price": 214412
    },
    {
      "car_type": "LUXURY",
      "price": 736456
    },
    {
      "car_type": "PEOPLE_CARRIER",
      "price": 982982
    },
    {
      "car_type": "LUXURY_PEOPLE_CARRIER",
      "price": 993459
    }
    ]
  }
  ```
  
## Possible improvements

The API can be easily expanded and made more complex, with more verbose error messages. 
It would also be extremely easy to handle different suppliers having different API implementations.

The tests use a local Wiremock server to mimic responses from the supplier API. This was kept simple for this project but could be improved by using for example [response templating](http://wiremock.org/docs/response-templating/).

The URLs for the API could be stored in properties file, facilitating changing the URL during testing.

In a production application, the vehicle types and capacity might be stored in a database, depending on how often these change or on different suppliers having different vehicle names.


