package io.netty.child.discard;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscardServerHandler extends SimpleChannelInboundHandler<Object> {
	
	private static final Logger logger = Logger.getLogger(DiscardServerHandler.class.getName());

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// discard
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.log(Level.WARNING, "Unexpected exception from downstrea.", cause);
		ctx.close();
	}
}
