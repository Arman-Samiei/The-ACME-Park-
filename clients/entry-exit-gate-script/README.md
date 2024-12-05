# Entry and Exit Script

Contains the script required to simulate entry/exit activities.

It creates 5 entry gates and 5 exit gates, and then allows any of LOT01-LOT05 selected.

The entry gate will scan the transponderId/voucherId (need to input) and check the selected lot to see if the car can park. If it can it will return the spotId. To use the transponderId or the voucherId you must use the service to generate the permit and voucher beforehand. Visitor can enter without any request to other services, it will check if the lot is full beforehand though.

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