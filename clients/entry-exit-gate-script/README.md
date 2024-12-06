# Entry and Exit Script

Contains the script required to simulate entry/exit activities.

It creates 5 entry gates and 5 exit gates, and then allows any of LOT01-LOT05 selected.

The entry gate will scan the transponderId/voucherId need to input and check the selected lot to see if the car can park. If it can it will return the spotId. To use the transponderId or the voucherId you must use the service to generate the permit and voucher beforehand. Visitor can enter without any request to other services, it will check if the lot is full beforehand though.

The exit gate will scan the transponderId/voucherId/qrCode. The transponder opens immediately, the voucher needs to input a credit card just to pay any fines, and the qrCode needs to input the credit card and simulate hours spent.

To run the client:
1. Install `pipenv` and required dependencies
    ```bash
    pipenv install
    ```
2. Connect to the shell
    ```bash
    pipenv shell
    ```
3. Run the script inside the shell
    ```bash
    python3 app.py
    ```
   
There is existing mock data in the database already:
- Permit
```
+----------------+--------------+------------+-----------+-------------+--------+---------+---------+---------------------+--------------+--------------------+-----------+-----+------------------+---------------------+
| TRANSPONDER_ID | PLATE_NUMBER | FIRST_NAME | LAST_NAME | EMPLOYEE_ID | LOT_ID | SPOT_ID | STATUS  | MEMBER_PAYMENT_TYPE | MEMBER_ROLE  | CC_NUMBER          | CC_EXPIRY | CVC | MONTHS_PURCHASED | EXPIRATION_TIME     |
+----------------+--------------+------------+-----------+-------------+--------+---------+---------+---------------------+--------------+--------------------+-----------+-----+------------------+---------------------+
| s1234567890    | DEF456       | Talha      | Asif      | E12345      | LOT01  | SPOT103 | issued  | bank                | student      | 4111111111111111   | 12/25     | 123 | 12               | 2024-12-31 23:59:59 |
| m9876543210    | GHI789       | Jane       | Smith     | S67890      | LOT02  | SPOT201 | issued  | payslip             | staff        | 4222222222222222   | 06/26     | 456 | 24               | 2025-06-30 23:59:59 |
| m1230984567    | VWX234       | Robert     | Brown     | S56789      | LOT03  | SPOT305 | issued  | bank                | staff        | 4333333333333333   | 11/27     | 789 | 18               | 2024-11-30 23:59:59 |
| s2345678901    | BCD890       | Maryam     | Emami     | E34567      | LOT04  | SPOT404 | issued  | bank                | student      | 4444444444444444   | 03/28     | 012 | 12               | 2024-03-31 23:59:59 |
| s3456789012    | NOP012       | Arman      | Samiei    | E45678      | LOT05  | SPOT505 | issued  | bank                | student      | 5555555555555555   | 09/27     | 345 | 12               | 2024-09-30 23:59:59 |
| f1234567890    | KLM789       | Sebastien  | Mosser    | F56789      | LOT05  | SPOT503 | issued  | payslip             | faculty      | 1666666666666666   | 10/27     | 678 | 24               | 2025-10-31 23:59:59 |
+----------------+--------------+------------+-----------+-------------+--------+---------+---------+---------------------+--------------+--------------------+-----------+-----+------------------+---------------------+
```

- Voucher
```
+--------------+--------+---------+---------+---------------------+
| PLATE_NUMBER | LOT_ID | SPOT_ID | STATUS  | EXPIRATION_TIME     |
+--------------+--------+---------+---------+---------------------+
| ABC123       | LOT01  | SPOT101 | issued  | 2024-12-31 23:59:59 |
| JKL012       | LOT02  | SPOT203 | issued  | 2024-12-31 23:59:59 |
| PQR678       | LOT03  | SPOT301 | issued  | 2024-12-31 23:59:59 |
| EFG123       | LOT04  | SPOT405 | issued  | 2024-12-31 23:59:59 |
| HIJ456       | LOT05  | SPOT502 | issued  | 2024-12-31 23:59:59 |
+--------------+--------+---------+---------+---------------------+
```

- Fines - means if the following licence plates are used they will be charged extra
```
+----------+--------------+--------+
| FINES_ID | PLATE_NUMBER | AMOUNT |
+----------+--------------+--------+
| 1        | DEF456       | 50.00  |  -- Fine for student
| 2        | MNO345       | 75.00  |  -- Fine for faculty
| 3        | VWX234       | 30.00  |  -- Fine for staff
| 4        | ABC123       | 100.00 |  -- Fine for visitor with voucher
| 5        | JKL012       | 80.00  |  -- Fine for visitor with voucher
| 6        | YZA567       | 60.00  |  -- Fine for visitor without voucher
| 7        | HIJ456       | 90.00  |  -- Fine for visitor without voucher
+----------+--------------+--------+
```

- Lot
```
+---------+--------+---------------+--------------+-------------------+-------------------------+-------------+
| SPOT_ID | LOT_ID | CUSTOMER_TYPE | PLATE_NUMBER | IS_SPOT_OCCUPIED  | SPOT_RESERVATION_STATUS | HAS_VOUCHER |
+---------+--------+---------------+--------------+-------------------+-------------------------+-------------+
| SPOT101 | LOT01  | visitor       | ABC123       | true              | reserved                | true        | -- Visitor with voucher
| SPOT102 | LOT01  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT103 | LOT01  | student       | DEF456       | true              | reserved                | false       | -- Reserved for student
| SPOT104 | LOT01  | faculty       | NULL         | false             | notReserved             | false       | -- Free spot for faculty
| SPOT105 | LOT01  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT201 | LOT02  | staff         | GHI789       | false             | reserved                | false       | -- Reserved for staff
| SPOT202 | LOT02  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT203 | LOT02  | visitor       | JKL012       | false             | reserved                | true        | -- Reserved for visitor with voucher
| SPOT204 | LOT02  | faculty       | MNO345       | true              | reserved                | false       | -- Reserved for faculty
| SPOT205 | LOT02  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT301 | LOT03  | visitor       | PQR678       | true              | reserved                | true        | -- Visitor with voucher
| SPOT302 | LOT03  | student       | NULL         | false             | notReserved             | false       | -- Free spot for student
| SPOT303 | LOT03  | faculty       | STU901       | true              | reserved                | false       | -- Reserved for faculty
| SPOT304 | LOT03  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT305 | LOT03  | staff         | VWX234       | true              | reserved                | false       | -- Reserved for staff
| SPOT401 | LOT04  | visitor       | YZA567       | true              | reserved                | false       | -- Visitor without voucher
| SPOT402 | LOT04  | faculty       | NULL         | false             | notReserved             | false       | -- Free spot for faculty
| SPOT403 | LOT04  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT404 | LOT04  | student       | BCD890       | false             | reserved                | false       | -- Reserved for student
| SPOT405 | LOT04  | visitor       | EFG123       | false             | reserved                | true        | -- Visitor with voucher
| SPOT501 | LOT05  | staff         | NULL         | false             | notReserved             | false       | -- Free spot for staff
| SPOT502 | LOT05  | visitor       | HIJ456       | true              | reserved                | true        | -- Visitor with voucher
| SPOT503 | LOT05  | faculty       | KLM789       | false             | reserved                | false       | -- Reserved for faculty
| SPOT504 | LOT05  | visitor       | NULL         | false             | notReserved             | false       | -- Free spot for voucher/non-voucher visitors
| SPOT505 | LOT05  | student       | NOP012       | false             | reserved                | false       | -- Reserved for student
+---------+--------+---------------+--------------+-------------------+-------------------------+-------------+
```