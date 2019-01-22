from pydub import AudioSegment
import time
import subprocess
import os
import sys
from pathlib import Path

input = Path("input.wav")
output = Path("output.wav")
cvtscript = "~/Documents/NabAlexa/Jesse/assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/pushtotalk.py"
cvtpath = Path(cvtscript)
response = Path("response.wav")

encodecmd = "ffmpeg -y -i ~/Documents/NabAlexa/Jesse/jNabServer_v2.0_full/input.wav -c:a pcm_s32le output.wav"
movecmd = "cp -f response.wav ~/Documents/NabAlexa/Jesse/jNabServer_v2.0_full/response.wav"

if(input.is_file):
    print("input exists")
    os.system(encodecmd)

if(output.is_file):
    print("output exists")
    req = AudioSegment.from_wav("output.wav")
    req.export("convert.wav",format="wav",bitrate="8k")

    if(cvtpath.is_file):
        print("pushTT exists")
        conv = subprocess.Popen(["python", "../assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/pushtotalk.py","-i", "convert.wav", "-o","response.wav"])
        poll = conv.poll()

while(poll == None):
    
    print("waiting...")
    time.sleep(1)
    poll = conv.poll()



#if (response.is_file):
#    print("moving")
#    os.system(movecmd)

#exec("pushtotalk.py -i convert.wav -o response.wav")
#shutil.copy("response.wav", dest_path+"response.wav")
#os.remove("input.wav")
#if (response.is_file):
#    os.remove("output.wav")
#    os.remove("response.wav")
#os.remove("response.wav")
print("finished")
sys.exit(0)