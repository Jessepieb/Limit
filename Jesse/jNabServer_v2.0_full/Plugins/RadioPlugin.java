package jNab.ext.helperPlugins;

import jNab.core.events.ClickEventListener;
import jNab.core.events.RFIDEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.*;

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
                audioPlayBack("http://18973.live.streamtheworld.com/RADIO538.mp3");
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
                audioPlayBack("http://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538");
                break;
            case "d0021a053b463984":
                System.out.println("RFID: Pink");
                audioPlayBack("http://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538.mp3 ");
                break;
            case "d0021a053b452c90":
                System.out.println("RFID: Orange");
                audioPlayBack("http://playerservices.streamtheworld.com/api/livestream-redirect/RADIO538AAC.aac");
                break;
            case "d0021a053b4240ca":
                System.out.println("RFID: Yellow");
                audioPlayBack("http:\u200B//icecast-qmusic.\u200Bcdp.\u200Btriple-it.\u200Bnl/Qmusic_nl_live_64.\u200Bogg");
                break;
        }
    }

    private void audioPlayBack(String url) {
        //Audio playback of file
        Packet p = new Packet();
        MessageBlock mb = new MessageBlock(674);
        mb.addPlayStreamCommand(url);
        //mb.addPlaySoundCommand(url);
        //mb.addWaitPreviousEndCommand();
        p.addBlock(mb);
        p.addBlock(new PingIntervalBlock(1));
        //dataToSend = p.generatePacket();
        this.bunny.addPacket(p);
        //this.bunny.forcePacket(p);
    }

    @Override
    public void onSingleClick() {
        //Audio playback of file
        Packet p = new Packet();
        MessageBlock mb = new MessageBlock(521);
        mb.addPlayStreamCommand("stream-uk1.radioparadise.com/mp3-32");
        //mb.addPlaySoundCommand(url);
        mb.addWaitPreviousEndCommand();
        p.addBlock(new PingIntervalBlock(2));
        p.addBlock(mb);

        this.bunny.addPacket(p);
        //this.bunny.forcePacket(p);
        //audioPlayBack("http://stream-uk1.radioparadise.com/mp3-32");
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
}
