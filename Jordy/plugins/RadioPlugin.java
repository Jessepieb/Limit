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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RadioPlugin extends AbstractPlugin implements RFIDEventListener, ClickEventListener
{
    private static String PLUGIN_NAME = "Radio_Plugin";

    private static String[] PARAMETERS = {};

    public RadioPlugin()
    {
	super(PLUGIN_NAME, PARAMETERS);
    }

    public void onRfid(String rfid) {
        //Setting current radio:
        switch(rfid){
            case "d0021a0353184691":
                System.out.println("RFID: Red");
                audioPlayBack("http://icecast.omroep.nl/3fm-sb-mp3");
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
                audioPlayBack("http://icecast.omroep.nl/3fm-sb-mp3");
                break;
            case "d0021a053b463984":
                System.out.println("RFID: Pink");
                audioPlayBack("http://icecast.omroep.nl/3fm-sb-mp3");
                break;
            case "d0021a053b452c90":
                System.out.println("RFID: Orange");
                audioPlayBack("http://icecast.omroep.nl/3fm-sb-mp3");
                break;
            case "d0021a053b4240ca":
                System.out.println("RFID: Yellow");
                audioPlayBack("http://icecast.omroep.nl/3fm-sb-mp3");
                break;
        }
    }

    private void audioPlayBack(String url) {
        //Audio playback of file
        Packet p = new Packet();
        MessageBlock mb = new MessageBlock(674);
        mb.addPlayStreamCommand(url);
        mb.addWaitPreviousEndCommand();
        p.addBlock(mb);
        p.addBlock(new PingIntervalBlock(1));
        this.bunny.addPacket(p);
    }

    public void onSingleClick() { }

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
}
