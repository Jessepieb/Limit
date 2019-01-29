import os

def lamp_off():
        print("Turning lamp off")
        os.system('python3 send.py -p 432 -t 1 5510484')

def lamp_on():
        print("Turning lamp on")
        os.system('python3 send.py -p 432 -t 1 5510487')

def send_temp():
        print("Sending temperature")
        os.system('python3 queue_send_temp.py')

def boiler_on():
        print("Turning boiler on")
        os.system('python3 send.py -p 432 -t 1 5510487')

def cooling_on():
        print("Turning cooling on")
        os.system('python3 send.py -p 432 -t 1 5526613')

def heat_up():
        print("Turning heat up")
        os.system('python3 send.py -p 431 -t 1 5526612')
        
