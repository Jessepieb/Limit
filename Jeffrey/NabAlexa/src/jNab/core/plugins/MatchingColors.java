package jNab.core.plugins;

import java.io.File;
import java.io.IOException;

import jNab.core.choreography.Choreography;
import jNab.core.events.RFIDEventListener;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;
import jNab.ext.persistency.Serializer;

public class MatchingColors extends AbstractPlugin implements RFIDEventListener{
    private static String PLUGIN_NAME = "MatchingColors_Plugin";
    private static String[] PARAMETERS = {};

	public MatchingColors() {
		super(PLUGIN_NAME, PARAMETERS);
	}

	@Override
	public void onRfid(String rfid) {
	    Serializer s;
		Packet p;
		MessageBlock mb;
		switch(rfid){
			case "d0021a0353184691" : System.out.println("Red");
									  Choreography cr = new Choreography("Tag_Red");
									  cr.addTempoCommand(0, 100);
									  cr.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 0);
									  cr.addLedColorCommand(0, Choreography.LED_CENTER, 255, 0, 0);
									  cr.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 0);
									  cr.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 0, 0);
									  cr.addLedColorCommand(0, Choreography.LED_NOSE, 255, 0, 0);
									  cr.addPlayRandomMidi(0);
									  s = new Serializer(new File("./files"));
									  try {
										  s.saveChoreography(cr);
										} catch (IOException e) {
										  // TODO Auto-generated catch block
										  e.printStackTrace();
									  }
									  p = new Packet();
									  p.addBlock(new PingIntervalBlock(20));
									  mb = new MessageBlock(12345);
									  mb.addPlayChoreographyFromLibraryCommand(cr.getName());
									  mb.addWaitPreviousEndCommand();
									  p.addBlock(mb);
									  this.bunny.addPacket(p);
					  	  			  break;
			case "d0021a053b462c56" : System.out.println("Green");
									  Choreography cg = new Choreography("Tag_Green");
									  cg.addTempoCommand(0, 100);
									  cg.addLedColorCommand(0, Choreography.LED_BOTTOM, 0, 153, 0);
									  cg.addLedColorCommand(0, Choreography.LED_CENTER, 0, 153, 0);
									  cg.addLedColorCommand(0, Choreography.LED_LEFT, 0, 153, 0);
									  cg.addLedColorCommand(0, Choreography.LED_RIGHT, 0, 153, 0);
									  cg.addLedColorCommand(0, Choreography.LED_NOSE, 0, 153, 0);
									  cg.addPlayRandomMidi(0);
									  s = new Serializer(new File("./files"));
									  try {
										  s.saveChoreography(cg);
										} catch (IOException e) {
										  // TODO Auto-generated catch block
										  e.printStackTrace();
									  }
									  p = new Packet();
									  p.addBlock(new PingIntervalBlock(20));
									  mb = new MessageBlock(12345);
									  mb.addPlayChoreographyFromLibraryCommand(cg.getName());
									  mb.addWaitPreviousEndCommand();
									  p.addBlock(mb);
									  this.bunny.addPacket(p);
			               		      break;
			case "d0021a053b463984" : System.out.println("Pink");
									  Choreography cp = new Choreography("Tag_Pink");
									  cp.addTempoCommand(0, 100);
									  cp.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 0, 127);
									  cp.addLedColorCommand(0, Choreography.LED_CENTER, 255, 0, 127);
									  cp.addLedColorCommand(0, Choreography.LED_LEFT, 255, 0, 127);
									  cp.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 0, 127);
									  cp.addLedColorCommand(0, Choreography.LED_NOSE, 255, 0, 127);
									  cp.addPlayRandomMidi(0);
									  s = new Serializer(new File("./files"));
									  try {
										  s.saveChoreography(cp);
										} catch (IOException e) {
										  // TODO Auto-generated catch block
										  e.printStackTrace();
									  }
									  p = new Packet();
									  p.addBlock(new PingIntervalBlock(20));
									  mb = new MessageBlock(12345);
									  mb.addPlayChoreographyFromLibraryCommand(cp.getName());
									  mb.addWaitPreviousEndCommand();
									  p.addBlock(mb);
									  this.bunny.addPacket(p);
			  			 			  break;
			case "d0021a053b452c90" : System.out.println("Orange");
									  Choreography co = new Choreography("Tag_Orange");
									  co.addTempoCommand(0, 100);
									  co.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 128, 0);
									  co.addLedColorCommand(0, Choreography.LED_CENTER, 255, 128, 0);
									  co.addLedColorCommand(0, Choreography.LED_LEFT, 255, 128, 0);
									  co.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 128, 0);
									  co.addLedColorCommand(0, Choreography.LED_NOSE, 255, 128, 0);
									  co.addPlayRandomMidi(0);
									  s = new Serializer(new File("./files"));
									  try {
										  s.saveChoreography(co);
										} catch (IOException e) {
										  // TODO Auto-generated catch block
										  e.printStackTrace();
									  }
									  p = new Packet();
									  p.addBlock(new PingIntervalBlock(20));
									  mb = new MessageBlock(12345);
									  mb.addPlayChoreographyFromLibraryCommand(co.getName());
									  mb.addWaitPreviousEndCommand();
									  p.addBlock(mb);
									  this.bunny.addPacket(p);
			  			  			  break;
			case "d0021a053b4240ca" : System.out.println("Yellow");
									  Choreography cy = new Choreography("Tag_Yellow");
									  cy.addTempoCommand(0, 100);
									  cy.addLedColorCommand(0, Choreography.LED_BOTTOM, 255, 255, 0);
								      cy.addLedColorCommand(0, Choreography.LED_CENTER, 255, 255, 0);
								      cy.addLedColorCommand(0, Choreography.LED_LEFT, 255, 255, 0);
								      cy.addLedColorCommand(0, Choreography.LED_RIGHT, 255, 255, 0);
								      cy.addLedColorCommand(0, Choreography.LED_NOSE, 255, 255, 0);
								      cy.addPlayRandomMidi(0);
								      s = new Serializer(new File("./files"));
								      try {
										  s.saveChoreography(cy);
										} catch (IOException e) {
										  // TODO Auto-generated catch block
										  e.printStackTrace();
									  }
								      p = new Packet();
									  p.addBlock(new PingIntervalBlock(20));
									  mb = new MessageBlock(12345);
									  mb.addPlayChoreographyFromLibraryCommand(cy.getName());
									  mb.addWaitPreviousEndCommand();
									  p.addBlock(mb);
									  this.bunny.addPacket(p);
			  			  			  break;
		}
	}
}
