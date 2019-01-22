package jNab.core.bunny;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import jNab.core.bunny.Bunny;
import jNab.core.bunny.Burrow;
import jNab.core.exceptions.NoSuchPluginException;
import jNab.core.plugins.AbstractPlugin;
import jNab.core.protocol.Packet;

public class BunnyTest {
	private Bunny testBunny;
	private String testSerial = "0013NABATEST";
	private String testName = "BroerKonijn";
	private AbstractPlugin testPlugin = mock(AbstractPlugin.class);
	
	@Before
	public void initialize(){
		testBunny = new Bunny(testSerial);
		when(testPlugin.getName()).thenReturn("test");
	}
	
	@Test
	public void testFinalVars(){
		assertEquals(Bunny.SIMPLE_PING_EVENT, 0);
		assertEquals(Bunny.DOUBLE_CLICK_EVENT, 1);
		assertEquals(Bunny.END_OF_MESSAGE_EVENT, 2);
		assertEquals(Bunny.SINGLE_CLICK_EVENT, 3);
		assertEquals(Bunny.STOP_EVENT, 5);
		assertEquals(Bunny.EARS_MOVE_EVENT, 8);
	}
	
	@Test
	public void testGetEnSetName(){		
		testBunny.setName(testName);
		assertEquals(testBunny.getName(), testName);
	}
	@Test
	public void testGetSerialNumber(){
		assertEquals(testBunny.getSerialNumber(), testSerial.toLowerCase());
	}
	
	@Test
	public void testGetEnSetBurrow(){
		Burrow testBurrow = new Burrow();
		testBunny.setBurrow(testBurrow);
		assertEquals(testBunny.getBurrow(), testBurrow);
	}
	
	@Test
	public void testAddEnRemovePlugin(){
		testBunny.addPlugin(testPlugin);
		assertFalse(testBunny.getPlugins().isEmpty());
		testBunny.removePlugin(testPlugin);
		assertTrue(testBunny.getPlugins().isEmpty());
	}
	
	@Test
	public void testGetPlugins(){
		Set<AbstractPlugin> testSet = Collections.synchronizedSet(new HashSet<AbstractPlugin>());
		testSet.add(testPlugin);
		testBunny.addPlugin(testPlugin);
		assertEquals(testBunny.getPlugins(), testSet);
	}
	
	@Test
	public void testGetPluginByName() throws NoSuchPluginException{
		testBunny.addPlugin(testPlugin);
		assertEquals(testBunny.getPluginByName("test"), testPlugin);
	}
	
	@Test
	public void testGetEnSetConnectionStatus(){
		assertTrue(testBunny.getConnectionStatus());
		testBunny.setConnectionStatus(false);
		assertFalse(testBunny.getConnectionStatus());
	}
	
	@Test
	public void testGetEnSetPingInterval(){
		int testInterval = 30;
		testBunny.setPingInterval(testInterval);
		assertEquals(testBunny.getPingInterval(), testInterval);
	}
	
	@Test
	public void testGetLastPlayedMessageID(){
		assertEquals(testBunny.getLastPlayedMessageID(), "0");
	}
	
	@Test
	public void testGoToSleepWakeUpEnIsAwaken(){
		testBunny.goToSleep();
		assertFalse(testBunny.isAwaken());
		testBunny.wakeUp();
		assertTrue(testBunny.isAwaken());
	}
	
	@Test
	public void testAddForceEnGetNextPacket(){
		Packet testPacket = new Packet();
		testBunny.addPacket(testPacket);
		testBunny.forcePacket(testPacket);
		assertEquals(testBunny.getNextPacket(), testPacket);
	}
	
	@Test
	public void testEquals(){
		Object testObject = new Object();
		Bunny testKloon = testBunny;
		assertFalse(testBunny.equals(testObject));
		assertTrue(testBunny.equals(testKloon));
		
	}
}