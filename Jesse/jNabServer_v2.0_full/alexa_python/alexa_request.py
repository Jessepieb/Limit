from avs_client import AlexaVoiceServiceClient
import soundfile
import os

alexa_client = AlexaVoiceServiceClient(
   client_id='',
    secret='',
    refresh_token='',
)
alexa_client.connect()  # authenticate and other handshaking steps

#path = os.getcwd()
data, samplerate = soundfile.read('./audio/input.wav')
soundfile.write('./audio/converted_input.wav', data, 8000, subtype='PCM_32')

with open('./audio/converted_input.wav', 'rb') as f:
    alexa_response_audio = alexa_client.send_audio_file(f)

with open('./audio/response.wav', 'wb') as f:
    f.write(alexa_response_audio)
