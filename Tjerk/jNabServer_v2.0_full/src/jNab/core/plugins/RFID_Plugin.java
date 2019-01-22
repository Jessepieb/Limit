package jNab.core.plugins;

import java.io.*;
import java.nio.file.*;

import jNab.core.choreography.*;
import jNab.core.events.*;
import jNab.core.protocol.*;
import jNab.ext.helperPlugins.*;
import jNab.ext.persistency.Serializer;

//RFID_Plugin
//Can read RFID tags and record voice.
//./bin/files/plugins/RFID_Plugin.jar

public class RFID_Plugin extends AbstractPlugin implements RFIDEventListener, RecordEventListener, ClickEventListener {
    private static String PLUGIN_NAME = "RFID_Plugin";
    private static String[] PARAMETERS = {};
    
    private static String lastTag = "";
    private static int mode = 0; // 0=Bunny, 1=Audio
    private static Path src, dst;
    private static Packet p;
	private static MessageBlock mb;
	private static AbstractPlugin voiceAssistant = new SystemOutLoggerPlugin();

    public RFID_Plugin() {
        super(PLUGIN_NAME, PARAMETERS);
    }

    @Override
    public void onRfid(String rfid) {
    	Choreography choreography;
    	lastTag = rfid;
    	
        switch(rfid){
            case "d0021a0353184691":
                //Change belly colours.
            	System.out.println("Red");
            	if(mode == 0) {
            		choreography = new Choreography("Colours");
                	choreography.addTempoCommand(0, 100);
                	choreography.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
                	choreography.addLedColorCommand(0, Choreography.LED_CENTER, 128,0, 128);
                	choreography.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 0, 255);
                	choreography.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
                	choreography.addLedColorCommand(0, Choreography.LED_NOSE, 0, 255, 0);
                	createChoreography(choreography);
                	
            		mb = new MessageBlock(12345);
            		mb.addPlayChoreographyFromLibraryCommand("Colours");
            		mb.addWaitPreviousEndCommand();
                	choreographyPacket(mb);
            	} else if (mode == 1) {
            		rfidAudio(rfid);
            	}
                break; //Red
                
            case "d0021a053b462c56":
                System.out.println("Green");
                if(mode == 0) {
                	/*choreography = new Choreography("Ears");
                	choreography.addRelativeEarMoveCommand(0, Choreography.EAR_LEFT, 1);          
                	choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_RIGHT, 5, Choreography.DIRECTION_BACKWARD);
                	createChorography(choreography);
                	*/
                	//String[] choreographies = { "Ears" };
                	//choreographyPacket(choreographies);
                	
                	this.bunny.removePlugin(voiceAssistant);
                	voiceAssistant = new EchoPlugin();
                	this.bunny.addPlugin(voiceAssistant);
                } else if(mode == 1) {
                	rfidAudio(rfid);
                }
                break; //Green
                
            case "d0021a053b463984":
				System.out.println("Pink");
				if (mode == 0) {
					//Something
					choreography = new Choreography("Ears_move");
					choreography.addTempoCommand(0, 100);
					choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_LEFT, 10, Choreography.DIRECTION_FORWARD);
					choreography.addAbsoluteEarMoveCommand(0, Choreography.EAR_RIGHT, 10, Choreography.DIRECTION_FORWARD);
					createChoreography(choreography);
					
					mb = new MessageBlock(12345);
            		mb.addPlayChoreographyFromLibraryCommand("Colours");
            		mb.addWaitPreviousEndCommand();
            		mb.addPlayChoreographyFromLibraryCommand("Ears");
            		mb.addWaitPreviousEndCommand();
                	choreographyPacket(mb);
				} else if(mode == 1) {
					rfidAudio(rfid);
				}
                break; //Pink
                
            case "d0021a053b452c90":
            	System.out.println("Orange");
            	if(mode == 0) {
            		this.bunny.removePlugin(voiceAssistant);
                	voiceAssistant = new DicePlugin();
                	this.bunny.addPlugin(voiceAssistant);
            		//String[] choreographies = { "Colours", "Ears"};
            		//choreographyPacket(choreographies);
            	} else if(mode == 1) {
            		rfidAudio(rfid);
            	}
                break; //Orange
                
            case "d0021a053b4240ca":
            	System.out.println("Yellow");
            	if(mode == 0) {
            		mb = new MessageBlock(12345);
            		mb.addPlayChoreographyFromLibraryCommand("Ears");
            		mb.addWaitPreviousEndCommand();
            		mb.addPlayChoreographyFromLibraryCommand("Colours");
            		mb.addWaitPreviousEndCommand();
                	choreographyPacket(mb);
            	} else if(mode == 1) {
            		rfidAudio(rfid);
            	}
                break; //Yellow
        }
    }
    
    public void choreographyPacket(MessageBlock mb) {
    	p = new Packet();
    	p.addBlock(new PingIntervalBlock(20));
    	p.addBlock(mb);
    	this.bunny.addPacket(p);
    	
    }
    
    public void createChoreography(Choreography c) {
    	Serializer serializer = new Serializer(new File("./files"));
    	try
    	{
    		System.out.println("Trying to save.");
    	    serializer.saveChoreography(c);
    	    System.out.println("Saved.");
    	}
    	catch (IOException e)
    	{
    	    System.out.println("unable to save choreography ("+e.getMessage()+")");
    	    return;
    	}
    }
    
    public void rfidAudio(String rfid_tag) {
    	src = Paths.get("./rfid" + rfid_tag + this.bunny.getSerialNumber() + ".wav");
    	dst = Paths.get("./input.wav");
		try {
			Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Unable to parse audiofile: " + e.getMessage());
		}
    }

	@Override
	public void onSimpleRecord(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoubleRecord(byte[] data) {
		// TODO Auto-generated method stub
		try
		{
		    FileOutputStream fos = new FileOutputStream("rfid" + lastTag + this.bunny.getSerialNumber() + ".wav");
		    for (int element : data)
			fos.write(element);
		    fos.close();
		}
		catch (IOException e)
		{
		    return;
		}
	}

	@Override
	public void onSingleClick() {
		if(mode == 0) {
			mode = 1;
		} else if (mode == 1) {
			mode = 0;
		}
		for(int i = 0; i < 11; i++) {
			System.out.println(mode);
		}
	}

	@Override
	public void onDoubleClick() {
		mode = 0;
		try {
			voiceAssistant = new DicePlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		try {
			voiceAssistant = new EchoPlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		try {
			voiceAssistant = new GreenOrangeRedWithEarsPlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		try {
			voiceAssistant = new NullPlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		try {
			voiceAssistant = new SystemOutLoggerPlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		try {
			voiceAssistant = new TalkiePlugin();
			this.bunny.removePlugin(voiceAssistant);
		}
		catch (Error e) {
			System.out.println(e);
		}
		
	}
}