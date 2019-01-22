package jNab.core.misc;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import jNab.core.misc.SerFileNameFilter;

public class SerFileNameFilterTest {
	private SerFileNameFilter testFilter;
	
	@Before
	public void initialize(){
		testFilter = new SerFileNameFilter();
	}
	
	@Test
	public void testAccept(){
		File testDir = new File("/test");
		String goodTestFile = "test.ser";
		String wrongTestFile = "test.txt";
		assertTrue(testFilter.accept(testDir, goodTestFile));
		assertFalse(testFilter.accept(testDir, wrongTestFile));
	}

}
