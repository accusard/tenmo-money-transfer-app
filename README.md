## **TEnmo**

### Overview
TEnmo is a peer-to-peer money transfer application that allows users to send, request, and approve transfers. The application features a command-line front end communicating with a RESTful API back end.

### Features
- User authentication and authorization with Spring Security.
- RESTful API for seamless client-server communication.
- PostgreSQL integration for efficient data storage.

### Technologies
- Java
- Spring Boot
- PostgreSQL
- RESTful API

### Setup
1. Clone the repository.
2. Configure the database connection in `application.properties`.
3. Start the Spring Boot server.
4. Run the CLI application to interact with the money transfer system.

# Capstone Project - TEnmo

Collaborated with a team to develop a robust service-side API, ensuring efficient data transfer as JSON objects from an SQL database to the client side, while adhering to MVC principles for enhanced maintainability and scalability.

## Use cases

The following use cases were implemented by our team:

1. **User Registration**
   - Users can register with a username and password.
   - Newly registered users start with an initial balance of 1,000 TE Bucks.
2. **User Login**
   - Users can log in with their registered credentials.
   - Logging in returns an authentication token required for subsequent interactions.
3. **View Account Balance**
   - Authenticated users can view their account balance.
4. **Send Transfers**
   - Authenticated users can send TE Bucks to other registered users.
     - Users select recipients from a list.
     - Users cannot send money to themselves.
     - Transfer reduces sender's balance and increases receiver's balance.
     - Restrictions include no negative or zero amounts, and insufficient funds.
5. **View Transfers**
   - Users can view transfers they have sent or received.
6. **Retrieve Transfer Details**
   - Users can retrieve details of any transfer using its ID.
7. **Request Transfers**
   - Authenticated users can request TE Bucks from others.
     - Users select requestees from a list.
     - Users cannot request money from themselves or request zero/negative amounts.
     - Requests remain pending until approved or rejected.
8. **View Pending Transfers**
   - Users can view their pending transfer requests.
9. **Approve/Reject Transfer Requests**
   - Users can approve or reject pending requests.
     - Approval updates balances; rejection does not.
     - Approval is restricted by available balance.

## Sample screens

### Use case 3: Current balance
```
Your current account balance is: $9999.99
```

### Use case 4: Send TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
23          Vanny
24          Robert
---------

Enter ID of user you are sending to (0 to cancel):
Enter amount:
```

### Use case 5: View transfers
```
-------------------------------------------
Transfers
ID          From/To                 Amount
-------------------------------------------
23          From:  Vanny            $ 903.14
24          To:    Robert           $  12.55
---------
Please enter transfer ID to view details (0 to cancel): "
```

### Use case 6: Transfer details
```
--------------------------------------------
Transfer Details
--------------------------------------------
 Id: 23
 From: Vanny
 To: Me Myselfandi
 Type: Send
 Status: Approved
 Amount: $903.14
```

### Use case 7: Requesting TE Bucks
```
-------------------------------------------
Users
ID          Name
-------------------------------------------
23          Vanny
24          Robert
---------

