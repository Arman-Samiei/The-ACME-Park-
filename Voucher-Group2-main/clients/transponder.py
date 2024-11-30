#! python

import sys                      # handling interruptions
import argparse                 # for parsing command line args
import json                     # for message formatting
import paho.mqtt.client as mqtt # for MQTT communication
import socket



MQTT_TOPIC_NAME  = 'transponder'
MQTT_BROKER_PORT = 1883


def send_data(identifier, client):
    """
        Start the data sending loop
    """
    print('**********')
    print('* Transponder device id:   ' + identifier)
    print('* Target receiver: ' + str(client))
    print('**********')
    data = {"id": identifier}
    client.publish(MQTT_TOPIC_NAME, json.dumps(data), qos = 1)




#####
# MQTT Communication Layer
#####

def mqtt_init(identifier, receiver, port):
    client = mqtt.Client(mqtt.CallbackAPIVersion.VERSION2, identifier)
    client.on_publish = on_publish
    client.connect(receiver, port, 60)
    return client

def on_connect(client, userdata, flags, reason_code, properties):
    if reason_code.is_failure:
        print(f"Failed to connect: {reason_code}")
    else:
        print(f"Connection established.")

def on_publish(client, userdata, mid, reason_code, properties):
    try:
        pass
    except KeyError:
        print("An error has occured " + reason_code)

#####
## Process command line and run the business logic
#####

def process_cli():
    """
        Process command line arguments provided by the user
    """
    parser = argparse.ArgumentParser()
    parser.add_argument("SID", help="Serial (unique) number of the transponder")
    parser.add_argument("receiver", help="Address of the receiver queue")
    args = parser.parse_args()
    return (args.SID, args.receiver)


if __name__ == "__main__":
    try:
        (sid, receiver) = process_cli()
        client = mqtt_init(sid, receiver, MQTT_BROKER_PORT)
        client.loop_start()
        send_data(sid, client)
        client.loop_stop()
        sys.exit(0)
    except socket.gaierror:
        print('Socket error (wrong receiver address?)')
    except ConnectionRefusedError:
        print('Connection refused (is broker alive?)')
