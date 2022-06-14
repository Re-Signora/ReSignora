package work.chiro.game.network.client;

import org.java_websocket.client.WebSocketClient;

import java.net.URI;

public abstract class MyWebsocketClient extends WebSocketClient {
    public MyWebsocketClient(URI serverUri) {
        super(serverUri);
    }
}
