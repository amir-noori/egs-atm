# ATM Assignment Task 

## Implemented Features
##### The followings are the main features that are implemented in this application:

#### ATM-SERVICE microservice: 
    This service connects to bank service via rest which is authenticated by JWT token.
    Also Resilience4J is used here for failover mechanism is the bank services have issues.

#### BANK-SERVICE microservice: 
    Validate bank card, execute transactions and persists in the database.
    Exception handling mechanism is implemented for authentication or transaction exceptions.
    Postgres is used as database. The initial data to test is in the following directory:
        project_directory/BankService/src/main/resources/sql

#### Spring API Gateway: 
    It is used as API gateway and loadbalancer for the previous services.

#### Eureka: 
    It is used as discovery server for microservices.




### ATM Rest Services
#### The following are the list of services available:

To authenticate with bank service from ATM:
```
http://localhost:4003/atm/service/authenticate:

{
    "cardNumber": "1",  
    "userId": "1",
    "pinCode": "1234",
    "authMethod": "0"
}
```

To check balance:
```
http://localhost:4003/atm/service/balance:

{
    "cardNumber": "1"
}
```

To deposit:
```
http://localhost:4003/atm/service/deposit:

{
    "cardNumber": "1",
    "amount": "100"
}
```

To withdrawal:
```
http://localhost:4003/atm/service/withdrawal:

{
    "cardNumber": "1",
    "amount": "100"

}

```
