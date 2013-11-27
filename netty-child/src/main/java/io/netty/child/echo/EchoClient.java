package io.netty.child.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Sends one message when a connection is open and echoes back any received data to the server.
 * Simply put, the echo client initiates the ping-pong traffic between the echo client and server
 * by sending the first message to the server.
 */
public class EchoClient {

	private final String host;
	private final int port;
	private final int firstMessageSize;
	
	public EchoClient(String host, int port, int firstMessageSize) {
		this.host = host;
		this.port = port;
		this.firstMessageSize = firstMessageSize;
	}
	
	public void run() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			Bootstrap bs = new Bootstrap();
			bs.group(group)
			  .channel(NioSocketChannel.class)
			  .option(ChannelOption.TCP_NODELAY, true)
			  .handler(new ChannelInitializer<SocketChannel>() {
				  @Override
				  public void initChannel(SocketChannel ch) throws Exception {
					  ch.pipeline().addLast(new EchoClientHandler(firstMessageSize));
				  }
			});
			
			// Start the client.
			ChannelFuture cf = bs.connect(host, port).sync();
			// Wait until the connection is closed.
			cf.channel().closeFuture().sync();
		}
		finally {
			// Shut down the event loop to terminate all threads.
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		/*if(args.length < 2 || args.length > 3) {
			System.err.println("Usage : " + EchoClient.class.getSimpleName() +
					" <host> <port> [<first message size]");
			return;
		}*/
		
		final String host = "localhost";//args[0];
		final int port = 9090; //Integer.parseInt(args[1]);
		final int firstMessageSize;
		if(args.length == 3) {
			firstMessageSize = Integer.parseInt(args[2]);
		}
		else {
			firstMessageSize = 256;
		}
		
		new EchoClient(host, port, firstMessageSize).run();
	}
}
