package io.netty.child.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a client-side channel.
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {
	
	private static final Logger logger = Logger.getLogger(DiscardClientHandler.class.getName());

	private final int messageSize;
	private ByteBuf content;
	private ChannelHandlerContext ctx;
	
	public DiscardClientHandler(int messageSize) {
		if(messageSize <= 0) {
			throw new IllegalArgumentException("messageSize : " + messageSize);
		}
		this.messageSize = messageSize;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.ctx = ctx;
		
		// Initialize the message.
		content = ctx.alloc().directBuffer(messageSize).writeZero(messageSize);
		
		// Send the initial message.
		generateTraffic();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		content.release();
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// Close the connection when an exception is raised.
		logger.log(Level.WARNING, "Unexpected exception from downstream.", cause);
		ctx.close();
	}
	
	long counter;
	
	private void generateTraffic() {
		// Flush the outbound buffer to the socket.
		// Once flushed, generate the same amount of traffic again.
		ctx.writeAndFlush(content.duplicate().retain()).addListener(trafficGenerator);
	}
	
	private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
			if(future.isSuccess()) {
				generateTraffic();
			}
		}
	};
}
