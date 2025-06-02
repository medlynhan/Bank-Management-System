# Bank Management System (Solutions Package)

This is a simple Java-based Bank Management System using JDBC and SQL Server, with improved modularization and code quality in the `solutions` package.

## Features

- User registration and login
- Open new bank accounts
- Debit, credit, and transfer money
- Check account balances
- View account with max/min balance
- Modular code with clear separation of concerns

## Folder Structure

```
bankmanagement/
├── bin/
├── libs/
│   ├── mssql-jdbc-12.10.0.jre11.jar
│   └── mssql-jdbc_auth-12.10.0.x64.dll
├── src/
│   └── solutions/
│       ├── AccountManager.java
│       ├── AccountRepository.java
│       ├── Accounts.java
│       ├── BankingApp.java
│       ├── LoginService.java
│       └── User.java
```

## Prerequisites

- **Java JDK 11 or higher**  
  [Download Java](https://adoptium.net/)
- **Microsoft SQL Server**  
  [Download SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-downloads)
- **MSSQL JDBC Driver**  
  Already included in `libs/` (`mssql-jdbc-12.10.0.jre11.jar` and `mssql-jdbc_auth-12.10.0.x64.dll`)
- **Database Setup**  
  - Create a database named `BankManagementSystem`
  - Create required tables: `users`, `Accounts`
  - Example table schemas:
    ```sql
    CREATE TABLE users (
        id INT PRIMARY KEY IDENTITY,
        full_name NVARCHAR(100),
        email NVARCHAR(100) UNIQUE,
        password NVARCHAR(100)
    );

    CREATE TABLE Accounts (
        account_number BIGINT PRIMARY KEY,
        full_name NVARCHAR(100),
        email NVARCHAR(100),
        balance FLOAT,
        security_pin NVARCHAR(10)
    );
    ```

## Configuration

1. **Update JDBC URL in `BankingApp.java`**
   - Edit the following line to match your SQL Server instance name:
     ```java
     private static final String connectUrl = "jdbc:sqlserver://<YOUR_SERVER_NAME>\\SQLEXPRESS;DatabaseName=BankManagementSystem;IntegratedSecurity=true;encrypt=true;trustServerCertificate=true";
     ```
   - Replace `<YOUR_SERVER_NAME>` with your computer/SQL Server instance name.

2. **Add JDBC Driver to Classpath**
   - Ensure `libs/mssql-jdbc-12.10.0.jre11.jar` is in your classpath when compiling and running.

3. **Windows Authentication**
   - The provided connection string uses Windows Authentication. Make sure your SQL Server supports it and the user running the app has access.

## How to Build and Run

### 1. Compile

Open a terminal in the `bankmanagement` directory and run:

```sh
javac -cp "libs/mssql-jdbc-12.10.0.jre11.jar" -d bin src/solutions/*.java
```

### 2. Run

```sh
java -cp "bin;libs/mssql-jdbc-12.10.0.jre11.jar;libs/mssql-jdbc_auth-12.10.0.x64.dll" solutions.BankingApp
```

> **Note:**  
> On Unix/Mac, replace `;` with `:` in the classpath.

### 3. Using the App

- Follow the on-screen menu to register, login, open accounts, and perform transactions.
- All user and account data is stored in your SQL Server database.

## Troubleshooting

- **SQL Server Connection Issues:**  
  - Ensure SQL Server is running and accessible.
  - Ensure SQL Server browser is running and accessible.
  - Check your instance name (make sure it is SQLEXPRESS) and database name (BankManagementSystem) in the connection URL.
  - Make sure the JDBC driver is in your classpath.
  - Make sure TCP/IP is enabled.

- **Authentication Issues:**  
  - If you want to use SQL Server authentication, modify the connection string and provide username/password.

- **JDBC Driver Issues:**  
  - Use the correct version of the driver for your Java version.

## Code Quality

- The `solutions` package uses:
  - `AccountManager` for business logic
  - `AccountRepository` for database access
  - `LoginService` for login/session logic
  - `Accounts` and `User` for account/user management

## License

This project is for educational purposes.

---
