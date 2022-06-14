package work.chiro.game.test;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;

import work.chiro.game.compatible.ResourceProviderPC;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.logic.attributes.loadable.BasicCharacterAttributes;
import work.chiro.game.network.client.MyWebsocketClient;
import work.chiro.game.network.sever.MyWebsocketServer;
import work.chiro.game.utils.Utils;
import work.chiro.game.x.compatible.ResourceProvider;
import work.chiro.game.x.ui.layout.XLayout;
import work.chiro.game.x.ui.layout.XLayoutBuilder;
import work.chiro.game.x.ui.layout.XLayoutManager;

public class PCTest {
    PCTest() {
        ResourceProvider.setInstance(new ResourceProviderPC());
    }

    @Test
    void testLayoutBuilder() {
        // XLayoutManager layoutManager = XLayoutManager.getInstance();
        XLayoutManager layoutManager = new XLayoutManager();
        XLayoutBuilder builder = new XLayoutBuilder(layoutManager, "main");
        XLayout layout = builder.build();
        Utils.getLogger().info("layout: {}", layout);
    }

    @Test
    void testLoadAttributes() {
        try {
            BasicCharacterAttributes characterBasicAttributes = ResourceProvider.getInstance().getAttributesFromResource("la-signora", BasicCharacterAttributes.class, "characters");
            Utils.getLogger().info("characterBasicAttributes: {}", characterBasicAttributes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void networkConnectTest() throws URISyntaxException {
        MyWebsocketServer server = new MyWebsocketServer(new InetSocketAddress("0.0.0.0", RunningConfig.targetServerPort)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                Utils.getLogger().info("server opened");
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                Utils.getLogger().info("server closed");
            }

            @Override
            public void onMessage(WebSocket conn, String message) {

            }

            @Override
            public void onError(WebSocket conn, Exception ex) {

            }

            @Override
            public void onStart() {

            }
        };
        final boolean[] connected = {false};
        server.start();
        MyWebsocketClient client = new MyWebsocketClient(new URI("ws://127.0.0.1:" + RunningConfig.targetServerPort), new Draft_6455()) {
            @Override
            public void onOpen(ServerHandshake handshakeData) {
                Utils.getLogger().info("client opened");
                connected[0] = true;
                PCTest.class.notify();
            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Utils.getLogger().info("client closed");
            }

            @Override
            public void onError(Exception ex) {

            }
        };
        client.connect();
        try {
            synchronized (PCTest.class) {
                PCTest.class.wait(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (connected[0]) Utils.getLogger().info("passed");
        else Utils.getLogger().error("timeout");
    }
}
