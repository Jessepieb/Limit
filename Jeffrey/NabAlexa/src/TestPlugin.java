import jNab.core.bunny.Bunny;
import jNab.core.events.ClickEventListener;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.MessageBlock;
import jNab.core.protocol.Packet;
import jNab.core.protocol.PingIntervalBlock;

/**
 * Plugin simulating a monitoring. The bunny plays a choreography illustrating the level of what is monitored: - Low
 * level: the three belly leds are blinking at 1Hz, in green, for 20 seconds. Both ears are pointed. - Medium level: the three
 * belly leds are blinking at 1Hz, in orange, for 20 seconds. Left ear is pointed, right ear is floppy. - High level: the three belly
 * leds are blinking at 1Hz, in red, for 20 seconds. Both ears are floppy.
 * 
 * The level of what is monitored is simulated, starting at medium level and switching (medium, high, low, ...) every 60 seconds.
 * 
 * @author Sylvain Gizard
 * @author Sebastien Jean
 */
public class TestPlugin extends AbstractPlugin implements ClickEventListener
{
    /**
     * Plugin name.
     */
    private static String PLUGIN_NAME = "Test_Plugin";

    /**
     * Set of parameters supported by the plugin;
     */
    private static String[] PARAMETERS = {};

    /**
     * Level (0 for low, 1 for medium, 2 for high) of the monitored process.
     */
    private int level;

    /**
     * The last time a level switch occured.
     */
    private long lastSwitchingTime;

    /**
     * Creating a new plugin instance.
     */
    public TestPlugin()
    {
	super(PLUGIN_NAME, PARAMETERS);
	this.level = 1;
	this.lastSwitchingTime = System.currentTimeMillis();
    }

    /**
     * Internal method used to retrieve the name of the choreography associated with the current level.
     * 
     * @return the name of the choreography associated with the current consumption level.
     */
    private String getChoreography()
    {
	switch (this.level)
	{
	case 0:
	    return "GreenBlinkingWithEars";
	case 1:
	    return "OrangeBlinkingWithEars";
	default:
	    return "RedBlinkingWithEars";
	}
    }

    /**
     * Playing the choreography associated to the current level, and switching the level if needed.
     * 
     * @see jNab.core.events.PingEventListener#onPing()
     */
    public void onDoubleClick()
    {
	// Preparing a new packet
	Packet p = new Packet();
	
	// Asking the bunny to ping 20s later (i.e. when the choreography has finished playing)
	p.addBlock(new PingIntervalBlock(20));
	MessageBlock mb = new MessageBlock(12345);
	
	// Playing the choreography associated to current level.
	mb.addPlayChoreographyFromLibraryCommand(this.getChoreography());
	mb.addWaitPreviousEndCommand();
	p.addBlock(mb);
	this.bunny.addPacket(p);
	
	// Switching level if needed
	if (System.currentTimeMillis() - this.lastSwitchingTime > 6000)
	{
	    this.level = (this.level + 1) % 3;
	    this.lastSwitchingTime = System.currentTimeMillis();
	}
    }

    /**
     * @see jNab.core.events.StopEventListener#onEndOfMessage()
     */
    public void onEndOfMessage()
    {
	// When choreography has finished playing, bunny notifies a end-of-message
	// Then it is asked to ping immediately
	Packet p = new Packet();
	p.addBlock(new PingIntervalBlock(1));
	this.bunny.addPacket(p);
    }

    /**
     * @see jNab.core.events.StopEventListener#onSingleClickWhilePlaying()
     */
    public void onSingleClickWhilePlaying()
    {
	// If some click occurs while the choreography is playing, it is handled as a single-click event
	// A choreography is sent to raise both ears and bunny is asked to ping immediately after (to be able to send
	// packets generated by single-click event handling)
	this.bunny.handleEvent(Bunny.SINGLE_CLICK_EVENT, null);
	Packet p = new Packet();
	p.addBlock(new PingIntervalBlock(1));
	MessageBlock mb = new MessageBlock(12345);
	mb.addPlayChoreographyFromLibraryCommand("raisedEars");
	p.addBlock(mb);
	this.bunny.forcePacket(p);
    }

	@Override
	public void onSingleClick() {
		// TODO Auto-generated method stub
		
	}
}
