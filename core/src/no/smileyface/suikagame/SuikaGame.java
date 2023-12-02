package no.smileyface.suikagame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;


public class SuikaGame extends ApplicationAdapter {
	private final Vector3 touchPos = new Vector3();

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private Rectangle bucketRect;

	private Array<Rectangle> raindropRects;
	private long lastDropTime;

	private void spawnRaindrop() {
		Rectangle raindropRect = new Rectangle(
				MathUtils.random(0, (int) camera.viewportWidth - dropImage.getWidth())
				, (int) camera.viewportHeight,
				dropImage.getWidth(),
				dropImage.getHeight()
		);
		raindropRects.addAll(raindropRect);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();

		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		rainMusic.setLooping(true);
		rainMusic.play();

		bucketRect = new Rectangle(
				camera.viewportWidth / 2 - (float) bucketImage.getWidth() / 2,
				20,
				bucketImage.getWidth(),
				bucketImage.getHeight()
		);

		raindropRects = new Array<>();
		spawnRaindrop();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucketRect.x, bucketRect.y);
		for (Rectangle raindropRect : raindropRects) {
			batch.draw(dropImage, raindropRect.x, raindropRect.y);
		}
		batch.end();

		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucketRect.x = (int) touchPos.x - bucketRect.width / 2;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			bucketRect.x -= 400 * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			bucketRect.x += 400 * Gdx.graphics.getDeltaTime();
		}

		if (bucketRect.x < 0) {
			bucketRect.x = 0;
		} else if (bucketRect.x > camera.viewportWidth - bucketRect.width) {
			bucketRect.x = (int) camera.viewportWidth - bucketRect.width;
		}

		if (TimeUtils.nanoTime() - lastDropTime > 1e9) {
			spawnRaindrop();
		}

		for (Iterator<Rectangle> iter = raindropRects.iterator(); iter.hasNext();) {
			Rectangle raindropRect = iter.next();
			raindropRect.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindropRect.overlaps(bucketRect)) {
				dropSound.play();
				iter.remove();
			} else if (raindropRect.y + raindropRect.height < 0) {
				iter.remove();
			}
		}
	}
	
	@Override
	public void dispose () {
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
		batch.dispose();
	}
}