Enter ID of user you are requesting from (0 to cancel):
Enter amount:
```

### Use case 8: Pending requests
```
-------------------------------------------
Pending Transfers
ID          To                     Amount
-------------------------------------------
88          Vanny                  $ 142.56
147         Robert                 $  10.17
---------
Please enter transfer ID to approve/reject (0 to cancel): "
```

### Use case 9: Approve or reject pending transfer
```
1: Approve
2: Reject
0: Don't approve or reject
---------
Please choose an option:
```

## Database schema

![Database schema](./img/Tenmo_erd.png)

### `tenmo_user` table

Stores the login information for users of the system.

| Field           | Description                                                                    |
|-----------------|--------------------------------------------------------------------------------|
| `user_id`       | Unique identifier of the user                                                  |
| `username`      | String that identifies the name of the user; used as part of the login process |
| `password_hash` | Hashed version of the user's password                                          |
| `role`          | Name of the user's role                                                        |

### `account` table

Stores the accounts of users in the system.

| Field           | Description                                                        |
| --------------- | ------------------------------------------------------------------ |
| `account_id`    | Unique identifier of the account                                   |
| `user_id`       | Foreign key to the `users` table; identifies user who owns account |
| `balance`       | The amount of TE bucks currently in the account                    |

### `transfer_type` table

Stores the types of transfers that are possible.

| Field                | Description                             |
| -------------------- | --------------------------------------- |
| `transfer_type_id`   | Unique identifier of the transfer type  |
| `transfer_type_desc` | String description of the transfer type |

There are two types of transfers:

| `transfer_type_id` | `transfer_type_desc` | Purpose                                                                |
| ------------------ | -------------------- | ---------------------------------------------------------------------- |
| 1                  | Request              | Identifies transfer where a user requests money from another user      |
| 2                  | Send                 | Identifies transfer where a user sends money to another user           |

### `transfer_status` table

Stores the statuses of transfers that are possible.

| Field                  | Description                               |
| ---------------------- | ----------------------------------------- |
| `transfer_status_id`   | Unique identifier of the transfer status  |
| `transfer_status_desc` | String description of the transfer status |

There are three statuses of transfers:

| `transfer_status_id` | `transfer_status_desc` |Purpose                                                                                 |
| -------------------- | -------------------- | ---------------------------------------------------------------------------------------  |
| 1                    | Pending                | Identifies transfer that hasn't occurred yet and requires approval from the other user |
| 2                    | Approved               | Identifies transfer that has been approved and occurred                                |
| 3                    | Rejected               | Identifies transfer that wasn't approved                                               |

### `transfer` table

Stores the transfers of TE bucks.

| Field                | Description                                                                                     |
| -------------------- | ----------------------------------------------------------------------------------------------- |
| `transfer_id`        | Unique identifier of the transfer                                                               |
| `transfer_type_id`   | Foreign key to the `transfer_types` table; identifies type of transfer                          |
| `transfer_status_id` | Foreign key to the `transfer_statuses` table; identifies status of transfer                     |
| `account_from`       | Foreign key to the `accounts` table; identifies the account that the funds are being taken from |
| `account_to`         | Foreign key to the `accounts` table; identifies the account that the funds are going to         |
| `amount`             | Amount of the transfer                                                                          |

> Note: there are two check constraints in the DDL that creates the `transfer` table. Be sure to take a look at `tenmo.sql` to understand these constraints.

## How to set up the database

Create a new Postgres database called `tenmo`. Run the `database/tenmo.sql` script in pgAdmin to set up the database.

### Datasource

A Datasource has been configured for you in `/src/resources/application.properties`. 

```
# datasource connection properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tenmo
spring.datasource.name=tenmo
spring.datasource.username=postgres
spring.datasource.password=postgres1
```

### JdbcTemplate

If you look in `/src/main/java/com/techelevator/dao`, you'll see `JdbcUserDao`. This is an example of how to get an instance of `JdbcTemplate` in your DAOs. If you declare a field of type `JdbcTemplate` and add it as an argument to the constructor, Spring automatically injects an instance for you:

```java
@Service
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

## Testing


### DAO integration tests

`com.techelevator.dao.BaseDaoTests` has been provided for you to use as a base class for any DAO integration test. It initializes a Datasource for testing and manages rollback of database changes between tests.

`com.techelevator.dao.JdbUserDaoTests` has been provided for you as an example for writing your own DAO integration tests.

Remember that when testing, you're using a copy of the real database. The schema and data for the test database are defined in `/src/test/resources/test-data.sql`. The schema in this file matches the schema defined in `database/tenmo.sql`.


## Authentication

The user registration and authentication functionality for the system has already been implemented. If you review the login code, you'll notice that after successful authentication, an instance of `AuthenticatedUser` is stored in the `currentUser` member variable of `App`. The user's authorization token—meaning JWT—can be accessed from `App` as `currentUser.getToken()`.

When the use cases refer to an "authenticated user", this means a request that includes the token as a header. You can also reference other information about the current user by using the `User` object retrieved from `currentUser.getUser()`.
