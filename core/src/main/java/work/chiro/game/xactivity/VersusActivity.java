package work.chiro.game.xactivity;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.dialogue.DialogueBean;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.game.Game;
import work.chiro.game.network.client.MyWebsocketClient;
import work.chiro.game.network.sever.MyWebsocketServer;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XBundle;

public class VersusActivity extends BattleActivity {
    protected LaSignora eSignora;
    protected boolean connectedAsServer = false;
    protected boolean connectedAsClient = false;
    protected MyWebsocketClient client;
    protected MyWebsocketServer server;

    public VersusActivity(Game game) {
        super(game);
    }

    @Override
    protected void onCreate(XBundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eSignora = new LaSignora(new Vec2(RunningConfig.windowWidth * 1. / 4, RunningConfig.windowHeight * 2. / 3), new AbstractAction(null));
        eSignora.getAnimateContainer().setThing(eSignora);
        eSignora.getDynamicCharacterAttributes().setEnemy(true);
        eSignora.normalAttack();
        getGame().getObjectController().setSecondaryTarget(eSignora);
        getGame().addThing(eSignora);
        server = new MyWebsocketServer(new InetSocketAddress("0.0.0.0", RunningConfig.targetServerPort)) {
            @Override
            public void onOpen(WebSocket conn, ClientHandshake handshake) {
                connectedAsServer = true;
                Utils.getLogger().info("Server got new connection: {}, {}", conn, handshake);
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                connectedAsServer = false;
                Utils.getLogger().info("Server lost a connection: {}, {}, {}, {}", conn, code, reason, remote);
            }

            @Override
            public void onMessage(WebSocket conn, String message) {

            }

            @Override
            public void onError(WebSocket conn, Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onStart() {

            }
        };
        server.start();
        Utils.getLogger().warn("Server started on {}", server.getAddress());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!connectedAsServer) {
            TimeManager.timePause();
            Utils.getLogger().info("pause");
            dialogue.setDialogueManager(new DialogueManager() {
                @Override
                public DialogueBean getDialogue() {
                    return new DialogueBean("等待连接…… as " + (RunningConfig.targetServerHost != null ? "Client" : "Server"));
                }
            });
            MyThreadFactory.getInstance().newThread(() -> {
                dialogue.setVisible(true);
                if (RunningConfig.targetServerHost != null) {
                    // client
                    try {
                        client = new MyWebsocketClient(new URI("ws://" + RunningConfig.targetServerHost + ":" + RunningConfig.targetServerPort)) {
                            @Override
                            public void onOpen(ServerHandshake handshakeData) {
                                connectedAsClient = true;
                                Utils.getLogger().warn("Client open done {}", handshakeData);
                            }

                            @Override
                            public void onMessage(String message) {
                            }

                            @Override
                            public void onClose(int code, String reason, boolean remote) {
                                connectedAsClient = false;
                            }

                            @Override
                            public void onError(Exception ex) {

                            }
                        };
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    while (!connectedAsClient) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                } else {
                    // server
                    while (!connectedAsServer) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                dialogue.setDialogueManager(new DialogueManager() {
                    @Override
                    public DialogueBean getDialogue() {
                        return new DialogueBean("连接成功");
                    }
                });
                dialogue.setOnNextText((xView, xEvent) -> dialogue.setVisible(false));
                MyThreadFactory.getInstance().newThread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dialogue.setVisible(false);
                    TimeManager.timeResume();
                }).start();
            }).start();
        }
    }

    @Override
    protected void onFrame() {
        super.onFrame();
        if (eSignora != null) {
            eSignora.setPosition(new Vec2(RunningConfig.windowWidth - signora.getLocationX(), signora.getLocationY()));
            eSignora.setFlipped(!signora.isFlipped());
        }
    }
}
