package jNab.core.choreography;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jNab.core.choreography.Choreography;
import jNab.core.choreography.ChoreographyLibrary;
import jNab.core.exceptions.NoSuchChoreographyException;

public class ChoreographyLibraryTest {
	private String testName = "The Greatest";
	private Choreography testC;
	private ChoreographyLibrary testLib;
	
	@Before
	public void initialize(){
		testLib = new ChoreographyLibrary();
		testC = new Choreography(testName);
	}
	
	@Test
	public void testRegisterEnUnregisterChoreography() throws NoSuchChoreographyException{
		testLib.registerChoreography(testC);
		assertFalse(testLib.getChoreographies().isEmpty());
		testLib.unregisterChoreography(testC);
		assertTrue(testLib.getChoreographies().isEmpty());
	}
	
	@Test
	public void testGetChoreography() throws NoSuchChoreographyException{
		testLib.registerChoreography(testC);
		assertEquals(testLib.getChoreography(testName), testC);
	}

	@Test
	public void testGetChoreographies(){
		testLib.registerChoreography(testC);
		assertEquals(testLib.getChoreographies().size(), 1);
	}
}
