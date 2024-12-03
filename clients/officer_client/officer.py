import requests
import argparse

def main():
    # Set up command-line argument parsing
    parser = argparse.ArgumentParser(description="Issue a fine by sending a POST request.")
    parser.add_argument("--plateNumber", type=str, required=True, help="The plate number of the vehicle")
    parser.add_argument("--amount", type=float, required=True, help="The fine amount")

    args = parser.parse_args()

    # Construct the data payload
    fines_data = {
        "plateNumber": args.plateNumber,
        "amount": args.amount
    }

    # Send the POST request
    url = "http://localhost:9086/api/fines/"
    try:
        response = requests.post(url, json=fines_data)
        if response.status_code == 201:
            print("Fine issued successfully.")
        else:
            print(f"Failed to issue fine. Status code: {response.status_code}, Response: {response.text}")
    except requests.RequestException as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    main()
