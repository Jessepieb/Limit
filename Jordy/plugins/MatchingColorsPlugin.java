package jNab.ext.helperPlugins;

import jNab.core.events.*;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

public class MatchingColorsPlugin extends AbstractPlugin implements RFIDEventListener, ClickEventListener {
    private static String PLUGIN_NAME = "MatchingColorsPlugin";
    private static String[] PARAMETERS = {};

	public MatchingColorsPlugin() {
		super(PLUGIN_NAME, PARAMETERS);
	}

	public void onRfid(String rfid) {
		switch(rfid){
			case "d0021a0353184691":
			    System.out.println("RFID: Red");
                playChoreography("Tag_Red");
                break;
			case "d0021a053b462c56":
                System.out.println("RFID: Green");
                playChoreography("Tag_Green");
                break;
			case "d0021a053b463984":
                System.out.println("RFID: Pink");
                playChoreography("Tag_Pink");
                break;
			case "d0021a053b452c90":
                System.out.println("RFID: Orange");
                playChoreography("Tag_Orange");
                break;
			case "d0021a053b4240ca":
                System.out.println("RFID: Yellow");
                playChoreography("Tag_Yellow");
                break;
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
}
