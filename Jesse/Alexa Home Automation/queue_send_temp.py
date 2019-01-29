import boto3
import temperature as temp

access_key = ""
access_secret = ""
region =""
queue_url = ""

def post_message(client, message_body, url):
    response = client.send_message(QueueUrl = url, MessageBody= message_body)
    
client = boto3.client('sqs', aws_access_key_id = access_key, aws_secret_access_key = access_secret, region_name = region)

temperature = temp.read_temp()
message = "temp_" + str(temperature)

print(message)
post_message(client, message, queue_url)
