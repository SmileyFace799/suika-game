package no.smileyface.suikagame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;
import no.smileyface.suikagame.SuikaGame;

public class GameScreen extends GenericScreen {
    private final OrthographicCamera camera;

    private final Texture dropImage;
    private final Texture bucketImage;
    private final Sound dropSound;
    private final Music rainMusic;

    private final Rectangle bucketRect;

    private final Array<Rectangle> raindropRects;
    private final Vector3 touchPos;

    private long lastDropTime;
    private int dropsCollected;

    public GameScreen(final SuikaGame game) {
        super(game);

        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        this.dropImage = new Texture(Gdx.files.internal("droplet.png"));
        this.bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        this.dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));
        this.rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);

        this.bucketRect = new Rectangle(
                camera.viewportWidth / 2 - (float) bucketImage.getWidth() / 2,
                20,
                bucketImage.getWidth(),
                bucketImage.getHeight()
        );

        this.raindropRects = new Array<>();
        this.touchPos = new Vector3();

        this.lastDropTime = 0;
        this.dropsCollected = 0;

        spawnRaindrop();
    }

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
    public void show() {
        rainMusic.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        getGame().getBatch().setProjectionMatrix(camera.combined);
        getGame().getBatch().begin();
        getGame().getBatch().draw(bucketImage, bucketRect.x, bucketRect.y);
        for (Rectangle raindropRect : raindropRects) {
            getGame().getBatch().draw(dropImage, raindropRect.x, raindropRect.y);
        }
        getGame().getFont().draw(getGame().getBatch(), "Drops collected: " + dropsCollected, 0, camera.viewportHeight);
        getGame().getBatch().end();

        if (Gdx.input.isTouched()) {
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            bucketRect.x = (int) touchPos.x - bucketRect.width / 2;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketRect.x -= 400 * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketRect.x += 400 * delta;
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
            raindropRect.y -= 200 * delta;
            if (raindropRect.overlaps(bucketRect)) {
                dropsCollected++;
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


}
