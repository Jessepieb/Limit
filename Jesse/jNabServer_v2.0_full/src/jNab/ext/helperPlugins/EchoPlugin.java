package jNab.ext.helperPlugins;

import jNab.core.events.ClickEventListener;
import jNab.core.events.RFIDEventListener;
import jNab.core.events.RecordEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Plugin echoing on a bunny the sound that has been previously recorded (on the same bunny).
 *
 * @author Juha-Pekka Rajaniemi
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class EchoPlugin extends AbstractPlugin implements RecordEventListener, ClickEventListener, RFIDEventListener
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
		try{
			//get directory path
			System.out.println(new File(".").getAbsoluteFile());
			//write to voice command to input.wav
			FileOutputStream fos = new FileOutputStream("input.wav");
			System.out.println("fos made");
			for (int element : data)
				fos.write(element);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try (
				//open socket to connect to localhost tcp proxy
                Socket echoSocket = new Socket("localhost", 9000);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()))
        ){
			//send byte to proxy
            out.write('g');
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

			// create packet
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

	@Override
	public void onRfid(String rfid) {

		switch (rfid) {
			case "d0021a0353184691":
				System.out.println("RFID: Red");
				newPlugin("Alexa_plugin");
				break;
			case "d0021a053b462c56":
				System.out.println("RFID: Green");
				newPlugin("Echo_plugin");
				break;
			case "d0021a053b463984":
				System.out.println("RFID: Pink");
				newPlugin("Dice_Plugin");
				break;
//			case "d0021a053b452c90":
//				System.out.println("RFID: Orange");
//				currentTag = "orange";
//				break;
//			case "d0021a053b4240ca":
//				System.out.println("RFID: Yellow");
//				currentTag = "yellow";
//				break;
		}
	}
	public void newPlugin(String plugin){
		try {
			System.out.println(plugin);
			Socket telnetSocket = new Socket("192.168.0.101", 6969);
			PrintWriter pout = new PrintWriter(telnetSocket.getOutputStream(), true);

			System.out.println(telnetSocket);
			String addString = "ADD bunny plugin: 0013d382ea94 ";
			String removeString = "REMOVE bunny plugin: 0013d382ea94 ";
			String shutdownString = "SHUTDOWN client";

			pout.write(addString+ plugin);
			System.out.println(pout);
			pout.flush();
			pout.write(removeString +PLUGIN_NAME);
			System.out.println(pout);
			pout.flush();
			pout.write(shutdownString);
			pout.flush();
			pout.close();

		} catch (IOException e) {
			System.out.println("failed to telnet bruv");
			e.printStackTrace();
		}
	}}
