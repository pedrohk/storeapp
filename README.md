

•	JPA: Used for object-relational mapping between Java objects and the database. </br>
•	H2 Database: An embedded, in-memory database, suitable for development and testing.</br></br></br>
# Store Application API Endpoints

## Product Categories

* **GET /api/product-categories**: Retrieves a list of all product categories.
* **GET /api/product-categories/{id}**: Retrieves a specific product category by its ID.
* **POST /api/product-categories**: Creates a new product category.
* **PUT /api/product-categories/{id}**: Updates an existing product category.
* **DELETE /api/product-categories/{id}**: Deletes a product category.

## Subcategories

* **GET /api/subcategories**: Retrieves a list of all subcategories.
* **GET /api/subcategories/{id}**: Retrieves a specific subcategory by its ID.
* **POST /api/subcategories**: Creates a new subcategory.
* **PUT /api/subcategories/{id}**: Updates an existing subcategory.
* **DELETE /api/subcategories/{id}**: Deletes a subcategory.

## Products

* **GET /api/products**: Retrieves a list of all products.
* **GET /api/products/{id}**: Retrieves a specific product by its ID.
* **POST /api/products**: Creates a new product.
* **PUT /api/products/{id}**: Updates an existing product.
* **DELETE /api/products/{id}**: Deletes a product.

## Addresses

* **GET /api/addresses/{id}**: Retrieves a specific address by its ID.
* **POST /api/addresses**: Creates a new address.
* **PUT /api/addresses/{id}**: Updates an existing address.
* **DELETE /api/addresses/{id}**: Deletes an address.

## Clients

* **GET /api/clients**: Retrieves a list of all clients.
* **GET /api/clients/{id}**: Retrieves a specific client by their ID.
* **POST /api/clients**: Creates a new client.
* **PUT /api/clients/{id}**: Updates an existing client.
* **DELETE /api/clients/{id}**: Deletes a client.
* **POST /api/clients/{clientId}/addresses**: Adds an existing address to a client.
* **DELETE /api/clients/{clientId}/addresses/{addressId}**: Removes an address from a client.

•	JUnit and Mockito: Used for testing the service layer in isolation.</br>
•	Maven: Manages project dependencies and builds.</br>
•	Java 23 and Spring Boot: The core technologies used.

