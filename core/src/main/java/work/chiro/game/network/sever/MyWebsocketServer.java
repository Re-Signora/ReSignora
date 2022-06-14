package work.chiro.game.network.sever;

import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public abstract class MyWebsocketServer extends WebSocketServer {
    public MyWebsocketServer(InetSocketAddress address) {
        super(address);
    }
}
