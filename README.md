# 🏦 Bank Management System

A RESTful API for managing banking operations including account management, ATM transactions (deposit & withdraw), internal and external transfers, and loan applications. Built with **Spring Boot**, **Spring Security**, **JWT Authentication**, and **PostgreSQL**.

## Features
- **Authentication**: User login and registration with JWT-based authentication and role-based access control.
- **User Management**: Secure login system and token-based session management.
- **Account Management**:
  - Create bank accounts
  - View personal accounts
  - Request account closure (requires admin approval)
- **ATM Transactions**:
  - Deposit and withdraw funds
- **Money Transfer**:
  - Internal transfer between user’s own accounts
  - External transfer to other users’ accounts
- **Loan System**:
  - Apply for a loan
  - Admin can approve or reject loans
- **Security**: Endpoints protected with JWT authentication
- **Lombok**: To reduce boilerplate code

## Technologies Used
- Java 17
- Spring Boot 3
- Spring Security (JWT Authentication)
- PostgreSQL
- Hibernate & JPA
- Lombok
- Liquibase (for DB migration)

## Installation & Setup

### Prerequisites
- Java 17+
- PostgreSQL installed and running
- Maven installed

### Configuration
1. Clone the repository:

   ```sh
   git clone https://github.com/your-repo/bank-management-system.git
   cd bank-management-system
2. Configure application.properties:

   ```sh
   spring.datasource.url=jdbc:postgresql://localhost:5432/bank_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=validate
   spring.jpa.show-sql=true

   liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
   jwt.secret=your_long_jwt_secret_key
   jwt.expiration=3600000
   
3.Build and run the application:
 sh
   mvn spring-boot:run


##API Endpoints

###Authentication
POST /api/auth/register – Register a new user and receive a JWT token

POST /api/auth/login – Authenticate and receive a JWT token

POST /api/auth/refresh – Refresh access token

###Bank Account
POST /api/accounts – Create a new bank account

GET /api/accounts – Get all accounts of the logged-in user

POST /api/accounts/{id}/request-closure – Request account closure

POST /api/accounts/{id}/approve-closure – Admin approves account closure

###ATM
POST /api/atm/deposit – Deposit funds into an account

POST /api/atm/withdraw – Withdraw funds from an account

###Transactions
POST /api/transactions/internal – Transfer money between user’s own accounts

POST /api/transactions/external – Transfer money to another user’s account

###Loans
POST /api/loans/apply – Apply for a loan

GET /api/loans/myloans – Get all loans of the logged-in user

GET /api/loans/{loanId} – Get loan details by ID

PUT /api/loans/{loanId}/approve – Admin approves a loan

PUT /api/loans/{loanId}/reject – Admin rejects a loan

##Authentication & Security
All protected endpoints require JWT authentication.
- Include Authorization: Bearer <token> in headers for authenticated requests.

###Contributing
Feel free to fork this repository and open a pull request. Contributions are very welcome!
