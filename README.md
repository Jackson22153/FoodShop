# FoodShop

## Introduction
This project uses microservices architecture and is deployed on Docker 

![image](https://github.com/Jackson22153/FoodShop/assets/96383013/1a843ec0-8c04-4023-b0eb-caae21d9b410)

### Functionalities
  - **Login**: User can log in to the website
  - **Register**: User can register to access to the website
  - **Manage Cart**: Customer can add products to cart, remove products from cart, select product to order.
  - **View and Find products**: User can view and search available products
  - **Update User information**: Customer, Employee, Admin can update their information
  - **View orders**: Customer and Employee can view their related orders 
  - **Process order**: Customer can place an order and employee can validate these orders.
  - **Notification**: A notifiation will be send to customer, employee if some activities relate to them have been executed.
  - **Update/Add category, product, user data**: Admin can update or add the category, product, user data.   
### Technologies
  - **Backend**: Keycloak, Rabbitmq, Websocket, Redis, Spring, Sql, Nginx, Oauth2 Proxy.
  - **Frontend**: Typescript, React js
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
**User interface**: http://localhost:5173

**Shop's document**: http://localhost:8081/document

**Account's document**: http://localhost:8082/document

**Order's document**: http://localhost:8083/document

**Notification's document**: http://localhost:8084/document

**Keycloak RestAPI's document**: http://localhost:8085/document 
