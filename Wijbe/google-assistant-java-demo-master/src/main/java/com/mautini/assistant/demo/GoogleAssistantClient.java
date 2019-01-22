package com.mautini.assistant.demo;

import com.mautini.assistant.demo.api.AssistantClient;
import com.mautini.assistant.demo.authentication.AuthenticationHelper;
import com.mautini.assistant.demo.client.audio.AudioPlayer;
import com.mautini.assistant.demo.client.audio.AudioRecorder;
import com.mautini.assistant.demo.config.AssistantConf;
import com.mautini.assistant.demo.config.AudioConf;
import com.mautini.assistant.demo.config.AuthenticationConf;
import com.mautini.assistant.demo.config.DeviceRegisterConf;
import com.mautini.assistant.demo.config.IoConf;
import com.mautini.assistant.demo.device.DeviceRegister;
import com.mautini.assistant.demo.exception.AudioException;
import com.mautini.assistant.demo.exception.AuthenticationException;
import com.mautini.assistant.demo.exception.ConverseException;
import com.mautini.assistant.demo.exception.DeviceRegisterException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;
import jNab.ext.helperPlugins.EchoPlugin;
import jNab.ext.helperPlugins.GreenOrangeRedWithEarsPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jNab.core.server.*;
import jNab.core.bunny.*;
import jNab.core.plugins.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GoogleAssistantClient {
    public static byte[] request1 = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAssistantClient.class);

    public static void main(String[] args) throws AuthenticationException, AudioException, ConverseException, DeviceRegisterException, IOException, WavFileException {
        //jnab


        String nabaztagSerial = "0013D382EA94";
        String serverAddress = "192.168.0.101";
        int serverPort = 80;
        File resourcesPath = new File("./files");
        File pluginsPath = new File("./files/plugins");
        MicroServer microServer = new MicroServer(serverAddress, serverPort, resourcesPath, pluginsPath);
        // Enabling all logging features, using stderr and stdout as logging stream
        microServer.setDebugLoggingStream(System.out);
        microServer.setErrorLoggingStream(System.err);
        microServer.setInfoLoggingStream(System.out);

        Bunny nabaztag = new Bunny(nabaztagSerial);
        microServer.getBurrow().addBunny(nabaztag);

        AbstractPlugin plugin = new GreenOrangeRedWithEarsPlugin();
        nabaztag.addPlugin(plugin);

        // Starting micro server
        microServer.start();

        //Google assistent
        Config root = ConfigFactory.load();
        AuthenticationConf authenticationConf = ConfigBeanFactory.create(root.getConfig("authentication"), AuthenticationConf.class);
        DeviceRegisterConf deviceRegisterConf = ConfigBeanFactory.create(root.getConfig("deviceRegister"), DeviceRegisterConf.class);
        AssistantConf assistantConf = ConfigBeanFactory.create(root.getConfig("assistant"), AssistantConf.class);
        AudioConf audioConf = ConfigBeanFactory.create(root.getConfig("audio"), AudioConf.class);
        IoConf ioConf = ConfigBeanFactory.create(root.getConfig("io"), IoConf.class);

        // Authentication
        AuthenticationHelper authenticationHelper = new AuthenticationHelper(authenticationConf);
        authenticationHelper
                .authenticate()
                .orElseThrow(() -> new AuthenticationException("Error during authentication"));

        // Check if we need to refresh the access token to request1 the api
        if (authenticationHelper.expired()) {
            authenticationHelper
                    .refreshAccessToken()
                    .orElseThrow(() -> new AuthenticationException("Error refreshing access token"));
        }

        // Register Device model and device
        DeviceRegister deviceRegister = new DeviceRegister(deviceRegisterConf, authenticationHelper.getOAuthCredentials().getAccessToken());
        deviceRegister.register();

        // Build the client (stub)
        AssistantClient assistantClient = new AssistantClient(authenticationHelper.getOAuthCredentials(), assistantConf,
                deviceRegister.getDeviceModel(), deviceRegister.getDevice(), ioConf);

        // Build the objects to record and play the conversation
        AudioRecorder audioRecorder = new AudioRecorder(audioConf);
        AudioPlayer audioPlayer = new AudioPlayer(audioConf);

        // Initiating Scanner to take user input
        Scanner scanner = new Scanner(System.in);

        // Main loop
        while (true) {
            // Check if we need to refresh the access token to request1 the api
            if (authenticationHelper.expired()) {
                authenticationHelper
                        .refreshAccessToken()
                        .orElseThrow(() -> new AuthenticationException("Error refreshing access token"));

                // Update the token for the assistant client
                assistantClient.updateCredentials(authenticationHelper.getOAuthCredentials());
            }



            // Get input (text or voice)
            byte[] request = getInput(ioConf, scanner, audioRecorder);

            // requesting assistant with text query
            assistantClient.requestAssistant(request);
            LOGGER.info(assistantClient.getTextResponse());

            if (Boolean.TRUE.equals(ioConf.getOutputAudio())) {
                byte[] audioResponse = assistantClient.getAudioResponse();
                if (audioResponse.length > 0) {
                    audioPlayer.play(audioResponse);
                    Packet p = new Packet();

                    // Playing the associated sound files
                    MessageBlock mb = new MessageBlock(333);
                    mb.addPlayLocalSoundCommand("files/sounds/dice/1.mp3");
                    mb.addWaitPreviousEndCommand();
                    p.addBlock(mb);
                    p.addBlock(new PingIntervalBlock(1));
                    nabaztag.addPacket(p);
                } else {
                    LOGGER.info("No response from the API");
                }
            }
        }

    }

    private static byte[] getInput(IoConf ioConf, Scanner scanner, AudioRecorder audioRecorder) throws AudioException {
        switch (ioConf.getInputMode()) {
            case IoConf.TEXT:
                LOGGER.info("Tap your request1 and press enter...");
                // Taking user input in
                String query = scanner.nextLine();
                // Converting user into byte array to keep consistency of requestAssistant params
                return query.getBytes();
            case IoConf.AUDIO:
                // Record
                return audioRecorder.getRecord();
            default:
                LOGGER.error("Unknown input mode {}", ioConf.getInputMode());
                return new byte[]{};
        }
    }
}
