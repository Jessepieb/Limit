import boto3
import os
import time
import rf_messages as rf

access_key = ""
access_secret = ""
region =""
queue_url = ""

def pop_message(client, url):
    response = client.receive_message(QueueUrl = url, MaxNumberOfMessages = 10)

    #last message posted becomes messages
    message = response['Messages'][0]['Body']
    receipt = response['Messages'][0]['ReceiptHandle']
    client.delete_message(QueueUrl = url, ReceiptHandle = receipt)
    return message
    
client = boto3.client('sqs', aws_access_key_id = access_key, aws_secret_access_key = access_secret, region_name = region)

waittime = 3
client.set_queue_attributes(QueueUrl = queue_url, Attributes = {'ReceiveMessageWaitTimeSeconds': str(waittime)})

time_start = time.time()
while (time.time() - time_start < 3600):
        print("Checking...")
        try:
                message = pop_message(client, queue_url)
                print(message)
                if message == "\"lamp_on\"":
                        rf.lamp_on()
                elif message == "\"lamp_off\"":
                        rf.lamp_off()
                elif message == "\"request_temp\"":
                        rf.send_temp()
                elif message == "\"cooling_on\"":
                        rf.lamp_on()
				elif message == "\"cooling_off\"":
                        rf.lamp_on()
                elif message == "\"boiler_on\"":
                        rf.lamp_on()
				elif message == "\"boiler_off\"":
                        rf.lamp_on()
                elif message == "\"heating_on\"":
                        rf.lamp_on()
				elif message == "\"heating_off\"":
					rf.lamp_on()
        except:
                pass
