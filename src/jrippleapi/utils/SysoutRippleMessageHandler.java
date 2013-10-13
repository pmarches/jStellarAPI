package jrippleapi.utils;

import java.io.IOException;

import jrippleapi.connection.AbstractRippleMessageHandler;
import jrippleapi.connection.RippleDaemonConnection;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class SysoutRippleMessageHandler extends AbstractRippleMessageHandler {

	public SysoutRippleMessageHandler() throws Exception {
		super(RippleDaemonConnection.LOCALHOST_SERVER_URL);
	}

	synchronized public void sendString(String string) throws IOException{
		try {
			session.getRemote().sendString(string);
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@OnWebSocketMessage
	synchronized public void onMessage(String msg) {
    	System.out.print(msg);
		notifyAll();
    }
}
