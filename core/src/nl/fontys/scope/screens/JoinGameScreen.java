package nl.fontys.scope.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

import nl.fontys.scope.ScopeGame;
import nl.fontys.scope.core.controller.CameraRotatingController;
import nl.fontys.scope.object.GameObject;

public class JoinGameScreen extends AbstractScreen {

    public JoinGameScreen(ScopeGame game) {
        super(game);
    }

    @Override
    protected void onShow() {
        GameObject planet = factory.createPlanet(0f, 30f, 0f, 0f);
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

    }
}