# FoodShop

## Introduction
This project uses microservices architecture and is deployed on Docker 

![image](https://github.com/Jackson22153/FoodShop/assets/96383013/1a843ec0-8c04-4023-b0eb-caae21d9b410)

### Functionalities
  - **Login**: Users can log in to the website
  - **Register**: Users can register to access to the website
  - **Manage Cart**: Customers can add products to cart, remove products from cart, select product to order.
  - **View and Find products**: Users can view and search available products
  - **Update User information**: All Customers, Employees and Admin can update their profiles
  - **View orders**: Customers and Employees can view their related orders 
  - **Process order**: Customers can place an order and employees can approve or reject these orders.
  - **Notification**: A notifiation will be send to customers, employees if some activities related to them have been executed.
  - **Update/Add category, product, user data**: Admin can update or add the category, product, employee's profile.   
### Technologies
  - **Backend**: Keycloak, Rabbitmq, Websocket, Redis, Spring, Sql, Nginx, Oauth2 Proxy.
  - **Frontend**: Typescript, React js
## Installation
  1. Open Terminal or Command prompt
  2. Navigate to *./FoodShop/dockercompose/hub/* (where the docker-compose.yml file is located)
  3. Execute the following command 
  ```
      docker compose up
  ```
**User interface**: http://localhost:5173

**Shop's document**: http://localhost:8081/document

**Account's document**: http://localhost:8082/document

**Order's document**: http://localhost:8083/document

**Notification's document**: http://localhost:8084/document

**Keycloak RestAPI's document**: http://localhost:8085/document 
