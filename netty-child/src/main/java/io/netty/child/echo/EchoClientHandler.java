package io.netty.child.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handler implementation for the echo client. It initiates the ping-pong traffic 
 * between the echo client and server by sending the first message to the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(EchoClientHandler.class.getName());
	
	private final ByteBuf firstMessage;
	
	/**
	 * Creates a client-side handler.
	 */
	public EchoClientHandler(int firstMessageSize) {
		if(firstMessageSize <= 0) {
			throw new IllegalArgumentException("firstMessageSize : " + firstMessageSize);
		}
		
		firstMessage = Unpooled.buffer(firstMessageSize);
		
		for(int i = 0; i < firstMessage.capacity(); i++) {
			logger.info("write byte : " + i);
			firstMessage.writeByte((byte) i);
		}
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.info("write and flush : " + firstMessage.toString());
		ctx.writeAndFlush(firstMessage);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("channelRead : " + msg.toString());
		ctx.write(msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelReadComplete");
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexptected exception from downstream.", cause);
		ctx.close();
	}
}
