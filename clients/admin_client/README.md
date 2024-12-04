# Admin Client

## Overview

The **Admin Client** is designed for **issuing vouchers**.

The application prompts the parking administrator to enter the following data:
- Plate number
- LotID they want that voucher to be assigned to
- days the voucher would be valid

Once the data is entered, a **RabbitMQ message** is sent to the **Voucher Service**. Since the application uses asynchronous communication, the administrator can proceed to enter data for the next voucher without waiting for a response.

Also, Responses, when received, are logged directly to the console.

## How to use

Use the following commands to compile and run the application:

### Compile and Build

```bash
mvn clean package
```

### Run the Application

```bash
mvn spring-boot:run
```

### Note

Since the administrator needs access to the console to input data, the application is intended to run **locally**.