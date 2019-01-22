package jNab.core.protocol;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import jNab.core.protocol.Block;
import jNab.core.protocol.Packet;

public class PacketTest {
	private Packet testPacket;
	private byte[] testData;
	
	@Before
	public void initialize(){
		testData = new byte[0];
		testPacket = new Packet(testData);
	}
	
	@Test
	public void testIsAmbientBlockEnPingBlockPresent(){
		assertFalse(testPacket.isAmbientBlockPresent());
		assertFalse(testPacket.isPingBlockPresent());
		testPacket.setPingIntervalBlock(0);
		assertTrue(testPacket.isPingBlockPresent());
		testPacket.addBlock(new Block((byte) 0x04, 0, new byte[0]));
		assertTrue(testPacket.isAmbientBlockPresent());
	}
	
	@Test
	public void testAddBlock(){
		Block testBlock = new Block((byte) 0x03, 0, new byte[0]);
		testPacket.addBlock(testBlock);
		assertTrue(testPacket.isPingBlockPresent());
	}
}
