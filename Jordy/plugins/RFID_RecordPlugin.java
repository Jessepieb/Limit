package jNab.ext.helperPlugins;

import jNab.core.events.ClickEventListener;
import jNab.core.events.RFIDEventListener;
import jNab.core.events.RecordEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

import java.io.FileOutputStream;
import java.io.IOException;

public class RFID_RecordPlugin extends AbstractPlugin implements RFIDEventListener, RecordEventListener, ClickEventListener {

	private static String PLUGIN_NAME = "RFID_RecordPlugin";

	private static String[] PARAMETERS = {};

    public RFID_RecordPlugin() {
        super(PLUGIN_NAME, PARAMETERS);
    }

	private String currentTag;

    public void onRfid(String rfid) {
        switch(rfid){
            case "d0021a0353184691":
            	System.out.println("RFID: Red");
            	currentTag = "red";
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
				currentTag = "green";
                break;
            case "d0021a053b463984":
				System.out.println("RFID: Pink");
				currentTag = "pink";
                break;
            case "d0021a053b452c90":
            	System.out.println("RFID: Orange");
				currentTag = "orange";
                break;
            case "d0021a053b4240ca":
            	System.out.println("RFID: Yellow");
				currentTag = "yellow";
                break;
        }
    }

	public void onSimpleRecord(byte[] data) {
		//Writing recording to rfid_"color".wav:
		try
		{
			FileOutputStream fos = new FileOutputStream("audio/" + currentTag + "_input.wav");
			for (int element : data)
				fos.write(element);
			fos.close();
			System.out.println("Input written to: audio/" + currentTag + "_input.wav");
		}
		catch (IOException e)
		{
			System.out.println(e);
			return;
		}
	}

	public void onDoubleRecord(byte[] data) { }

	public void onSingleClick() {
		audioPlayBack(currentTag);
	}

	public void onDoubleClick() {
		//Trigger the SetModePlugin:
		try {
			System.out.println("Adding SetModePlugin...");
			this.bunny.addPlugin(new SetModePlugin());
			System.out.println("Removing RFID_RecordPlugin...");
			this.bunny.removePlugin(this);
		}
		catch (Error e) {
			System.out.println(e);
		}
	}

	private void audioPlayBack(String filename) {
		//Audio playback of file
		Packet p = new Packet();
		MessageBlock mb = new MessageBlock(674);
		mb.addPlayLocalSoundCommand("audio/" + filename + "_input.wav");
		mb.addWaitPreviousEndCommand();
		p.addBlock(mb);
		p.addBlock(new PingIntervalBlock(1));
		this.bunny.addPacket(p);
	}
}