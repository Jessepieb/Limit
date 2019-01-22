import socket
import subprocess
import threading

from pydub import AudioSegment
import sys
import os
import os.path


def proxy_handler(client_socket):
    try:
        buffer = client_socket.recv(1024)
            
        data = buffer
        print(data)
        letter = data.decode("UTF-8")
        if letter == "g":
            try:
                gcommand()
                client_socket.close()
            except IOError:
                print("Google command couldn't execute")
        if letter == "a":
            try:
                acommand()
                client_socket.close()
            except IOError:
                print("Amazon command couldn't execute")
        else:
            print("couldn't find g or a")
    except IOError:
        print("couldn't start receiving")



def gcommand():
    root = os.getcwd()
    print(root)
    wavinput = os.path.exists("./input.wav")
    output = os.path.exists("./output.wav")
    convertscript = root + "/assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/pushtotalk.py"
    convertpath = os.path.exists(convertscript)

    encodecmd = "ffmpeg -y -i " + root + "/input.wav -c:a pcm_s32le " + root + "/output.wav"

    if wavinput:
        print("input exists")
        os.system(encodecmd)

        # if output:
        #     print("output exists")
        req = AudioSegment.from_wav(root + "/output.wav")
        req.export("convert.wav", format="wav", bitrate="8k")

        if convertpath:
            print("pushTT exists")
            conv = subprocess.call(["python", convertscript ,"-i", "convert.wav", "-o","response.wav"])
            #poll = conv.poll()

        else:
            print("Error, PushTT can not be found")

            #while(poll == None):
    
            #    print("waiting...")
            #    time.sleep(1)
            #    poll = conv.poll()
        #else:
         #   print("Error, output does not exist")

    else:
        print("Error, input does not exist")

    print("finished")


def acommand():
    root = os.getcwd()
    wavinput = os.path.exists("./audio/input.wav")
    convertscript = root + "/alexa_python/alexa_request.py"
    convertpath = os.path.exists(convertscript)

    if wavinput:
        print("input exists")

        if convertpath:
            print("Alexa request exists")
            conv = subprocess.call(["python", convertscript])

        else:
            print("Alexa request does not exist")
    else:
        print("input does not exist")


def server_loop(local_host, local_port):
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        server.bind((local_host, local_port))

    except ConnectionError:
        print("[!!] failed to listen on '{0}':'{1}'".format(local_host, int(local_port)))
        sys.exit(0)

    print("listening on '{0}':'{1}'".format(local_host, int(local_port)))

    server.listen(5)

    while True:
        client_socket, addr = server.accept()
        print("[==>] received incoming connection from %s:&d" % addr[0], addr[1])
        proxy_thread = threading.Thread(target=proxy_handler, args=(client_socket,))

        proxy_thread.start()


def main():
    try:
        server_loop("127.0.0.1", 9000)
    except IOError:
        print("Couldn't start server")


main()
