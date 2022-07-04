package work.chiro.game.xactivity;

import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Locale;

import work.chiro.game.animate.action.AbstractAction;
import work.chiro.game.config.RunningConfig;
import work.chiro.game.dialogue.DialogueBean;
import work.chiro.game.dialogue.DialogueManager;
import work.chiro.game.game.Game;
import work.chiro.game.network.client.MyWebsocketClient;
import work.chiro.game.network.data.DataBean;
import work.chiro.game.network.data.PositionBean;
import work.chiro.game.network.sever.MyWebsocketServer;
import work.chiro.game.objects.thing.character.signora.LaSignora;
import work.chiro.game.utils.Utils;
import work.chiro.game.utils.thread.MyThreadFactory;
import work.chiro.game.utils.timer.TimeManager;
import work.chiro.game.vector.Vec2;
import work.chiro.game.x.activity.XBundle;

@SuppressWarnings({"SwitchStatementWithTooFewBranches", "BusyWait"})
public class VersusActivity extends BattleActivity {
//    我不理解这个报错是怎么来的 ，但是似乎他并不影响运行
    protected LaSignora eSignora;
    protected boolean connectedAsServer = false;
    protected boolean connectedAsClient = false;
    protected MyWebsocketClient client;
    protected MyWebsocketServer server;
    protected WebSocket clientConn = null;

    public VersusActivity(Game game) {
        super(game);
    }

    public void onMessage(String message) {
        Utils.getLogger().debug("server got string: {}", message);
        try {
            DataBean data = new Gson().fromJson(message, DataBean.class);
            if (eSignora != null) {
                switch (data.getCommand()) {
                    case "position":
                        eSignora.setPosition(new Vec2(RunningConfig.windowWidth, 0).minus(new Vec2(data.getPosition().getX(), -data.getPosition().getY())));
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                clientConn = conn;
            }

            @Override
            public void onClose(WebSocket conn, int code, String reason, boolean remote) {
                connectedAsServer = false;
                Utils.getLogger().info("Server lost a connection: {}, {}, {}, {}", conn, code, reason, remote);
                finish();
            }

            @Override
            public void onMessage(WebSocket conn, String message) {
                VersusActivity.this.onMessage(message);
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
                    return new DialogueBean("等待连接…… as " + (RunningConfig.targetServerHost != null ?
                            ("Client" + String.format(Locale.CHINA, "(%s:%d)", RunningConfig.targetServerHost, RunningConfig.targetServerPort)) :
                            ("Server" + String.format(Locale.CHINA, "(%s)", server.getAddress()))));
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
                                VersusActivity.this.onMessage(message);
                            }

                            @Override
                            public void onClose(int code, String reason, boolean remote) {
                                connectedAsClient = false;
                                finish();
                            }

                            @Override
                            public void onError(Exception ex) {

                            }
                        };
                        client.connect();
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

    private static int dataSendCnt = 0;

    @Override
    protected void onFrame() {
        super.onFrame();
        if (eSignora != null) {
            eSignora.setFlipped(!signora.isFlipped());
        }
        if (connectedAsClient || connectedAsServer) {
            if (dataSendCnt == RunningConfig.eventSendDivide - 1) {
                DataBean data = new DataBean();
                data.setCommand("position");
                data.setPosition(new PositionBean(Game.getInstance().getObjectController().getTarget().getPosition()));
                String text = new Gson().toJson(data);
                if (connectedAsServer && clientConn != null) {
                    clientConn.send(text);
                }
                if (connectedAsClient && client != null) {
                    client.send(text);
                }
                dataSendCnt = 0;
            } else {
                dataSendCnt += 1;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (client != null && client.isOpen()) client.close();
        if (server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
