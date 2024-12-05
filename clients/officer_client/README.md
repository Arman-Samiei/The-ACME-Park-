# Member Client

## Overview

The **Officer Client** is designed for **issuing fines** to parking lot customers.

---

The python script gets these command arguments:

- **Plate number**
- **Amount**

Once the data is entered, a **POST REQUEST** will be sent to the fines service on `/api/fines` endpoint.

Then it prints the response of the fine service.

# How to use

Use the following commands to run the script and provide arguments

```bash
pipenv install
```

```bash
pipenv shell
```

```bash
python ./officer.py --plateNumber [plateNumber] --amount [amount] 
```

### Note

Since the officer needs access to the console to input their data, this application is designed to run **locally**.

