package work.chiro.game.network.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;

import java.net.URI;

public abstract class MyWebsocketClient extends WebSocketClient {
    public MyWebsocketClient(URI serverUri) {
        super(serverUri);
    }

    public MyWebsocketClient(URI serverUri, Draft_6455 draft_6455) {
        super(serverUri, draft_6455);
    }
}
