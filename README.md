# Banking Transactions API

A simple REST API for creating accounts, transferring funds, and viewing
transaction history. Built with Java 21 and Spring Boot.

## Requirements
- Java 21
- No other installs needed (the Maven wrapper is included)

## Build and run
```bash
git clone https://github.com/Haran030501/Banking-Transactions-API.git
cd Banking-Transactions-API
./mvnw spring-boot:run
```
On Windows, use `mvnw.cmd` in place of `./mvnw`. The API starts on
http://localhost:8080.

## Run tests
```bash
./mvnw test
```

## Try it out

### Swagger UI
With the app running, open http://localhost:8080/swagger-ui.html to see every
endpoint and send requests from the browser (expand an endpoint, click
**Try it out**, then **Execute**). The raw OpenAPI spec is at
http://localhost:8080/v3/api-docs.

### Postman collection
`postman/Banking-API.postman_collection.json` covers every endpoint, including
error cases, with a test on each response. The create requests save the new
account IDs so later requests reuse them, so there is nothing to copy by hand.

- **In Postman:** click **Import**, choose the file, then right-click the
  collection and select **Run collection**.
- **From the terminal** (needs Node.js):
  ```bash
  npx newman run postman/Banking-API.postman_collection.json
  ```

## Endpoints

| Method | Path                          | Description                     |
|--------|-------------------------------|---------------------------------|
| POST   | /accounts                     | Create an account               |
| GET    | /accounts/{id}                | Get an account and its balance  |
| GET    | /accounts/{id}/transactions   | Get an account's history        |
| POST   | /transfers                    | Transfer funds between accounts |

### Example walkthrough (Swagger UI)
With the app running, open http://localhost:8080/swagger-ui.html and follow
these steps. For each one, expand the endpoint, click **Try it out**, fill in
the fields, then click **Execute**.

1. **POST /accounts**: create Amy. Copy the `id` from the response.
   ```json
   { "accountHolder": "Amy", "initialBalance": 100 }
   ```
2. **POST /accounts**: create Bob. Copy that `id` too.
   ```json
   { "accountHolder": "Bob", "initialBalance": 50 }
   ```
3. **POST /transfers**: send 30 from Amy to Bob, pasting in the two IDs.
   ```json
   { "fromAccountID": "<Amy's id>", "toAccountID": "<Bob's id>", "amount": 30 }
   ```
4. **GET /accounts/{id}**: enter Amy's ID. Her balance is now 70.
5. **GET /accounts/{id}/transactions**: enter Amy's ID to see the transfer.

To see the error handling, repeat step 3 with an `amount` of `1000`
(insufficient funds) or with the same ID in both fields (same account).

### Errors
All errors return the same JSON shape:
`status`, `error`, `message`, `details`, `timestamp`.

| Situation                         | Status |
|-----------------------------------|--------|
| Invalid input / malformed JSON    | 400    |
| Insufficient funds                | 400    |
| Transfer to the same account      | 400    |
| Account not found                 | 404    |
| Unexpected error                  | 500    |

Validation errors list each failing field in `details`, for example
`"accountHolder: must not be blank"`.

## Assumptions
- Data is stored in memory and is lost when the app stops.
- Single currency; amounts use `BigDecimal` to avoid rounding errors.
- Account IDs are server-generated UUIDs.
- Initial balance must be zero or more; transfer amounts must be more than zero.
- Overdrafts are not allowed.
- Transfers to the same account are rejected.
- Opening an account does not create a transaction record; history
  contains transfers only (both sent and received), newest first.
- No authentication or authorization (out of scope for this exercise).

## Possible improvements
- Persist data with Spring Data JPA and a real database
- Pagination for transaction history
- Idempotency keys so a retried transfer isn't applied twice
- Authentication
