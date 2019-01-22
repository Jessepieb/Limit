package jNab.ext.helperPlugins;

import jNab.core.events.ClickEventListener;
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
public class EchoPlugin extends AbstractPlugin implements RecordEventListener, ClickEventListener
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
            Process process = null;
            Process convert = null;
            String command = "";

            try {
                ProcessBuilder processBuilder = new ProcessBuilder("//usr/bin/python","convert.py");
                process = processBuilder.start();

                InputStream inputStream = process.getInputStream();

                String is = printStream(inputStream);
                System.out.println(is);
                InputStream errorStream = process.getErrorStream();
                is = printStream(errorStream);
                System.out.println(is);
                process.waitFor();

                ProcessBuilder convertBuilder = new ProcessBuilder("/bin/sh","test.sh");
                convert = convertBuilder.start();

                InputStream convertStream = convert.getInputStream();
                is = printStream(convertStream);
                System.out.println(is);
                InputStream ErrorStream = convert.getErrorStream();
                is = printStream(ErrorStream);
                System.out.println(is);
                process.waitFor();


            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            //Process p = Runtime.getRuntime().exec("python -m ~/Documents/NabAlexa/Jesse/jNabServer_v2.0_full/convert.py");
            //System.out.println(p.toString());

            //	StringBuilder output = new StringBuilder();

            //	BufferedReader reader = new BufferedReader(
            //			new InputStreamReader(p.getInputStream()));

            //    boolean pisalive = true;
            String line;
            //	while ((line = reader.readLine()) != null) {
            //		output.append(line + "\n");
            //	}
            //	while (pisalive) {
            //		System.out.println("waiting...");
            //		pisalive = p.isAlive();

            //		if (!pisalive){
            //			System.out.println("Success!");
            //			System.out.println(output);
            //		}
            //	}
            // Playing recorded sound
        Packet p = new Packet();
            MessageBlock mb = new MessageBlock(600);
            mb.addPlayLocalSoundCommand("response.wav");
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

	@Override
	public void onSingleClick() {
		Packet p = new Packet();
		MessageBlock mb = new MessageBlock(666);
		mb.addPlayLocalSoundCommand("response.wav");
		mb.addWaitPreviousEndCommand();
		p.addBlock(mb);
		p.addBlock(new PingIntervalBlock(1));
		this.bunny.addPacket(p);
	}

	@Override
	public void onDoubleClick() {

	}

	public String printStream(InputStream inputStream){
	    BufferedReader br = null;
	    StringBuilder sb = new StringBuilder();

	    String line;
	    try {
                br = new BufferedReader((new InputStreamReader(inputStream)));
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
        }
        catch (IOException e){
	        e.printStackTrace();
        }
        finally {
	        if (br != null){
	            try{
	                br.close();
                }
                catch (IOException e){
	                e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}
