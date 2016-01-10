package nl.fontys.scope.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import net.engio.mbassy.listener.Handler;

import nl.fontys.scope.ScopeGame;
import nl.fontys.scope.core.World;
import nl.fontys.scope.core.controller.CameraRotatingController;
import nl.fontys.scope.event.EventType;
import nl.fontys.scope.event.Events;
import nl.fontys.scope.networking.ScopeClient;
import nl.fontys.scope.object.GameObject;
import nl.fontys.scope.ui.Styles;

public class JoiningGameScreen extends AbstractScreen {

    private IngameScreen ingameScreen;

    private Thread keepAliveThread;

    private Events events = Events.getInstance();

    private String gameName;

    public JoiningGameScreen(ScopeGame game, String name) {
        super(game);
        this.gameName = name;
    }

    @Override
    protected void onShow() {
        World world = new World();
        this.ingameScreen = new IngameScreen(game, world, false);
        events.register(this);
        GameObject planet = factory.createPlanet(30f);
        world.addController(new CameraRotatingController(800f, world.getCamera(), planet));
    }

    @Override
    protected void onUpdate(float delta) {
        // Input handling
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            setScreen(new MenuScreen(game));
        }
    }

    @Override
    protected void onCreateStage(Stage stage) {
        Table layout = new Table();
        layout.setFillParent(true);

        Label caption = new Label("Joining game '" + gameName + "'", Styles.LABEL_CAPTION);

        World world = new World();
        ingameScreen = new IngameScreen(game, world, false);
        game.setClient(new ScopeClient(world));
        game.getClient().connectToServer(game.getClient().findServer(), 54555, 54777);
        long gameID = game.getClient().searchGame(gameName);
        game.getClient().joinGame(gameID);

        layout.add(caption);
        stage.addActor(layout);

        keepAliveThread = new Thread() {
            public void run() {
                do {
                    game.getClient().isStarted();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!game.getClient().isStarted());
            }
        };
        keepAliveThread.start();
    }

    @Handler
    public void onEvent(Events.GdxEvent event) {
        System.out.println("THIS IS " + event.getType());
        if (event.isTypeOf(EventType.GAME_START)) {
            System.out.println("Starting Game Event");
            keepAliveThread.stop();
            game.getClient().setStarted(true);
            setScreen(ingameScreen);
        }
    }
}