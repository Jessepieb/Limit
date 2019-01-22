/*
 * EarPlugin.java
 *
 * Created on 12. syyskuuta 2008, 10:25
 *
 */

import jNab.core.server.*;
import jNab.core.protocol.*;
import jNab.core.plugins.*;
import jNab.core.bunny.*;
import jNab.core.events.*;
import jNab.core.choreography.*;
import jNab.core.exceptions.*;

/**
 * A small example plugin for testing.
 *
 * @author jh
 */
public class EarPlugin extends AbstractPlugin implements ClickEventListener {

	String EAR_CHOREOGRAPHY_NAME = "earMove";

	public EarPlugin(ChoreographyLibrary lib) {
		super("Ear_plugin", new String[] {});

		// create a choreography, i.e, an ear movement and led colour sequence
		Choreography chor = new Choreography(EAR_CHOREOGRAPHY_NAME);
		chor.addTempoCommand(0, 30); // sets the length of the time unit
		chor.addAbsoluteEarMoveCommand(1, Choreography.EAR_LEFT, 8, Choreography.DIRECTION_FORWARD);
		chor.addRelativeEarMoveCommand(1, Choreography.EAR_RIGHT, 4);

		// store the choreography into library for future use
		try {
			lib.getChoreography(chor.getName());
		} catch (NoSuchChoreographyException e) {
			lib.registerChoreography(chor);
		}
	}

	public void onSingleClick() {
		System.out.println("ON SINGLE CLICK");
		MessageBlock smb = new MessageBlock(600);
		smb.addPlayChoreographyFromLibraryCommand(EAR_CHOREOGRAPHY_NAME);
		Packet p = new Packet();
		p.addBlock(smb);
		this.bunny.addPacket(p);
	}

	public void onDoubleClick() {
		System.out.println("ON DOUBLE CLICK");
		PingIntervalBlock pib = new PingIntervalBlock(1); // zero is acceptable value and makes the pinging interval pretty fast, however, the Nabaztag seems to get a bit unreliable (push to talk fails often)
		Packet p = new Packet();
		p.addBlock(pib);
		this.bunny.addPacket(p);
	}

}
