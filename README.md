
# 🏦 Bank Management System

A RESTful API for managing banking operations including account management, ATM transactions (deposit & withdraw), internal and external transfers, and loan applications.  
Built with **Spring Boot**, **Spring Security**, **JWT Authentication**, and **PostgreSQL**.

---

## 🚀 Features

- **Authentication**: JWT-based login and registration with role-based access.
- **User Management**: Secure authentication and token handling.
- **Account Management**:
  - Create bank accounts
  - View personal accounts
  - Request account closure (admin approval required)
- **ATM Transactions**:
  - Deposit funds
  - Withdraw funds
- **Money Transfer**:
  - Internal (between own accounts)
  - External (to another user's account)
- **Loan System**:
  - Apply for a loan
  - Admin can approve or reject
- **Security**: All endpoints secured with JWT
- **Lombok**: Cleaner code with less boilerplate

---

## 🛠️ Technologies Used

- Java 17  
- Spring Boot 3  
- Spring Security (JWT)  
- PostgreSQL  
- Hibernate & JPA  
- Liquibase  
- Lombok  

---

## ⚙️ Installation & Setup

### ✅ Prerequisites

- Java 17+
- PostgreSQL installed and running
- Maven installed

### 🔧 Configuration

1. Clone the repository:

   ```bash
   git clone https://github.com/Farid-Fizard/CoreBankingApp.git
   cd CoreBankingApp


2. Configure `application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/bank_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=validate
   spring.jpa.show-sql=true

   liquibase.change-log=classpath:/db/changelog/db.changelog-master.xml
   jwt.secret=your_long_jwt_secret_key
   jwt.expiration=3600000
   ```

3. Build and run the application:

   ```bash
   mvn spring-boot:run
   ```

---

## 📡 API Endpoints

### 🔐 Authentication

* `POST /api/auth/register` – Register a new user and receive a JWT token
* `POST /api/auth/login` – Authenticate and receive a JWT token
* `POST /api/auth/refresh` – Refresh access token

### 🧾 Bank Account

* `POST /api/accounts` – Create a new bank account
* `GET /api/accounts` – Get all accounts of the logged-in user
* `POST /api/accounts/{id}/request-closure` – Request account closure
* `POST /api/accounts/{id}/approve-closure` – Admin approves account closure

### 🏧 ATM

* `POST /api/atm/deposit` – Deposit funds into an account
* `POST /api/atm/withdraw` – Withdraw funds from an account

### 💸 Transactions

* `POST /api/transactions/internal` – Transfer between user’s own accounts
* `POST /api/transactions/external` – Transfer to another user’s account

### 💳 Loans

* `POST /api/loans/apply` – Apply for a loan
* `GET /api/loans/myloans` – Get all loans of logged-in user
* `GET /api/loans/{loanId}` – Get loan details by ID
* `PUT /api/loans/{loanId}/approve` – Admin approves a loan
* `PUT /api/loans/{loanId}/reject` – Admin rejects a loan

---

## 🔒 Authentication & Security

All protected endpoints require JWT authentication.
Include the following in request headers:

```
Authorization: Bearer <your_jwt_token>
```

---

## 🤝 Contributing

Feel free to fork this repository and submit a pull request.
All contributions are welcome!

---

## 📄 License

This project is licensed under the **MIT License**.

```

