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
        Choreography choreography;
        //Blinking:
        /*choreography = new Choreography("Blink");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(1, Choreography.LED_RIGHT, 0, 0, 255);
        choreography.addLedColorCommand(1, Choreography.LED_CENTER, 0, 0, 255);
        choreography.addLedColorCommand(1, Choreography.LED_LEFT, 0, 0, 255);
        saveChoreography(choreography);
        playChoreography("Blink");*/

        //Colours:
        /*choreography = new Choreography("Colours");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 128,0, 128);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 0, 255);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 0, 255, 0);
        saveChoreography(choreography);
        playChoreography("Colours");*/

        //Ears:
        /*choreography = new Choreography("Ears");
        choreography.addTempoCommand(0, 100);
        choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_LEFT, 10, Choreography.DIRECTION_FORWARD);
        choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_RIGHT, 10, Choreography.DIRECTION_FORWARD);
        saveChoreography(choreography);
        playChoreography("Ears");*/

        //Tag_Red:
        /*choreography = new Choreography("Tag_Red");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 0, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 255, 0, 0);
        choreography.addPlayRandomMidi(0);
        saveChoreography(choreography);
        //playChoreography("Tag_Red");

        //Tag_Green:
        choreography = new Choreography("Tag_Green");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 153, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 0, 153, 0);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 0, 153, 0);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 153, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 0, 153, 0);
        choreography.addPlayRandomMidi(0);
        saveChoreography(choreography);
        //playChoreography("Tag_Green");

        //Tag_Pink:
        choreography = new Choreography("Tag_Pink");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 127);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 0, 127);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 127);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 0, 127);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 255, 0, 127);
        choreography.addPlayRandomMidi(0);
        saveChoreography(choreography);
        //playChoreography("Tag_Pink");

        //Tag_Orange:
        choreography = new Choreography("Tag_Orange");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 128, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 128, 0);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 128, 0);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 128, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 255, 128, 0);
        choreography.addPlayRandomMidi(0);
        saveChoreography(choreography);
        //playChoreography("Tag_Orange");

        //Tag_Yellow:
        choreography = new Choreography("Tag_Yellow");
        choreography.addTempoCommand(0, 100);
        choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 255, 0);
        choreography.addLedColorCommand(0, Choreography.LED_NOSE, 255, 255, 0);
        choreography.addPlayRandomMidi(0);
        saveChoreography(choreography);
        //playChoreography("Tag_Yellow");*/

        //Disco:
        choreography = new Choreography("Disco");
        choreography.addTempoCommand(0, 10);
        for(int i = 0; i < 150; i++) {
            switch(i % 6) {
                case(0):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 255, 0, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 165, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 0, 255);
                    choreography.addRelativeEarMoveCommand(0, Choreography.EAR_LEFT, 12);
                    choreography.addRelativeEarMoveCommand(0, Choreography.EAR_RIGHT, 12);
                    continue;
                case(1):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 255, 165, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 0, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 0, 255);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 128, 0, 128);
                    continue;
                case(2):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 255, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 0, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 0, 0, 255);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 128, 0, 128);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 0);
                    continue;
                case(3):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 0, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 0, 0, 255);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 128, 0, 128);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 0, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 165, 0);
                    continue;
                case(4):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 0, 0, 255);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 128, 0, 128);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 0, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 165, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
                    continue;
                case(5):
                    choreography.addLedColorCommand(1, Choreography.LED_NOSE, 128, 0 , 128);
                    choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 165, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 255, 0);
                    choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 255, 0);
                    continue;
            }
        }
        saveChoreography(choreography);
        //playChoreography("Disco");
    }

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
