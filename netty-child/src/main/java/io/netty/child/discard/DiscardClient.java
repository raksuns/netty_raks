package io.netty.child.discard;

/**
 * Keeps sending random data to the specified address.
 */
public class DiscardClient {
	private final String host;
	private final int port;
	private final int firstMessageSize;
	
	public DiscardClient(String host, int port, int firstMessageSize) {
		this.host = host;
		this.port = port;
		this.firstMessageSize = firstMessageSize;
	}
	
	public void run() throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		
	}
}
