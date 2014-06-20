package jrippleapi.connection;

import java.net.URI;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class WebSocketConnection {
	WebSocketClient wsclient = new WebSocketClient(new SslContextFactory());
	
    public WebSocketConnection(URI serverURI, RippleDaemonWebsocketConnection msgHandler) throws Exception {
    	wsclient.start();
    	msgHandler.session=wsclient.connect(msgHandler, serverURI, new ClientUpgradeRequest()).get();
    	msgHandler.session.getPolicy().setMaxTextMessageSize(10_000_000);
//		msgHandler.session.getPolicy().setMaxTextMessageBufferSize(Integer.MAX_VALUE);
	}

    public void close() throws Exception{
    	wsclient.stop();
    }

}
