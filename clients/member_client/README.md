# Member Client

## Overview

The **Member Client** is designed for **issuing permits** to university members.

---

### Simulation Note

This application is intended to run on a physical device within the university premises, where it can scan transponders
to retrieve the corresponding **transponderID**. To simulate this behavior, the `generateRandomTransponderID` method is
used to generate a mock **transponderID**.

---

The application scans the **transponderID** (they way mentioned above) and then prompts the university member to input
the following data:

- **Plate number**
- **First name**
- **Last name**
- **Employee ID**
- **Lot ID**
- **Payment type** (payslip or bank)
- **Credit card number**
- **Credit card expiry date**
- **Credit card CVC**
- **Number of months purchased**

Once the data is entered, a **RabbitMQ message** is sent to the **Permit Service**. Since the application uses
asynchronous communication, the next member can immediately enter data for their permit issuance without waiting for the
response of the previous member.

Responses, when received, are logged directly to the console for members to review.

# How to use

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

Since university members need access to the console to input their data, this application is designed to run **locally**
.

