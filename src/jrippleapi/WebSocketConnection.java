package jrippleapi;

import java.net.URI;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class WebSocketConnection {
	WebSocketClient wsclient = new WebSocketClient(new SslContextFactory());
	
    public WebSocketConnection(URI serverURI, AbstractRippleMessageHandler msgHandler) throws Exception {
    	wsclient.start();
    	msgHandler.session=wsclient.connect(msgHandler, serverURI, new ClientUpgradeRequest()).get();
	}

    public void close() throws Exception{
    	wsclient.stop();
    }

}
