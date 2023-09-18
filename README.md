## README - Purchase Transactions Project with Currency Conversion

This repository contains a Java project for managing purchase transactions and currency conversion. The project uses an embedded H2 database, eliminating the need for external installations.

Usage Instructions:

1. Clone the repository to your local environment.

2. Open the project in your preferred IDE.

3. Run the Java application.

4. Access Swagger to interact with the API endpoints: [Swagger UI](http://localhost:8080/swagger-ui/#)

## Key Features:

/transaction/add: Create a new purchase transaction.

/transaction/{id}: Retrieve a purchase transaction by ID.

/transactions: Retrieve all purchase transactions.

/transaction/currency/convert: Convert purchase transactions to a specific currency.

Database:

The project uses an embedded H2 database, which means you don't need to configure an external database. All data is stored locally during the application's execution.
