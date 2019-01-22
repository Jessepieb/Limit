package jNab.core.misc;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import jNab.core.misc.Couple;

public class CoupleTest {
	private Couple testCouple;
	
	@Before
	public void initialize(){
		testCouple = new Couple("", "");
	}
	
	@Test
	public void testGetEnSetFirstElement(){
		String testString = "The Greatest";
		testCouple.setFirstElement(testString);
		assertEquals(testCouple.getFirstElement(), testString);
	}
	
	@Test
	public void testGetEnSetSecondElement(){
		String testString = "Of All Time";
		testCouple.setSecondElement(testString);
		assertEquals(testCouple.getSecondElement(), testString);
	}
}
