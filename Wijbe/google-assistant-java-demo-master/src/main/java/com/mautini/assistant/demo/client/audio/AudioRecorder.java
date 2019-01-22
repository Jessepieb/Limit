package com.mautini.assistant.demo.client.audio;

import com.mautini.assistant.demo.config.AudioConf;
import com.mautini.assistant.demo.exception.AudioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.*;

public class AudioRecorder {

    private static final Logger LOGGER = LoggerFactory.getLogger(AudioRecorder.class);

    private TargetDataLine microphone;

    private boolean stopped = false;

    private AudioConf audioConf;

    public AudioRecorder(AudioConf audioConf) {
        this.audioConf = audioConf;
    }

    public byte[] getRecord() throws AudioException {
        try {
//            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("echo0013d382ea94.wav"));
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            int read;
//            byte[] buff = new byte[1024];
//            while ((read = ais.read(buff)) > 0)
//            {
//                out.write(buff, 0, read);
//            }
//            out.flush();
//            return out.toByteArray();
            // Reset the flag
            stopped = false;

            // Start a new thread to wait during listening
            Thread stopper = new Thread(() -> {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    br.readLine();
                    stopped = true;
                    LOGGER.info("End of the capture");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                stop();
            });

            // Start the thread that can stop the record
            stopper.start();

            return record();
        } catch (Exception e) {
            throw new AudioException("Unable to record your voice", e);
        }
    }

    private byte[] record() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
//        AudioFormat format = AudioUtil.getAudioFormat(audioConf);
//        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
//
//        // Checks if system supports the data line
//        if (!AudioSystem.isLineSupported(info)) {
//            LOGGER.error("Line not supported");
//            System.exit(0);
//        }
//
//        microphone = (TargetDataLine) AudioSystem.getLine(info);
//        microphone.open(format);
//        microphone.start();
//
//        LOGGER.info("Listening, tap enter to stop ...");
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        int numBytesRead;
//        byte[] data = new byte[microphone.getBufferSize() / 5];
//
//        // Begin audio capture.
//        microphone.start();
//
//        // Here, stopped is a global boolean set by another thread.
//        while (!stopped) {
//            // Read the next chunk of data from the TargetDataLine.
//            numBytesRead = microphone.read(data, 0, data.length);
//            // Save this chunk of data.
//            byteArrayOutputStream.write(data, 0, numBytesRead);
//        }
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("echo0013d382ea94.wav"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int read;
        byte[] buff = new byte[1024];
        while ((read = ais.read(buff)) > 0)
        {
            out.write(buff, 0, read);
        }
        out.flush();
        return out.toByteArray();
        //return byteArrayOutputStream.toByteArray();
    }

    /**
     * Stop the capture
     */
    private void stop() {
        microphone.stop();
        microphone.close();
    }
}
