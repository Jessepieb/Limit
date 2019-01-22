package jNab.core.misc;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import jNab.core.misc.JarFileNameFilter;

public class JarFileNameFilterTest {
	private JarFileNameFilter testFilter;
	
	@Before
	public void initialize(){
		testFilter = new JarFileNameFilter();
	}
	
	@Test
	public void testAccept(){
		File testDir = new File("/test");
		String goodTestFile = "test.jar";
		String wrongTestFile = "test.txt";
		assertTrue(testFilter.accept(testDir, goodTestFile));
		assertFalse(testFilter.accept(testDir, wrongTestFile));
	}
}
