#networking
import socket
import subprocess
import getopt

#audio
from pydub import AudioSegment
import sys
import os
import os.path


def server_loop():
        global target
        global port
        
        # if no target is defined we listen on all interfaces
        if not len(target):
                target = "0.0.0.0"
                
        server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        server.bind((target,port))
        
        server.listen(1)        
        
        while True:
                
                client_socket, addr = server.accept()
                clientdata(client_socket,)


def client_handler(client_socket):
    if len(client_socket):
        command()
                
                
#root = os.path.dirname(os.path.realpath()+"/")   
def command():
    root = os.getcwd()
    print(root)
    input = os.path.exists("./input.wav")
    output = os.path.exists("./output.wav")
    cvtscript = root+"/assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/pushtotalk.py"
    cvtpath = os.path.exists(cvtscript)

    encodecmd = "ffmpeg -y -i "+root+"/input.wav -c:a pcm_s32le "+root+"/output.wav"

    if(input):
        print("input exists")
        os.system(encodecmd)

        if(output):
            print("output exists")
            req = AudioSegment.from_wav(root + "/output.wav")
            req.export("convert.wav",format="wav",bitrate="8k") 

            if(cvtpath):
                print("pushTT exists")
                conv = subprocess.Popen(["python", cvtscript ,"-i", "convert.wav", "-o","   "])
                poll = conv.poll()

            else:
                print("Error, PushTT can not be found")

            while(poll == None):
    
                print("waiting...")
                time.sleep(1)
                poll = conv.poll()
        else:
            print("Error, output does not exist")

    else:
        print("Error, input does not exist")

    print("finished")

def usage():
        print "Usage: convert.py -t target_host -p port"
        print "-l --listen                - listen on [host]:[port] for incoming connections"
        print
        print "Examples: "
        print "convert.py -t 192.168.0.1 -p 5555 -l"
        sys.exit(0)

def main():
        global listen
        global port
        global execute
        global command
        global upload_destination
        global target
        
        if not len(sys.argv[1:]):
                usage()
                
        # read the commandline options
        try:
                opts, args = getopt.getopt(sys.argv[1:],"hle:t:p:cu:",["help","listen","target","port"])
        except getopt.GetoptError as err:
                print (str(err))
                usage()
                
                
        for o,a in opts:
                if o in ("-h","--help"):
                        usage()
                elif o in ("-l","--listen"):
                        listen = True
                elif o in ("-t", "--target"):
                        target = a
                elif o in ("-p", "--port"):
                        port = int(a)
                else:
                        assert False,"Unhandled Option"
        

        # are we going to listen or just send data from stdin
        if not listen and len(target) and port > 0:

                buffer = sys.stdin.read()
                

                client_sender(buffer)   

        if listen:
                server_loop()

main()       