

•	JPA: Used for object-relational mapping between Java objects and the database. </br>
•	H2 Database: An embedded, in-memory database, suitable for development and testing.</br></br></br>
•	REST Endpoints:</br>
o	GET /api/clients: Retrieves all clients.</br>
o	GET /api/clients/{id}: Retrieves a specific client by ID.</br>
o	POST /api/clients: Creates a new client.</br>
o	PUT /api/clients/{id}: Updates an existing client.</br>
o	DELETE /api/clients/{id}: Deletes a client.</br>
o	POST /api/clients/{clientId}/addresses: Adds an address to a client.</br>
o	DELETE /api/clients/{clientId}/addresses/{addressId}: Removes an address from a client.</br>
o	GET /api/addresses/{id}: Retrieves a specific address.</br>
o	POST /api/addresses: Creates a new address.</br>
o	PUT /api/addresses/{id}: Updates an existing address.</br>
o	DELETE /api/addresses/{id}: Deletes an address.</br></br></br>
•	JUnit and Mockito: Used for testing the service layer in isolation.</br>
•	Maven: Manages project dependencies and builds.</br>
•	Java 23 and Spring Boot: The core technologies used.

