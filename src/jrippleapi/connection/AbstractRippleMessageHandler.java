package jrippleapi.connection;

import java.net.URI;

import org.eclipse.jetty.websocket.api.Session;

public class AbstractRippleMessageHandler {
	WebSocketConnection connection;
	public Session session;
	
	public AbstractRippleMessageHandler(URI serverUrl) throws Exception {
		this.connection = new WebSocketConnection(serverUrl, this);
	}
	
	public void close() throws Exception {
		connection.close();
	}
}
