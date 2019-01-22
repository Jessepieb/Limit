package jNab.ext.helperPlugins;

import jNab.core.events.ClickEventListener;
import jNab.core.events.RFIDEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

public class PlaygroundPlugin extends AbstractPlugin implements RFIDEventListener, ClickEventListener {

	private static String PLUGIN_NAME = "Playground_Plugin";

	private static String[] PARAMETERS = {};

    public PlaygroundPlugin() {
        super(PLUGIN_NAME, PARAMETERS);
    }

    public void onRfid(String rfid) {
        //Playing Choreography by color:
        switch(rfid){
            case "d0021a0353184691":
            	System.out.println("RFID: Red");
            	playChoreography("Ears");
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
                playChoreography("Colours");
                break;
            case "d0021a053b463984":
				System.out.println("RFID: Pink");
                playChoreography("Colours");
                break;
            case "d0021a053b452c90":
            	System.out.println("RFID: Orange");
                playChoreography("Colours");
                break;
            case "d0021a053b4240ca":
            	System.out.println("RFID: Yellow");
                playChoreography("Colours");
                break;
        }
    }

	public void onSingleClick() { }

	public void onDoubleClick() {
		//Trigger the SetModePlugin:
		try {
			System.out.println("Adding SetModePlugin...");
			this.bunny.addPlugin(new SetModePlugin());
			System.out.println("Removing PlaygroundPlugin...");
			this.bunny.removePlugin(this);
		}
		catch (Error e) {
			System.out.println(e);
		}
	}

	private void playChoreography(String name) {
        Packet p = new Packet();
        p.addBlock(new PingIntervalBlock(20));
        MessageBlock mb = new MessageBlock(12345);
        mb.addPlayChoreographyFromLibraryCommand(name);
        mb.addWaitPreviousEndCommand();
        p.addBlock(mb);
        this.bunny.addPacket(p);
    }
}