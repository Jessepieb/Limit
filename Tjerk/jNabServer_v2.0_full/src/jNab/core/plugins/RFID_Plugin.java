package jNab.core.plugins;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;

import jNab.core.choreography.*;
import jNab.core.events.*;
import jNab.core.protocol.*;
import jNab.ext.helperPlugins.*;
import jNab.ext.persistency.Serializer;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

//RFID_Plugin
//Can read RFID tags and record voice.
//./bin/files/plugins/RFID_Plugin.jar

public class RFID_Plugin extends AbstractPlugin implements RFIDEventListener, RecordEventListener, ClickEventListener {
    private static String PLUGIN_NAME = "RFID_Plugin";
    private static String[] PARAMETERS = {};
    
    private static int channelID = 2;
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
                	createChoreography(choreography);
                	*/
                	//String[] choreographies = { "Ears" };
                	//choreographyPacket(choreographies);
                	
                	/* Spinning ears only
                	choreography = new Choreography("Spin_Ears");
                	choreography.addTempoCommand(0,100);
                	for(int i = 0; i < 200; i++){
                		choreography.addRelativeEarMoveCommand(1, Choreography.EAR_LEFT, 12);
                    	choreography.addRelativeEarMoveCommand(0, Choreography.EAR_RIGHT, 12);
                	}
                	createChoreography(choreography); */
                	
                	choreography = new Choreography("Disco");
                	choreography.addTempoCommand(0, 10); //100 = 1sec
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
                	createChoreography(choreography);
                	
                	mb = new MessageBlock(12345);
                	mb.addPlayLocalSoundCommand("./Not_The_Limit_2.mp3");
                	mb.addPlayChoreographyFromLibraryCommand("Disco");
            		//mb.addPlayChoreographyFromLibraryCommand("Spin_Ears");
            		mb.addWaitPreviousEndCommand();
                	choreographyPacket(mb);
                	
                	/*this.bunny.removePlugin(voiceAssistant);
                	voiceAssistant = new EchoPlugin();
                	this.bunny.addPlugin(voiceAssistant);*/
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
            		mb = new MessageBlock(54321);
            		
            		
            		
            		//mb.addPlayStreamCommand("https://tunein.com/station/?stationid=30377&amp;utm_medium=referral&amp;utm_content=s30377&amp;utm_source=geminiEmbedArt&amp;st=52");
            		//mb.addPlayStreamCommand("http://185.85.28.166:8000");
            		
            		mb.addPlayStreamCommand("broadcast/http://stream-uk1.radioparadise.com/mp3-32/;");
            		//mb.addPlayStreamCommand("broadcast/files/streams/rockchannel.m3u");
            		
            		//mb.addPlayStreamCommand("192.168.0.100:1234"); //Streamen vanaf VLC?
            		//mb.addPlayStreamCommand("http://tunein.com/embed/player/s30377/");
            		//http://tun.in/seqZ1
            		//https://tunein.com/radio/Rock-92-923-s30377/
            		
            		//mb.addWaitPreviousEndCommand();
            		//mb.addPlayChoreographyFromLibraryCommand("Ears");
            		//mb.addWaitPreviousEndCommand();
            		//mb.addPlayChoreographyFromLibraryCommand("Colours");
            		
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
    	p.addBlock(new PingIntervalBlock(1));
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
	
	/*
	
	private static String[] channel = {
	        "http://radio.flex.ru:8000/radionami",
	        "http://stream-uk1.radioparadise.com/mp3-32",
	        "https://stream.gal.io/arrow"
	    };
	    
	private static void removeSecurity() {
	        for (int i = 0; i < channel.length; i++) {
	            if(channel[i].substring(0,5).equals("https")) {
	                channel[i] = "http" + channel[i].substring(5);
	            }
	        }
	    }
	
	private static void playRadioStream ( String spec ) throws IOException, JavaLayerException
    {
        // Connection
        URLConnection urlConnection = new URL ( spec ).openConnection ();

        // If you have proxy
        //        Properties systemSettings = System.getProperties ();
        //        systemSettings.put ( "proxySet", true );
        //        systemSettings.put ( "http.proxyHost", "host" );
        //        systemSettings.put ( "http.proxyPort", "port" );
        // If you have proxy auth
        //        BASE64Encoder encoder = new BASE64Encoder ();
        //        String encoded = encoder.encode ( ( "login:pass" ).getBytes () );
        //        urlConnection.setRequestProperty ( "Proxy-Authorization", "Basic " + encoded );

        // Connecting
        urlConnection.connect ();

        // Playing
        System.out.println("Playing");
        Player player = new Player ( urlConnection.getInputStream () );
        player.play ();
    }

	@Override
	public void onSingleClick() {
	        
		 //Possible if it works: loadChannelsFromFIle();
        removeSecurity();
        
        try
        {
            playRadioStream ( channel[channelID] );
        }
        catch ( IOException e )
        {
            e.printStackTrace ();
        }
        catch ( JavaLayerException e )
        {
            e.printStackTrace ();
        }
	}

	@Override
	public void onDoubleClick() {
		// TODO Auto-generated method stub
		if(channelID == channel.length) {
			channelID = 0;
		} else {
			channelID++;
		}
	}
	
	*/
	
	

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