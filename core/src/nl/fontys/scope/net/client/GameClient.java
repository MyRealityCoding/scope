package nl.fontys.scope.net.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import net.engio.mbassy.listener.Handler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.fontys.scope.Config;
import nl.fontys.scope.core.PlayerManager;
import nl.fontys.scope.core.World;
import nl.fontys.scope.event.EventType;
import nl.fontys.scope.event.Events;
import nl.fontys.scope.net.handlers.responses.ClientJoinedHandler;
import nl.fontys.scope.net.handlers.responses.ClientLeftHandler;
import nl.fontys.scope.net.handlers.responses.ClientUpdatedHandler;
import nl.fontys.scope.net.handlers.responses.GameCreatedHandler;
import nl.fontys.scope.net.handlers.responses.GameOverHandler;
import nl.fontys.scope.net.handlers.responses.GameReadyHandler;
import nl.fontys.scope.net.handlers.responses.ObjectAddedHandler;
import nl.fontys.scope.net.handlers.responses.ObjectRemovedHandler;
import nl.fontys.scope.net.handlers.responses.ObjectUpdatedHandler;
import nl.fontys.scope.net.kryo.KryoConfig;
import nl.fontys.scope.net.server.Responses;
import nl.fontys.scope.net.server.Router;
import nl.fontys.scope.object.GameObject;

public class GameClient extends Listener implements Disposable {

    public static class GameClientHandler {

        public void onGameCreated(Responses.GameCreated created) { }

        public void onGameClosed(Responses.GameClosed closed) {}

        public void onClientJoined(Responses.ClientJoined joined) {}

        public void onClientLeft(Responses.ClientLeft left) {}

        public void onGameReady(Responses.GameReady ready) {}

        public void onConnectionFailed() {}
    }

    /* Resolves concurrency and executes everything in the main render thread */
    private GameClientHandler libGdxHandler = new GameClientHandler() {
        @Override
        public void onConnectionFailed() {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onConnectionFailed();
                    }
                }
            });
        }

        @Override
        public void onClientJoined(final Responses.ClientJoined joined) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onClientJoined(joined);
                    }
                }
            });
        }

        @Override
        public void onClientLeft(final Responses.ClientLeft left) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onClientLeft(left);
                    }
                }
            });
        }

        @Override
        public void onGameClosed(final Responses.GameClosed closed) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onGameClosed(closed);
                    }
                }
            });
        }

        @Override
        public void onGameCreated(final Responses.GameCreated created) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onGameCreated(created);
                    }
                }
            });
        }

        @Override
        public void onGameReady(final Responses.GameReady ready) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    for (GameClientHandler handler : listeners) {
                        handler.onGameReady(ready);
                    }
                }
            });
        }
    };

    private Events events;

    private Client client;

    private String gameId;

    private String clientId;

    private Router router;

    private Set<GameClientHandler> listeners;

    private ExecutorService executorService;

    public GameClient(Events events, String gameId, World world, PlayerManager playerManager) {
        executorService = Executors.newSingleThreadExecutor();
        this.listeners = new HashSet<GameClientHandler>();
        this.events = events;
        this.gameId = gameId;
        this.clientId = PlayerManager.getCurrent().getId();
        this.events.register(this);
        this.client = new Client();
        this.router = new Router();
        this.client.addListener(this);
        this.client.start();
        KryoConfig.configure(client.getKryo());
        setupHandlers(world, playerManager);
    }

    public void setEvents(Events events) {
        if (this.events != null) {
            this.events.unregister(this);
        }
        this.events = events;
        this.events.register(this);
    }

    public void addHandler(GameClientHandler listener) {
        this.listeners.add(listener);
    }

    public void removeHandler(GameClientHandler listener) {
        this.listeners.remove(listener);
    }

    public void connect(final boolean createNewGame) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                GameClient.this.client.start();
                try {
                    GameClient.this.client.connect(Config.SERVER_TIMEOUT_MS, Config.SERVER_IP, Config.SERVER_TCP_PORT, Config.SERVER_UDP_PORT);
                    if (createNewGame) {
                        GameClient.this.client.sendTCP(new Requests.CreateGame(gameId, clientId));
                    } else {
                        GameClient.this.client.sendTCP(new Requests.JoinGame(gameId, clientId, PlayerManager.getCurrent().getShip().getId()));
                    }
                } catch (IOException e) {
                    libGdxHandler.onConnectionFailed();
                }
            }
        });
    }

    public void leaveCurrentGame() {
        if (this.client.isConnected()) {
            this.client.sendTCP(new Requests.LeaveGame(gameId, clientId));
            dispose();
        }
    }

    @Override
    public void dispose() {
        events.unregister(this);
        executorService.shutdown();
        this.client.close();
    }

    @Override
    public void received(Connection connection, Object object) {
        router.route(connection, object);
        if (object instanceof Responses.GameCreated) {
            libGdxHandler.onGameCreated((Responses.GameCreated) object);
        } else if (object instanceof Responses.ClientJoined) {
            libGdxHandler.onClientJoined((Responses.ClientJoined) object);
        } else if (object instanceof Responses.ClientLeft) {
            libGdxHandler.onClientLeft((Responses.ClientLeft) object);
        } else if (object instanceof Responses.GameReady) {
            libGdxHandler.onGameReady((Responses.GameReady) object);
        } else if (object instanceof Responses.GameClosed) {
            libGdxHandler.onGameClosed((Responses.GameClosed) object);
        }
    }

    @Handler
    public void onEvent(Events.GdxEvent event) {
        if (client.isConnected()) {
            if (event.isTypeOf(EventType.OBJECT_CREATED)) {
                GameObject object = (GameObject) event.getPrimaryParam();
                if (object.getClientId().equals(clientId)) {
                    client.sendTCP(new Requests.AddObject(gameId, clientId, object));
                }
            } else if (event.isTypeOf(EventType.OBJECT_REMOVED)) {
                GameObject object = (GameObject) event.getPrimaryParam();
                if (object.getClientId().equals(clientId)) {
                    client.sendTCP(new Requests.RemoveObject(gameId, clientId, object.getId()));
                }
            } else if (event.isTypeOf(EventType.GAME_OVER)) {
                if (PlayerManager.getCurrent().getGameProgress() == 1f) {
                    client.sendTCP(new Requests.WinGame(gameId, clientId));
                }
            }
        }
    }

    private void setupHandlers(World world, PlayerManager playerManager) {
        router.registerHandler(new ObjectAddedHandler(this, world));
        router.registerHandler(new ObjectRemovedHandler(this, world));
        router.registerHandler(new ObjectUpdatedHandler(this, world));
        router.registerHandler(new ClientJoinedHandler(this, world, playerManager));
        router.registerHandler(new ClientLeftHandler(this, world, playerManager));
        router.registerHandler(new ClientUpdatedHandler(this, world, playerManager));
        router.registerHandler(new GameCreatedHandler(this, world));
        router.registerHandler(new GameReadyHandler(this, world));
        router.registerHandler(new GameOverHandler(this, world));
    }
}
