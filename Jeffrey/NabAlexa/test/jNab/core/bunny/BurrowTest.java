package jNab.core.bunny;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

import jNab.core.bunny.Bunny;
import jNab.core.bunny.Burrow;
import jNab.core.exceptions.NoSuchBunnyException;
import jNab.core.server.MicroServer;

public class BurrowTest {
	private Burrow testBurrow;
	private Bunny testBunny = mock(Bunny.class);
	private String testSerial = "0013NABATEST";
	
	@Before
	public void initialize(){
		testBurrow = new Burrow();
		when(testBunny.getSerialNumber()).thenReturn(testSerial);
	}
	
	@Test
	public void testGetEnSetMicroServer(){
		MicroServer testServer = mock(MicroServer.class);
		testBurrow.setMicroServer(testServer);
		assertEquals(testBurrow.getMicroServer(), testServer);
	}
	
	@Test
	public void testAddGetRemoveBunnyGetBunniesEnIsBunnyInBurrow() throws NoSuchBunnyException{
		// De addBunny() methode komt twee keer voor in de Burrow class.
		// Het is mogelijk om zowel op basis van een Bunny instantie als 
		// op basis van een serialnumber een bunny toe te voegen aan de burrow.
		testBurrow.addBunny(testBunny);
		assertEquals(testBurrow.getBunny(testSerial), testBunny);
		assertTrue(testBurrow.isBunnyInBurrow(testBunny));
		testBurrow.removeBunny(testSerial);
		assertTrue(testBurrow.getBunnies().isEmpty());
		testBurrow.addBunny("test");
		assertEquals(testBurrow.getBunny("test").getSerialNumber(), "test");
		assertEquals(testBurrow.getBunnies().size(), 1);
	}
}
