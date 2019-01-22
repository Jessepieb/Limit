package jNab.ext.helperPlugins;

import jNab.core.choreography.Choreography;
import jNab.core.events.ClickEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;
import jNab.ext.persistency.Serializer;

import java.io.File;
import java.io.IOException;

public class ChoreographyPlugin extends AbstractPlugin implements ClickEventListener
{
    private static String PLUGIN_NAME = "Choreography_Plugin";

    private static String[] PARAMETERS = {};

    public ChoreographyPlugin()
    {
	super(PLUGIN_NAME, PARAMETERS);
    }

    public void onSingleClick()
    {
        //Blinking:
        Choreography choreography;
        /*choreography = new Choreography("Blink");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(1, Choreography.LED_RIGHT, 0, 0, 255);
        choreography.addLedColorCommand(1, Choreography.LED_CENTER, 0, 0, 255);
        choreography.addLedColorCommand(1, Choreography.LED_LEFT, 0, 0, 255);
        saveChoreography(choreography);
        playChoreography("Blink");*/

        //Colours:
        choreography = new Choreography("Colours");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 128,0, 128);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 0, 255);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 0, 255, 0);
        saveChoreography(choreography);
        playChoreography("Colours");

        //Ears:
        /*choreography = new Choreography("Ears");
        choreography.addTempoCommand(0, 100);
        choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_LEFT, 10, Choreography.DIRECTION_FORWARD);
        choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_RIGHT, 10, Choreography.DIRECTION_FORWARD);
        saveChoreography(choreography);
        playChoreography("Ears");*/
    }

    public void onDoubleClick() { }

    private void saveChoreography(Choreography c) {
        Serializer serializer = new Serializer(new File("./files"));
        try
        {
            System.out.println("Trying to save: " + c.getName());
            serializer.saveChoreography(c);
            System.out.println("Saved.");
        }
        catch (IOException e)
        {
            System.out.println("unable to save choreography ("+e.getMessage()+")");
            return;
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
