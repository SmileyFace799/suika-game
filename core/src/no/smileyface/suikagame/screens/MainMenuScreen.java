package no.smileyface.suikagame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import no.smileyface.suikagame.SuikaGame;

public class MainMenuScreen extends GenericScreen {
    private final OrthographicCamera camera;

    public MainMenuScreen(final SuikaGame game) {
        super(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void show() {
        // Do nothing
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        getGame().getBatch().setProjectionMatrix(camera.combined);

        getGame().getBatch().begin();
        getGame().getFont().draw(getGame().getBatch(), "Welcome to (what will become) Suika game!", 100, 180);
        getGame().getFont().draw(getGame().getBatch(), "Tap anywhere to play drop game instead (Suika game not implemented)", 100, 150);
        getGame().getBatch().end();

        if (Gdx.input.isTouched()) {
            getGame().setScreen(new GameScreen(getGame()));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Do nothing
    }

    @Override
    public void pause() {
        // Do nothing
    }

    @Override
    public void resume() {
        // Do nothing
    }

    @Override
    public void hide() {
        // Do nothing
    }

    @Override
    public void dispose() {
        // Do nothing
    }
}
