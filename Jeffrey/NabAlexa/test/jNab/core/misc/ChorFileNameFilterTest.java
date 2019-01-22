package jNab.core.misc;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import jNab.core.misc.ChorFileNameFilter;

public class ChorFileNameFilterTest {
	private ChorFileNameFilter testFilter;
	
	@Before
	public void initialize(){
		testFilter = new ChorFileNameFilter();
	}
	
	@Test
	public void testAccept(){
		File testDir = new File("/test");
		String goodTestFile = "test.chor";
		String wrongTestFile = "test.txt";
		assertTrue(testFilter.accept(testDir, goodTestFile));
		assertFalse(testFilter.accept(testDir, wrongTestFile));
	}	
}
