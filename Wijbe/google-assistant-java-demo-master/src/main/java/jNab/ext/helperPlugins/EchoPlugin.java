package jNab.ext.helperPlugins;

import jNab.core.events.RecordEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

import java.io.*;
import java.lang.Runtime;

/**
 * Plugin echoing on a bunny the sound that has been previously recorded (on the same bunny).
 *
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class EchoPlugin extends AbstractPlugin implements RecordEventListener
{
    /**
     * Plugin name.
     */
    private static String PLUGIN_NAME = "Echo_Plugin";

    /**
     * Set of parameters supported by the plugin;
     */
    private static String[] PARAMETERS = {};

    /**
     * Creating a new Echo plugin instance.
     */
    public EchoPlugin()
    {
        super(PLUGIN_NAME, PARAMETERS);
    }

    /**
     * Echoing the recorded voice.
     *
     * @see RecordEventListener#onSimpleRecord(byte[])
     */
    public void onSimpleRecord(byte[] data) {
        // Writing data to echo-<MAC>.wav test
        try {
            System.out.println(new File(".").getAbsoluteFile());
            FileOutputStream fos = new FileOutputStream("input.wav");
            System.out.println("fos made");
            for (int element : data)
                fos.write(element);
            fos.close();
            Process p = Runtime.getRuntime().exec("python ~/Documents/NabAlexa/Jesse/assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/convert.py");
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = p.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
                System.exit(0);
            } else {
                //abnormal...
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Playing recorded sound
        Packet p = new Packet();
        MessageBlock mb = new MessageBlock(600);
        mb.addPlayLocalSoundCommand("~/Documents/NabAlexa/Jesse/assistant-sdk-python-master/google-assistant-sdk/googlesamples/assistant/grpc/response.wav");
        mb.addWaitPreviousEndCommand();
        p.addBlock(mb);
        p.addBlock(new PingIntervalBlock(1));
        this.bunny.addPacket(p);
        System.out.println("finished");
    }

    /**
     * @see jNab.core.events.RecordEventListener#onDoubleRecord(byte[])
     */
    public void onDoubleRecord(byte[] data)
    {}

}
