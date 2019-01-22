package jNab.core.plugins;

import java.io.File;
import java.io.IOException;

import jNab.core.choreography.Choreography;
import jNab.core.events.*;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;
import jNab.ext.persistency.Serializer;

public class RFID_Plugin extends AbstractPlugin implements RFIDEventListener {
    private static String PLUGIN_NAME = "RFID_Plugin";
    private static String[] PARAMETERS = {};

    public RFID_Plugin() {
        super(PLUGIN_NAME, PARAMETERS);
    }

    @Override
    public void onRfid(String rfid) {
        switch(rfid){
            case "d0021a0353184691":
                //Change belly colours.
            	System.out.println("Red");
            	Choreography choreography = new Choreography("Colours");
            	choreography.addTempoCommand(0, 100);
            	choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
            	choreography.addLedColorCommand(0, Choreography.LED_CENTER, 255, 255, 0);
            	choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 0, 255);
            	choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 255, 0);
            	choreography.addLedColorCommand(0, Choreography.LED_NOSE, 128, 0, 128);
            	
            	Serializer serializer = new Serializer(new File("./files"));
            	try
            	{
            		System.out.println("Trying to save.");
            	    serializer.saveChoreography(choreography);
            	    System.out.println("Saved.");
            	}
            	catch (IOException e)
            	{
            	    System.out.println("unable to save choreography ("+e.getMessage()+")");
            	    return;
            	}
            	
            	Packet p = new Packet();
            	p.addBlock(new PingIntervalBlock(20));
            	MessageBlock mb = new MessageBlock(12345);
            	mb.addPlayChoreographyFromLibraryCommand("Colours");
            	mb.addWaitPreviousEndCommand();
            	p.addBlock(mb);
            	this.bunny.addPacket(p);
            	
                break; //Red
            case "d0021a053b462c56":
                System.out.println("Green");
                break; //Green
            case "d0021a053b463984":
                break; //Pink
            case "d0021a053b452c90":
                break; //Orange
            case "d0021a053b4240ca":
                break; //Yellow

        }
    }
}


