package no.smileyface.suikagame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;
import no.smileyface.suikagame.screens.MainMenuScreen;

public class SuikaGame extends Game {
	private final List<Screen> screens;
	private SpriteBatch batch;
	private BitmapFont font;

	public SuikaGame() {
		super();
		screens = new ArrayList<>();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	public void addScreen(Screen screen) {
		if (screens.contains(screen)) {
			throw new IllegalArgumentException("This disposable is already added");
		}
		screens.add(screen);
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		this.setScreen(new MainMenuScreen(this));
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		screens.forEach(Screen::dispose);
	}
}
