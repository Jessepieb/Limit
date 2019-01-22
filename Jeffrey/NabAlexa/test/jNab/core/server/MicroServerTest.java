package jNab.core.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.PrintStream;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import jNab.core.server.MicroServer;

public class MicroServerTest {
	private MicroServer testServer;
	private String testAddress = "127.0.0.1";
	private int testPort = 9876;
	private File testResPath = new File("/testje");
	private File testPluginPath = new File("/testje2");
	
	@Before
	public void initialize(){
		testServer = new MicroServer(null, 0, null, testPluginPath);
	}
	
	@Test
	public void testIsRunning(){
		assertFalse(testServer.isRunning());
	}
	
	@Test
	public void testGetBurrow(){
		assertThat(testServer.getBurrow(), IsNull.notNullValue());
	}
	
	@Test
	public void testGetPluginFactory(){
		assertThat(testServer.getPluginFactory(), IsNull.notNullValue());
	}
	
	@Test
	public void testGetChoreographyLibrary(){
		assertThat(testServer.getChoregraphyLibrary(), IsNull.notNullValue());
	}
	
	@Test
	public void testGetEnSetResourcesPath(){
		testServer.setResourcePath(testResPath);
		assertEquals(testServer.getResourcesPath(), testResPath);
	}
	
	@Test
	public void testGetAddressGetPortEnBindTo(){
		testServer.bindTo(testAddress, testPort);
		assertEquals(testServer.getAddress(), testAddress);
		assertEquals(testServer.getPort(), testPort);
	}
	
	@Test
	public void testGetEnSetErrorLoggingStream(){
		PrintStream testErrorStream = mock(PrintStream.class);
		testServer.setErrorLoggingStream(testErrorStream);
		assertEquals(testServer.getErrorLoggingStream(), testErrorStream);
	}
	
	@Test
	public void testGetEnSetInfoLoggingStream(){
		PrintStream testInfoStream = mock(PrintStream.class);
		testServer.setInfoLoggingStream(testInfoStream);
		assertEquals(testServer.getInfoLoggingStream(), testInfoStream);
	}
	
	@Test
	public void testGetEnSetInfoDebugLoggingStream(){
		PrintStream testDebugStream = mock(PrintStream.class);
		testServer.setDebugLoggingStream(testDebugStream);
		assertEquals(testServer.getDebugLoggingStream(), testDebugStream);
	}
}