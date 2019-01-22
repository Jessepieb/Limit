package jNab.core.protocol;

import org.junit.Before;
import org.junit.Test;
import jNab.core.protocol.Block;
import static org.junit.Assert.*;

public class BlockTest {
	private Block testBlock;
	private byte testType;
	private int testSize;
	private byte[] testData;
	
	@Before
	public void initialize(){
		testType = (byte) 0x00;
		testSize = 0;
		testData = new byte[0];
		testBlock = new Block(testType, testSize, testData);
	}
	
	@Test
	public void testFinalVars(){
		assertEquals(Block.PING_INTERVAL_BLOCK_TYPE, (byte) 0x03);
		assertEquals(Block.AMBIENT_BLOCK_TYPE, (byte) 0x04);
		assertEquals(Block.REBOOT_BLOCK_TYPE, (byte) 0x09);
		assertEquals(Block.MESSAGE_BLOCK_TYPE, (byte) 0x0A);
	}
	
	@Test
	public void testGetMethods(){
		assertEquals(testBlock.getType(), testType);
		assertEquals(testBlock.getSize(), testSize);
		assertEquals(testBlock.getData(), testData);
	}
	
	@Test
	public void testToString(){
		String testString = "[Data block type: " + testType + " size: " 
				+ testSize + "]\n" + "[Raw data: ]\n";
		assertEquals(testBlock.toString(), testString);
	}
}
