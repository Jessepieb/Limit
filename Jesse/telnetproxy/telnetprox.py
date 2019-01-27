import socket
import threading
import sys
import time


def sendplugin(newplugin, oldplugin):
    new = "ADD bunny plugin: 0013d382ea94 " + str(newplugin)
    old = "REMOVE bunny plugin: 0013d382ea94 " + str(oldplugin)
    telnetsocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        print("connecting")
        telnetsocket.connect(("127.0.0.1", 6969))
        print(telnetsocket)
        if len(new):
            telnetsocket.send(bytes(new, "UTF-8"))
            print("new sent")
            time.sleep(1)
        if len(old):
            telnetsocket.send(bytes(old, "UTF-8"))
            print("old sent")
            time.sleep(1)

        telnetsocket.shutdown(0)
        telnetsocket.close()
        sys.exit(0)
    except ConnectionError as e:
        print("couldn't connect")
        print(str(e))


def main():

    newplugin = sys.argv[1]
    oldplugin = sys.argv[2]

    if len(newplugin) & len(oldplugin):
        sendplugin(newplugin, oldplugin)
    else:
        print("No plugin was given")


main()
