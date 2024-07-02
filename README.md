# FoodShop

## Introduction
This project uses microservices architecture and is deployed on Docker 

**User interface**: *http://localhost:5173*

**Home page**

![image](https://github.com/Jackson22153/FoodShop/assets/96383013/a043443f-a1d0-434f-922c-b88d3edb21ec)


### Functionalities
  - **Login**: User can login to the website
  - **Register**: User can register to access to the website
  - **Manage Cart**: Customers can add products to cart, remove products from cart, select product to order.
  - **View and Find products**: Users can view and search available products.
  - **Update User information**: Customers, Employees, Admin can update their user' information
  - **View orders**: Customers and Employees can view their related orders 
  - **Process order**: This is a procedure of an order that relate to both customer and employee. 
  - **Notification**: a notifiation will be send to customers, employees if some activities relate to them have been executed.
  - **Update/Add category, product, user data**: Admin can update or add the category, product, user data.   
### Technologies
  - **Backend**: Keycloak, Rabbitmq, Websocket, Redis, Spring, Sql, Nginx, Oauth2 Proxy,...
  - **Frontend**: Typescript
### Service
1. **Account**: a service that is used to process data related to users
2. **Shop**: a service that is used to process data related to products, cart
3. **Order**: a service that is used to process order.
4. **Notification**: a service that is used to send and receive notification from other services using websocket.
5. **Nginx**: a reverse proxy routing client to microservices
6. **Keycloak**: an authorization server which is used to authenticate and authorize users
7. **Oauth2 proxy**: a proxy which is placed between nginx and keycloak for Oauth2 Authorization Code Flow and validate request from client and pass access token to microservices
8. **Sql**: Store databases of Account Service, Order Service, Notification Service, Shop Service and Keycloak
9. **Redis**: is used to cache data of other services and store Oauth2 proxy's session
10. **Rabbitmq**: creating some message queues for services to interact with each other and also used for an order queue to validate and update customer's order

## Installation
  ### For the **Backend**
  1. Open Terminal or Command prompt
  2. Navigate to *./FoodShop/dockercompose/hub/* (where the docker-compose.yml file located at)
  3. Execute the following command 
  ```
      docker compose up
  ```
  ### For the Frontend
  1. Open Terminal or Command prompt
  2. Navigate to *./FoodShop/food-shop-ui/*
  3. Execute the following commands
  ```
      npm install
      npm run dev
  ```
User interface: *http://localhost:5173*

Shop service: *http://localhost:8081/document*

Account service: *http://localhost:8082/document*

Order service: *http://localhost:8083/document*

Notification service: *http://localhost:8084/document*
