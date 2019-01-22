package jNab.core.choreography;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import jNab.core.choreography.Choreography;
import jNab.core.choreography.ChoreographyLibrary;

public class ChoreographyTest {
	private String testName = "The Greatest";
	private Choreography testC;
	private ChoreographyLibrary testLib;
	
	@Before
	public void initialize(){
		testC = new Choreography(testName);
		testLib = mock(ChoreographyLibrary.class);
	}
	
	@Test
	public void testFinalVars(){
		assertEquals(Choreography.LED_BOTTOM, (byte) 0x00);
		assertEquals(Choreography.LED_LEFT, (byte) 0x01);
		assertEquals(Choreography.LED_CENTER, (byte) 0x02);
		assertEquals(Choreography.LED_RIGHT, (byte) 0x03);
		assertEquals(Choreography.LED_NOSE, (byte) 0x04);
		assertEquals(Choreography.EAR_RIGHT, (byte) 0x00);
		assertEquals(Choreography.EAR_LEFT, (byte) 0x01);
		assertEquals(Choreography.DIRECTION_FORWARD, 0x00);
		assertEquals(Choreography.DIRECTION_BACKWARD, 0x01);
	}

	@Test
	public void testGetName(){		
		assertEquals(testC.getName(), testName);
	}
	
	@Test
	public void testGetEnSetData(){
		byte[] testData = new byte[]{0x00};
		testC.setData(testData);
		assertEquals(testC.getData()[0], testData[0]);
	}
	
	@Test
	public void testGetEnSetChoreoGraphyLibrary(){
		testC.setChoreographyLibrary(testLib);
		assertEquals(testC.getChoreographyLibrary(), testLib);
	}
	
	@Test
	public void testAddTempoCommand(){
		int testTime = 5555;
		int testFreq = 4444;
		testC.addTempoCommand(testTime, testFreq);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0x01);
		assertEquals(testC.getData()[2], (byte) testFreq);
	}
	
	@Test
	public void testAddLedColorCommand(){
		int testTime = 5555;
		byte testLed = 0x00;
		int testR = 255;
		int testG = 128;
		int testB = 64;
		testC.addLedColorCommand(testTime, testLed, testR, testG, testB);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0x07);
		assertEquals(testC.getData()[2], testLed);
		assertEquals(testC.getData()[3], (byte) testR);
		assertEquals(testC.getData()[4], (byte) testG);
		assertEquals(testC.getData()[5], (byte) testB);
		assertEquals(testC.getData()[6], 0x00);
		assertEquals(testC.getData()[7], 0x00);
	}
	
	@Test
	public void testAddAbsoluteEarMoveCommand(){
		int testTime = 5555;
		byte testEar = 0x05;
		int testPos = 55;
		byte testDirection = 0x05;
		testC.addAbsoluteEarMoveCommand(testTime, testEar,testPos, testDirection);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0x08);
		assertEquals(testC.getData()[2], testEar);
		assertEquals(testC.getData()[3], (byte) testPos);
		assertEquals(testC.getData()[4], testDirection);
	}
	
	@Test
	public void testAddRelativeEarMoveCommand(){
		int testTime = 5555;
		byte testEar = 0x05;
		int testSteps = 55;
		testC.addRelativeEarMoveCommand(testTime, testEar, testSteps);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0x11);
		assertEquals(testC.getData()[2], testEar);
		assertEquals(testC.getData()[3], (byte) testSteps);
	}
	
	@Test
	public void testAddLedPaletteCommand(){
		int testTime = 5555;
		byte testLed = 0x05;
		int testValue = 5;
		testC.addLedPaletteCommand(testTime, testLed, testValue);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0xE);
		assertEquals(testC.getData()[2], testLed);
		assertEquals(testC.getData()[3], (byte) (240 + testValue));
	}
	
	@Test
	public void testAddPlayRandomMidi(){
		int testTime = 5555;
		testC.addPlayRandomMidi(testTime);
		assertEquals(testC.getData()[0], (byte) testTime);
		assertEquals(testC.getData()[1], 0x10);
	}
	
	@Test
	public void testEquals(){
		Object testObject = mock(Object.class);
		Object testKloon = testC;
		assertFalse(testC.equals(testObject));
		assertTrue(testC.equals(testKloon));
	}
}