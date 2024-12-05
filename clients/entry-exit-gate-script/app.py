import pika
import json
import random
import string
import time
from threading import Thread

rabbitmq_host = 'localhost'
rabbitmq_port = 5672
rabbitmq_user = 'admin'
rabbitmq_password = 'cas735'

credentials = pika.PlainCredentials(rabbitmq_user, rabbitmq_password)
parameters = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
connection = pika.BlockingConnection(parameters)
channel = connection.channel()

exchange_name = 'pac.exchange'
channel.exchange_declare(exchange=exchange_name, exchange_type='topic', durable=True)

spots_parked = []

def main():
    while True:
        time.sleep(1)
        try:
            print("Is this an Entry or Exit request?")
            print("1. Entry")
            print("2. Exit")
            access_type = input("> ").strip()
            
            if access_type == "1":
                handle_entry_flow()
            elif access_type == "2":
                handle_exit_flow()
            else:
                print("Invalid choice. Please enter 1 for Entry or 2 for Exit.")
                continue
            
        except KeyboardInterrupt:
            print("\nExiting program...")
            break
        except Exception as e:
            print(f"Error: {e}")

def handle_entry_flow():
    print("\n--- Enter Gate Access Request ---")

    # Prompt the user for the request type
    print("\nChoose request type:")
    print("1. Visitor")
    print("2. Voucher")
    print("3. Transponder")
    request_type = input("> ").strip()

    if request_type == "1":
        request_sender = 'visitor'
    elif request_type == "2":
        request_sender = 'voucher'
    elif request_type == "3":
        request_sender = 'transponder'
    else:
        print("Invalid choice. Please enter 1, 2, or 3.")
        return

    # Prompt for lot ID
    print("Enter Lot ID (LOT01, LOT02, LOT03, LOT04, LOT05):")
    lot_id = input("> ").strip()
    if lot_id not in ['LOT01', 'LOT02', 'LOT03', 'LOT04', 'LOT05']:
        print("Invalid Lot ID. Please try again.")
        return

    # Prompt for plate number
    print("Enter License Plate Number:")
    plate_number = input("> ").strip()
    if not plate_number:
        print("Plate number cannot be empty. Please try again.")
        return

    # Publish the message
    publish_entry_message(lot_id, request_sender, plate_number)

def publish_entry_message(lot_id, request_sender, plate_number):
    credentials = pika.PlainCredentials(rabbitmq_user, rabbitmq_password)
    parameters = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    message_entry = {
        "lotID": lot_id,
        "plateNumber": plate_number
    }

    routing_key_entry = 'gate.entry.' + request_sender

    channel.basic_publish(
        exchange=exchange_name,
        routing_key=routing_key_entry,
        body=json.dumps(message_entry),
        properties=pika.BasicProperties(content_type='application/json')
    )
    
def handle_exit_flow():
    print("\n--- Exit Gate Access Request ---")

    # Prompt the user for the request type
    print("\nChoose request type:")
    print("1. Visitor")
    print("2. Voucher")
    print("3. Transponder")
    request_type = input("> ").strip()

    if request_type == "1":
        request_sender = 'visitor'
    elif request_type == "2":
        request_sender = 'voucher'
    elif request_type == "3":
        request_sender = 'transponder'
    else:
        print("Invalid choice. Please enter 1, 2, or 3.")
        return

    # Prompt for lot ID
    print("Enter Lot ID (LOT01, LOT02, LOT03, LOT04, LOT05):")
    lot_id = input("> ").strip()
    if lot_id not in ['LOT01', 'LOT02', 'LOT03', 'LOT04', 'LOT05']:
        print("Invalid Lot ID. Please try again.")
        return

    # Prompt for plate number
    print("Enter License Plate Number:")
    plate_number = input("> ").strip()
    if not plate_number:
        print("Plate number cannot be empty. Please try again.")
        return

    # Prompt for spot ID
    print("Enter Spot ID:")
    spot_id = input("> ").strip()
    if not spot_id:
        print("Spot ID cannot be empty. Please try again.")
        return
    
    if lot_id + '_' + spot_id not in spots_parked:
        print("Car not parked in the specified spot. Please try again.")
        return

    # Additional details based on request type
    if request_sender == 'visitor':
        print("Enter hours occupied:")
        hours_occupied = input("> ").strip()
        try:
            hours_occupied = int(hours_occupied)
        except ValueError:
            print("Invalid hours. Please enter a valid number.")
            return
        
        print("Enter Credit Card Number (will be rejected if it starts with 1):")
        cc_number = input("> ").strip()
        if not cc_number.isdigit():
            print("Credit Card Number must be a number.")
            return
        print("Enter Credit Card Expiry Date (MMYY):")
        cc_expiry = input("> ").strip()
        if not cc_expiry.isdigit() or len(cc_expiry) != 4:
            print("Invalid expiry date. Please enter a 4-digit number.")
            return
        print("Enter Credit Card CVC:")
        cc_cvc = input("> ").strip()
        if not cc_cvc.isdigit() or len(cc_cvc) != 3:
            print("Invalid CVC. Please enter a 3-digit number.")
            return

        publish_exit_message(
            lot_id=lot_id,
            request_sender=request_sender,
            plate_number=plate_number,
            spot_id=spot_id,
            hours_occupied=hours_occupied,
            cc_number=cc_number,
            cc_expiry=cc_expiry,
            cc_cvc=cc_cvc
        )

    elif request_sender == 'voucher':
        print("Enter Credit Card Number (will be rejected if it starts with 1):")
        cc_number = input("> ").strip()
        if not cc_number.isdigit():
            print("Credit Card Number must be a number.")
            return
        print("Enter Credit Card Expiry Date (MMYY):")
        cc_expiry = input("> ").strip()
        if not cc_expiry.isdigit() or len(cc_expiry) != 4:
            print("Invalid expiry date. Please enter a 4-digit number.")
            return
        print("Enter Credit Card CVC:")
        cc_cvc = input("> ").strip()
        if not cc_cvc.isdigit() or len(cc_cvc) != 3:
            print("Invalid CVC. Please enter a 3-digit number.")
            return

        publish_exit_message(
            lot_id=lot_id,
            request_sender=request_sender,
            plate_number=plate_number,
            spot_id=spot_id,
            cc_number=cc_number,
            cc_expiry=cc_expiry,
            cc_cvc=cc_cvc
        )

    elif request_sender == 'transponder':
        publish_exit_message(
            lot_id=lot_id,
            request_sender=request_sender,
            plate_number=plate_number,
            spot_id=spot_id
        )

