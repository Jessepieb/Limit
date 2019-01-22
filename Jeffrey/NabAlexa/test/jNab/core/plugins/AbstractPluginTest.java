package jNab.core.plugins;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import jNab.core.bunny.Bunny;
import jNab.core.misc.Couple;
import jNab.core.plugins.MatchingColors;

public class AbstractPluginTest {
// AbstractPlugin is een abstracte class. Dit houdt in dat er geen instantie van kan worden aangemaakt.
// Vandaar dat er een instantie van de MatchingColors plugin wordt aangemaakt en doormiddel hiervan worden
// de methodes van de AbstractPlugin class getest.
	MatchingColors testPlugin;
	
	@Before
	public void initialize(){
		testPlugin = new MatchingColors();
	}
	
	@Test
	public void testGetEnSetBunny(){
		Bunny testBunny = new Bunny("0013NABATEST");
		testPlugin.setBunny(testBunny);
		assertEquals(testPlugin.getBunny(), testBunny);
	}
	
	@Test
	public void testParameterMethods(){
		// Het setten van een map van parameters
		String testName = "test";
		Map<String, Couple<Boolean, String>> testParameters = new HashMap<String, Couple<Boolean, String>>();
		Couple<Boolean, String> testParameter = new Couple<Boolean, String>(false, "hoi");
		testParameters.put(testName, testParameter);
		testPlugin.setParameters(testParameters);
		assertEquals(testPlugin.getParameters(), testParameters);
		assertTrue(testPlugin.isParameterValid(testName));
		assertFalse(testPlugin.isParameterValid("Test"));
		assertTrue(testPlugin.setParameter(testName, ""));
		assertEquals(testPlugin.getParameterNames(), testParameters.keySet());
		assertTrue(testPlugin.isParameterSet(testName));
		assertEquals(testPlugin.getParameterValue(testName), testParameter.getSecondElement());
	}	
	
	@Test
	public void testGetName(){
		assertEquals(testPlugin.getName(), "MatchingColors_Plugin");
	}
	
	@Test
	public void testEquals(){
		Object testObject = mock(Object.class);
		Object testKloon = testPlugin;
		assertFalse(testPlugin.equals(testObject));
		assertTrue(testPlugin.equals(testKloon));
	}
}
