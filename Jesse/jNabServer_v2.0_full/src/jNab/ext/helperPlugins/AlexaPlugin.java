package jNab.ext.helperPlugins;

import jNab.core.events.*;
import jNab.core.plugins.*;
import jNab.core.protocol.*;
import java.nio.file.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.*;

public class AlexaPlugin extends AbstractPlugin implements RecordEventListener, ClickEventListener, RFIDEventListener
{
    private static String PLUGIN_NAME = "Alexa_Plugin";

    private static String[] PARAMETERS = {};

    public AlexaPlugin()
    {
	super(PLUGIN_NAME, PARAMETERS);
    }

    public void onSimpleRecord(byte[] data) {
        //Writing recording to input.wav:
        try
        {
            FileOutputStream fos = new FileOutputStream("audio/input.wav");
            for (int element : data)
                fos.write(element);
            fos.close();
        }
        catch (IOException e) {
            System.out.println(e);
            return;
        }
        alexaRequest();
    }

    public void onDoubleRecord(byte[] data) {}

    public void onSingleClick() {
        //Audio playback trigger:
        audioPlayBack("response");
    }

    public void onDoubleClick() {
        //Trigger the SetModePlugin:
        try {
            System.out.println("Adding SetModePlugin...");
            this.bunny.addPlugin(new SetModePlugin());
            System.out.println("Removing AlexaPlugin...");
            this.bunny.removePlugin(this);
        }
        catch (Error e) {
            System.out.println(e);
        }
    }

    public void onRfid(String rfid) {
        //Setting current input.wav file to saved recording:
        switch(rfid){
            case "d0021a0353184691":
                System.out.println("RFID: Red");
                setInputFile("red");
                audioPlayBack("input");
                alexaRequest();
                audioPlayBack("response");
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
                setInputFile("green");
                audioPlayBack("input");
                alexaRequest();
                audioPlayBack("response");
                break;
            case "d0021a053b463984":
                System.out.println("RFID: Pink");
                setInputFile("pink");
                audioPlayBack("input");
                alexaRequest();
                audioPlayBack("response");
                break;
            case "d0021a053b452c90":
                System.out.println("RFID: Orange");
                setInputFile("orange");
                audioPlayBack("input");
                alexaRequest();
                audioPlayBack("response");
                break;
            case "d0021a053b4240ca":
                System.out.println("RFID: Yellow");
                setInputFile("yellow");
                audioPlayBack("input");
                alexaRequest();
                audioPlayBack("response");
                break;
        }
    }

    private void alexaRequest() {
        try (
            //open socket to connect to localhost tcp proxy
            Socket echoSocket = new Socket("localhost", 9000);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
        ){
            //send byte to proxy
            out.write('a');
            out.flush();
            String inString;
            while((inString = in.readLine()) != null){
                System.out.println("String: " + inString);
                if(inString.equals("done")){
                    System.out.println("true");
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        /*
        //Executing AVS python script:
        try
        {
            System.out.println("Process: trying to execute python script...");
            String command = "cmd /c python alexa_python\\alexa_request.py";
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            System.out.println("Process: finished process.");
        }
        catch (IOException e) {
            System.out.println(e);
            return;
        }
        catch (InterruptedException e) {
            System.out.println(e);
            return;
        }*/
    }

    private void setInputFile(String color) {
        Path src = Paths.get("audio/" + color + "_input.wav");
        Path dst = Paths.get("audio/input.wav");
        try {
            Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Unable to parse audiofile: " + e.getMessage());
        }
    }

    private void audioPlayBack(String filename) {
        //Audio playback of file
        Packet p = new Packet();
        MessageBlock mb = new MessageBlock(674);
        mb.addPlayLocalSoundCommand("audio/" + filename + ".wav");
        mb.addWaitPreviousEndCommand();
        p.addBlock(mb);
        p.addBlock(new PingIntervalBlock(1));
        this.bunny.addPacket(p);
    }
}
