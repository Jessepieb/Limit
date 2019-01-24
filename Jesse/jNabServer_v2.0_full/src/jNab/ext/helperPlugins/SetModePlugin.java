package jNab.ext.helperPlugins;

import jNab.core.events.RFIDEventListener;
import jNab.core.plugins.AbstractPlugin;

public class SetModePlugin extends AbstractPlugin implements RFIDEventListener
{
    private static String PLUGIN_NAME = "SetMode_Plugin";

    private static String[] PARAMETERS = {};

    public SetModePlugin()
    {
	super(PLUGIN_NAME, PARAMETERS);
    }

    public void onRfid(String rfid) {
        //Setting active plugin based on rfid bunnies:
        switch(rfid){
            case "d0021a0353184691":
                System.out.println("RFID: Red");
                /*
                System.out.println("Adding RadioPlugin...");
                this.bunny.addPlugin(new RadioPlugin());
                System.out.println("Removing SetModePlugin...");
                this.bunny.removePlugin(this);*/
                break;
            case "d0021a053b462c56":
                System.out.println("RFID: Green");
                System.out.println("Adding RFID_RecordPlugin...");
                this.bunny.addPlugin(new RFID_RecordPlugin());
                System.out.println("Removing SetModePlugin...");
                this.bunny.removePlugin(this);
                break;
            case "d0021a053b463984":
                System.out.println("RFID: Pink");
                /*
                System.out.println("Adding PlaygroundPlugin...");
                this.bunny.addPlugin(new PlaygroundPlugin());
                System.out.println("Removing SetModePlugin...");
                this.bunny.removePlugin(this);*/
                break;
            case "d0021a053b452c90":
                System.out.println("RFID: Orange");
                System.out.println("Adding AlexaPlugin...");
                this.bunny.addPlugin(new AlexaPlugin());
                System.out.println("Removing SetModePlugin...");
                this.bunny.removePlugin(this);
                break;
            case "d0021a053b4240ca":
                /*
                System.out.println("RFID: Yellow");
                System.out.println("Adding GooglePlugin...");
                this.bunny.addPlugin(new GooglePlugin());
                System.out.println("Removing SetModePlugin...");
                this.bunny.removePlugin(this);*/
                break;
        }
    }
}
