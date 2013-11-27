package io.netty.child.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Echoes back any received data from a client.
 */
public class EchoServer {

	private final int port;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public void run() throws Exception {
		// Configure the Server.
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap sb = new ServerBootstrap();
			sb.group(bossGroup, workerGroup)
			  .channel(NioServerSocketChannel.class)
			  .option(ChannelOption.SO_BACKLOG, 100)
			  .handler(new LoggingHandler(LogLevel.INFO))
			  .childHandler(new ChannelInitializer<SocketChannel>() {
				 @Override
				 public void initChannel(SocketChannel ch) throws Exception {
					 ch.pipeline().addLast(
							 // new LoggingHandler(LogLevel.INFO),
							 new EchoServerHandler());
				 }
			  });
			
			// Start the server.
			ChannelFuture cf = sb.bind(port).sync();
			
			// Wait until the server socket is closed.
			cf.channel().closeFuture().sync();
		}
		finally {
			// SHut down all event loops to terminate all threads.
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port;
		
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		}
		else {
			port = 9090;
		}
		
		new EchoServer(port).run();
	}
}