def publish_exit_message(lot_id, request_sender, plate_number, spot_id, hours_occupied=None, cc_number=None, cc_expiry=None, cc_cvc=None):
    credentials = pika.PlainCredentials(rabbitmq_user, rabbitmq_password)
    parameters = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    # Construct the exit message
    message_exit = {
        "lotID": lot_id,
        "plateNumber": plate_number,
        "spotID": spot_id,
    }

    if request_sender == "visitor":
        message_exit["hoursOccupied"] = hours_occupied
        message_exit["ccNumber"] = cc_number
        message_exit["ccExpiry"] = cc_expiry
        message_exit["ccCVC"] = cc_cvc
        message_exit["paymentType"] = ""
    elif request_sender == "voucher":
        message_exit["ccNumber"] = cc_number
        message_exit["ccExpiry"] = cc_expiry
        message_exit["ccCVC"] = cc_cvc

    routing_key_exit = 'gate.exit.' + request_sender

    channel.basic_publish(
        exchange=exchange_name,
        routing_key=routing_key_exit,
        body=json.dumps(message_exit),
        properties=pika.BasicProperties(content_type='application/json')
    )

def generate_random_id():
    """Generate a random alphanumeric ID."""
    return ''.join(random.choices(string.ascii_letters + string.digits, k=5))

def handle_entry_action(ch, method, properties, body):
    """Callback function to process messages from the queue."""
    try:
        message = json.loads(body)
        should_open = message.get("shouldOpen")
        lot_id = message.get("lotID")
        spot_id = message.get("spotID")
        is_visitor = message.get("isVisitor")

        if should_open:
            print(f"Entry Gate opened for lot {lot_id} assigned {spot_id}.")
            spots_parked.append(lot_id + '_' + spot_id)
            if is_visitor:
                visitor_id = generate_random_id()
                print(f"Generated visitor ID: {visitor_id} (QR Code)")
        else:
            print(f"Entry Gate remains closed for {lot_id}.")
    except Exception as e:
        print(f"Error processing message: {e}")
        
def handle_exit_action(ch, method, properties, body):
    """Callback function to process messages from the queue."""
    try:
        message = json.loads(body)
        should_open = message.get("shouldOpen")
        lot_id = message.get("lotID")
        spot_id = message.get("spotID")

        if should_open:
            print(f"Exit Gate opened for lot {lot_id}, car exiting.")
            spots_parked.remove(lot_id + '_' + spot_id)
        else:
            print(f"Exit Gate remains closed for {lot_id}.")
    except Exception as e:
        print(f"Error processing message: {e}")

def listen_to_entry_routing_keys(routing_key):
    credentials = pika.PlainCredentials(rabbitmq_user, rabbitmq_password)
    parameters = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    result = channel.queue_declare(queue=routing_key + '.queue', durable=True) 
    queue_name = result.method.queue
    channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

    print(f"{queue_name} listening to routing key - {routing_key}")
    
    channel.basic_consume(queue=queue_name, on_message_callback=handle_entry_action, auto_ack=True)
    channel.start_consuming()
    
def listen_to_exit_routing_keys(routing_key):
    credentials = pika.PlainCredentials(rabbitmq_user, rabbitmq_password)
    parameters = pika.ConnectionParameters(host=rabbitmq_host, port=rabbitmq_port, credentials=credentials)
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    result = channel.queue_declare(queue=routing_key + '.queue', durable=True) 
    queue_name = result.method.queue
    channel.queue_bind(exchange=exchange_name, queue=queue_name, routing_key=routing_key)

    print(f"{queue_name} listening to routing key - {routing_key}")
    
    channel.basic_consume(queue=queue_name, on_message_callback=handle_exit_action, auto_ack=True)
    channel.start_consuming()

if __name__ == "__main__":
    routing_keys_entry = [
        "LOT01.entry.action",
        "LOT02.entry.action",
        "LOT03.entry.action",
        "LOT04.entry.action",
        "LOT05.entry.action"
    ]
    routing_keys_exit = [
        "LOT01.exit.action",
        "LOT02.exit.action",
        "LOT03.exit.action",
        "LOT04.exit.action",
        "LOT05.exit.action"
    ]

    for routing_key in routing_keys_entry:
        thread = Thread(target=listen_to_entry_routing_keys, args=(routing_key,), daemon=True)
        thread.start()
    
    # Create listeners for exit routing keys
    for routing_key in routing_keys_exit:
        thread = Thread(target=listen_to_exit_routing_keys, args=(routing_key,), daemon=True)
        thread.start()

    main()