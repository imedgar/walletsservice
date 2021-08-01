# Wallets Service

Technical test for Playtomic

## Getting Started

Clone the project from the Github repository (https://github.com/imedgar/walletsservice) and import into your preferred IDE.

### Prerequisites

Java SDK 11

### Dependencies

- Lombok (https://projectlombok.org/)
- Spock (https://spockframework.org/)

### Installing

In order to test the project, you should do a SpringBoot run and it will deploy the project into the embedded tomcat

```
mvn spring-boot:run
```

## Running the tests

Run all the unit test classes.
```
mvn test
```
Run a single test class.
```
mvn -Dtest=TestApp1 test
```

## Dummy data
There is a data model population method in the WalletConfig class that triggers when the app is running 
for testing purposes. 

## Request Examples:

###Get a wallet using its identifier.
Request:
```
curl --request GET \
  --url http://localhost:8090/wallets/2
```
Response:
```
{
  "data": {
    "id": 1,
    "balance": 200.00,
    "user": "Sam"
  }
}
```

###Recharge money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.
Request:
```
curl --request PUT \
  --url http://localhost:8090/wallets/1/recharge \
  --header 'Content-Type: application/json' \
  --data '{
	"creditCardNumber": "4001020000000009",
	"amount": 15.0
}'
```
Response:
```
{
  "data": {
    "id": 1,
    "balance": 115.00,
    "user": "Sam"
  }
}
```

###Subtract an amount from a wallet (that is, make a charge in that wallet).
Request:
```
curl --request PUT \
  --url http://localhost:8090/wallets/1/subtract \
  --header 'Content-Type: application/json' \
  --data '{
	"amount": 100.0
}'
```
Response:
```
{
  "data": {
    "id": 1,
    "balance": 15.00,
    "user": "Sam"
  }
}
```

## Authors

* **Edgar Garcia**
