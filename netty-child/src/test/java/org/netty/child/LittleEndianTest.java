package org.netty.child;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

public class LittleEndianTest {

	
	@Test
	public void endianTest() {
		ByteBuf buf = Unpooled.buffer(4);
		buf.setInt(0,  1);
		
		System.out.format("%08x%n", buf.getInt(0));
		
		ByteBuf leBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
		System.out.format("%08x%n", leBuf.getInt(0));
		
		assert buf != leBuf;
		assert buf == buf.order(ByteOrder.BIG_ENDIAN);
	}
}
