Credit data storage system
===

### Description of system

The system is responsible for the following functionalities:
* creating credit data
* storing credit data
* returning stored credit data


### System components
* #### Credit microservice

    **Credit microservice** is the entry point of the system.
It exposes two endpoints to receive credit data and to return list of available credits.
Generates credit number, creates requests to customer microservice and product microservice.
Credit service is responsible for storing credit number and name.

* #### Product microservice
    **Product microservice** is responsible for storing product data.

* #### Customer microservice
  **Customer microservice** is responsible for storing customer data.

* #### Postgres database
    **Postgres database** operates of credits information by storing, retrieving and managing.

### Example of use

To create new credit, send request to endpoint **POST /create-credit**. Example request object:
```
{
  "credit": {
    "creditName": "Grenades"
  },
  "customer": {
    "firstName": "John",
    "pesel": "01234567890",
    "surname": "Rambo"
  },
  "product": {
    "productName": "Credit",
    "value": 100
  }
}
```

Endpoint **GET /get-credits** returns all available credit data.

Endpoints require application/json content type.

Detailed information is available on swagger API page at **host-address:30000/swagger-ui.html**.

### Requirements
* Java 11

##### Build requirements
* Apache Maven
* Docker
* Kubernetes

##### Build and deploy process
 * run build script (creates executable jar and docker image of each microservice)
 * run init-postgres script (downloads - if not present, latest postgres docker image, creates kubernetes deployment)
 * run init-microservice script (should be executed after database init, creates microservices kubernetes deployment)

To remove all created kubernetes deployments and docker images (excluded postgres) run clean-up script.