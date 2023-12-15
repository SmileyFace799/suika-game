package no.smileyface.suikagame.screens;

import com.badlogic.gdx.Screen;
import no.smileyface.suikagame.SuikaGame;

public abstract class GenericScreen implements Screen {
    private final SuikaGame game;

    protected GenericScreen(SuikaGame game) {
        game.addScreen(this);
        this.game = game;
    }

    public SuikaGame getGame() {
        return game;
    }
}
