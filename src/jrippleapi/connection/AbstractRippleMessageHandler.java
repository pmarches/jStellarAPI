package jrippleapi.connection;

import org.eclipse.jetty.websocket.api.Session;

public class AbstractRippleMessageHandler {
	WebSocketConnection connection;
	public Session session;
	
	public AbstractRippleMessageHandler() throws Exception {
		this.connection = new WebSocketConnection(RippleConnection.RIPPLE_SERVER_URL, this);
	}
	
	public void close() throws Exception {
		connection.close();
	}
}
