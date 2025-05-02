

•	JPA: Used for object-relational mapping between Java objects and the database.
•	H2 Database: An embedded, in-memory database, suitable for development and testing.
•	REST Endpoints:
o	GET /api/clients: Retrieves all clients.
o	GET /api/clients/{id}: Retrieves a specific client by ID.
o	POST /api/clients: Creates a new client.
o	PUT /api/clients/{id}: Updates an existing client.
o	DELETE /api/clients/{id}: Deletes a client.
o	POST /api/clients/{clientId}/addresses: Adds an address to a client.
o	DELETE /api/clients/{clientId}/addresses/{addressId}: Removes an address from a client.
o	GET /api/addresses/{id}: Retrieves a specific address.
o	POST /api/addresses: Creates a new address.
o	PUT /api/addresses/{id}: Updates an existing address.
o	DELETE /api/addresses/{id}: Deletes an address.
•	JUnit and Mockito: Used for testing the service layer in isolation.
•	Maven: Manages project dependencies and builds.
•	Java 23 and Spring Boot: The core technologies used.

